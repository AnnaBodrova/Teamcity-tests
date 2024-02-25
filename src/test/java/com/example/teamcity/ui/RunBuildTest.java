package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

public class RunBuildTest extends BaseUITest {
    @Test
    public void authorizedUserShouldBeAbleToRunBuild()  {
        var url = "https://github.com/AlexPshe/spring-core-for-qa";
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        step("Create a new project", () -> {
            new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                    .createProjectByUrl(url)
                    .setupProject(testData.getNewProjectDescription().getName(), testData.getBuildType().getName());
        });

        step("Find and open created project", () ->{
            ProjectsPage projectsPage = new ProjectsPage().open();
            for (int i = 0; i < 3; i++) {
                try {
                    projectsPage.getLastSubproject().click();
                    break;
                } catch (StaleElementReferenceException e) {
                    Selenide.refresh();
                }
            }
        });

        step("Run build", ()->{
            new ProjectPage()
                    .runBuild()
                    .waitRunBlockShown()
                    .getStatusDescription().shouldHave(Condition.text("Build #1 is Running"));
        });
    }
}


