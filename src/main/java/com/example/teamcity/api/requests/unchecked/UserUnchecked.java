package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserUnchecked extends Request implements CrudInterface {
    private static final String USER_ENDPOINT = "app/rest/users";

    public UserUnchecked(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object o) {
        return given()
                .spec(spec)
                .body(o)
                .post(USER_ENDPOINT);
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
    public Response delete(String userName) {
        return given()
                .spec(spec)
                .delete(USER_ENDPOINT+"/username:"+userName);
    }
}
