package com.example.definitions;

import org.testng.Assert;
import org.testng.SkipException;
import com.example.actions.HomePageActions;
import com.example.actions.LoginPageActions;
import com.example.utils.HelperClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginPageDefinitions {

    LoginPageActions objLogin = new LoginPageActions();
    HomePageActions objHomePage = new HomePageActions();

    @Given("User is on HRMLogin page {string}")
    public void loginTest(String url) {

        HelperClass.openPage(url);

    }

    @When("User enters username as {string} and password as {string}")
    public void goToHomePage(String userName, String passWord) {

        objLogin.login(userName, passWord);

    }

    @Then("User should be able to login successfully and new page open")
    public void verifyLogin() {

        Assert.assertTrue(objHomePage.getHomePageText().contains("Time at Work"));

    }

    @Then("User should be able to see error message {string}")
    public void verifyErrorMessageForInvalidCredentials(String expectedErrorMessage) {

        Assert.assertEquals(objLogin.getErrorMessage(),expectedErrorMessage);

    }


    @Then("User should be able to see error message for empty username as {string}")
    public void verifyErrorMessageForEmptyUsername(String expectedErrorMessage) {
        Assert.assertEquals(objLogin.getMissingUsernameText(),expectedErrorMessage);

    }

}
