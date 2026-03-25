package utils;

import api.TokenAPI;
import io.restassured.response.Response;

public class TokenManager {

    private static String token;

    public static synchronized String getToken() {

        if (token == null) {

            Response response = TokenAPI.generateToken();

            System.out.println("Token API Response:");
            System.out.println(response.asPrettyString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Token API Failed: " + response.asPrettyString());
            }

            // ✅ Correct JSON Path
            token = response.jsonPath().getString("data[0].token");

            if (token == null || token.isEmpty()) {
                throw new RuntimeException(
                        "Token not found in response: "
                                + response.asPrettyString());
            }

            System.out.println("Generated Token: " + token);
        }

        return token;
    }

    public static void clearToken() {
        token = null;
    }
}