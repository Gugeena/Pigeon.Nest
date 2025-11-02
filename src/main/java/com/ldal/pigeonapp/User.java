package com.ldal.pigeonapp;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private int id;
    private String login;
    private String email;
    private boolean isBanned;

    static final String[] nonowords = {"niggas","bitch","nigga","nigger","niggers","megreli","dick","cock","pussy","cunt","genital",
            "fuck","shit","piss","faggot","retard","andria","ass","titties","boobs","boob","tit","slut","whore","slave",
            "cum","job","sucker","sperm","vagina"};

    @Serial
    private final static long serialVersionUID = 7032;

    ArrayList<Email> receivedEmails;

    User(int id, String login, String email){
        setId(id);
        setLogin(login);
        setEmail(email);
        setBanned(false);
    }

    User(int id, String login, String email, boolean isBanned)
    {
        setId(id);
        setLogin(login);
        setEmail(email);
        setBanned(isBanned);
    }

    public static boolean validateLogin(String login)
    {
        String regex1 = "[^a-zA-Z0-9_-]";
        boolean valid = true;

        validateLoop:
        for (int i = 0; i < login.length(); i++)
        {
            if ((login.charAt(i) + "").matches(regex1))
            {
                valid = false;
                break validateLoop;
            }
        }

        validateLoop1:
        for (int i = 0; i < nonowords.length; i++)
        {
            if (login.toLowerCase().contains(nonowords[i]))
            {
                valid = false;
                break validateLoop1;
            }
        }

        if(valid) return login.length() >= 4 && login.length() <= 20;
        else return false;
    }

    public static boolean validateEmail(String email)
    {
        String regex1 = "[^a-zA-Z0-9_-]";
        boolean valid = true;

        validateLoop:
        for (int i = 0; i < email.length(); i++) {
            if((email.charAt(i) + "").matches(regex1)) {valid = false; break validateLoop;}
        }

        validateLoop1:
        for (int i = 0; i < nonowords.length; i++)
        {
            if (email.toLowerCase().contains(nonowords[i]))
            {
                valid = false;
                break validateLoop1;
            }
        }
        if(valid) return email.length() >= 4 && email.length() <= 20;
        else return false;
    }

    public static boolean validatePassword(String password) {
        String regex1 = "[^a-zA-Z0-9_!@#$%^&*()+/{}~<>,.:-]";
        boolean valid = true;

        validateLoop:
        for (int i = 0; i < password.length(); i++) {
            if((password.charAt(i) + "").matches(regex1)) {valid = false; break validateLoop;}
        }
        if(valid) return password.length() >= 8 && password.length() <= 25;
        else return false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) throws NegativeIDException{
        if(id < -1) {
            throw new NegativeIDException("User ID cannot be negative!");
        }
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isAdmin(){
        return false;
    }



    @Override
    public String toString() {
        return "-----" + login + "-----" +
                "\nUser ID : " + id +
                "\nEmail : " + email +
                "\nIsBanned : " + isBanned;
    }
}
