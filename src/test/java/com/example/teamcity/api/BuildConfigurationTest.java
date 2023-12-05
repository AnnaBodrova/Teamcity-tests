package com.example.teamcity.api;

import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;


public class BuildConfigurationTest extends BaseApiTest {

    @Test
    public void buildConfigurationMappingTest() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        soft.assertThat(project.getId()).isEqualTo(testData.getNewProjectDescription().getId());


        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());
        soft.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
        soft.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
        soft.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildType().getName());
        soft.assertThat(buildConfig.getProject().getId()).isEqualTo(testData.getBuildType().getProject().getId());
        soft.assertThat(buildConfig.getSteps().getStep().get(0).getName())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getName());
        soft.assertThat(buildConfig.getSteps().getStep().get(0).getType())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getType());
        soft.assertThat(buildConfig.getSteps().getStep().get(0).getProperties().getProperty().get(0).getName())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getProperties().getProperty().get(0).getName());
        soft.assertThat(buildConfig.getSteps().getStep().get(0).getProperties().getProperty().get(0).getValue())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getProperties().getProperty().get(0).getValue());
        soft.assertThat(buildConfig.getParameters().getProperty().get(0).getName())
                .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getName());
        soft.assertThat(buildConfig.getParameters().getProperty().get(0).getValue())
                .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getValue());
    }

    @Test
    public void buildConfigurationWithoutNameCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setName(null);

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void buildConfigurationWithId255lengthCreated() {// there is a bug, returns 500
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().setId(RandomData.getStringOfLength(255));

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void buildConfigurationWithId256lengthCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().setId(RandomData.getStringOfLength(256));

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

    @Test
    public void buildConfigurationIdContainsAllLatinSymbolsAndDigits() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().setId("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");


        var buildType = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());

    }

    @Test
    public void buildConfigurationIdCannotStartsWithUnderScore() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setId("_" + RandomData.getString());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);


    }

    @Test
    public void buildConfigurationNameContainsAllAsciiCharacters() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setName(" !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");

        var buildType = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildType.getName()).isEqualTo(testData.getBuildType().getName());

    }

    @Test
    public void buildConfigurationIdCannotStartsWithDigits() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setId("0" + RandomData.getString());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);


    }

    @Test
    public void buildConfigurationIdCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setId("");

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

    @Test
    public void buildConfigurationNameCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setName("");

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void buildConfigurationCannotBeCreatedWithoutProject() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());


        testData.getBuildType().setProject(null);

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void buildConfigurationPropertyNameContainsAllAscciiSymbols() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().getParameters().getProperty().get(0).setName(" !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");

        var buildtype = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildtype.getParameters().getProperty().get(0).getName())
                .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getName());

    }

    @Test
    public void buildConfigurationPropertyValueContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().getParameters().getProperty().get(0).setValue(" !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");

        var buildtype = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildtype.getParameters().getProperty().get(0).getValue())
                .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getValue());

    }

    @Test
    public void buildConfigurationStepNameContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().getSteps().getStep().get(0).setName(" !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");

        var buildtype = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildtype.getSteps().getStep().get(0).getName())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getName());
    }

    @Test
    public void buildConfigurationStepTypeContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().getSteps().getStep().get(0).setType(" !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");

        var buildtype = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(BuildType.class);

        soft.assertThat(buildtype.getSteps().getStep().get(0).getType())
                .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getType());


    }

    @Test
    public void buildConfigurationCannotBeCreatedForUnknownProjectId() {
        var testData = testDataStorage.addTestData();
        new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());

        testData.getBuildType().getProject().setId(RandomData.getString());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);


    }

}