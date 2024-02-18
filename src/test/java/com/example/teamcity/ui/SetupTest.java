package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.FirstStartPage;
import org.testng.annotations.Test;

public class SetupTest extends BaseUITest {
    @Test
    public void setupTeamcityTest() {
        new FirstStartPage()
                .open()
                .setupTeamcity()
                .shouldHave(Condition.text("Create Administrator Account"));
    }
}
