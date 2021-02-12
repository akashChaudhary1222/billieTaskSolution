package main.java.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import main.java.constants.GlobalData;
import main.java.reports.ExtentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TestListener implements ITestListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int pass, fail, total;

    public synchronized void onStart(ITestContext context) {
    }

    public synchronized void onFinish(ITestContext context) {

        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        Iterator iterator = skippedTests.iterator();

        while (iterator.hasNext()) {
            ITestResult temp = (ITestResult) iterator.next();
            ITestNGMethod method = temp.getMethod();
            if (context.getFailedTests().getResults(method).size() > 0) {
                skippedTests.remove(temp);
            } else if (context.getPassedTests().getResults(method).size() > 0) {
                skippedTests.remove(temp);
            }
        }

        iterator = org.testng.Reporter.getOutput().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            ExtentManager.getInstance().setTestRunnerOutput(s);
        }
        ExtentManager.getInstance().flush();
        GlobalData.passPercentage = String.valueOf(pass * 100 / (pass + fail));
    }

    public synchronized void onTestStart(ITestResult result) {
        String methodName = this.getMethodNameWithParams(result);
        ExtentTest extentTest = ExtentManager.getInstance().createTest(methodName, result.getMethod().getDescription());
        ExtentManager.setThreadLocal(extentTest);
        this.addParametersInReport(result);
    }

    public synchronized void onTestSuccess(ITestResult result) {
        pass++;
        org.testng.Reporter.log(result.getName() + " = [Pass]<br>");
        String className = result.getMethod().getRealClass().getSimpleName();
        ExtentManager.getThreadLocal().get().assignCategory(className);
        ExtentManager.getThreadLocal().get().pass(MarkupHelper.createLabel("Test passed", ExtentColor.GREEN));
    }

    public synchronized void onTestFailure(ITestResult result) {
        fail++;
        org.testng.Reporter.log(result.getName() + " = [Fail]<br>");
        String className = result.getMethod().getRealClass().getSimpleName();
        ExtentManager.getThreadLocal().get().assignCategory(className);
        ExtentManager.getThreadLocal().get().fail(result.getThrowable());
        ExtentManager.getThreadLocal().get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
    }

    public synchronized void onTestSkipped(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (null != throwable && throwable.getMessage().contains("depends on not successfully finished methods")) {
            ExtentTest extentTest = ExtentManager.getInstance().createTest(result.getName(), result.getMethod().getDescription());
            ExtentManager.setThreadLocal(extentTest);
        }

        org.testng.Reporter.log(result.getName() + " = [Skip]<br>");
        String className = result.getMethod().getRealClass().getSimpleName();
        ExtentManager.getThreadLocal().get().assignCategory(className);
        ExtentManager.getThreadLocal().get().skip(result.getThrowable());
        ExtentManager.getThreadLocal().get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));
    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }


    private void addParametersInReport(ITestResult result) {
        if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
            ExtentManager.getThreadLocal().get().log(Status.PASS, MarkupHelper.createTable(this.getParameterArray((HashMap) result.getParameters()[0])));
        }

    }

    private String[][] getParameterArray(HashMap<String, String> hm) {
        String[][] parameters = new String[hm.size()][2];
        int row = 0;
        int column = 0;

        for (Iterator iterator = hm.keySet().iterator(); iterator.hasNext(); column = 0) {
            String str = (String) iterator.next();
            parameters[row][column] = "<b>" + str + "</b>";
            column++;
            parameters[row][column] = hm.get(str);
            ++row;
        }

        return parameters;
    }

    private String getMethodNameWithParams(ITestResult result) {
        String methodName = result.getName();
        String nextLineCharacter = "<br>";
        if (result.getParameters().length > 0 && null != result.getParameters()[0]) {
            String paramName = result.getParameters()[0].toString();
            methodName = methodName + nextLineCharacter + "(" + paramName + ")";
        }

        return methodName;
    }
}
