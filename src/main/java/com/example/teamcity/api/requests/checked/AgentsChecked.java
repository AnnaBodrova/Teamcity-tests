package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.AgentsList;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.AgentsUnchecked;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;


public class AgentsChecked extends Request {

    public AgentsChecked(RequestSpecification spec) {
        super(spec);
    }

    public AgentsList get() {
        return new AgentsUnchecked(spec).
                get().then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(AgentsList.class);
    }

    public String put(String id, String body) {
        return new AgentsUnchecked(spec).put(id, body).then().assertThat().statusCode(HttpStatus.SC_OK).extract().body().asString();
    }
}
