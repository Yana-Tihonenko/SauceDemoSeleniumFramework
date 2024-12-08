import org.example.constants.PageContent;
import org.example.models.Item;
import org.example.models.ProductCard;
import org.example.pages.CartPage;
import org.example.pages.CheckoutPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CheckoutTest extends BaseTest {

  CartPage cartPage;
  CheckoutPage checkoutPage;


  @BeforeMethod
  public void additionalSetUp() {
    logger.info("Initializing CartPage.");
    cartPage = new CartPage(driver);

    logger.info("Initializing CheckoutPage.");
    checkoutPage = new CheckoutPage(driver);
    logger.debug("CheckoutPage initialized successfully.");
  }

  @Test
  public void verifyCheckoutPageDisplaysAllElements() {
    logger.info("Test: Verifying all elements are displayed on the Checkout page.");
    addItemAndNavigateToCheckoutPage(1);

    logger.info("Verifying the title of Checkout Step One page.");
    assertEquals(headerPage.getTitlePage(), PageContent.CheckoutPageContent.TITLE_PAGE_ONE_STEP, "Page title does not match expected value.");

    logger.debug("Checking presence of input fields and buttons.");
    assertTrue(checkoutPage.isFirstNameDisplayed(), "'First Name' field not displayed.");
    assertTrue(checkoutPage.isLastNameDisplayed(), "'Last Name' field not displayed.");
    assertTrue(checkoutPage.isPostalCodeDisplayed(), "'Postal Code' field not displayed.");
    assertTrue(checkoutPage.isCancelButtonDisplayed(), "'Cancel' button not displayed.");
    assertTrue(checkoutPage.isContinueButtonDisplayed(), "'Continue' button not displayed.");

    logger.info("Verifying placeholders for input fields.");
    assertEquals(checkoutPage.getPlaceholderFieldFirstName(), PageContent.CheckoutPageContent.FIRST_NAME, "First Name placeholder mismatch.");
    assertEquals(checkoutPage.getPlaceholderFieldLastName(), PageContent.CheckoutPageContent.LAST_NAME, "Last Name placeholder mismatch.");
    assertEquals(checkoutPage.getPlaceholderFieldPostCode(), PageContent.CheckoutPageContent.POSTAL_CODE, "Postal Code placeholder mismatch.");
  }

  @Parameters("cartPageUrl")
  @Test
  public void verifyCancelButtonNavigatesToCartPage(String cartPageUrl) {
    logger.info("Test: Verifying Cancel button navigates to the Cart page.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Clicking the 'Cancel' button.");
    checkoutPage.clickCancelButton();
    logger.debug("Cancel button clicked, verifying navigation to Cart page.");

    String actualTitlePage = headerPage.getTitlePage();
    logger.debug("Current page title: {}", actualTitlePage);

    logger.info("Verifying the page title matches the expected Cart page title.");
    assertEquals(actualTitlePage, PageContent.CartPageContent.TITLE_PAGE, "Title mismatch for Cart page.");

    logger.info("Verifying the current URL matches the expected Cart page URL.");
    verifyCurrentUrl(cartPageUrl);


  }

  @Test
  public void verifyContinueButtonNavigatesToOrderSummary() {
    logger.info("Test: Verifying Continue button navigates to Order Summary.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Entering client information.");
    checkoutPage.generateAndInputClientInformation();

    logger.info("Clicking 'Continue' button to proceed to Checkout Step Two.");
    checkoutPage.clickContinueButton();

    String actualTitlePage = headerPage.getTitlePage();
    logger.debug("Current page title: {}", actualTitlePage);

    assertEquals(actualTitlePage, PageContent.CheckoutPageContent.TITLE_PAGE_SECOND_STEP, "Title mismatch for Checkout Step Two.");
  }

  @Test
  public void verifyItemDetailInOderSummary() {
    logger.info("Test: Verifying item details in Order Summary.");
    List<Integer> listIndexAddedItems = productPage.addItemsToCartAndReturnListAddedIndexItem(2);

    logger.debug("Fetching added product details for comparison.");
    List<ProductCard> addedProductCardList = productPage.getListProductCardByIndexItemList(listIndexAddedItems);
    List<Item> itemDetailListExpect = extractItemDetailFromProductCardList(addedProductCardList, "");
    logger.debug("Expected item details: {}", itemDetailListExpect);

    headerPage.openCart();
    logger.debug("Cart page opened.");

    cartPage.clickCheckoutButton();
    checkoutPage.generateAndInputClientInformation();
    checkoutPage.clickContinueButton();

    List<Item> itemDetailActual = checkoutPage.getListItemInOrder();
    logger.debug("Actual item details retrieved: {}", itemDetailActual);

    assertEquals(itemDetailListExpect, itemDetailActual, "Item details mismatch between Cart and Order Summary.");
  }

  @Test
  public void verifyOderTotalCalculation() {
    logger.info("Test: Verifying total order calculation.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Entering client information.");
    checkoutPage.generateAndInputClientInformation();

    logger.info("Proceeding to Checkout Step Two.");
    checkoutPage.clickContinueButton();

    logger.debug("Retrieving actual totals from Order Summary.");
    Double actualItemTotalOrder = checkoutPage.getItemTotalOrder();
    Double actualTaxOrder = checkoutPage.getTaxOrder();
    Double actualTotalOrder = checkoutPage.getTotalOrder();
    logger.debug("Actual - Item Total: {}, Tax: {}, Total: {}", actualItemTotalOrder, actualTaxOrder, actualTotalOrder);

    logger.debug("Calculating expected totals.");
    Double expectItemTotalOrder = checkoutPage.calculateSumItemOrder();
    Double expectTaxOrder = checkoutPage.calculateTaxOrder();
    Double expectTotalOrder = checkoutPage.calculateTotal();
    logger.debug("Expected - Item Total: {}, Tax: {}, Total: {}", expectItemTotalOrder, expectTaxOrder, expectTotalOrder);

    assertEquals(expectItemTotalOrder, actualItemTotalOrder, "Item total mismatch.");
    assertEquals(expectTaxOrder, actualTaxOrder, "Tax mismatch.");
    assertEquals(expectTotalOrder, actualTotalOrder, "Total mismatch.");
  }

  @Test
  public void verityOrderCompletion() {
    logger.info("Test: Verifying order completion process.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Entering client information.");
    checkoutPage.generateAndInputClientInformation();

    logger.info("Completing the order process.");
    checkoutPage.clickContinueButton();
    checkoutPage.clickFinishButton();

    logger.debug("Verifying order completion details.");
    assertEquals(headerPage.getTitlePage(), PageContent.CheckoutPageContent.TITLE_PAGE_COMPLETE, "Order completion page title mismatch.");
    assertEquals(checkoutPage.getTextCompleteHeader(), PageContent.CheckoutPageContent.COMPLETE_HEADER, "Order completion header mismatch.");
    assertEquals(checkoutPage.getTextComplete(), PageContent.CheckoutPageContent.COMPLETE_TEXT, "Order completion text mismatch.");
    assertTrue(checkoutPage.isBackToProductButtonDisplayed(), "'Back Home' button not displayed.");
    assertEquals(checkoutPage.getBackToProductButtonContent(), PageContent.CheckoutPageContent.BACK_TO_HOME_BUTTON, "'Back Home' button content mismatch.");
  }

  @Test
  public void verityBackButtonNavigatesToProductPage() {
    logger.info("Test: Verifying 'Back Home' button navigates to Product page.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Entering client information and completing the order.");
    checkoutPage.generateAndInputClientInformation();
    checkoutPage.clickContinueButton();
    checkoutPage.clickFinishButton();

    logger.info("Clicking 'Back Home' button.");
    checkoutPage.clickBackToProductButton();

    logger.debug("Verifying navigation to Product page.");
    assertEquals(headerPage.getTitlePage(), PageContent.ProductPage.TITLE_PAGE, "Navigation to Product page failed.");
  }

  @Test
  public void verityCartEmptyAfterCompletedOrder() {
    logger.info("Test: Verifying cart is empty after order completion.");
    addItemAndNavigateToCheckoutPage(2);

    logger.info("Completing the order process.");
    checkoutPage.generateAndInputClientInformation();
    checkoutPage.clickContinueButton();
    checkoutPage.clickFinishButton();
    checkoutPage.clickBackToProductButton();

    logger.debug("Checking if cart is empty.");
    assertTrue(headerPage.isCartQuantityAbsent(), "Cart is not empty after order completion.");
  }
}
