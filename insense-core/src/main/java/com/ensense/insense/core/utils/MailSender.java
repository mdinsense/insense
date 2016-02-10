package com.ensense.insense.core.utils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

	public static boolean sendMail(String from, String to, String subject,
			String messageContent, String smtpHost) {
		boolean isSend = false;
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHost);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setText(messageContent);
			Transport.send(message);
			System.out.println("Sent message successfully....");
			isSend = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSend;
	}

	public static boolean sendEmailWithAttachement(String from, String to,
			String subject, String emailContent, List<String> filePathList,
			String smtpHost) {
		boolean status = false;
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHost);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setContent(emailContent, "text/html; charset=utf-8");
			message = addAttachements(filePathList, message, emailContent);

			Transport.send(message);
			System.out
					.println("Sent message successfully with Attachement....");
			status = true;

		} catch (MessagingException mex) {
			status = false;
			System.out
					.println("Exception in EmailUtil. Error while sending email."
							+ mex);
			mex.printStackTrace();
		}

		return status;
	}

	public static MimeMessage addAttachements(List<String> filePathList,
			MimeMessage message, String emailContent) {
		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		try {
			messageBodyPart
					.setContent(emailContent, "text/html; charset=utf-8");
			// creates multi-part
			Multipart multipart = new MimeMultipart("mixed");
			multipart.addBodyPart(messageBodyPart);

			for (String filePath : filePathList) {
				MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.attachFile(filePath);
				multipart.addBodyPart(attachPart);
			}

			// sets the multi-part as e-mail's content
			message.setContent(multipart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public static void main(String[] args) {

		String to = "sudha.perumal@cognizant.com";

		String from = "sudha.perumal@cognizant.com";

		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", "APACSMTP.CTS.COM");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject("This is the Subject Line!");
			message.setText("This is actual message");

			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
