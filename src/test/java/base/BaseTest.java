package base;

import config.ConfigManager;
import io.restassured.RestAssured;
import org.testng.annotations.*;

import utils.EmailUtil;
import utils.ReportManager;

public class BaseTest {

    @BeforeSuite
    public void setup() {

        RestAssured.baseURI = ConfigManager.get("base.url");

        ReportManager.initReport();
        ReportManager.extent.setSystemInfo(
                "Environment",
                System.getProperty("env", "DEV"));
    }

    @AfterSuite
    public void tearDown() throws Exception {

        ReportManager.flushReport();

        EmailUtil.sendReport(
                ReportManager.getReportPath());
    }
}