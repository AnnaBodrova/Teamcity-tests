package com.example.teamcity.api;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected SoftAssertions soft;
    public static final String allAsciiSymbols = " !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    public TestDataStorage testDataStorage;
    public CheckedRequests checkedWithSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    public UncheckedRequests uncheckedWithSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());


    @BeforeMethod
    public void beforeTest() {
        soft = new SoftAssertions();
        testDataStorage = TestDataStorage.getTestDataStorage();
    }
@AfterMethod
    public void afterTest(){
    soft.assertAll();
    }

}
