package com.ldal.pigeonapp;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class GMAILEmailsender
{
    public static void EmailSender(String toEmail, String recoveryPass, String confcode, int scenario, String username)
    {
        final String fromEmail = "randomaltaki@gmail.com";
        final String password = "wego zcpt aiuh epaj";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try
        {
            if(scenario == 0)
            {
                Message messege = new MimeMessage(session);
                messege.setFrom(new InternetAddress(fromEmail));
                messege.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                messege.setSubject("Welcome to pigeon!");
                messege.setText("Thank you for signing up for Pigeon " + username + "! Here's your recoverypass: " + recoveryPass);

                Transport.send(messege);
            }
            else if(scenario == 1)
            {
                Message messege = new MimeMessage(session);
                messege.setFrom(new InternetAddress(fromEmail));
                messege.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                messege.setSubject("Your close to linking up your gmail!");
                messege.setText("Here's your confirmation code: " + confcode);

                Transport.send(messege);
            }
            else if(scenario == 2)
            {
                Message messege = new MimeMessage(session);
                messege.setFrom(new InternetAddress(fromEmail));
                messege.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                messege.setSubject("You have successfully linked your gmail!");
                messege.setText("Greetings " + username + "! You have linked up pigeon account with gmail successfully");

                Transport.send(messege);
            }
            else if(scenario == 3)
            {
                Message messege = new MimeMessage(session);
                messege.setFrom(new InternetAddress(fromEmail));
                messege.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                messege.setSubject("New Recovery Password");
                messege.setText("Your new Recovery Password: " + recoveryPass);

                Transport.send(messege);
            }
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
