package com.ldal.pigeonapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimer
{
    public static String localDateTime()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formatted = now.format(formatter);

        System.out.println(formatted);
        return  formatted;
    }
}
