package com.example.teamcity.api.generator;

import com.example.teamcity.api.models.*;

import java.util.Arrays;

public class TestDataGenerator {
    public static TestData generator(){
        var user = User.builder()
                .username(RandomData.getString())
                .password(RandomData.getString())
                .email(RandomData.getString()+"@GMAIL.COM")
                .roles(Roles.builder()
                                .role(Arrays.asList(Role.builder()
                                        .roleId("SYSTEM_ADMIN")
                                        .scope("g")
                                        .build()))
                                .build())
                .build();
        var projectDescription = NewProjectDescription
                .builder()
                .parentProject(Project
                        .builder()
                        .locator("_Root")
                        .build())
                .name(RandomData.getString())
                .id(RandomData.getString())
                .copyAllAssociatedSettings(true)
                .build();

        var buildType = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(projectDescription)
                .parameters(Parameters.builder()
                        .property(Arrays.asList(Property.builder()
                                .name(RandomData.getString())
                                .value(RandomData.getString())
                                .build()
                        ))
                        .build())
                .steps(Steps.builder()
                        .step(Arrays.asList(Step.builder()
                                .name(RandomData.getString())
                                .type(RandomData.getString())
                                        .properties(Properties.builder()
                                                .property(Arrays.asList(Property.builder()
                                                                .name(RandomData.getString())
                                                                .value(RandomData.getString())
                                                                .build()))
                                                .build()).build())).build()).build();

        return TestData.builder()
                .user(user)
                .buildType(buildType)
                .newProjectDescription(projectDescription).build();

    }

    public static Roles generateRole(com.example.teamcity.api.enums.Role roles, String scope ){
        return Roles.builder().role
                (Arrays.asList(Role.builder().roleId(roles.getText())
                        .scope(scope).build())).build();
    }
}
