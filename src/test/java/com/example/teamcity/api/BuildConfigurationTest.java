package com.example.teamcity.api;

import com.example.teamcity.api.generator.RandomData;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;


public class BuildConfigurationTest extends BaseApiTest {

    @Test
    public void buildConfigurationMappingTest() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project and check it's created with correct id", ()-> {
            var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            soft.assertThat(project.getId()).isEqualTo(testData.getNewProjectDescription().getId());
        });

        step("Create build config and check mapping", ()-> {
            var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());

            soft.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
            soft.assertThat(buildConfig.getName()).isEqualTo(testData.getBuildType().getName());
            soft.assertThat(buildConfig.getProject().getId()).isEqualTo(testData.getBuildType().getProject().getId());
            soft.assertThat(buildConfig.getParameters()).isEqualTo(testData.getBuildType().getParameters());
            soft.assertThat(buildConfig.getSteps()).isEqualTo(testData.getBuildType().getSteps());
        });
    }

    @Test
    public void buildConfigurationWithoutNameCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Try to create build config without name and check it's not created", ()-> {
            testData.getBuildType().setName(null);

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void buildConfigurationWithId255lengthCreated() {// there is a bug, returns 500
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with id lenght 255. Check it's created successfully", ()-> {
            testData.getBuildType().setId(RandomData.getStringOfLength(255));

            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
        });
    }

    @Test
    public void buildConfigurationWithId256lengthCannotBeCreated() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with id lenght 256. Check it's not created", ()-> {
            testData.getBuildType().setId(RandomData.getStringOfLength(256));

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void buildConfigurationIdContainsAllLatinSymbolsAndDigits() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });
        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with id containing all latin symbols. Check it's created with all symbols", ()-> {
            testData.getBuildType().setId("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");

            var buildType = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
            soft.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());
        });
    }

    @Test
    public void buildConfigurationIdCannotStartsWithUnderScore() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with id starting with_. Check it's not created", ()-> {
            testData.getBuildType().setId("_" + RandomData.getString());

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void buildConfigurationNameContainsAllAsciiCharacters() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with name containing all ascii. Check it's created", ()-> {
            testData.getBuildType().setName(allAsciiSymbols);

            var buildType = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
            soft.assertThat(buildType.getName()).isEqualTo(testData.getBuildType().getName());
        });
    }

    @Test
    public void buildConfigurationIdCannotStartsWithDigits() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with id starting with digit. Check it's not created", ()-> {
            testData.getBuildType().setId("0" + RandomData.getString());

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void buildConfigurationIdCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });
        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with empty id. Check it's not created", ()-> {
            testData.getBuildType().setId("");

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    public void buildConfigurationNameCannotBeEmpty() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });
        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with empty name. Check it's not created", ()-> {
            testData.getBuildType().setName("");

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void buildConfigurationCannotBeCreatedWithoutProject() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config without project. Check it's not created", ()-> {
            testData.getBuildType().setProject(null);

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
        });
    }

    @Test
    public void buildConfigurationPropertyNameContainsAllAscciiSymbols() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });
        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with property name containing all ascii. Check it's created", ()-> {
            testData.getBuildType().getParameters().getProperty().get(0).setName(allAsciiSymbols);

            var buildtype = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());

            soft.assertThat(buildtype.getParameters().getProperty().get(0).getName())
                    .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getName());
        });
    }

    @Test
    public void buildConfigurationPropertyValueContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with property value containing all ascii. Check it's created", ()-> {
            testData.getBuildType().getParameters().getProperty().get(0).setValue(allAsciiSymbols);

            var buildtype = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());

            soft.assertThat(buildtype.getParameters().getProperty().get(0).getValue())
                    .isEqualTo(testData.getBuildType().getParameters().getProperty().get(0).getValue());
        });
    }

    @Test
    public void buildConfigurationStepNameContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with step name containing all ascii. Check it's created", ()-> {
            testData.getBuildType().getSteps().getStep().get(0).setName(allAsciiSymbols);

            var buildtype = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());

            soft.assertThat(buildtype.getSteps().getStep().get(0).getName())
                    .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getName());
        });
    }

    @Test
    public void buildConfigurationStepTypeContainsAllAsciiSymbols() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with step type containing all ascii. Check it's created", ()-> {
            testData.getBuildType().getSteps().getStep().get(0).setType(allAsciiSymbols);

            var buildtype = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());

            soft.assertThat(buildtype.getSteps().getStep().get(0).getType())
                    .isEqualTo(testData.getBuildType().getSteps().getStep().get(0).getType());
        });
    }

    @Test
    public void buildConfigurationCannotBeCreatedForUnknownProjectId() {
        var testData = testDataStorage.addTestData();
        step("Create user for future steps", ()-> {
            new UserChecked(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        });

        step("Create project", ()-> {
            new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
        });

        step("Create build config with random projectId. Check it's not created", ()-> {
            testData.getBuildType().getProject().setId(RandomData.getString());

            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
        });
    }
}
