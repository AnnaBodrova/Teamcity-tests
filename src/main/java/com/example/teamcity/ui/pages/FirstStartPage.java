package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class FirstStartPage extends Page{
    private static final String START_PAGE_URL = "/";
    private SelenideElement proceedButton = element(Selectors.byId("proceedButton"));
    private SelenideElement connectionSetupHeader = element(Selectors.byHeader("Database connection setup"));
    private SelenideElement teamcityStartingHeader = element(Selectors.byHeader("TeamCity is starting"));
    private SelenideElement licenseAgreementHeader = element(Selectors.byHeader("License Agreement for JetBrains"));
    private SelenideElement licenseAcceptCheckbox = element(Selectors.byId("accept"));
    private SelenideElement continueButton = element(Selectors.byType("submit"));
    private SelenideElement createAdministratorAccountHeader = element(Selectors.byHeader("Create Administrator Account"));

    public FirstStartPage open() {
        Selenide.open(START_PAGE_URL);
        return this;

    }
    public FirstStartPage proceed() {
        proceedButton.click();
        return this;
    }

    public FirstStartPage waitInitializing(){
        connectionSetupHeader.shouldBe(Condition.visible, Duration.ofSeconds(30));
        return this;
    }

    public FirstStartPage proceedDataBaseConnection() {
        proceedButton.click();
        return this;
    }

    public FirstStartPage waitTeamcityStarted(){
        teamcityStartingHeader.shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }

    public FirstStartPage waitLisenceHeader() throws InterruptedException {
        licenseAgreementHeader.shouldBe(Condition.visible, Duration.ofSeconds(60));
        return this;
    }

    public FirstStartPage setAgreementTrue(){
        licenseAcceptCheckbox.click();
        return this;
    }

    public FirstStartPage clickContinue(){
        continueButton.click();
        return this;
    }


}
