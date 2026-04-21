package utils;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ClaudeAIUtil {

    private static final String API_KEY = ConfigManager.get("claude.api.key");

    public static String askAI(String prompt) {
    	
    	if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("Claude API Key is missing in config");
        }

        Response response = RestAssured.given()
                .baseUri("https://api.anthropic.com/v1/messages")
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"model\": \"claude-3-sonnet-20240229\",\n" +
                        "  \"max_tokens\": 300,\n" +
                        "  \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]\n" +
                        "}")
                .post();
        System.out.println("Claude API Key: " + API_KEY);

        return response.getBody().asString();
        
    }
}