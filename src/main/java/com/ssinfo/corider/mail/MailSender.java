package com.ssinfo.corider.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ssinfo.ws.rider.constants.Constants;


public class MailSender {

	private String sendTo;
	private String from;
	private String message;
	private Session mMailSession = null;
	
	public MailSender() {
		
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());   
		Properties props = new Properties();  
		props.put("mail.smtp.transport.protocol", "smtp");   
		props.put("mail.smtp.user", Constants.FEEDBACK_EMAIL_ID);
		props.put("mail.smtp.host", Constants.EMAIL_HOST);  
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.ssl.enable", "false");
		
		 mMailSession = Session.getDefaultInstance(props,new Authenticator() {
	    	 protected PasswordAuthentication getPasswordAuthentication() {
	    	        return new PasswordAuthentication("coridr@netxylem.in", "Coridr@123");
	    	    }
		});
		 //code for sending mail using gmail account..
		/*props.put("mail.smtp.transport.protocol", "smtp");   
		props.put("mail.smtp.user", "vinitforyou@gmail.com");
		props.put("mail.smtp.host", "smtp.gmail.com");  
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
		props.put("mail.debug", "true");  
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		props.put("mail.smtp.socketFactory.fallback", "false");  
		
	    mMailSession = Session.getDefaultInstance(props,new Authenticator() {
	    	 protected PasswordAuthentication getPasswordAuthentication() {
	    	        return new PasswordAuthentication("vinitforyou@gmail.com", "software@1234");
	    	    }
		});*/
	
	}

	public void sendMessage(String messageContent){
		mMailSession.setDebug(true);   
		try {
			Transport transport = mMailSession.getTransport("smtp");
			InternetAddress addressFrom = new InternetAddress(from);   
			MimeMessage message = new MimeMessage(mMailSession);
			message.setSender(addressFrom);
			message.setSubject(Constants.MESSAGE_HEADER);  
			message.setContent(messageContent, "text/html");  
			InternetAddress addressTo = new InternetAddress(sendTo);  
			message.setRecipient(RecipientType.TO, addressTo);  
			transport.connect();  
			Transport.send(message);  
			//transport.close();  
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	 
	}
	
	

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Session getmMailSession() {
		return mMailSession;
	}

	public void setmMailSession(Session mMailSession) {
		this.mMailSession = mMailSession;
	}
	
	

}
