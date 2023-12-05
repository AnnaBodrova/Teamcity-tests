package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildQueue extends Request implements CrudInterface {

    private static final String BUILD_QUEUE_ENDPOINT = "/app/rest/buildQueue";

    public UncheckedBuildQueue(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object o) {
        return given().spec(spec).body(o).post(BUILD_QUEUE_ENDPOINT);
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
    public Object delete(String id) {
        return null;
    }
}
