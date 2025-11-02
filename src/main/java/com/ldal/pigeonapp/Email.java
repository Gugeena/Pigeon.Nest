package com.ldal.pigeonapp;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Email implements Serializable
{
    private User sender, receiver;
    private String subject, text;
    private Media attachment;
    private String dateTime;

    @Serial
    private static final long serialVersionUID = 2009L;

    Email(User sender, User receiver, String text, String subject)
    {
        setSender(sender);
        setReceiver(receiver);
        setText(text);
        setSubject(subject);
    }

    Email(){

    }


    public static String dateTimeToString(LocalDateTime localDateTime){
        String month = "" + localDateTime.getMonthValue();
        if(localDateTime.getMonthValue() < 10) month = "0" + month;

        String day = "" + localDateTime.getDayOfMonth();
        if(localDateTime.getDayOfMonth() < 10) day = "0" + day;

        String minute = "" + localDateTime.getMinute();
        if(localDateTime.getMinute() < 10) minute = "0" + minute;

        String hour = "" + localDateTime.getHour();
        if(localDateTime.getHour() < 10) hour = "0" + hour;

        return localDateTime.getYear() + "-" + month + "-" + day + " " + hour + ":" + minute;
    }



    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Media getAttachment() {
        return attachment;
    }

    public void setAttachment(Media attachment) {
        this.attachment = attachment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString()
    {
        return "To : " + receiver.getEmail() + ";" +
                "\nSubject : " + subject +
                "\nAt : " + dateTime +
                "\n---------------------------\n" +
                text +
                "\n---EMAIL-END---";

    }

    public long minutesAgo()
    {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));

        LocalDateTime sentTime = LocalDateTime.of(year, month, day, hour, minute);

        LocalDateTime localDateTime = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(sentTime, localDateTime);
    }
}
