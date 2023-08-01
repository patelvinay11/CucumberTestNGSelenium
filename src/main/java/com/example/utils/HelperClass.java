package com.example.utils;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.util.StringUtil;
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

        // Adding ignore webdrivermanager feature in Qmetry
        //ConfigurationManager.getBundle().setProperty("manage.driver.executable", false);

        // set webdriver properties as system property
        //setSystemProperty();

        driver = new WebDriverTestBase().getDriver();

        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
        driver.manage().window().maximize();

    }


    private void setSystemProperty() {
        // updated to support any webdriver property to add as system property
        Iterator<String> driverproperties = ConfigurationManager.getBundle().getKeys("webdriver");
        while (driverproperties.hasNext()) {
            String key = driverproperties.next();
            String val = ConfigurationManager.getBundle().getString(key);
            if (StringUtil.isNotBlank(val)) {
                System.setProperty(key, ConfigurationManager.getBundle().getString(key));
            }
        }
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
