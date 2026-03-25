package utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {

    public static void validateFloat(Response response,
                                     String jsonPath,
                                     String expectedValue,
                                     String fieldName) {

        Float actual = response.jsonPath().get(jsonPath);

        // ❌ NULL case
        if (actual == null) {

            String message = "❌ " + fieldName + " is MISSING in response";

            ReportManager.test.get().fail(message);  // ✅ log first
            System.out.println(message);             // ✅ console

            Assert.fail(message);                    // ❌ then fail
        }

        float expected = Float.parseFloat(expectedValue);

        // ❌ MISMATCH
        if (actual.floatValue() != expected) {

            String message = "❌ MISMATCH in " + fieldName +
                    " | Expected: " + expected +
                    " | Actual: " + actual;

            ReportManager.test.get().fail(message);  // ✅ report
            System.out.println(message);             // ✅ console

            Assert.fail(message);                    // ❌ fail
        }

        // ✅ PASS
        String passMsg = "✅ " + fieldName +
                " matched | Expected: " + expected +
                " | Actual: " + actual;

        ReportManager.test.get().pass(passMsg);
        System.out.println(passMsg);
    }
}