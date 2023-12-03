package com.example.teamcity.api;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected SoftAssertions soft;

@BeforeMethod
    public void beforeTest(){
soft = new SoftAssertions();
    }
@AfterMethod
    public void afterTest(){
    soft.assertAll();
    }

}
