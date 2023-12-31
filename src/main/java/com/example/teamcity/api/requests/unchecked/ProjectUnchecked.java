package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ProjectUnchecked extends Request implements CrudInterface {

    private static final String PROJECTS_ENDPOINT = "/app/rest/projects";

    public ProjectUnchecked(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object o) {
        return given()
                .spec(spec)
                .body(o)
        .post(PROJECTS_ENDPOINT);
    }

    @Override
    public Response get(String name) {

        return given()
                .spec(spec)
                .get(PROJECTS_ENDPOINT+"/name:"+name);
    }

    @Override
    public Object update(Object o) {
        return null;
    }

    @Override
    public Response delete(String id) {

        return given()
                .spec(spec)
                .delete(PROJECTS_ENDPOINT+"/id:"+id);
    }
}
