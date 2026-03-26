package testAPI;

import api.PricingAPI;
import base.BaseTest;
import config.ConfigManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;
import utils.ReportManager;


import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PricingTest extends BaseTest {

    /**
     * Excel Data Provider
     * Returns Map<String,String> for each row
     */
    @DataProvider(name = "CustomerAPIData", parallel = true)
    public Object[][] pricingDataProvider() throws Exception {

        Object[][] data = ExcelReader.getData("Shipment_Data_RowWise");

        System.out.println("Total Rows Loaded: " + data.length);
        return data;
    }

    /**
     * Main Pricing Test
     */
    @Test(dataProvider = "CustomerAPIData")
	public void pricingTest(Map<String, String> data) throws Exception {

		// Create Test in Extent Report
		ReportManager.test.set(
		        ReportManager.extent.createTest("Pricing Test - Account: " + data.get("custAccountId"))
		    );
		try {

		    Response response = PricingAPI.calculate(data);

		    int httpStatus = response.getStatusCode();
		    int apiStatus = response.jsonPath().getInt("status");
		    long responseTime= response.getTimeIn(TimeUnit.SECONDS);
		    String env=ConfigManager.get("env");
		    String urlString=ConfigManager.get("base.url");
		    String pathString=ConfigManager.get("pricing.endpoint");
		    String errorMessage = response.jsonPath().getString("errors");

		    // Log request data
		    ReportManager.test.get().info("Request Data: " + data);

		    // Log response
		    ReportManager.test.get().info("Response: <pre>" + response.asPrettyString() + "</pre>");

		    ReportManager.test.get().info("HTTP Status Code: " + httpStatus);
		    ReportManager.test.get().info("API Status Code: " + apiStatus);
		    ReportManager.test.get().info("API Response Time : " + responseTime);
		    ReportManager.test.get().info("Execution Environment:" + env);
		    ReportManager.test.get().info("base Url : " + urlString);
		    ReportManager.test.get().info("pricing.endpoint : " + pathString);
		    ReportManager.test.get().info("Error Message : " + errorMessage);

		    // ❌ FAIL CONDITION
		    if (httpStatus == 500 || apiStatus == 500) {

		        ReportManager.test.get().fail("API FAILED - HTTP or API status returned 500");

		        Assert.fail("API returned status 500");
		    }

		    if (httpStatus != 200) {

		        ReportManager.test.get().fail("Unexpected HTTP Status Code: " + httpStatus);

		        Assert.fail("HTTP Status Code not 200");
		    }
		    
		    // ✅ PASS CONDITION
		    
		} catch (Exception e) {

			ReportManager.test.get().fail("Test Failed: " + e.getMessage());
			ReportManager.test.get().pass("Pricing API Test Passed Successfully");
			
			

			throw e;
		}
	}
}