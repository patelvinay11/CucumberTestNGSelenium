package com.example.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class LoginPage {

    public WebDriver driver;

    By userName = By.name("username");
    By passWord = By.name("password");
    By login = By.xpath("//button[@type='submit']");
    By errorMessage = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");
    By forgotPasswordLink = By.xpath("//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']");
    By loginPageTitle = By.xpath("//h5[normalize-space()='Login']");
    By missingUsernameErrorMessage = By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public void login(String strUserName, String strPassword) {

        // Fill user name
        driver.findElement(userName).sendKeys(strUserName);

        // Fill password
        driver.findElement(passWord).sendKeys(strPassword);

        // Click Login button
        driver.findElement(login).click();

    }

    // Click on Forgot Password link
    public void clickOnForgotPasswordLink() {
        driver.findElement(forgotPasswordLink).click();
    }

    //Get Login Page Title
    public String getLoginPageTitle() {
        return driver.findElement(loginPageTitle).getText();
    }

    //Get Error message for missing Username
    public String getMissingUsernameText() {
        return driver.findElement(missingUsernameErrorMessage).getText();
    }
}