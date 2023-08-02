package com.example.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.*;

@CucumberOptions(
        tags = "@MissingUsername or @InvalidCredentials or @ValidCredentials",
        features = {"src/test/resources/features/LoginPage.feature"},
        glue = {"com.example.definitions"},
        plugin = { "pretty",
                "html:target/cucumber-reports/cucumber-html",
                "json:target/cucumber-reports/cucumber.json",
                "com.qmetry.qaf.automation.cucumber.QAFCucumberPlugin",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {


        @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
        public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws Throwable {
                super.runScenario(pickleWrapper, featureWrapper);
        }

        @Override
        @DataProvider(name = "scenarios", parallel = true)
        public Object[][] scenarios(){
                return super.scenarios();
        }

}
