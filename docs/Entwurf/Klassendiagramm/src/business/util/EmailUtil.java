package business.util;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class EmailUtil {

	private Event<UIMessage> uiMessageEvent;

	public static void sendEmail(String sender, String recipient, String subject, String body) throws MessagingException { }
	
	public static String generateMailTo(String[] receiver, String[] cc, String subject, String body) { return null; }
	
	public static boolean checkEmailAddress(String address) { return false; }

}
