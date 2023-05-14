package com.affiliate.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailSSL {

	static public String mailCounter = "";
	public static String host = "imap.mail.yahoo.com";// change accordingly
	public static String mailStoreType = "imaps";
	public static String username = "sachinmehta915@yahoo.com";// change accordingly
	public static String password = "ocsayodqtzhgunpt";// change accordingly

	public static void main(String[] args) throws InterruptedException, MessagingException {
		if (false) {
			mailCounter = check(host, mailStoreType, username, password);
			while (true) {
//			Thread.sleep(1000);
				String msg = check(host, mailStoreType, username, password);
				if (!mailCounter.equals(msg) && !"".equals(msg)) {
					mailCounter = msg;
					sendNotify(true);
				} else {
					System.out.println("No new mail found");
				}
			}
		} else {
			check(host, mailStoreType, username, password);
		}
	}

	public static void sendNotify(Boolean b) {

		// from,password,to,subject,message
		Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
		Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
//		System.out.println(batteryStatus.toString());
		int battery = Integer
				.valueOf(((batteryStatus.toString().split("\n"))[2]).replace("Battery Life: ", "").replace("%", ""));
		String msg = battery + "% - " + ((batteryStatus.toString().split("\n"))[0]).replace("ACLineStatus: ", "")
				+ " \n\n\n" + " sent at " + new Date();
		String[] to = { "ssachinmmehta@gmail.com" };
		if (battery < 20 || b) {
			Thread t = new Thread(() -> SendMailSSL.sendFromGMail("sachinmehta915@yahoo.com", "ocsayodqtzhgunpt", to,
					battery < 20 ? "Battery low" : "Battery fine", msg));
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		;
		// change from, password and to

	}

	public static synchronized String check(String host, String storeType, String user, String password) {
		String currentMailCount = "";
		Store store = null;
		Folder emailFolder = null;
		try {
			// create properties field
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imap");
			properties.put("mail.imaps.host", host);
			properties.put("mail.imap.ssl.enable", "true");
			properties.put("mail.imaps.port", "993");
			properties.put("mail.imap.mail.auth", "true");
//			properties.put("mail.imaps.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			store = emailSession.getStore(storeType);

			store.connect(host, user, password);

			// create the folder object and open it
			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it

			mailCounter = Integer.toString(emailFolder.getMessageCount());

			while (true) {
//				Thread.sleep(100);
				currentMailCount = Integer.toString(emailFolder.getMessageCount());
				if (!mailCounter.equals(currentMailCount) && !"".equals(currentMailCount)) {
					mailCounter = currentMailCount;
					sendNotify(true);
				} else {
//					System.out.println("No new mail found");
				}
			}
//			Message[] messages = emailFolder.getMessages();
////			System.out.println(emailFolder.getPermanentFlags().getUserFlags().toString());
////			System.out.println("messages.length---" + messages.length);
//
////			for (int i = 0, n = messages.length; i < n; i++) {
//			Message message = messages[messages.length - 1];
////			System.out.println("---------------------------------");
////			System.out.println("Email Number " + (i + 1));
////			System.out.println("Subject: " + message.getSubject());
//			msg = message.getSubject();
////			System.out.println("From: " + message.getFrom()[0]);
////			System.out.println("Text: " + message.getContent().toString());
//			message.setFlag(FLAGS.Flag.DELETED, false);
//			emailFolder.expunge();
//			}

			// close the store and folder objects

		} catch (Exception e) {
			e.printStackTrace();
			try {
				emailFolder.close(false);
				store.close();
//				check(host, mailStoreType, username, password);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return currentMailCount;
	}

	static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {

		String host = "smtp.mail.yahoo.com";
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});

//		session.setDebug(true);
		Arrays.asList(to).forEach((address) -> {
			MimeMessage message = new MimeMessage(session);

			try {
				message.setFrom(new InternetAddress(from));

				message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
				message.setSubject(subject);
				message.setText(body);

				System.out.println("sending...");
				Transport.send(message);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		System.out.println("Sent message successfully....");
	}
}
