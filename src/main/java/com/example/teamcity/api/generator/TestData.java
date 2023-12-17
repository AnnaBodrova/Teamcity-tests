package com.example.teamcity.api.generator;

import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import com.example.teamcity.api.requests.unchecked.UserUnchecked;
import com.example.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestData {
    private User user;
    private NewProjectDescription newProjectDescription;
    private BuildType buildType;
    private Build build;

    public void delete(){
        var spec = Specifications.getSpec().authSpec(user);
        new ProjectUnchecked(spec).delete(newProjectDescription.getId());
        new UserUnchecked(spec).delete(user.getUsername());
    }
}
