package com.example.teamcity.api;

import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildQueue;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BuildQueueTest extends BaseApiTest {

    @Test
    public void buildCanBeAddedIntoQueue() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());

        new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
        ;


    }

    @Test
    public void buildWithNullIdCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        testData.getBuild().getBuildType().setId(null);
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());

        new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
        ;
    }

    @Test
    public void buildEmptyStringIdCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        testData.getBuild().getBuildType().setId("");
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());

        new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                .then()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
        ;
    }

    @Test
    public void buildWithUnknownIdCannotBeAddedIntoQueue() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        testData.getBuild().getBuildType().setId(RandomData.getString());
        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuildType());

        new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                .then()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
        ;


    }
}
