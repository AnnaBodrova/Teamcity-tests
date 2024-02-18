package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByAttribute;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

@Getter
public class ProjectPage extends Page {
    private SelenideElement editProjectButton = element(Selectors.byTitle("Edit project..."));
    private SelenideElement runBuildButton = element(Selectors.byDataTest("run-build"));
    private SelenideElement statusDescription = element(Selectors.byClass("ring-message-description"));
    private SelenideElement runBlock = element(new ByAttribute("aria-label", "Build #1"));

    public ProjectPage openProjectEditPage() {
        editProjectButton.click();
        return this;
    }

    public ProjectPage runBuild() {
        runBuildButton.shouldBe(Condition.interactable, Duration.ofSeconds(10));
        runBuildButton.click();
        return this;
    }

    public ProjectPage waitMessageDescriptionShown() {
        statusDescription.shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }

    public ProjectPage waitRunBlockShown() {
        runBlock.shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }

}
