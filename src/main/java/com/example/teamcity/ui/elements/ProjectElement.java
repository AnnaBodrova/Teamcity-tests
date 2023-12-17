package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

@Getter
public class ProjectElement extends PageElement {
    private final SelenideElement header;
    private final SelenideElement icon;
    private final SelenideElement link;

    public ProjectElement(SelenideElement selenideElement) {
        super(selenideElement);
        this.header = findElement(Selectors.byDataTest("subproject"));
        this.icon = findElement("svg");
        this.link = findElement(Selectors.byDataTest("ring-link"));
    }

    public void openProject() {
        this.link.click();
    }
}
