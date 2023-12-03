package com.example.teamcity.api.requests;

import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UserUnchecked;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class UncheckedRequests {
    private UserUnchecked userRequest;
    private ProjectUnchecked projectRequest;
    private UncheckedBuildConfig buildConfigRequest;

    public UncheckedRequests(RequestSpecification spec) {
        this.userRequest = new UserUnchecked(spec);
        this.projectRequest = new ProjectUnchecked(spec);
        this.buildConfigRequest = new UncheckedBuildConfig(spec);

    }
}
