package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBuildQueue extends Request implements CrudInterface {
    public CheckedBuildQueue(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public CheckedBuildQueue create(Object o) {
        return new UncheckedBuildConfig(spec).create(o)
                .then().assertThat().statusCode(HttpStatus.SC_OK).extract().as(CheckedBuildQueue.class);
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
