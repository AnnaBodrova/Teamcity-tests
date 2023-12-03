package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildConfig extends Request implements CrudInterface {
private static final String BUILD_CONFIG_ENPOINT = "/app/rest/buildTypes";
    public UncheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object o) {
        return given().spec(spec).body(o).post(BUILD_CONFIG_ENPOINT);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(Object o) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given().spec(spec).delete(BUILD_CONFIG_ENPOINT+"/id:"+id);
    }
}
