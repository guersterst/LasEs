package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.KeyExistsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Johann Schicho
 */
@ExtendWith(WeldJunit5Extension.class)
public class ScientificForumRepoMultiTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    private static Transaction transaction;

    private ScientificForum scientificForum1;
    private ScientificForum scientificForum2;

    private User user1;
    private User user2;

    private ResultListParameters globalSeachWordParams;
    private ResultListParameters filterColumnParams;

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() throws Exception {
        Class<ScientificForumRepoMultiTest> clazz = ScientificForumRepoMultiTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        FileDTO file = new FileDTO();
        file.setInputStream(inputStream);
        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        transaction = new Transaction();

        createData();
    }

    void createData() throws Exception {
        scientificForum1 = new ScientificForum();
        scientificForum1.setName("Forum1");
        scientificForum1.setDescription("great forum");
        scientificForum1.setReviewManual("just review");

        scientificForum2 = new ScientificForum();
        scientificForum2.setName("Forum2");
        scientificForum2.setDescription("also a great forum");
        scientificForum2.setReviewManual("don't");

        globalSeachWordParams = new ResultListParameters();
        globalSeachWordParams.setPageNo(1);
        globalSeachWordParams.setSortColumn("name");
        globalSeachWordParams.setSortOrder(SortOrder.ASCENDING);
        globalSeachWordParams.setGlobalSearchWord("Forum1");

        filterColumnParams = new ResultListParameters();
        filterColumnParams.setPageNo(1);
        filterColumnParams.setSortColumn("name");
        filterColumnParams.setSortOrder(SortOrder.ASCENDING);
        filterColumnParams.getFilterColumns().put("name", "Forum2");

        user1 = new User();
        user1.setFirstName("Otto");
        user1.setLastName("Motor");
        user1.setEmailAddress("otto.motor@example.com");
        user1.setAdmin(false);

        user2 = new User();
        user2.setFirstName("Toto");
        user2.setLastName("Wolff");
        user2.setEmailAddress("toto.wolff@example.com");
        user2.setAdmin(false);
    }

    @AfterEach
    void shutDownConnectionPool() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    @Test
    void testAddGet() throws DataNotWrittenException, KeyExistsException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForum ret2 = ScientificForumRepository.add(scientificForum2, transaction);

        assertAll(
                () -> assertEquals(scientificForum1.getName(), ScientificForumRepository.get(ret1, transaction).getName()),
                () -> assertEquals(scientificForum2.getName(), ScientificForumRepository.get(ret2, transaction).getName())
        );
    }

    @Test
    void testChange() throws DataNotWrittenException, KeyExistsException, NotFoundException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        scientificForum1.setName("a different name!");
        scientificForum1.setId(ret1.getId());

        ScientificForumRepository.change(scientificForum1, transaction);

        assertAll(
                // changed
                () -> assertEquals(scientificForum1.getName(), ScientificForumRepository.get(scientificForum1, transaction).getName()),
                // stays unchanged
                () -> assertEquals(scientificForum1.getDescription(), ScientificForumRepository.get(scientificForum1, transaction).getDescription())
        );
    }

    @Test
    void testChangeNameExists() throws DataNotWrittenException, KeyExistsException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForum ret2 = ScientificForumRepository.add(scientificForum2, transaction);

        ret1.setName(scientificForum2.getName());

        assertThrows(KeyExistsException.class,
                () -> ScientificForumRepository.change(ret1, transaction)
        );
    }

    @Test
    void testChangeNotFound() throws DataNotWrittenException, KeyExistsException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);

        scientificForum1.setId(ret1.getId() << 3);

        assertThrows(NotFoundException.class,
                () -> ScientificForumRepository.change(scientificForum1, transaction)
        );
    }

    @Test
    void testAddAndRemove() throws DataNotWrittenException, KeyExistsException, NotFoundException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForumRepository.remove(ret1, transaction);

        assertThrows(NotFoundException.class,
                () -> ScientificForumRepository.get(ret1, transaction)
        );
    }

    @Test
    void testGetList() throws DataNotWrittenException, KeyExistsException, DataNotCompleteException, NotFoundException {
        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForum ret2 = ScientificForumRepository.add(scientificForum2, transaction);

        List<ScientificForum> list1 = ScientificForumRepository.getList(transaction, globalSeachWordParams);
        List<ScientificForum> list2 = ScientificForumRepository.getList(transaction, filterColumnParams);

        int count1 = ScientificForumRepository.getCountItemsList(transaction, globalSeachWordParams);
        int count2 = ScientificForumRepository.getCountItemsList(transaction, filterColumnParams);

        assertAll(
                () -> assertTrue(list1.contains(ret1)),
                () -> assertEquals(list1.size(), count1),

                () -> assertTrue(list2.contains(ret2)),
                () -> assertEquals(list2.size(), count2)
        );
    }

    @Test
    void addRemoveEditor() throws DataNotWrittenException, KeyExistsException, NotFoundException, DataNotCompleteException {
        User uRet1 = UserRepository.add(user1, transaction);
        User uRet2 = UserRepository.add(user2, transaction);

        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);

        ScientificForumRepository.addEditor(ret1, uRet1, transaction);
        ScientificForumRepository.addEditor(ret1, uRet2, transaction);

        List<User> userList = UserRepository.getList(transaction, ret1);

        assertAll(
                () -> assertTrue(userList.contains(uRet1)),
                () -> assertTrue(userList.contains(uRet2))
        );

        ScientificForumRepository.removeEditor(ret1, uRet1, transaction);

        List<User> userListAfter = UserRepository.getList(transaction, ret1);

        assertAll(
                () -> assertFalse(userListAfter.contains(uRet1)),
                () -> assertTrue(userListAfter.contains(uRet2))
        );
    }

    @Test
    void addRemoveScienceFieldsToForum()
            throws DataNotWrittenException, KeyExistsException, NotFoundException, DataNotCompleteException {
        ScienceField abcdefg = new ScienceField();
        abcdefg.setName("abcdefg");
        ScienceFieldRepository.add(abcdefg, transaction);

        ScientificForum ret1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForumRepository.addScienceField(ret1, abcdefg, transaction);

        List<ScienceField> scienceFields = ScienceFieldRepository.getList(ret1, transaction, new ResultListParameters());
        assertTrue(scienceFields.contains(abcdefg));

        ScientificForumRepository.removeScienceField(ret1, abcdefg, transaction);
        List<ScienceField> scienceFieldsAfter = ScienceFieldRepository.getList(ret1, transaction, new ResultListParameters());
        assertTrue(scienceFieldsAfter.isEmpty());
    }
}