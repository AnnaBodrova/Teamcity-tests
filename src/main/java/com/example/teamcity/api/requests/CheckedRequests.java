package com.example.teamcity.api.requests;

import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.ProjectChecked;
import com.example.teamcity.api.requests.checked.UserChecked;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class CheckedRequests  {
    private UserChecked userRequest;
    private ProjectChecked projectRequest;
    private CheckedBuildConfig buildConfigRequest;

    public CheckedRequests(RequestSpecification spec) {
        this.userRequest = new UserChecked(spec);
        this.projectRequest = new ProjectChecked(spec);
        this.buildConfigRequest = new CheckedBuildConfig(spec);

    }
}
