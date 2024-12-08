import io.github.bonigarcia.wdm.WebDriverManager;
import listeners.TestLoggerListener;
import org.example.models.Item;
import org.example.models.ProductCard;
import org.example.pages.CartPage;
import org.example.pages.HeaderPage;
import org.example.pages.LoginPage;
import org.example.pages.ProductPage;
import org.example.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(TestLoggerListener.class)
public class BaseTest {
  WebDriver driver;
  LoginPage loginPage;
  ProductPage productPage;
  CartPage cartPage;

  HeaderPage headerPage;
  ScreenshotUtil screenshotUtil;

  protected final Logger logger = LogManager.getLogger(getClass());

  @Parameters({"baseUrl", "loginUsername", "loginPassword"})
  @BeforeMethod
  public void setUp(String baseUrl, String loginUsername, String loginPassword) {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    loginPage = new LoginPage(driver);
    productPage = new ProductPage(driver);
    headerPage = new HeaderPage(driver);
    driver.get(baseUrl);
    productPage = loginPage.login(loginUsername, loginPassword);
    screenshotUtil = new ScreenshotUtil(driver);


  }

  @AfterMethod
  public void afterEachTest(ITestResult result) {
    if (ITestResult.FAILURE == result.getStatus()) {
      screenshotUtil.takeScreenshot(result.getName());
    }
    if (driver != null) {
      driver.quit();
    }
  }

  protected void verifyProductCardDisplayed(ProductCard productCard, String detailScope) {
    logger.info("Verifying product card with detail scope: {}", detailScope);

    Item itemDetails = productCard.getItemDetails();
    verifyItemDetail(itemDetails, detailScope);

    assertTrue(productCard.isAddOrRemoveButtonDisplayed(), "Button 'Add to cart/Remove' is not displayed");
    logger.debug("Verified 'Add to cart/Remove' button is displayed.");
  }

  public void verifyItemDetail(Item item, String detailScope) {
    verifyNonEmptyAttribute("Item name", item.getName());
    logger.debug("Verified item name: {}", item.getName());

    verifyNonEmptyAttribute("Item description", item.getDescription());
    logger.debug("Verified item description: {}", item.getDescription());

    verifyPositivePrice(item.getPrice());
    logger.debug("Verified item price: {}", item.getPrice());

    if ("all".equals(detailScope)) {
      verifyNonEmptyAttribute("Item image", item.getImageSrc());
      logger.debug("Verified item image: {}", item.getImageSrc());
    }
  }

  private void verifyNonEmptyAttribute(String attributeName, String value) {
    assertTrue(value != null && !value.isEmpty(), attributeName + " is not displayed");
  }

  private void verifyPositivePrice(Double value) {
    assertTrue(value != null && value > 0, "Item price is not displayed or invalid");
  }


  protected void verifyProductCardDisplayed(String detailScope) {
    List<ProductCard> productCardList = productPage.getListProductCard();
    logger.info("Verifying product card list. Total items: {}, Detail scope: {}", productCardList.size(), detailScope);
    for (ProductCard item : productCardList) {
      logger.debug("Verifying product card: {}", item);
      verifyProductCardDisplayed(item, detailScope);
    }
    logger.info("All product cards verified successfully.");

  }

  public static int getRandomItem(List<ProductCard> listProductCard) {
    return ThreadLocalRandom.current().nextInt(0, listProductCard.size());
  }

  public void addRandomItemAndNavigateToCartPage() {
    productPage.addItemToCart();
    headerPage.openCart();
    logger.debug("Item added to cart. Navigating to cart page.");


  }

  public Item extractItemDetailFromProductCard(ProductCard productCard, String detailScope) {
    logger.info("Extracting item details from a single product card. Detail scope: {}", detailScope);
    Item item = createItemFromProductCard(productCard, detailScope);
    logger.debug("Extracted item: {}", item);
    return item;
  }

  public List<Item> extractItemDetailFromProductCardList(List<ProductCard> productCardList, String detailScope) {
    logger.info("Extracting item details from a list of product cards. Detail scope: {}", detailScope);
    return productCardList.stream()
        .map(productCard -> {
          Item item = createItemFromProductCard(productCard, detailScope);
          logger.debug("Extracted item: {}", item);
          return item;
        })
        .collect(Collectors.toList());
  }

  private Item createItemFromProductCard(ProductCard productCard, String detailScope) {
    boolean includeImage = detailScope.equals("all");

    return includeImage
        ? new Item(
        productCard.getItemDetails().getName(),
        productCard.getItemDetails().getDescription(),
        productCard.getItemDetails().getPrice(),
        productCard.getItemDetails().getImageSrc())
        : new Item(
        productCard.getItemDetails().getName(),
        productCard.getItemDetails().getDescription(),
        productCard.getItemDetails().getPrice());
  }


  public void addItemAndNavigateToCheckoutPage(int countItem) {
    logger.debug("Starting process to add items and navigate to the checkout page.");

    logger.debug("Adding " + countItem + " items to the cart.");
    productPage.addItemsToCart(countItem);

    logger.debug("Opening the cart.");
    headerPage.openCart();

    logger.debug("Initializing CartPage object.");
    cartPage = new CartPage(driver);

    logger.debug("Clicking the checkout button.");
    cartPage.clickCheckoutButton();

  }

  public void verifyCurrentUrl(String expectedUrl) {
    String currentUrl = driver.getCurrentUrl();
    assertEquals(currentUrl, expectedUrl, "URL after navigation is incorrect");
  }

}


