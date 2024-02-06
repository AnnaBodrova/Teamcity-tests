package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.FirstStartPage;
import org.testng.annotations.Test;

public class SetupTest extends BaseUITest {
    @Test
    public void setupTeamcityTest() throws InterruptedException {
        new FirstStartPage().open()
                .proceed()
                .waitInitializing()
                .proceedDataBaseConnection()
                .waitTeamcityStarted()
                .waitLisenceHeader()
                .setAgreementTrue()
                .clickContinue();

    }
}
