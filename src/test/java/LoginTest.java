import io.github.bonigarcia.wdm.WebDriverManager;
import listeners.TestLoggerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.constants.PageContent;
import org.example.pages.HeaderPage;
import org.example.pages.LoginPage;
import org.example.pages.ProductPage;
import org.example.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import static org.testng.Assert.*;
@Listeners(TestLoggerListener.class)
public class LoginTest {
  WebDriver driver;
  LoginPage loginPage;
  ProductPage productPage;

  HeaderPage headerPage;

  ScreenshotUtil screenshotUtil;
  protected final Logger logger = LogManager.getLogger(getClass());

  @Parameters("baseUrl")
  @BeforeMethod
  public void setUp(String baseUrl) {
    logger.info("Setting up WebDriver and initializing pages.");
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    loginPage = new LoginPage(driver);
    productPage = new ProductPage(driver);
    headerPage = new HeaderPage(driver);
    driver.get(baseUrl);
    screenshotUtil = new ScreenshotUtil(driver);

    logger.info("Navigated to login page {}", baseUrl);

  }

  @Test
  public void successLogin() {
    logger.info("Successful login with valid credentials.");

    loginPage.enterUserName("standard_user");
    logger.debug("Entered username: standard_user");

    loginPage.enterPassword("secret_sauce");
    logger.debug("Entered password: secret_sauce");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualTitle = headerPage.getTitlePage();
    logger.debug("Actual page title: {}", actualTitle);

    assertEquals(actualTitle, PageContent.ProductPage.TITLE_PAGE, "Login was not successful!");
    logger.info("Login test passed. Navigated to Products page.");
  }


  @Test

  public void loginWithLockedUser() {
    logger.info("Login with locked user.");

    loginPage.enterUserName("locked_out_user");
    logger.debug("Entered username: locked_out_user");

    loginPage.enterPassword("secret_sauce");
    logger.debug("Entered password: secret_sauce");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualErrorMessage = loginPage.getErrorMessage();
    logger.debug("Actual error message: {}", actualErrorMessage);

    assertEquals(actualErrorMessage, PageContent.LoginPageContent.USER_LOCKED_OUT_ERROR);
    logger.info("Login with locked user test passed.");

  }

  @Test
  void failLoginWithWrongUserName() {
    logger.info("Failed login with wrong username.");

    loginPage.enterUserName("aaa");
    logger.debug("Entered username: aaa");

    loginPage.enterPassword("secret_sauce");
    logger.debug("Entered password: secret_sauce");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualErrorMessage = loginPage.getErrorMessage();
    logger.debug("Actual error message: {}", actualErrorMessage);

    assertEquals(actualErrorMessage,  PageContent.LoginPageContent.USERNAME_PASSWORD_MISMATCH_ERROR);
    logger.info("Fail login with wrong username test passed.");
  }

  @Test
  void failLoginWithWrongPassword() {
    logger.info("Failed login with wrong password.");

    loginPage.enterUserName("standard_user");
    logger.debug("Entered username: standard_user");

    loginPage.enterPassword("secret");
    logger.debug("Entered password: secret");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualErrorMessage = loginPage.getErrorMessage();
    logger.debug("Actual error message: {}", actualErrorMessage);

    assertEquals(actualErrorMessage,  PageContent.LoginPageContent.USERNAME_PASSWORD_MISMATCH_ERROR);
    logger.info("Fail login with wrong username test passed.");
  }

  @Test
  public void failLoginWithEmptyUsername() {
    logger.info("Failed login with empty username.");

    loginPage.enterPassword("secret_sauce");
    logger.debug("Entered password: secret_sauce");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualErrorMessage = loginPage.getErrorMessage();
    logger.debug("Actual error message: {}", actualErrorMessage);

    assertEquals(actualErrorMessage,  PageContent.LoginPageContent.USERNAME_REQUIRED_ERROR);
    logger.info("Fail login with empty username test passed.");
  }

  @Test
  public void failLoginWithEmptyPassword() {
    logger.info("Failed login with empty password.");

    loginPage.enterUserName("standard_user");
    logger.debug("Entered username: standard_user");

    loginPage.clickLoginButton();
    logger.info("Clicked login button.");

    String actualErrorMessage = loginPage.getErrorMessage();
    logger.debug("Actual error message: {}", actualErrorMessage);

    assertEquals(actualErrorMessage,  PageContent.LoginPageContent.PASSWORD_REQUIRED_ERROR);
    logger.info("Fail login with empty password test passed.");
  }

  @Test
  public void disableErrorMessage() {
    logger.info("Disable error message.");

    loginPage.clickLoginButton();
    logger.info("Clicked login button without entering credentials.");

    loginPage.closeButtonErrorMessage();
    logger.info("Clicked close button on the error message.");

    boolean isErrorMessageDisable = loginPage.ErrorMessageIsDisabled();
    logger.debug("Error message enabled state: {}", isErrorMessageDisable);

    assertTrue(isErrorMessageDisable);
    logger.info("Disable error message test passed.");
  }

  @AfterMethod
  public void tearDown() {
    logger.info("Tearing down WebDriver.");
    if (driver != null) {
      driver.quit();
    }
    logger.info("WebDriver quit successfully.");
  }
}


