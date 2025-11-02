package com.ldal.pigeonapp;

import java.util.ArrayList;
import java.util.Comparator;

public class Miraclesort
{
    private ArrayList<Integer> pleasesort = new ArrayList<>();

    public boolean Miraclesort()
    {
        while(true)
        {
            if (isSorted(pleasesort)) return true;
        }
    }

    public static boolean isSorted(ArrayList<Integer> list)
    {
        for (int i = 0; i < list.size() - 1; i++)
        {
            if (list.get(i) > list.get(i + 1))
            {
                return false;
            }
        }
        return true;
    }
}
