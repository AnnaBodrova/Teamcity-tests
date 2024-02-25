package com.example.teamcity.api;

import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildQueue;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

public class BuildQueueTest extends BaseApiTest {

    @Test
    public void buildCanBeAddedIntoQueue() {
        var testData = testDataStorage.addTestData();

        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project and build configuration", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());
        });

        step("Add build to the queue and check it's added successfully", ()-> {
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    public void buildWithNullIdCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project and build config", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());
        });

        step("Create build with null id and check it's not created", ()-> {
            testData.getBuild().getBuildType().setId(null);
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void buildEmptyStringIdCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project and build config", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());
        });

        step("Create build with null id and check it's not created", ()-> {
            testData.getBuild().getBuildType().setId("");
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
        });
    }

    @Test
    public void buildWithUnknownIdCannotBeAddedIntoQueue() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project and build config", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());
        });

        step("Create build with non-existing id and check it's not created", ()-> {
            testData.getBuild().getBuildType().setId(RandomData.getString());
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
        });
    }
}
