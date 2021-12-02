public class EmailUtilTest {

    private static final String emailSubjectCCnoBody
    	= "mailto:info@example.com?subject=important&cc=cc@example.com";
    private static final String emailSubjectCCBody
    	=  "mailto:info@example.com?subject=important&body=Important%20Mail%0D%0ARegards&cc=cc@example.com";
    private static final String emailOnlyAddress = "mailto:info@example.com";
    private static final String emailMultipleRecipients = "mailto:info@example.com,service@example.com";

    private static final String emailAddressRecipient = "info@example.com";
    private static final String emailAddressRecipient2 = "service@example.com";
    private static final String emailAddressCC = "cc@example.com";
    private static final String subject = "important";
    private static final String body = "Important Mail\nRegards";

    @Test
    void testGenerateMailToLinkNoBody() {
        String mailto = EmailUtil.generateMailToLink(new String[]{emailAddressRecipient},
        	new String[]{emailAddressCC}, subject, null);
        assertEquals(emailSubjectCCnoBody, mailto);
    }

    @Test
    void testGenerateMailToLinkWithBody() {
        String mailto = EmailUtil.generateMailToLink(new String[]{emailAddressRecipient},
        	new String[]{emailAddressCC}, subject, body);
        assertEquals(emailSubjectCCBody, mailto);
    }

    @Test
    void testGenerateMailToEmptyEmail() {
        String mailto = EmailUtil.generateMailToLink(new String[]{emailAddressRecipient}, null,null,null);
        assertEquals(emailOnlyAddress, mailto);
    }

    @Test
    void testGenerateMailToMultipleRecipients(){
        String mailto = EmailUtil.generateMailToLink(new String[]{emailAddressRecipient, emailAddressRecipient2},
        	null, null, null);
        assertEquals(emailMultipleRecipients, mailto);
    }
}
