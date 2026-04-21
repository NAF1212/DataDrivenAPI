package validators;

import io.restassured.response.Response;
import org.testng.Assert;
import utils.ReportManager;

public class PricingValidator {

    public static void validateSuccess(Response response) {

        int httpStatus = response.getStatusCode();
        int apiStatus = getApiStatus(response);
        String responseBody = response.asString();

        log(response);

        if (httpStatus >= 400 || apiStatus >= 400) {
            failValidation(response, "Expected success but got error response");
        }

        Assert.assertEquals(httpStatus, 200, "HTTP status mismatch");
        Assert.assertEquals(apiStatus, 200, "API status mismatch");

        ReportManager.test.get().pass("✅ API validation passed");
    }

    public static void validateError(Response response, int expectedStatus) {

        int httpStatus = response.getStatusCode();
        int apiStatus = getApiStatus(response);

        log(response);

        Assert.assertEquals(httpStatus, expectedStatus, "HTTP error code mismatch");
        Assert.assertEquals(apiStatus, expectedStatus, "API error code mismatch");

        ReportManager.test.get().pass("✅ Expected error validated successfully");
    }

    private static int getApiStatus(Response response) {
        try {
            return response.jsonPath().getInt("status");
        } catch (Exception e) {
            return -1;
        }
    }

    private static void failValidation(Response response, String message) {

        ReportManager.test.get().fail("❌ " + message);
        ReportManager.test.get().fail("HTTP Status: " + response.getStatusCode());
        ReportManager.test.get().fail("Response: <pre>" + response.asPrettyString() + "</pre>");

        Assert.fail(message);
    }

    private static void log(Response response) {

        ReportManager.test.get().info("HTTP Status: " + response.getStatusCode());
        ReportManager.test.get().info("Response: <pre>" + response.asPrettyString() + "</pre>");
    }
}