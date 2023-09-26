package com.inn.cafe.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
	 @Autowired
	    private JavaMailSender emailSender;

	    public void sendSimpleEmail(String to,
	                                String subject,
	                                String text,
	                                List<String> list
	    ) {
	        SimpleMailMessage message = new SimpleMailMessage();
	       // message.setFrom("sony@gmail.com");
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);
	        if(list != null && list.size() > 0)
	    	       message.setCc(getCcArray(list));
	        //message.
	        emailSender.send(message);
	        System.out.println("Email Send to ..."+to);
	        //System.out.println("Mail Send...");
	        String temp[]=getCcArray(list);
	        for(int i=0; i< temp.length;i++) {
	        	 System.out.println("Cc Send to ..."+temp[i]);
	        }
	        }


	

		public void forgotMail(String to, String subject, String password) throws MessagingException{
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setFrom("bhadanes4384@gmail.com");
			helper.setTo(to);
			helper.setSubject(subject);
			String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
			message.setContent(htmlMsg,"text/html");
			emailSender.send(message);
			 System.out.println("Email Send to ..."+to);
		}
	    
	    private String[] getCcArray(List<String> ccList) {
			String [] cc = new String[ccList.size()];
			
			for(int i=0; i< ccList.size();i++) {
			cc[i] = ccList.get(i);
			}
			return cc;
		}

}
