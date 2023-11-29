/* 
 *  Copyright 2012 CodeMagi, Inc.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.codemagi.util;

import java.io.File;
import java.util.*;
import java.util.regex.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides static methods for sending email and validating email addresses.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class EmailUtils {

    //Logger to view debug output
    static Logger log = LogManager.getLogger(EmailUtils.class);

    //regex for email address validation
    private static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile(".+@.+\\.[a-zA-Z]+");

    /**
     * Block constructor by design. EmailUtils is not meant to be instantiated.
     */
    private EmailUtils() {
    }

    /**
     * Sends an plaintext email
     */
    public static boolean sendEmail(String from, String to, String subject, String body, String smtpHost) {
        return sendEmail(from, to, subject, body, null, smtpHost, "");
    }

    /**
     * Sends an plaintext email
     */
    public static boolean sendEmail(String from, String to, String subject, StringBuffer body, String smtpHost) {
        return sendEmail(from, to, subject, body.toString(), null, smtpHost, "");
    }

    /**
     * Sends an plaintext email
     */
    public static boolean sendEmail(String from, String to, String subject, StringBuffer body, String smtpHost, String cc) {
        return sendEmail(from, to, subject, body.toString(), null, smtpHost, cc);
    }

    /**
     * Sends a plaintext email
     */
    public static boolean sendEmail(String from, String to, String subject, String body, String smtpHost, String cc) {
        return sendEmail(from, to, subject, body, null, smtpHost, cc);
    }

    /**
     * Sends an email with both plaintext and HTML possibilities
     */
    public static boolean sendEmail(String from, String to, String subject, StringBuffer body, StringBuffer htmlBody,
            String smtpHost, String cc) {

        String sBody = (body == null) ? "" : body.toString();
        String sHtmlBody = (htmlBody == null) ? "" : htmlBody.toString();

        return sendEmail(from, to, subject, sBody, sHtmlBody, smtpHost, cc);
    }

    /**
     * Sends an email with both plaintext and HTML possibilities
     */
    public static boolean sendEmail(String from, String to, String subject, String body, String htmlBody, String smtpHost, String cc) {

        Session session = null;

        Stopwatch timer = new Stopwatch();
        timer.start();

        try {
            //first try to get a mail session from JDNI
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Object oSession = envCtx.lookup("mail/Session");
            log.debug("oSession: " + oSession);
            session = (Session) oSession;
            log.debug("JNDI Session: " + session);

        } catch (Throwable e) {
	    //here we catch Throwable in case mail.jar/activation.jar are not 
            //installed at the SERVER LEVEL which will cause this JNDI lookup 
            //to fail with a NoClassDefFoundError
            log.debug("", e);
        }
        timer.stop();
        log.debug("Got JNDI Session in:" + timer.getElapsedTime());

        try {

            //if there is no JNDI session available, create our own
            if (session == null) {
                timer.start();

                // Get system properties 
                Properties props = System.getProperties();

                // Setup mail server  
                props.put("mail.smtp.host", smtpHost);

                // Get session          
                session = Session.getDefaultInstance(props, null);
                log.debug("Local Session: " + session);

                timer.stop();
                log.debug("Got Local Session in:" + timer.getElapsedTime());
            }

            // Define message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, to);
            if (!Utils.isEmpty(cc)) {
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            message.setSubject(subject);
            message.setText(body);
            if (htmlBody != null) {
                message.setContent(htmlBody, "text/html");
            }

            // Send message  
            Transport.send(message);

            return true;

        } catch (Exception e) {
            log.debug("", e);
            return false;
        }
    }

    /**
     * Sends an email with an attachment
     */
    public static boolean sendEmail(String from, String to, String subject, String body, String smtpHost, String cc, File attachment) {

        try {
            // Get system properties 
            Properties props = System.getProperties();

            // Setup mail server    
            props.put("mail.smtp.host", smtpHost);

            // Get session          
            Session session = Session.getDefaultInstance(props, null);

            // Define message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, to);
            if (!Utils.isEmpty(cc)) {
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            message.setSubject(subject);

            // Set the message text as the first MIME part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(body);

            // Set the attachment as the next MIME part
            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.attachFile(attachment);

            // create the Multipart and its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            message.setContent(mp);

            // Send message  
            Transport.send(message);

            return true;

        } catch (Exception e) {
            log.debug("", e);
            return false;
        }
    }

    /**
     * Returns the domain portion of an email address (the part after the @)
     */
    public static String getDomain(String emailAddress) {
        if (Utils.isEmpty(emailAddress)) {
            return "";
        }

        return StringUtils.substring(emailAddress, emailAddress.lastIndexOf("@") + 1);
    }

    /**
     * Checks for a valid email address, according to Internet Mail Consortium
     * RFC822 http://www.imc.orc/rfc822
     *
     * Update 2008-06-02: Also checks input against a regex to determine
     * validity
     *
     * @param paramToCheck the email address to verify
     * @return boolean true if the String is an email address, false otherwise
     */
    public static boolean isValidAddress(String paramToCheck) {
        log.debug("Validating email address: " + paramToCheck);
        try {
            InternetAddress address = new InternetAddress(paramToCheck);
            address.validate();

        } catch (AddressException ae) {
            log.debug("", ae);
            return false;
        }

        return EMAIL_ADDRESS_REGEX.matcher(paramToCheck).find(); //true;
    }

    /**
     * MAIN METHOD FOR TESTING ONLY!
     */
    public static void main(String[] args) {

        System.out.println("Domain for augsutd@codemagi.com: " + getDomain("augsutd@codemagi.com"));

        System.out.println("Sending test email to: " + args[0]);
        System.out.println("    Using SMTP server: " + args[1]);

        boolean success = sendEmail("test@codemagi.com", args[0], "EmailUtils.sendEmail() TEST", "Test successful", args[1]);

        if (success) {
            System.out.println("SUCCESS!");
        } else {
            System.out.println("FAILED!");
        }

    }

}
