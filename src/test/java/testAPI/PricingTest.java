package testAPI;

import api.PricingAPI;
import base.BaseTest;
import config.ConfigManager;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.ClaudeAIUtil;
import utils.ExcelReader;
import utils.ReportManager;
import validators.PricingValidator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PricingTest extends BaseTest {

    /**
     * Excel Data Provider
     */
    @DataProvider(name = "CustomerAPIData", parallel = true)
    public Object[][] pricingDataProvider() throws Exception {

        Object[][] data = ExcelReader.getData("Shipment_Data_RowWise");
        System.out.println("Total Rows Loaded: " + data.length);

        return data;
    }

    /**
     * Positive Test - Pricing API Success Validation
     */
    @Test(dataProvider = "CustomerAPIData")
    public void pricingTest(Map<String, String> data) throws Exception {

        // ✅ Create Test Report Entry
        ReportManager.test.set(
                ReportManager.extent.createTest("Pricing Test - Account: " + data.get("custAccountId"))
        );

        try {
            // ✅ API Call
            Response response = PricingAPI.calculate(data);

            // ✅ Basic Logging (before validation)
            logBasicDetails(data, response);

            // ✅ Centralized Validation
            PricingValidator.validateSuccess(response);

            // ✅ Optional AI Validation
            runAIValidation(response);

        } catch (Exception e) {

            ReportManager.test.get().fail("❌ Test Execution Failed: " + e.getMessage());
            throw e; // Important → keeps TestNG failing correctly
        }
    }

    /**
     * 🔹 Helper: Log basic request/response details
     */
    private void logBasicDetails(Map<String, String> data, Response response) {

        ReportManager.test.get().info("Request Data: <pre>" + data + "</pre>");
        ReportManager.test.get().info("Response: <pre>" + response.asPrettyString() + "</pre>");
        ReportManager.test.get().info("HTTP Status Code: " + response.getStatusCode());
        ReportManager.test.get().info("Response Time: " + response.getTimeIn(TimeUnit.MILLISECONDS) + " ms");

        ReportManager.test.get().info("Environment: " + ConfigManager.get("env"));
        ReportManager.test.get().info("Base URL: " + ConfigManager.get("base.url"));
        ReportManager.test.get().info("Endpoint: " + ConfigManager.get("pricing.endpoint"));
    }

    /**
     * 🔹 Optional AI Validation Layer
     */
    private void runAIValidation(Response response) {

        try {
            String responseBody = response.asString();

            String aiPrompt = "Validate this pricing API response. Check for correctness, anomalies, or hidden issues: "
                    + responseBody;

            String aiResult = ClaudeAIUtil.askAI(aiPrompt);

            ReportManager.test.get().info("AI Analysis: <pre>" + aiResult + "</pre>");

        } catch (Exception e) {
            ReportManager.test.get().warning("AI Validation Failed: " + e.getMessage());
        }
    }
}