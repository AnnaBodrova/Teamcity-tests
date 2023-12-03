package com.example.teamcity.api.generator;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    private static final int LENGHT = 10;
    public static String getString(){
        return "test_"+ RandomStringUtils.randomAlphabetic(LENGHT);
    }
    public static String getStringOfLength(int l){
        return "test_"+ RandomStringUtils.randomAlphabetic(l-5);
    }
}
