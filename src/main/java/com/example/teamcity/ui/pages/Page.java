package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class Page {
    private SelenideElement submitButton = element(Selectors.byType("submit"));
    private SelenideElement savingWaitMarker = element(Selectors.byId("submit"));
public void submit(){
    submitButton.click();
    waitUntilDataIsSaved();
}
    public void waitUntilDataIsSaved() {
        savingWaitMarker.shouldBe(Condition.not(Condition.visible), Duration.ofSeconds(30));
    }

}
