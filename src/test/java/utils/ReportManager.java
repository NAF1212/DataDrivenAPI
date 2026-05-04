package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import config.ConfigManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportManager {

	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	private static String reportPath;

	public static void initReport() {

		String env = ConfigManager.get("env");

		String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		reportPath = "reports/DailyReport_" + env.toUpperCase() + "_" + time + ".html";

		//ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
		
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setReportName("Pricing Automation");
        spark.config().setDocumentTitle("Test Results");

		extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", "Chrome");
        System.out.println("📊 Extent Report Path: " + reportPath);
	}

	public static String getReportPath() {
		return reportPath;
	}

	public static void flushReport() {
		extent.flush();
	}
}