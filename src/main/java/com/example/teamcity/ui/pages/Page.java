package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.PageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.element;

public abstract class Page {
    private SelenideElement submitButton = element(Selectors.byType("submit"));
    private SelenideElement savingWaitMarker = element(Selectors.byId("saving"));
    private SelenideElement pageWaitingMarker = element(Selectors.byDataTest("ring-loader"));

    public void submit() {
        submitButton.click();
        waitUntilDataIsSaved();
    }

    public void waitUntilDataIsSaved() {
        savingWaitMarker.shouldBe(Condition.not(Condition.visible), Duration.ofSeconds(30));
    }

    public void waitUntilPageIsLoaded() {
        pageWaitingMarker.shouldNotBe((Condition.visible), Duration.ofMinutes(1));
    }

    public <T extends PageElement> List<T> generatePageElements(ElementsCollection collection, Function<SelenideElement, T> creator) {
        var elements = new ArrayList<T>();
        collection.forEach(webElement -> {
                    elements.add(creator.apply(webElement));
                }
        );
        return elements;
    }
}
