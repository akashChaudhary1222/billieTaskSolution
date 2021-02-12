package main.java.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import main.java.listeners.SuiteListener;

import static main.java.constants.GlobalData.*;


public class ExtentManager {
    private static ExtentReports extent = null;
    private static final ThreadLocal<ExtentTest> THREAD_LOCAL = new ThreadLocal<>();

    private ExtentManager() {
    }

    public static synchronized ThreadLocal<ExtentTest> getThreadLocal() {
        return THREAD_LOCAL;
    }

    public static synchronized void setThreadLocal(ExtentTest threadLocal) {
        getThreadLocal().set(threadLocal);
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }

        return extent;
    }

    public static synchronized void createInstance() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER_REPORT + FILE_NAME_REPORT);
        htmlReporter.config().setDocumentTitle("Execution Report for Restful-Booker API flow");
        htmlReporter.config().setReportName("Automation Report - (" + SuiteListener.componentName + ")");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setChartVisibilityOnOpen(true);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    public static void addExecutionDetails_ExtentReport() {
        extent.setSystemInfo("Pass %", MarkupHelper.createLabel(passPercentage, ExtentColor.GREEN).getMarkup());
        extent.flush();
    }

}
