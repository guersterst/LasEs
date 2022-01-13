package de.lases.persistence.repository;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.SortOrder;
import de.lases.global.transport.User;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TESTINFO @basti not working
 */
@ExtendWith(MockitoExtension.class)
public class UserRepositoryGetListTest {

    private static final User user1 = new User();
    private static final User user2 = new User();
    private static final User user3 = new User();
    private static final User user4 = new User();
    private static final User user5 = new User();
    private static final User user6 = new User();
    private static final User user7 = new User();

    private static List<User> userTestData;

    @BeforeAll
    static void init() {
        user1.setFirstName("Anton");
        user2.setFirstName("Bertha");
        user3.setFirstName("Cäsar");
        user4.setFirstName("Dora");
        user5.setFirstName("Emil");
        user6.setFirstName("Friedrich");
        user7.setFirstName("Gustav");

        user1.setLastName("Grimm");
        user2.setLastName("Fischer");
        user3.setLastName("Eckert");
        user4.setLastName("Decker");
        user5.setLastName("Claas");
        user6.setLastName("Becker");
        user7.setLastName("Ackermann");

        userTestData = Arrays.asList(user1, user2, user3, user4, user5, user6, user7);

        // Give every user an ID.
        int i = 1;
        for (User user : userTestData) {
            user.setId(i);
            i++;
        }

        user1.setAdmin(true);
    }

    @Test
    void testGetListSortByFirstname() throws NoSuchFieldException, IllegalAccessException, DataNotCompleteException {
        ResultListParameters params = new ResultListParameters();
        params.setPageNo(1);
        params.setSortColumn("firstname");
        params.setSortOrder(SortOrder.ASCENDING);

        CDI cdiMock = mock(CDI.class);
        ConfigReader mockConfig = mock(ConfigReader.class);
        Instance configReaderInstance = mock(Instance.class);


        Transaction mockTransaction = mock(Transaction.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet rst = mock(ResultSet.class);

        try {
            // Return default pagination size
            when(mockConfig.getProperty("MAX_PAGINATION_LENGTH")).thenReturn("25");

            // Mock CDI
            MockedStatic<CDI> cdiMockedStatic = mockStatic(CDI.class);
            cdiMockedStatic.when(CDI::current).thenReturn(cdiMock);
            when(cdiMock.select(ConfigReader.class)).thenReturn(configReaderInstance);
            when(configReaderInstance.get()).thenReturn(mockConfig);

            // get the modified connection from transaction.
            when(mockTransaction.getConnection()).thenReturn(mockConnection);

            // Mock the connection to get a mocked statement.
            when(mockConnection.prepareStatement(any())).thenReturn(mockStmt);

            // Mock the statement to get a mocked ResultSet.
            when(mockStmt.executeQuery()).thenReturn(rst);

            // Finally, mock the ResultSet to return the test values
            // for all 7 entries of the testdata in the order we expect.
            final int[] i = {-1};
            when(rst.next()).thenAnswer((Answer<Boolean>) invocation -> {
                if (i[0] < userTestData.size() - 1) {
                    i[0]++;
                    return true;
                } else {
                    return false;
                }
            });

            // All return values are based on the number of previous rst.next() calls.
            lenient().when(rst.getInt("id")).thenAnswer(I -> userTestData.get(i[0]).getId());
            lenient().when(rst.getString("user_role")).thenAnswer(I -> "none");
            lenient().when(rst.getString("email_address")).thenAnswer(I -> userTestData.get(i[0]).getEmailAddress());
            lenient().when(rst.getString("is_administrator")).thenAnswer(I -> userTestData.get(i[0]).isAdmin());
            lenient().when(rst.getString("firstname")).thenAnswer(I -> userTestData.get(i[0]).getFirstName());
            lenient().when(rst.getString("lastname")).thenAnswer(I -> userTestData.get(i[0]).getLastName());
            lenient().when(rst.getString("title")).thenAnswer(I -> userTestData.get(i[0]).getTitle());
            lenient().when(rst.getString("employer")).thenAnswer(I -> userTestData.get(i[0]).getEmployer());
            lenient().when(rst.getString("birthdate")).thenAnswer(I -> userTestData.get(i[0]).getDateOfBirth());
            lenient().when(rst.getString("is_registered")).thenAnswer(I -> userTestData.get(i[0]).isRegistered());
            lenient().when(rst.getString("password_hash")).thenAnswer(I -> userTestData.get(i[0]).getPasswordHashed());
            lenient().when(rst.getString("password_salt")).thenAnswer(I -> userTestData.get(i[0]).getPasswordSalt());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(userTestData, UserRepository.getList(mockTransaction, params));
    }
}
