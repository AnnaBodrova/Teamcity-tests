package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

public class RunBuildTest extends BaseUITest {
    @Test
    public void authorizedUserShouldBeAbleToRunBuild()  {
        var url = "https://github.com/AlexPshe/spring-core-for-qa";
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getNewProjectDescription().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .openProject();

        new ProjectPage()
                .runBuild()
                .waitRunBlockShown()
                .getStatusDescription().shouldHave(Condition.text("Build #1 is Running"));
    }
}


