package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;
@Getter
public class CreateNewProject extends Page {
    private SelenideElement urlInput = element(Selectors.byId("url"));
    private SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));
    private SelenideElement buildDefaultBranchInput = element(Selectors.byId("branch"));
    private SelenideElement defaultBranchError = element(Selectors.byId("error_branch"));

    public CreateNewProject open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }

    public CreateNewProject createProjectByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    public void setupProject(String projectName, String builTypeName){
        projectNameInput.shouldBe(Condition.visible, Duration.ofSeconds(10));
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);

        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(builTypeName);
        submit();
    }

    public CreateNewProject setupProjectWithError(String projectName, String builTypeName){
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);

        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(builTypeName);
        submit();
        return this;
    }

    public CreateNewProject clearDefaultBranchInput(){
        buildDefaultBranchInput.clear();
        return this;
    }
}
