package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;


public class RolesTest extends BaseApiTest {
    @Test
    public void unauthorizedUser() {
        var testData = testDataStorage.addTestData();
        uncheckedWithSuperUser.getProjectRequest()
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.equalTo("Authentication required\n" +
                        "To login manually go to \"/login.html\" page"));
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getNewProjectDescription().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + testData.getNewProjectDescription().getId()));
    }

    @Test
    public void systemAdminUser() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        soft.assertThat(project.getId()).isEqualTo(testData.getNewProjectDescription().getId());

    }

    @Test
    public void projectAdminUserShouldHaveRightsToCreateBuildConfigToHisProject() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + testData.getNewProjectDescription().getId()));
        checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        //var testData = new TestDataGenerator().generator();
        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());
        soft.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());

    }

    @Test
    public void projectAdminUserAnotherProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        new CheckedRequests(Specifications.getSpec().authSpec(firstTestData.getUser()));
        new CheckedRequests(Specifications.getSpec().authSpec(secondTestData.getUser()));

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getNewProjectDescription());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getNewProjectDescription());

        firstTestData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + firstTestData.getNewProjectDescription().getId()));
        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());


        secondTestData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + secondTestData.getNewProjectDescription().getId()));
        checkedWithSuperUser.getUserRequest().create(secondTestData.getUser());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);


    }

}
