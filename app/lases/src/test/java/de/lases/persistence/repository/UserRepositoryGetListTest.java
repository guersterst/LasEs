package de.lases.persistence.repository;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.SortOrder;
import de.lases.global.transport.User;
import de.lases.persistence.exception.DataNotCompleteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        Transaction transaction = new Transaction();
        Connection mockConnection = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);
        ResultSet rst = mock(ResultSet.class);

        try {
            // Mock the connection to get a mocked statement.
            when(mockConnection.createStatement()).thenReturn(mockStmt);

            // Mock the statement to get a mocked ResultSet.
            when(mockStmt.executeQuery(any())).thenReturn(rst);

            // Finally, mock the ResultSet to return the test values
            // for all 7 entries of the testdata in the order we expect.
            final int[] i = {0};
            when(rst.next()).thenAnswer((Answer<Boolean>) invocation -> {
                if (i[0] < 7) {
                    i[0]++;
                    return true;
                } else {
                    return false;
                }
            });

            // All return values are based on the number of previous rst.next() calls.
            when(rst.getString("id")).thenAnswer(I -> userTestData.get(i[0]).getId());
            when(rst.getString("email_address")).thenAnswer(I -> userTestData.get(i[0]).getEmailAddress());
            when(rst.getString("is_administrator")).thenAnswer(I -> userTestData.get(i[0]).isAdmin());
            when(rst.getString("firstname")).thenAnswer(I -> userTestData.get(i[0]).getFirstName());
            when(rst.getString("lastname")).thenAnswer(I -> userTestData.get(i[0]).getLastName());
            when(rst.getString("title")).thenAnswer(I -> userTestData.get(i[0]).getTitle());
            when(rst.getString("employer")).thenAnswer(I -> userTestData.get(i[0]).getEmployer());
            when(rst.getString("birthdate")).thenAnswer(I -> userTestData.get(i[0]).getDateOfBirth());
            when(rst.getString("is_registered")).thenAnswer(I -> userTestData.get(i[0]).isRegistered());
            when(rst.getString("password_hash")).thenAnswer(I -> userTestData.get(i[0]).getPasswordHashed());
            when(rst.getString("password_salt")).thenAnswer(I -> userTestData.get(i[0]).getPasswordSalt());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inject our mocked connection into the  transaction with reflection.
        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        assertEquals(userTestData, UserRepository.getList(transaction, params));
    }
}
