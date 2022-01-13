package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.ScientificForum;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
public class ScientificForumRepositoryNoMocksTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class<ScienceFieldRepositoryTest> clazz = ScienceFieldRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGetNotFound() {
        ScientificForum forum = new ScientificForum();
        forum.setId(-17);
        Transaction t = new Transaction();
        assertThrows(NotFoundException.class, () -> ScientificForumRepository.get(forum, t));
        t.commit();
    }

    @Test
    void testExistsId() {
        ScientificForum forum = new ScientificForum();
        forum.setId(-17);
        Transaction t = new Transaction();
        assertFalse(ScientificForumRepository.exists(forum, t));
        t.commit();
    }

    @Test
    void testExistsName() {
        ScientificForum forum = new ScientificForum();
        forum.setName("NONEXISTANT FORUM");
        Transaction t = new Transaction();
        assertFalse(ScientificForumRepository.exists(forum, t));
        t.commit();
    }

    @Test
    void testAddAndRemove() throws DataNotWrittenException, KeyExistsException, NotFoundException {
        Transaction t = new Transaction();
        try {
            ScientificForum forum = new ScientificForum();
            forum.setName("TEMP FORUM");
            forum.setDescription("THIS FORUM IS TEMPORARY AND SHOULD NOT BE SEEN IN THE DB");
            forum.setReviewManual("revmnl");
            forum = ScientificForumRepository.add(forum, t);
            assertTrue(ScientificForumRepository.exists(forum, t));
            ScientificForumRepository.remove(forum, t);
            assertFalse(ScientificForumRepository.exists(forum, t));
        } finally {
            t.commit();
        }

    }

}
