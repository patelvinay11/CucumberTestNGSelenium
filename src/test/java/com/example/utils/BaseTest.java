package com.example.utils;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.util.StringUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public WebDriver driver;
    public final static int TIMEOUT = 10;

    private ThreadLocal<Map<String, String>> data = new ThreadLocal<>();

    public Map<String, String> getData() {
        return data.get();
    }

    public void setData(Map<String, String> testData) {
        data.set(testData);
    }

    public void removeData() {
        data.remove();
    }

    public WebDriver WebDriverManager () throws IOException {

        /*FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//resources//application.properties");
        Properties prop = new Properties();
        prop.load(fis);
        String url= prop.getProperty("QAUrl");

        if (driver == null) {
            if (prop.getProperty("driver.name").equalsIgnoreCase("chromeDriver")) {
                System.setProperty("webdriver.chrome.driver", "./resources/drivers/chromedriver");
                ChromeOptions options = new ChromeOptions();
                ///options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            } else if (prop.getProperty("driver.name").equalsIgnoreCase("firefoxDriver")) {
                System.setProperty("webdriver.gecko.driver", "./resources/drivers/geckodriver");
                FirefoxOptions options = new FirefoxOptions();
                driver = new FirefoxDriver(options);
            } else if (prop.getProperty("driver.name").equalsIgnoreCase("edgeDriver")) {
                System.setProperty("webdriver.edge.driver", "./resources/drivers/msedgedriver");
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            } else {
                System.out.println("No Browser is provided");
            }
            driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(url);

        }*/

        String url= ConfigurationManager.getBundle().getPropertyValueOrNull("QAUrl");

        if (driver == null) {
            // Adding ignore webdrivermanager feature in Qmetry
            ConfigurationManager.getBundle().setProperty("manage.driver.executable", false);

            // set webdriver properties as system property
            setSystemProperty();

            driver = new WebDriverTestBase().getDriver().getUnderLayingDriver();

            driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(url);
        }

        return driver;
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
}
