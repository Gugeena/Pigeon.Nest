package com.ldal.pigeonapp;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GMAILEmailsender
{
    static Boolean haswentthrough = false;
    private static Session session;

    private static final String FROM_EMAIL = "lashag.gugenishvili@gmail.com";
    private static final String PASSWORD = "xltt nqmk csza akaw";

    static
    {
        if(session == null)
        {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            session = Session.getInstance(properties, new jakarta.mail.Authenticator()
            {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            });
        }
    }

    public static void EmailSender(String toEmail, String recoveryPass, String confcode, int scenario, String username)
    {
        try
        {
            Message messege = new MimeMessage(session);
            messege.setFrom(new InternetAddress(FROM_EMAIL));
            messege.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            if(scenario == 0)
            {
                messege.setSubject("Welcome to pigeon!");
                messege.setText("Thank you for signing up for Pigeon " + username + "! Here's your recoverypass: " + recoveryPass);
            }
            else if(scenario == 1)
            {
                messege.setSubject("Your close to linking up your gmail!");
                messege.setText("Here's your confirmation code: " + confcode);
            }
            else if(scenario == 2)
            {
                messege.setSubject("You have successfully linked your gmail!");
                messege.setText("Greetings " + username + "! You have linked up pigeon account with gmail successfully");
            }
            else if(scenario == 3)
            {
                messege.setSubject("New Recovery Password");
                messege.setText("Your new Recovery Password: " + recoveryPass);
            }
            Transport.send(messege);
            haswentthrough = true;
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e)
        {
            haswentthrough = false;
            e.printStackTrace();
        }
    }
}
