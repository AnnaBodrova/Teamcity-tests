package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildQueue;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;


public class RolesTest extends BaseApiTest {
    @Test
    public void unauthorizedUserCannotCreateProject() {
        var testData = testDataStorage.addTestData();

        step("Try to create project without auth and check error message and status", ()-> {
            new UncheckedRequests(Specifications.getSpec().unauthSpec()).getProjectRequest()
                    .create(testData.getNewProjectDescription())
                    .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .body(Matchers.containsString("Authentication required"));
        });

        step("Check by superuser that previos project was not really created", ()-> {
            uncheckedWithSuperUser.getProjectRequest()
                    .get(testData.getNewProjectDescription().getId())
                    .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(Matchers.containsString("Nothing is found by locator 'count:1,name:" + testData.getNewProjectDescription().getId()));
        });
    }

    @Test
    public void systemAdminUserCanCreateProject() {
        var testData = testDataStorage.addTestData();

        step("Create system admin user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create project by system admin user", ()-> {
            var project = new ProjectChecked(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getNewProjectDescription());
            soft.assertThat(project.getId()).isEqualTo(testData.getNewProjectDescription().getId());
        });
    }

    @Test
    public void projectAdminUserShouldHaveRightsToCreateBuildConfigToProject() {
        var testData = testDataStorage.addTestData();
        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project admin user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by project admin user. Check it's created ", ()-> {
            var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
            soft.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
        });
    }

    @Test
    public void projectAdminUserCanCreateBuildConfigToAnotherUserProjects() {// there is a bug
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        step("Create 2 projects", ()-> {
            checkedWithSuperUser.getProjectRequest().create(firstTestData.getNewProjectDescription());
            checkedWithSuperUser.getProjectRequest().create(secondTestData.getNewProjectDescription());
        });

        step("Create first project admin user that has rights to first project", ()-> {
            firstTestData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + firstTestData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());
        });

        step("Create second project admin user that has rights to second project", ()-> {
            secondTestData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + secondTestData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(secondTestData.getUser());
        });

        step("Second user creates build config to first project ", ()-> {
            new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                    .create(firstTestData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    public void projectAdminCanRunBuild() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project admin user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by super user", ()-> {
            checkedWithSuperUser.getBuildConfigRequest()
                    .create(testData.getBuildType());
        });

        step("Run build by project admin user and check", ()-> {
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    public void projectDeveloperUserCannotCreateBuildConfiguration() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project developer user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_DEVELOPER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Try to create build config by project developer user. Check it's not created", ()-> {
            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        });
    }

    @Test
    public void projectDeveloperUserCanRunBuild() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project developer user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_DEVELOPER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by super user", ()-> {
            checkedWithSuperUser.getBuildConfigRequest()
                    .create(testData.getBuildType());
        });

        step("Run build by project developer user and check", ()-> {
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    public void projectViwerUserCannotCreateBuildConfiguration() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project viewer user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_VIEWER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Try to create build config by project viewer user. Check it's not created", ()-> {
            new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType())
                    .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
        });
    }

    @Test
    public void projectViwerUserCanRunBuild() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create project viewer user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.PROJECT_VIEWER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by super user", ()-> {
            checkedWithSuperUser.getBuildConfigRequest()
                    .create(testData.getBuildType());
        });

        step("Run build by project viewer user and check", ()-> {
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK);
        });
    }

    @Test
    public void agentManagerUserCanCreateBuildConfiguration() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create agent manager user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.AGENT_MANAGER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by agent manager", ()-> {
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
        });
    }

    @Test
    public void agentManagerUserCanRunBuild() {
        var testData = testDataStorage.addTestData();

        step("Create project", ()-> {
            checkedWithSuperUser.getProjectRequest().create(testData.getNewProjectDescription());
        });

        step("Create agent manager user", ()-> {
            testData.getUser().setRoles(TestDataGenerator.generateRole(Role.AGENT_MANAGER, "p:" + testData.getNewProjectDescription().getId()));
            checkedWithSuperUser.getUserRequest().create(testData.getUser());
        });

        step("Create build config by agent manager", ()-> {
            new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                    .create(testData.getBuildType());
        });

        step("Run build by agent manager ", ()-> {
            new UncheckedBuildQueue(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getBuild())
                    .then()
                    .assertThat().statusCode(HttpStatus.SC_OK);
        });
    }
}
