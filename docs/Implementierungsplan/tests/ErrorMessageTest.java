class ErrorMessageTest {

    private static final String EXAMPLE_MESSAGE = "ERROR: This is an example error message";
    private static final String EXAMPLE_STACKTRACE = "java.lang.Exception: This is an example stacktrace";

    @Test
    void testErrorMessage() {
        ErrorMessage errorMessage = new ErrorMessage(EXAMPLE_MESSAGE, EXAMPLE_STACKTRACE);
        assertAll(
                () -> assertEquals(EXAMPLE_MESSAGE, errorMessage.getMessage()),
                () -> assertEquals(EXAMPLE_STACKTRACE, errorMessage.getStackTrace()),
                () -> assertEquals(MessageCategory.FATAL, errorMessage.getCategory())
        );
    }

}