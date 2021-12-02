class UserRepositoryTest {

    private static final String EXAMPLE_EMAIL_ADDRESS = "john.doe@foo.bar";

    @Test
    void testAddEmailExists() throws Exception {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);

        Transaction transaction = new Transaction();
        UserRepository.add(user, transaction);
        assertTrue(UserRepository.emailExists(user, transaction));
        transaction.abort();
    }

    @Test
    void testEmailDoesNotExist() throws Exception {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);

        Transaction transaction = new Transaction();
        UserRepository.remove(user, transaction);
        assertFalse(UserRepository.emailExists(user, transaction));
        transaction.abort();
    }
}