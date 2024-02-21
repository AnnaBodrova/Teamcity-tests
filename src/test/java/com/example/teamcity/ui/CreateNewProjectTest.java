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
import org.testng.annotations.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;


public class CreateNewProjectTest extends BaseUITest {
    private String url = "https://github.com/AlexPshe/spring-core-for-qa";

    @Test
    public void authorizedUserShouldBeAbleCreateNewProjectAndBuildConfig() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getNewProjectDescription().getName(), testData.getBuildType().getName());

//        new ProjectsPage().open()
//                .getSubprojects()
//                .stream().reduce((first, second) -> second).get()
//                .getHeader().shouldHave(Condition.text(testData.getNewProjectDescription().getName()));
        new ProjectsPage().open();
        $$(".Subproject__container--Px").get(0).shouldHave(Condition.text(testData.getNewProjectDescription().getName()));

        var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getNewProjectDescription().getName());
        soft.assertThat(project.getId()).isNotEmpty();
        soft.assertThat(project.getName()).isEqualTo(testData.getNewProjectDescription().getName());
        soft.assertThat(project.getParentProjectId()).isEqualTo(testData.getNewProjectDescription().getParentProject().getLocator());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getBuildType().getName());
        soft.assertThat(buildConfig.getId()).isNotEmpty();
        soft.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildType().getName());
        soft.assertThat(buildConfig.getProject().getName()).isEqualTo(testData.getBuildType().getProject().getName());
        soft.assertThat(buildConfig.getSteps().getStep()).isNull();
    }

    @Test
    public void buildConfigCannotBeCreatedWithoutDefaultBranch() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getLocator())
                .createProjectByUrl(url)
                .clearDefaultBranchInput()
                .setupProjectWithError(testData.getNewProjectDescription().getName(), testData.getBuildType().getName())
                .getDefaultBranchError().shouldHave(Condition.text("Branch name must be specified"))
        ;

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getNewProjectDescription().getName()).then().statusCode(HttpStatus.SC_NOT_FOUND);

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getBuildType().getName()).then().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
