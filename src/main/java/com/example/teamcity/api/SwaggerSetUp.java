package com.example.teamcity.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerSetUp {
    public static OpenAPI openAPI = new OpenAPI();

    public static void setupSwagger(){
        openAPI.setInfo(new Info()
                .title("Your API Title")
                .version("1.0")
                .description("Your API Description"));

        Paths paths = new Paths();
        PathItem pathItem = new PathItem();
        Operation operation = new Operation();
// Define operation properties like responses, request body, etc.
        pathItem.setGet(operation); // Set the HTTP method for the path
        paths.addPathItem("/yourEndpoint", pathItem);
        openAPI.setPaths(paths);

    }
}
