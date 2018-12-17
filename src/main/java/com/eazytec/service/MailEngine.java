package com.eazytec.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.EmailConfiguration;

/**
 * Class for sending e-mail messages based on Velocity templates
 * or with attachments.
 * 
 * @author madan
 */
public class MailEngine {
    private final Log log = LogFactory.getLog(MailEngine.class);
    private MailSender mailSender;
    private VelocityEngine velocityEngine;
    private String defaultFrom;
    private MailService mailService;
	 
	@Autowired
	public void setMailService(MailService mailService) {
	    this.mailService = mailService;
	}
	
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setFrom(String from) {
        this.defaultFrom = from;
    }

    /**
     * Send a simple message based on a Velocity template.
     * @param msg the message to populate
     * @param templateName the Velocity template to use (relative to classpath)
     * @param model a map containing key/value pairs
     */
    public void sendMessage(SimpleMailMessage msg, String templateName, Map model) {
        String result = null;

        try {
            result =
                VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                            templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        msg.setText(result);
        send(msg);
    }

    /**
     * Send a simple message with pre-populated values.
     * @param msg the message to send
     * @throws org.springframework.mail.MailException when SMTP server is down
     */
    public void send(SimpleMailMessage msg) throws MailException {
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    /**
     * Convenience method for sending messages with attachments.
     * 
     * @param recipients array of e-mail addresses
     * @param sender e-mail address of sender
     * @param resource attachment from classpath
     * @param bodyText text in e-mail
     * @param subject subject of e-mail
     * @param attachmentName name for attachment
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public void sendMessage(String[] recipients, String sender, 
                            ClassPathResource resource, String bodyText,
                            String subject, String attachmentName)
    throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients);

        // use the default sending if no sender specified
        if (sender == null) {
            helper.setFrom(defaultFrom);
        } else {
           helper.setFrom(sender);
        }

        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, resource);

        ((JavaMailSenderImpl) mailSender).send(message);
    }
    
    
    /**
     * Convenience method for sending messages with Html contents.
     * 
     * @param recipients array of e-mail addresses
     * @param body html content in e-mail
     * @param subject subject of e-mail
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public void sendMessage(String[] recipients, String body, String subject)
    throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();

        Properties props = new Properties();
        
        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients);

        setFrom("EazyTec Notification <eazytecnotification@gmail.com>");

        // use the default sending if no sender specified
        helper.setFrom("EazyTec Notification <eazytecnotification@gmail.com>");
        
        message.setContent(body, "text/html");
        
        //helper.setText(bodyText);
        helper.setSubject(subject);
        ((JavaMailSenderImpl) mailSender).setHost(props.get("mail.host").toString());
        ((JavaMailSenderImpl) mailSender).setUsername(props.get("mail.username").toString());
        ((JavaMailSenderImpl) mailSender).setPassword(props.get("mail.password").toString());
        ((JavaMailSenderImpl) mailSender).setPort(Integer.parseInt(props.get("mail.port").toString()));
        ((JavaMailSenderImpl) mailSender).setProtocol(props.get("mail.transport.protocol").toString());
        System.out.println("------------"+((JavaMailSenderImpl) mailSender).getJavaMailProperties());
        
        ((JavaMailSenderImpl) mailSender).setJavaMailProperties(props);
        System.out.println("------------"+((JavaMailSenderImpl) mailSender).getJavaMailProperties());
        ((JavaMailSenderImpl) mailSender).send(message);
    }
    
    /**
     * Convenience method for sending messages with Html contents.
     * 
     * @param recipients array of e-mail addresses
     * @param body html content in e-mail
     * @param subject subject of e-mail
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public Map<String, Object> sendEmail(String[] recipients, String subject, String body )  {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try{
	        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
	        EmailConfiguration emailConfig = mailService.getEmailConfig();
	        Properties props = new Properties();
	        props.setProperty("mail.smtp.socketFactory.port",""+emailConfig.getMtaPort());
	        props.setProperty("mail.smtp.socketFactory.fallback","false");
			if(emailConfig.isMtaSecure()){
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.smtp.starttls.enable", "true");
			} else {
				props.setProperty("mail.smtp.socketFactory.class", "");
				props.setProperty("mail.smtp.starttls.enable", "false");
			}
	        
	        ((JavaMailSenderImpl) mailSender).setJavaMailProperties(props);
	        
	        ((JavaMailSenderImpl) mailSender).setProtocol(emailConfig.getMtaProtocol());
	        ((JavaMailSenderImpl) mailSender).setHost(emailConfig.getMtaAddress());
	        ((JavaMailSenderImpl) mailSender).setPort(emailConfig.getMtaPort());
	        /*((JavaMailSenderImpl) mailSender).setUsername(I18nUtil.getMailProperty("mail.username"));
	        ((JavaMailSenderImpl) mailSender).setPassword(I18nUtil.getMailProperty("mail.password"));*/
	        ((JavaMailSenderImpl) mailSender).setUsername(CommonUtil.getEmailNotificationId());
	        ((JavaMailSenderImpl) mailSender).setPassword(CommonUtil.getEmailNotificationPassword());
	        
	        // use the true flag to indicate you need a multipart message
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setTo(recipients);
	        if(StringUtil.isEmptyString(body)){
	        	body = "";
	        }
	        message.setContent(body, "text/html");
	        if(StringUtil.isEmptyString(subject)){
	        	subject = "(no subject)";
	        }
	        helper.setFrom(CommonUtil.getEmailNotificationId());
	        helper.setSubject(subject);
	        ((JavaMailSenderImpl) mailSender).send(message);
	        resultMap.put("sent", true);
	        resultMap.put("successMessage", "Email sent successfully.");
	        return resultMap;
    	}catch (Exception e) {
			log.error(e.getMessage(),e);
			resultMap.put("sent", false);
			resultMap.put("errorMessage", "Email sending failed - "+e.getMessage());
    		return resultMap;
		}
    }
}
