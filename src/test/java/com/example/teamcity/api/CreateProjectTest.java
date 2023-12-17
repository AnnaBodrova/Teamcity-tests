package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseApiTest {
    @Test
    public void projectWithSameNameCannotBeCreated() {
        var testDataFirst = testDataStorage.addTestData();
        var testDataSecond = testDataStorage.addTestData();

        testDataSecond.getNewProjectDescription().setName(testDataFirst.getNewProjectDescription().getName());

        testDataSecond.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testDataSecond.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testDataSecond.getUser())).create(testDataFirst.getNewProjectDescription());
        new ProjectUnchecked(Specifications.getSpec().authSpec(testDataSecond.getUser()))
                .create(testDataSecond.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void projectWithSameIdCannotBeCreated() {
        var testDataFirst = testDataStorage.addTestData();
        var testDataSecond = testDataStorage.addTestData();

        testDataSecond.getNewProjectDescription().setId(testDataFirst.getNewProjectDescription().getId());

        testDataFirst.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testDataFirst.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testDataFirst.getUser())).create(testDataFirst.getNewProjectDescription());
        new ProjectUnchecked(Specifications.getSpec().authSpec(testDataFirst.getUser()))
                .create(testDataSecond.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void projectWithNoNameCannotBeCreated() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setName(null);

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void projectWithNoIdCanBeCreated() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setId(null);


        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project  = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

        soft.assertThat(project.getId())
                .isNotEmpty();
    }

    @Test
    // There is a bug, response is 500
    public void projectWithMaxLongId255CanBeCreated() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setId(RandomData.getStringOfLength(255));

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

        soft.assertThat(project.getId())
                .isEqualTo(testData.getNewProjectDescription().getId());
    }

    @Test
    public void projectWithIdMorethan255LenghtCannotCreated() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setId(RandomData.getStringOfLength(256));

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @Test
    public void projectIdCannotStartsWithUnderscore() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setId("_" + RandomData.getString());

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void projectIdCannotBeEmptyString() {
        var testData = testDataStorage.addTestData();
        testData.getNewProjectDescription().setId("");


        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void projectNameCannotBeEmptyString() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setName("");

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void projectIdCannotStartsWithDigits() {
        var testData = testDataStorage.addTestData();

        testData.getNewProjectDescription().setId("8" + RandomData.getString());


        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void projectIdContainsAllLatinSymbolsAndDigits() {
        var testData = testDataStorage.addTestData();
        testData.getNewProjectDescription().setId("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

        soft.assertThat(project.getId())
                .isEqualTo(testData.getNewProjectDescription().getId());
    }

    @Test
    public void projectNameContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        testData.getNewProjectDescription().setName(allAsciiSymbols);

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getNewProjectDescription())
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

        soft.assertThat(project.getName())
                .isEqualTo(testData.getNewProjectDescription().getName());
    }
}
