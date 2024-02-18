package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.ProjectElement;
import com.example.teamcity.ui.pages.favorites.FavoritePage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.elements;

public class ProjectsPage extends FavoritePage {
    private static final String FAVORITE_PROJECTS_PAGE_URL = "/favorite/projects";

    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));

    public ProjectsPage open() {
        Selenide.open(FAVORITE_PROJECTS_PAGE_URL);
        waitUntilFavouritePageIsLoaded();
        return this;
    }
    public List<ProjectElement> getSubprojects() {
        subprojects.get(0).shouldBe(Condition.visible, Duration.ofSeconds(10));
        return generatePageElements(subprojects, ProjectElement::new);
    }
}
