package com.example.definitions;

import com.example.utils.HelperClass;
import org.testng.*;

public class BDDTestListener implements ITestListener, ISuiteListener {

    @Override
    public void onTestStart(ITestResult result) {
        HelperClass.setUpDriver();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        HelperClass.tearDown();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        HelperClass.tearDown();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        HelperClass.tearDown();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        HelperClass.tearDown();
    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }

    @Override
    public void onStart(ISuite suite) {

    }

    @Override
    public void onFinish(ISuite suite) {

    }
}
