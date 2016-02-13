package com.ensense.insense.core.analytics.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailUtil {
	
	private static Logger logger = Logger.getLogger(EmailUtil.class);
	private String smptHost;
	private String from;
	private List<String> recepientList;
	private String subject;
	private String emailContent;
	private List<String> filePathList;
	
	public EmailUtil(String smptHost, String from) {
		this.smptHost = smptHost;
		this.from = from;
	}
	
	public List<String> getRecepientList() {
		return recepientList;
	}

	public void setRecepientList(List<String> recepientList) {
		this.recepientList = recepientList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}
	
	public boolean sendEmail() {
		boolean status = false;
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smptHost);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			if ( addRecipients(this.recepientList, message) ){
			
				message.setSubject(this.subject);
				message.setContent(this.emailContent, "text/html; charset=utf-8");

				Transport.send(message);
				logger.info("Sent message successfully....");
				status = true;
			}
		} catch (MessagingException mex) {
			status = false;
			logger.error("Exception in EmailUtil. Error while sending email."+mex);
			mex.printStackTrace();
		}
		
		return status;
	}

	public boolean sendEmailWithAttachement(List<String> to, String subject,
			String emailContent, List<String> filePathList){
		boolean status = false;
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smptHost);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			if ( addRecipients(to, message) ){
			
				message.setSubject(subject);
				message.setContent(emailContent, "text/html; charset=utf-8");

				message = addAttachements(filePathList, message, emailContent);
				
				logger.info("Sending Email From :"+from);
				logger.info("Sending Email To :"+to);
				logger.info("Subject :"+subject);
				logger.info("emailContent :"+emailContent);
				
				Transport.send(message);
				logger.info("Sent message successfully....");
				status = true;
			}
		} catch (MessagingException mex) {
			status = false;
			logger.error("Exception in EmailUtil. Error while sending email."+mex);
			mex.printStackTrace();
		}
		
		return status;
	}
	
	public MimeMessage addAttachements(List<String> filePathList, MimeMessage message, String emailContent){
		// creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        try {
			messageBodyPart.setContent(emailContent, "text/html; charset=utf-8");
	        // creates multi-part
			Multipart multipart = new MimeMultipart("mixed");
	        multipart.addBodyPart(messageBodyPart);
			
			for ( String filePath: filePathList){
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
	
	public boolean sendEmail(List<String> to, String subject, String emailContent) {
		boolean status = false;
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smptHost);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			if ( addRecipients(to, message) ){
			
				message.setSubject(subject);
				message.setContent(emailContent, "text/html; charset=utf-8");

				logger.info("Sending Email From :"+from);
				logger.info("Sending Email To :"+to);
				logger.info("Subject :"+subject);
				logger.info("emailContent :"+emailContent);
				
				Transport.send(message);
				logger.info("Sent message successfully....");
				status = true;
			}
		} catch (MessagingException mex) {
			status = false;
			logger.error("Exception in EmailUtil. Error while sending email."+mex);
			mex.printStackTrace();
		} catch (Exception e){
			logger.error("Exception in EmailUtil. Error while sending email."+e);
		}
		
		return status;
	}

	private boolean addRecipients(List<String> to, MimeMessage message) {
		boolean status = true;
		
		for ( String emailTo : to){
			try {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(
						emailTo));
			} catch (AddressException e) {
				status = false;
				logger.error("Exception in EmailUtil. Error while adding recepient."+e);
				e.printStackTrace();
			} catch (MessagingException e) {
				status = false;
				logger.error("Exception in EmailUtil. Error while adding recepient."+e);
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public List<String> getRecepientsList(String emailIds) {
		List<String> emailRecepientsList = new ArrayList<String>();

		if ( null != emailIds && emailIds.length() > 0 ){
			String[] emailIdArray = emailIds.split(",");
			
			for ( String emailId : emailIdArray){
				emailRecepientsList.add(emailId);
			}
		}
		return emailRecepientsList;
	}

}
