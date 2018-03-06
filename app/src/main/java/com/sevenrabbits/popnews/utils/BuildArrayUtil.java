package com.sevenrabbits.popnews.utils;

/**
 * Created by elena on 6/28/17.
 */

public class BuildArrayUtil {

    public static String [] buildDataArray(String data){
        String [] oneDataArray;
        oneDataArray = data.split("\n");
        return oneDataArray;
    }
}
