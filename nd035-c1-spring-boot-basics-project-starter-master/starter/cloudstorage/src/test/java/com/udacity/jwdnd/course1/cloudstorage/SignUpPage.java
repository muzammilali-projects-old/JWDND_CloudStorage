package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {
    @FindBy(id = "inputFirstName")
    private WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    private WebElement inputLastName;

    @FindBy(id = "inputUsername")
    private WebElement inputUserName;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setInputFirstName(String firstName) {
        inputFirstName.sendKeys(firstName);
    }

    public void setInputLastName(String lastName) {
        inputLastName.sendKeys(lastName);
    }

    public void setInputUserName(String userName) {
        inputUserName.sendKeys(userName);
    }

    public void setInputPassword(String password) {
        inputPassword.sendKeys(password);
    }

    public void submitForm() {
        submitButton.click();
    }
}
