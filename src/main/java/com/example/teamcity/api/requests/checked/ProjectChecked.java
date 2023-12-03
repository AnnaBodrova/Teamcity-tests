package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.ProjectUnchecked;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;



public class ProjectChecked extends Request implements CrudInterface {

    private static final String PROJECTS_ENDPOINT = "/app/rest/projects";

    public ProjectChecked(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Project create(Object o) {
        return new ProjectUnchecked(spec).create(o)
        .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Project get(String id) {

        return new ProjectUnchecked(spec)
                .get(id)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Object update(Object o) {
        return null;
    }

    @Override
    public Object delete(String id) {

        return new ProjectUnchecked(spec)
                .delete(id)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
