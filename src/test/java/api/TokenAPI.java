package api;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TokenAPI {

    public static Response generateToken() {

        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body("""
                        {
                            "applicationName": "%s",
                            "userEmail": "%s",
                            "userPassword": "%s"
                        }
                        """.formatted(
                                ConfigManager.get("applicationName"),
                                ConfigManager.get("userEmail"),
                                ConfigManager.get("userPassword")
                        ))
                .post(ConfigManager.get("token.endpoint"));
    }
}