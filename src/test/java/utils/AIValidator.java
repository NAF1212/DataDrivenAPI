package utils;

public class AIValidator {

    public static void validateResponse(String responseBody) {

        String prompt = "Analyze this API response and tell if it is valid or contains any issue: "
                + responseBody;

        String result = ClaudeAIUtil.askAI(prompt);

        System.out.println("AI Analysis: " + result);

        if (result.toLowerCase().contains("error") ||
            result.toLowerCase().contains("invalid")) {

            throw new AssertionError("AI detected issue: " + result);
        }
    }
}