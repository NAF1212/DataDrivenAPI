package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportManager {

    public static ExtentReports extent;
    public static ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    private static String reportPath;

    public static void initReport() {

        String time = new SimpleDateFormat(
                "yyyyMMdd_HHmmss").format(new Date());

        reportPath = "reports/DailyReport_" + time + ".html";

        ExtentSparkReporter reporter =
                new ExtentSparkReporter(reportPath);

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static void flushReport() {
        extent.flush();
    }
}