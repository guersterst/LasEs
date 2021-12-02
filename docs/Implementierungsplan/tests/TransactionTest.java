@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @Test
    void testCommitCommitsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.commit();
        Mockito.verify(mockConnection).commit();
    }

    @Test
    void testAbortAbortsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.abort();
        Mockito.verify(mockConnection).rollback();
    }

}