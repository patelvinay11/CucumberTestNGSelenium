package com.example.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageLocators {

    @FindBy(xpath = "//p[normalize-space()='Time at Work']")
    public  WebElement homePageUserName;

}