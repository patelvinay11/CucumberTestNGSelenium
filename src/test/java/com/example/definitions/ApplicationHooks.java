package com.example.definitions;

import com.qmetry.qaf.automation.util.ClassUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.example.utils.TestSetUp;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ApplicationHooks {

 public TestSetUp setUp;

    public ApplicationHooks(TestSetUp setUp) {
        this.setUp = setUp;
    }

    @Before
    public void setUp(Scenario scenario) {
        setUp.baseTest.setData(getTestData(scenario));
    }

    private Map<String, String> getTestData(Scenario scenario) {
        Map<String, String> testData = (Map<String, String>) ClassUtil.getField("testData", ClassUtil.getField("pickle", ClassUtil.getField("pickle", ClassUtil.getField("testCase", ClassUtil.getField("delegate", scenario)))));
        if (testData == null) {
            testData = Collections.emptyMap();
        }
        return testData;
    }

    @After
    public void tearDown( ) throws IOException {
        setUp.baseTest.WebDriverManager().quit();
        setUp.baseTest.removeData();
    }

    @AfterStep
    public void addScreenshot(Scenario scenario) throws IOException {

        WebDriver driver =  setUp.baseTest.WebDriverManager();
        if(scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "image");
        }

    }

}
