package com.example.utils;

import java.util.concurrent.TimeUnit;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class HelperClass {

    private static HelperClass helperClass;
    //private static WebDriver driver;
    private static QAFExtendedWebDriver driver;
    public final static int TIMEOUT = 10;

    private HelperClass() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //WebDriverManager.chromedriver().setup();
        //driver = new ChromeDriver(options);

        driver = new WebDriverTestBase().getDriver();

        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
        driver.manage().window().maximize();

    }


    public static void openPage(String url) {
        driver.get(url);
    }


    public static QAFExtendedWebDriver getDriver() {
        return driver;
    }

    public static void setUpDriver() {

        if (helperClass==null) {

            helperClass = new HelperClass();
        }
    }

    public static void tearDown() {

        if(driver!=null) {
            driver.close();
            driver.quit();
        }

        helperClass = null;
    }

}
