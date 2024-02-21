package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;

public class AuthRequest {
    private User user;


    public AuthRequest(User user) {
        this.user = user;
    }

    public String getCSRFToken(){
        return RestAssured
                .given()
                .spec(Specifications.getSpec().authSpec(user))
                .get("http://admin:admin@192.168.0.104:8111/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
