import org.example.constants.PageContent;
import org.example.models.Item;
import org.example.models.ProductCard;
import org.example.pages.ItemDetailPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class ItemDetailTest extends BaseTest {

  ItemDetailPage itemDetailPage;
  List<ProductCard> productCardList;

  @BeforeMethod
  public void additionalSetUp() {
    logger.info("Setting up ItemDetailPage and fetching product card list.");
    itemDetailPage = new ItemDetailPage(driver);
    productCardList = productPage.getListProductCard();
    logger.debug("Product card list size: {}", productCardList.size());
  }

  private int navigateToRandomProductDetailPage() {
    int itemIndex = getRandomItem(productCardList);
    productPage.navigateToProductDetailsPage(itemIndex);
    logger.info("Navigated to product details page for item index: {}", itemIndex);
    return itemIndex;
  }

  @Test
  public void verifyItemDetailsDisplaysElements() {
    logger.info("Test: Verifying item detail page displays all elements.");
    navigateToRandomProductDetailPage();
    Item item = itemDetailPage.getItemDetail();
    logger.debug("Fetched item detail: {}", item);

    verifyItemDetail(item,"all");

    logger.debug("Verifying 'Add/Remove' button state.");
    assertButtonState(itemDetailPage.isAddOrRemoveItemButtonDisabled(), "'Add/Remove' button is not displayed.");

    logger.debug("Verifying 'Back to Products' button state.");
    assertButtonState(itemDetailPage.isBackToProductButtonDisabled(), "'Back to Products' button is not displayed.");
  }

  @Test
  public void verifyItemDetailContentCorrect() {
    logger.info("Test: Verifying item detail content matches product card details.");
    int itemIndex = navigateToRandomProductDetailPage();
    logger.debug("Selected item index: {}", itemIndex);

    ProductCard productCard = productCardList.get(itemIndex);
    Item expected = extractItemDetailFromProductCard(productCard, "all");
    logger.debug("Expected item detail: {}", expected);

    Item actual = itemDetailPage.getItemDetail();
    logger.debug("Actual item detail: {}", actual);

    verifyItemDetails(expected, actual);
  }

  @Parameters({"productsPageUrl"})
  @Test
  public void verifyBackToProductsButton(String productsPageUrl) {
    logger.info("Test: Verifying 'Back to Products' button navigates to the correct URL.");
    navigateToRandomProductDetailPage();

    logger.info("Clicking 'Back to Products' button.");
    itemDetailPage.clickBackToProductButton();

    logger.debug("Verifying current URL matches the expected products page URL.");
    verifyCurrentUrl(productsPageUrl);
  }

  @Test
  public void verifyCartItemCountUpdates() {
    logger.info("Test: Verifying cart item count updates when an item is added.");
    navigateToRandomProductDetailPage();

    logger.info("Clicking 'Add/Remove' button to add item to cart.");
    itemDetailPage.clickAddOrRemoveItemButton();

    String cartQuantity = headerPage.getCartQuantity(true);
    logger.debug("Cart quantity after update: {}", cartQuantity);

    assertEquals(cartQuantity, "1", "Cart item count did not update correctly.");
  }

  @Test
  public void verifyItemDetailPersistsOnNavigation() {
    logger.info("Test: Verifying item detail persists across navigation.");
    navigateToRandomProductDetailPage();

    Item expected = itemDetailPage.getItemDetail();
    logger.debug("Expected item detail: {}", expected);

    String currentUrl = driver.getCurrentUrl();
    logger.debug("Current item detail page URL: {}", currentUrl);

    logger.info("Navigating back to products and reloading item detail page.");
    itemDetailPage.clickBackToProductButton();
    driver.get(currentUrl);

    Item actual = itemDetailPage.getItemDetail();
    logger.debug("Actual item detail after navigation: {}", actual);

    verifyItemDetails(expected, actual);
  }

  @Parameters({"itemDetailPageUrl"})
  @Test
  public void verifyErrorDisplayedForInvalidItem(String itemDetailPageUrl) {
    logger.info("Test: Verifying error messages are displayed for an invalid item ID.");
    String invalidUrl = itemDetailPageUrl + "999";
    logger.debug("Navigating to invalid item detail page URL: {}", invalidUrl);
    driver.get(invalidUrl);

    String actualNameError = itemDetailPage.getItemName();
    logger.debug("Actual error name: {}", actualNameError);

    String actualDescriptionError = itemDetailPage.getItemDescription();
    logger.debug("Actual error description: {}", actualDescriptionError);

    assertEquals(actualNameError.toLowerCase(), PageContent.ItemDetailPageContent.ITEM_NOT_FOUND_ERROR.toLowerCase(),
        "Error message for invalid item ID is incorrect.");
    assertEquals(actualDescriptionError.toLowerCase(), PageContent.ItemDetailPageContent.INVALID_ID_DESCRIPTION_ERROR.toLowerCase(),
        "Error description for invalid item ID is incorrect.");
  }

  private void assertButtonState(boolean condition, String errorMessage) {
    logger.debug("Asserting button state. Expected condition: {}", condition);
    assertTrue(condition, errorMessage);
  }

  private void verifyItemDetails(Item expected, Item actual) {
    logger.debug("Verifying item details.");
    logger.debug("Expected item detail: {}", expected);
    logger.debug("Actual item detail: {}", actual);
    assertEquals(expected, actual, "Item details do not match.");
  }

}
