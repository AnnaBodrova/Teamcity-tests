package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

public class CreateProjectTest extends BaseApiTest {
    @Test
    public void projectWithSameNameCannotBeCreated() {
        var testDataFirst = testDataStorage.addTestData();
        var testDataSecond = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testDataSecond.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testDataSecond.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testDataSecond.getUser())).create(testDataFirst.getNewProjectDescription());
        });

        step("Try to create  project with same name and check it's not created", ()-> {
            testDataSecond.getNewProjectDescription().setName(testDataFirst.getNewProjectDescription().getName());

            new ProjectUnchecked(Specifications.getSpec().authSpec(testDataSecond.getUser()))
                    .create(testDataSecond.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void projectWithSameIdCannotBeCreated() {
        var testDataFirst = testDataStorage.addTestData();
        var testDataSecond = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testDataSecond.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testDataSecond.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testDataSecond.getUser())).create(testDataFirst.getNewProjectDescription());
        });

        step("Try to create  project with same id and check it's not created", ()-> {
            testDataSecond.getNewProjectDescription().setId(testDataFirst.getNewProjectDescription().getId());
            new ProjectUnchecked(Specifications.getSpec().authSpec(testDataSecond.getUser()))
                    .create(testDataSecond.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void projectWithNoNameCannotBeCreated() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with null name and check it's not created", ()-> {
            testData.getNewProjectDescription().setName(null);
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void projectWithNoIdCanBeCreated() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with id null and check it's not created", ()-> {
            testData.getNewProjectDescription().setId(null);
            var project  = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

            soft.assertThat(project.getId())
                    .isNotEmpty();
        });
    }

    @Test
    // There is a bug, response is 500
    public void projectWithMaxLongId255CanBeCreated() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with id 255 symbols and check it's created", ()-> {
            testData.getNewProjectDescription().setId(RandomData.getStringOfLength(255));
            var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

            soft.assertThat(project.getId())
                    .isEqualTo(testData.getNewProjectDescription().getId());
        });
    }

    @Test
    public void projectWithIdMorethan255LenghtCannotCreated() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with id 256 symbols and check it's not created", ()-> {
            testData.getNewProjectDescription().setId(RandomData.getStringOfLength(256));
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }


    @Test
    public void projectIdCannotStartsWithUnderscore() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with id starting with _ and check it's not created", ()-> {
            testData.getNewProjectDescription().setId("_" + RandomData.getString());
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void projectIdCannotBeEmptyString() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with blank id and check it's not created", ()-> {
            testData.getNewProjectDescription().setId("");
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void projectNameCannotBeEmptyString() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with blank name and check it's not created", ()-> {
            testData.getNewProjectDescription().setName("");
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void projectIdCannotStartsWithDigits() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with id starting with digit and check it's not created", ()-> {
            testData.getNewProjectDescription().setId("8" + RandomData.getString());
            new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void projectIdContainsAllLatinSymbolsAndDigits() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with is containing all latin symbols amd digits  symbols and check", ()-> {
            testData.getNewProjectDescription().setId("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
            var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

            soft.assertThat(project.getId())
                    .isEqualTo(testData.getNewProjectDescription().getId());
        });
    }

    @Test
    public void projectNameContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project with name containing all ascii symbols and check", ()-> {
            testData.getNewProjectDescription().setName(allAsciiSymbols);
            var project = new ProjectUnchecked(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getNewProjectDescription())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK).extract().as(NewProjectDescription.class);

            soft.assertThat(project.getName())
                    .isEqualTo(testData.getNewProjectDescription().getName());
        });
    }
}
