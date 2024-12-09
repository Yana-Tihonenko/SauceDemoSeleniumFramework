import org.example.constants.PageContent;
import org.example.models.Item;
import org.example.models.ProductCard;
import org.example.pages.CartPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.example.utils.MessageUtils.formatMessage;
import static org.testng.Assert.*;

public class CartPageTest extends BaseTest {
  List<ProductCard> listItemsFromProductPage;

  @BeforeMethod
  public void additionalSetUp() {
    logger.info("Setting up CartPage and initializing product list.");
    cartPage = new CartPage(driver);
    listItemsFromProductPage = productPage.getListProductCard();
    logger.debug("Initialized product list with {} items.", listItemsFromProductPage.size());
  }

  @Test
  public void verifyCartPageDisplayedElement() {
    logger.info("Test: Verifying all elements on the Cart page are displayed.");
    productPage.addItemToCart();
    logger.info("Added item to the cart. Navigating to Cart page.");
    headerPage.openCart();

    String actualTitle = headerPage.getTitlePage();
    List<ProductCard> cartItems = cartPage.getListProductCard();
    logger.debug("Cart page title: {}", actualTitle);
    logger.debug("Cart contains {} items: {}", cartItems.size(), cartItems);

    assertEquals(actualTitle, PageContent.CartPageContent.TITLE_PAGE, formatMessage("Cart page title mismatch. Expected: '%s', Actual: '%s'", PageContent.CartPageContent.TITLE_PAGE, actualTitle));
    assertTrue(cartPage.getCheckoutButtonElement().isDisplayed(), "'Checkout' button is not displayed.");
    assertTrue(cartPage.getContinueShoppingButtonElement().isDisplayed(), "'Continue Shopping' button is not displayed.");

    logger.info("Verified all Cart page elements are displayed.");
  }

  @Test
  public void verifyItemDetailContentCorrect() {
    logger.info("Test: Verifying item details in the Cart match the selected product.");
    int randomIndex = getRandomItem(listItemsFromProductPage);
    ProductCard expectedProductCard = listItemsFromProductPage.get(randomIndex);
    Item expectedItem = extractItemDetailFromProductCard(expectedProductCard, "");
    logger.debug("Expected item details: {}", expectedItem);

    productPage.addItemToCartByIndexItem(randomIndex);
    logger.info("Added item at index {} to the cart.", randomIndex);

    headerPage.openCart();
    ProductCard actualProductCard = cartPage.getProductCard(0);
    Item actualItem = extractItemDetailFromProductCard(actualProductCard, "");
    logger.debug("Actual item details in cart: {}", actualItem);

    assertEquals(expectedItem, actualItem, "Item details in cart do not match the expected product.");
    logger.info("Verified item details in the Cart match the selected product.");
  }

  @Test
  public void removeItemFromCart() {
    logger.info("Test: Removing an item from the cart and verifying the cart is empty.");
    addRandomItemAndNavigateToCartPage();

    List<ProductCard> cartItemsBeforeRemoval = cartPage.getListProductCard();
    logger.debug("Cart items before removal: {}", cartItemsBeforeRemoval);

    cartItemsBeforeRemoval.get(0).clickAddOrRemoveButton();
    logger.info("Removed the first item from the cart.");

    List<ProductCard> cartItemsAfterRemoval = cartPage.getListProductCard();
    logger.debug("Cart items after removal: {}", cartItemsAfterRemoval);

    assertTrue(cartItemsAfterRemoval.isEmpty(), "Cart is not empty after item removal.");
    logger.info("Verified the cart is empty after item removal.");
  }

  @Parameters("productsPageUrl")
  @Test
  public void verifyContinueShoppingButtonNavigateToProductPage(String productsPageUrl) {
    logger.info("Test: Verifying 'Continue Shopping' button navigates to the Product page.");
    addRandomItemAndNavigateToCartPage();

    cartPage.clickContinueShoppingButton();
    logger.debug("Clicked 'Continue Shopping' button.");

    verifyCurrentUrl(productsPageUrl);
    logger.info("Verified 'Continue Shopping' button navigates to the Product page.");
  }

  @Parameters("checkoutPageUrl")
  @Test
  public void verifyCheckoutButtonNavigateToCheckoutPage(String checkoutPageUrl) {
    logger.info("Test: Verifying 'Checkout' button navigates to the Checkout page.");
    addRandomItemAndNavigateToCartPage();

    cartPage.clickCheckoutButton();
    logger.debug("Clicked 'Checkout' button.");

    verifyCurrentUrl(checkoutPageUrl);
    logger.info("Verified 'Checkout' button navigates to the Checkout page.");
  }

  @Test
  public void verifyRemoveButtonIsClickable() {
    logger.info("Test: Verifying all 'Remove' buttons in the Cart are clickable.");
    productPage.addItemsToCart(2);
    headerPage.openCart();

    List<ProductCard> cartItems = cartPage.getListProductCard();
    logger.debug("Cart items: {}", cartItems);

    cartPage.verifyRemoveButtonForAllItemsClickable(cartItems);
    logger.info("Verified all 'Remove' buttons in the Cart are clickable.");
  }

  @Test
  public void verifyQuantityCanUpdate() {
    logger.info("Test: Verifying cart quantity updates after item removal.");
    productPage.addItemsToCart(2);
    headerPage.openCart();

    List<ProductCard> cartItems = cartPage.getListProductCard();
    logger.debug("Cart items before quantity update: {}", cartItems);

    cartItems.get(0).clickAddOrRemoveButton();
    logger.info("Removed the first item from the cart.");

    String actualQuantity = headerPage.getCartQuantity(true);
    logger.debug("Cart quantity after update: {}", actualQuantity);

    assertEquals(actualQuantity, "1", formatMessage("Cart quantity mismatch. Expected: '%s', Actual: '%s'", "1", actualQuantity));
    logger.info("Verified cart quantity updates correctly after item removal.");
  }

  @Parameters("itemDetailPageUrl")
  @Test
  public void verifyCartItemNavigatesToDetailPage(String itemDetailPageUrl) {
    logger.info("Test: Verifying cart item navigates to the correct detail page.");
    addRandomItemAndNavigateToCartPage();

    List<ProductCard> cartItems = cartPage.getListProductCard();
    logger.debug("Cart items: {}", cartItems);

    cartItems.get(0).clickNameLink();
    logger.info("Clicked on the first item's name link.");

    String currentUrl = driver.getCurrentUrl();
    assertTrue(currentUrl.contains("inventory-item.html?id="));
  }
}
