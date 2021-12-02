public class HashingTest {

    private static final String PASSWORD1 = "winter45sommer";
    private static final String SALT1 = "idkfjADGdkds945803jdkl==";
    private static final String HASH1 = "VlE2L0tuG/djzLbVz0r6OA==";

    private static final String PASSWORD2 = "basti3schi";
    private static final String SALT2 = "i845jdlkfjDkjf34ljsdSS==";
    private static final String HASH2 = "Yz7+fC9F2UuxC1dsOI4OGg==";

    @Test
    void testCorrectHashing() {
        String calculatedHash1 = Hashing.hashWithGivenSalt(PASSWORD1, SALT1);
        String calculatedHash2 = Hashing.hashWithGivenSalt(PASSWORD2, SALT2);
        assertAll(
                () -> assertEquals(HASH1, calculatedHash1),
                () -> assertEquals(HASH2, calculatedHash2)
        );
    }

}
