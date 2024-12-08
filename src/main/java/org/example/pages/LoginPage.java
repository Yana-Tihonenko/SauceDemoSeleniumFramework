package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class LoginPage extends BasePage {
  private final By usernameField = By.id("user-name");
  private final By passwordField = By.id("password");
  private final By loginButton = By.name("login-button");

  private final By errorMessage = By.tagName("h3");

  private final By closeErrorMessage = By.xpath("//button[@class='error-button']//*[name()='svg']");

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  protected WebElement findElementIfExist(By locator) {
    List<WebElement> element = driver.findElements(locator);
    return element.isEmpty() ? null : element.get(0);
  }

  public WebElement getFieldEnterUserName() {
    WebElement getFieldEnterUserName = findElementIfExist(usernameField);
    if (getFieldEnterUserName == null) {
      logger.error("Field \"Enter user name\" is not found. {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return getFieldEnterUserName;
  }

  public void enterUserName(String userName) {
    getFieldEnterUserName().sendKeys(userName);
  }

  public WebElement getFieldEnterPassword() {
    WebElement getFieldEnterPassword = findElementIfExist(passwordField);
    if (getFieldEnterPassword == null) {
      logger.error("Field \"Enter password\" is not found. {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return getFieldEnterPassword;
  }

  public void enterPassword(String password) {
    getFieldEnterPassword().sendKeys(password);
  }

  public WebElement loginButtonElement() {
    WebElement loginButtonElement = findElementIfExist(loginButton);
    if (loginButtonElement == null) {
      logger.error("Login button is not found. {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return loginButtonElement;
  }

  public void clickLoginButton() {
    loginButtonElement().click();
  }

  public WebElement buttonErrorMessageElement() {
    WebElement buttonErrorMessageElement = findElementIfExist(closeErrorMessage);
    if (buttonErrorMessageElement == null) {
      logger.error("Error message button is not found. {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return buttonErrorMessageElement;
  }

  public WebElement errorMessageElement(boolean isMandatory) {
    WebElement errorMessageElement = findElementIfExist(errorMessage);
    if (isMandatory && errorMessageElement == null) {
      logger.error("Error message is not found. {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return errorMessageElement;
  }

  public String getErrorMessage() {
    return errorMessageElement(true).getText();
  }

  public void closeButtonErrorMessage() {
    buttonErrorMessageElement().click();
  }

  public boolean ErrorMessageIsDisabled() {
    logger.debug("Error message element states {}",errorMessageElement(false) );
    return errorMessageElement(false) == null;
  }

  public ProductPage login(String userName, String password) {
    enterUserName(userName);
    enterPassword(password);
    clickLoginButton();
    return new ProductPage(driver);
  }

}

