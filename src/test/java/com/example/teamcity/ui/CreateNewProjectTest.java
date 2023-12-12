package com.example.teamcity.ui;


import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;




public class CreateNewProjectTest extends BaseUITest {
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {
var url = "https://github.com/avito-tech/go-mutesting";
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getNewProjectDescription().getParentProject().getId())
                .createProjectByUrl(url);
        ;
    }
}
