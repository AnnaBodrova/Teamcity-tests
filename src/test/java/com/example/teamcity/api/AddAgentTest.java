package com.example.teamcity.api;

import com.example.teamcity.api.models.AgentsList;
import org.testng.annotations.Test;

public class AddAgentTest extends BaseApiTest {

    @Test
    public void authorizeAgentTest() {
        AgentsList agentsList = checkedWithSuperUser.getAgentsChecked().get();

        if (!agentsList.getAgent().isEmpty()) {
            checkedWithSuperUser.getAgentsChecked().put(agentsList.getAgent().get(0).getId(), "true");
        }
        else System.out.println("Teamcity Agent has not been authorized");
    }
}
