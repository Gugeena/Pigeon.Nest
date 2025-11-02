package com.ldal.pigeonapp;

import java.util.ArrayList;
import java.util.Random;

public class PassHasher
{
    public String hasher(String password)
    {
        StringBuilder stringBuilder = new StringBuilder(password);
        String hashedpassword = AsciiMult(stringBuilder.toString(), Asciier(stringBuilder.toString()));
        return hashedpassword;
    }

    private int Asciier(String password)
    {
        ArrayList<Character> passwordindchars = new ArrayList<>();
        for(char c : password.toCharArray())
        {
            passwordindchars.add(c);
        }
        ArrayList<Integer> Indasci = new ArrayList<>();
        for(int i = password.length() - 1; i > -1;i--)
        {
            Indasci.add(Integer.valueOf(passwordindchars.get(i) * (i + 1)));
        }
        Integer sum = Indasci.stream().reduce(0, (tmp1, tmp2) -> tmp1 + tmp2);
        return sum;
    }

    private String AsciiMult(String password, long ascivalue)
    {
        for(int i = 0; i < password.length(); i++)
        {
            ascivalue = ascivalue + (ascivalue + Integer.valueOf(password.charAt(i)));
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(ascivalue));
        stringBuilder.reverse();
        String result = stringBuilder.toString();
        return result;
    }

    public String backuppassword()
    {
        String result = "";
        char[] greenlitchars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890_!@#$%&*\";:<>-+=()".toCharArray();
        for(int i = 0; i < 12; i++)
        {
            Random random = new Random();
            int h = random.nextInt(70);

            result += String.valueOf(greenlitchars[h]);
        }
        return result;
    }
}
