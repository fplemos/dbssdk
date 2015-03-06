package br.com.dbsoft.mail;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message.RecipientType;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.NETWORK.PROTOCOL;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSObject;


public class DBSEmailSend {

	protected static Logger					wLogger = Logger.getLogger(DBSEmailSend.class);
	
	private String 		wHostUserName;
	private String 		wHostPassword;
	private String 		wHost;
	private String 		wHostPort = "587";
	private PROTOCOL 	wProtocol;
	private String		wProtocolString;

	public DBSEmailSend(){
		setProtocol(null);
	}
	
	//================================================================================================================
	public String getHostUserName() {return wHostUserName;}
	public void setHostUserName(String pHostUserName) {wHostUserName = pHostUserName;}

	public String getHostPassword() {return wHostPassword;}
	public void setHostPassword(String pHostPassword) {wHostPassword = pHostPassword;}

	public String getHost() {return wHost;}
	public void setHost(String pHost) {wHost = pHost;}

	public String getHostPort() {return wHostPort;}
	public void setHostPort(String pHostPort) {wHostPort = pHostPort;}

	public PROTOCOL getProtocol() {return wProtocol;}
	public void setProtocol(PROTOCOL pProtocol) {
		wProtocol = pProtocol;
		if (pProtocol == null){
			wProtocolString = "smtp";
		}else if (pProtocol == PROTOCOL.SSL){
			wProtocolString = "smtps";
		}
	}

	public String getProtocolString() {return wProtocolString;}
	
	//================================================================================================================
	public boolean send(DBSEmailMessage pMessage){
		if (pMessage == null
			|| DBSObject.isEmpty(pMessage.getTo())
			|| DBSObject.isEmpty(pMessage.getSubject())){
			return false;
		}
		Properties 		xProps = new Properties();
		Session 		xMailSession = Session.getInstance(xProps, new SMTPAuthenticator());
	    Message 		xMessage = new MimeMessage(xMailSession);
		InternetAddress xFromAddress = null;
		Transport 		xTransport;
		BodyPart 		xMessageBodyPart = new MimeBodyPart();
		Multipart 		xMultipart;

		xProps.put("mail.transport.protocol", wProtocolString);
		xProps.put("mail." + wProtocolString + ".host", wHost); 
		xProps.put("mail." + wProtocolString + ".port", wHostPort); 
		
		if (!DBSObject.isEmpty(wHostUserName)
		 && !DBSObject.isEmpty(wHostPassword)){
			xProps.put("mail." + wProtocolString + ".auth", "true");   
		}
		
		if (wProtocol != null){
			if (wProtocol == PROTOCOL.SSL){
				xProps.put("mail." + wProtocolString + ".ssl.enable", "true");
				xProps.put("mail." + wProtocolString + ".ssl.required", "true");
			}
		}
	    
		try {
			//FROM
			xFromAddress = new InternetAddress(pMessage.getFrom().getAddress());
			xFromAddress.setPersonal(pMessage.getFrom().getName());
			xMessage.setFrom(xFromAddress);
			
			//TO ---------------------
			for (DBSEmailAddress xEmailAddress:pMessage.getTo()){
				pvAddRecipient(xMessage, RecipientType.TO, xEmailAddress);
			}
			//CC ---------------------
			for (DBSEmailAddress xEmailAddress:pMessage.getCC()){
				pvAddRecipient(xMessage, RecipientType.CC, xEmailAddress);
			}
			//BCC ---------------------
			for (DBSEmailAddress xEmailAddress:pMessage.getBCC()){
				pvAddRecipient(xMessage, RecipientType.BCC, xEmailAddress);
			}
			
			//Texto da mensagem----------
			xMessage.setSubject(pMessage.getSubject());
			xMessageBodyPart.setText(pMessage.getText());
			xMultipart = new MimeMultipart();
			xMultipart.addBodyPart(xMessageBodyPart);
			
			//Anexos---------------------
			for (String xFilename:pMessage.getAttachments()){
				xMessageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(xFilename);
				xMessageBodyPart.setDataHandler(new DataHandler(source));
				xMessageBodyPart.setFileName(DBSFile.getFileNameFromPath(xFilename));
				xMultipart.addBodyPart(xMessageBodyPart);
			}

			
	        //Envio-----------------------------------
	        xMessage.setContent(xMultipart);

			xTransport = xMailSession.getTransport(wProtocolString.toString());
			xTransport.connect();
			xMessage.saveChanges(); 
			xTransport.sendMessage(xMessage, xMessage.getAllRecipients());
			xTransport.close();
			return true;
		} catch (AddressException e) {
			wLogger.error(e);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			wLogger.error(e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			wLogger.error(e);
		}
		return false;
		
	}
	
	private void pvAddRecipient(Message pMessage, RecipientType pRecipientType, DBSEmailAddress pEmailAddress) throws MessagingException, UnsupportedEncodingException{
		InternetAddress xToAddress = new InternetAddress(pEmailAddress.getAddress());
		xToAddress.setPersonal(pEmailAddress.getName());
		pMessage.addRecipient(pRecipientType, xToAddress);
	}
	
    private class SMTPAuthenticator extends Authenticator {
        @Override
		public PasswordAuthentication getPasswordAuthentication() {
           String username = wHostUserName;
           String password = wHostPassword;
           return new PasswordAuthentication(username, password);
        }
    }
	
}
