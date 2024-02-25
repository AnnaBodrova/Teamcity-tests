package com.example.teamcity.ui;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.elements.ProjectElement;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.apache.http.HttpStatus;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;


public class CreateNewProjectTest extends BaseUITest {
    private String url = "https://github.com/AlexPshe/spring-core-for-qa";

    @Test
    public void authorizedUserShouldBeAbleCreateNewProjectAndBuildConfig() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        step("Create a new project", ()->{
            new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                    .createProjectByUrl(url)
                    .setupProject(testData.getNewProjectDescription().getName(), testData.getBuildType().getName());
        });

        step("Find created project in projects list", ()->{
            ProjectsPage projectsPage = new ProjectsPage().open();
            for (int i = 0; i < 3; i++ ) {
                try {
                    projectsPage.getLastSubproject().shouldHave(Condition.text(testData.getNewProjectDescription().getName()));
                    break;
                } catch (StaleElementReferenceException e) {
                    Selenide.refresh();
                }
            }
        });

        step("Check that project has been created with all attributes", ()->{
            var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .get(testData.getNewProjectDescription().getName());
            soft.assertThat(project.getId()).isNotEmpty();
            soft.assertThat(project.getName()).isEqualTo(testData.getNewProjectDescription().getName());
            soft.assertThat(project.getParentProjectId()).isEqualTo(testData.getNewProjectDescription().getParentProject().getLocator());
        });


        step("Check that build config has been created with all attributes", ()->{
            var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .get(testData.getBuildType().getName());
            soft.assertThat(buildConfig.getId()).isNotEmpty();
            soft.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildType().getName());
            soft.assertThat(buildConfig.getProject().getName()).isEqualTo(testData.getBuildType().getProject().getName());
            soft.assertThat(buildConfig.getSteps().getStep()).isNull();
        });
    }

    @Test
    public void buildConfigCannotBeCreatedWithoutDefaultBranch() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        step("Create new project with empty branch", ()->{
            new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                    .createProjectByUrl(url)
                    .clearDefaultBranchInput()
                    .setupProjectWithError(testData.getNewProjectDescription().getName(), testData.getBuildType().getName())
                    .getDefaultBranchError().shouldHave(Condition.text("Branch name must be specified"))
            ;
        });

        step("Check that project was not created", ()->{
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .get(testData.getNewProjectDescription().getName()).then().statusCode(HttpStatus.SC_NOT_FOUND);
        });

        step("Check that biuld config was not created", ()->{
            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .get(testData.getBuildType().getName()).then().statusCode(HttpStatus.SC_NOT_FOUND);
        });

    }
}
