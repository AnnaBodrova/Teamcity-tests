package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.UserChecked;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.LoginPage;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public class BaseUITest extends BaseTest {
    @BeforeSuite
    public void setupUITest() {
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.reportsFolder = "target/surefare-reports";
        Configuration.downloadsFolder = "target/downloads";

        BrowserSettings.setup(Config.getProperty("browser"));
    }

    public void loginAsUser(User user) {
        new UserChecked(Specifications.getSpec().superUserSpec()).create(user);
        new LoginPage().open().login(user);
    }
}
