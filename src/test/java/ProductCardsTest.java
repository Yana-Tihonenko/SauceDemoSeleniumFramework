import org.example.constants.BusinessSetting;
import org.example.constants.PageContent;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.MessageUtils.formatMessage;
import static org.testng.Assert.*;

public class ProductCardsTest extends BaseTest {


  @Test
  public void verifyProductCardDisplayedElements() {
    logger.info("Test: Verifying that all product cards display the required elements.");
    verifyProductCardDisplayed("all");

    int actualProductCount = productPage.getNumberOfProducts();
    int expectedProductCount = Integer.parseInt(BusinessSetting.NUMBER_PRODUCTS_PRODUCT_PAGE);
    logger.debug("Verification of product count: Actual={}, Expected={}", actualProductCount, expectedProductCount);

    assertEquals(actualProductCount, expectedProductCount,
        formatMessage("Mismatch in product count. Actual='%s', Expected='%s'", actualProductCount, expectedProductCount));
  }

  @Test
  public void verifySortProductsByNameAscending() {
    logger.info("Test: Sorting products by Name (A to Z).");
    productPage.sortProductsCarsListBy("az");

    List<String> actualSortProductList = productPage.getListProductCard()
        .stream().map(productCard -> productCard.getItemDetails().getName()).collect(Collectors.toList());
    logger.debug("Sorted product names (Actual): {}", actualSortProductList);

    List<String> expectedSortProductList = new ArrayList<>(actualSortProductList);
    Collections.sort(expectedSortProductList);
    logger.debug("Sorted product names (Expected): {}", expectedSortProductList);

    assertEquals(actualSortProductList, expectedSortProductList, "Sorting by Name (A to Z) failed.");
  }

  @Test
  public void verifySortProductsByNameDescending() {
    logger.info("Test: Sorting products by Name (Z to A).");
    productPage.sortProductsCarsListBy("za");

    List<String> actualSortProductList = productPage.getListProductCard()
        .stream().map(productCard -> productCard.getItemDetails().getName()).collect(Collectors.toList());
    logger.debug("Sorted product names (Actual): {}", actualSortProductList);

    List<String> expectedSortProductList = new ArrayList<>(actualSortProductList);
    expectedSortProductList.sort(Comparator.reverseOrder());
    logger.debug("Sorted product names (Expected): {}", expectedSortProductList);

    assertEquals(actualSortProductList, expectedSortProductList, "Sorting by Name (Z to A) failed.");
  }

  @Test
  public void verifySortProductsByPriceAscending() {
    logger.info("Test: Sorting products by Price (low to high).");
    productPage.sortProductsCarsListBy("lohi");

    List<Double> actualSortProductList = productPage.getListProductCard()
        .stream().map(productCard -> productCard.getItemDetails().getPrice()).collect(Collectors.toList());
    logger.debug("Sorted product prices (Actual): {}", actualSortProductList);

    List<Double> expectedSortProductList = new ArrayList<>(actualSortProductList);
    Collections.sort(expectedSortProductList);
    logger.debug("Sorted product prices (Expected): {}", expectedSortProductList);

    assertEquals(actualSortProductList, expectedSortProductList, "Sorting by Price (low to high) failed.");
  }

  @Test
  public void verifySortProductsByPriceDescending() {
    logger.info("Test: Sorting products by Price (high to low).");
    productPage.sortProductsCarsListBy("hilo");

    List<Double> actualSortProductList = productPage.getListProductCard()
        .stream().map(productCard -> productCard.getItemDetails().getPrice()).collect(Collectors.toList());
    logger.debug("Sorted product prices (Actual): {}", actualSortProductList);

    List<Double> expectedSortProductList = new ArrayList<>(actualSortProductList);
    expectedSortProductList.sort(Comparator.reverseOrder());
    logger.debug("Sorted product prices (Expected): {}", expectedSortProductList);

    assertEquals(actualSortProductList, expectedSortProductList, "Sorting by Price (high to low) failed.");
  }

  @Test
  public void addSingleItemToCart_ButtonTextChangesToRemove() {
    logger.info("Test: Adding a single item to the cart and verifying the button text changes to 'Remove'.");
    int indexItemAdded = productPage.addItemToCardAndReturnItemIndex();
    logger.debug("Item added to the cart at index: {}", indexItemAdded);

    String actualButtonName = productPage.getActualNameButton(indexItemAdded);
    String expectedButtonName = PageContent.CommonContent.REMOVE_BUTTON_TEXT;
    logger.debug("Button text verification: Actual='{}', Expected='{}'", actualButtonName, expectedButtonName);

    assertEquals(actualButtonName, expectedButtonName,
        formatMessage("Mismatch in button text. Actual='%s', Expected='%s'", actualButtonName, expectedButtonName));
  }

  @Test
  public void addMultipleItemsToCart_ButtonTextChangesToRemove() {
    logger.info("Test: Adding multiple items to the cart and verifying the button text changes to 'Remove'.");
    int firstIndexItemAdded = productPage.addItemToCardAndReturnItemIndex();
    logger.debug("First item added to the cart at index: {}", firstIndexItemAdded);

    int secondIndexItemAdded = productPage.addItemToCardAndReturnItemIndex();
    logger.debug("Second item added to the cart at index: {}", secondIndexItemAdded);

    String actualFirstButtonName = productPage.getActualNameButton(firstIndexItemAdded);
    String actualSecondButtonName = productPage.getActualNameButton(secondIndexItemAdded);

    assertEquals(actualFirstButtonName, PageContent.CommonContent.REMOVE_BUTTON_TEXT,
        formatMessage("Mismatch in button text for the first item. Actual='%s', Expected='Remove'", actualFirstButtonName));
    assertEquals(actualSecondButtonName, PageContent.CommonContent.REMOVE_BUTTON_TEXT,
        formatMessage("Mismatch in button text for the second item. Actual='%s', Expected='Remove'", actualSecondButtonName));
  }

  @Test
  public void removeSingleItemFromCart_ButtonTextChangesToAddToCart() {
    logger.info("Test: Removing a single item from the cart and verifying the button text changes to 'Add to Cart'.");
    int indexItem = productPage.addItemToCardAndReturnItemIndex();
    logger.debug("Item added to the cart at index: {}", indexItem);

    productPage.removeItemFromCard(indexItem);
    logger.debug("Item removed from the cart at index: {}", indexItem);

    String actualButtonName = productPage.getActualNameButton(indexItem);
    String expectedButtonName = PageContent.CommonContent.ADD_TO_CART_BUTTON_TEXT;

    assertEquals(actualButtonName, expectedButtonName,
        formatMessage("Mismatch in button text after removing the item. Actual='%s', Expected='%s'", actualButtonName, expectedButtonName));
  }

  @Test
  public void addItemsToCart_CartItemCountIsUpdated() {
    logger.info("Test: Adding multiple items to the cart and verifying the cart item count is updated.");
    int countAddedItem = 3;

    productPage.addItemsToCart(countAddedItem);
    String actualCartQuantity = headerPage.getCartQuantity(true);
    String expectedCartQuantity = String.valueOf(countAddedItem);
    logger.debug("Cart quantity verification: Actual='{}', Expected='{}'", actualCartQuantity, expectedCartQuantity);

    assertEquals(actualCartQuantity, expectedCartQuantity,
        formatMessage("Mismatch in cart item count. Actual='%s', Expected='%s'", actualCartQuantity, expectedCartQuantity));
  }

  @Test
  public void removeSingleItemFromCart_CartItemCountIsUpdated() {
    logger.info("Test: Removing a single item from the cart and verifying the cart item count is updated.");
    int countAddedItem = 3;

    List<Integer> listIndexAddedItems = productPage.addItemsToCartAndReturnListAddedIndexItem(countAddedItem);
    logger.debug("Items added to the cart. Indices: {}", listIndexAddedItems);

    productPage.removeItemFromCard(listIndexAddedItems.get(1));
    logger.debug("Item removed from the cart at index: {}", listIndexAddedItems.get(1));

    String actualCartQuantity = headerPage.getCartQuantity(true);
    String expectedCartQuantity = String.valueOf(countAddedItem - 1);
    logger.debug("Cart quantity verification: Actual='{}', Expected='{}'", actualCartQuantity, expectedCartQuantity);

    assertEquals(actualCartQuantity, expectedCartQuantity,
        formatMessage("Mismatch in cart item count after removal. Actual='%s', Expected='%s'", actualCartQuantity, expectedCartQuantity));
  }

  @Test
  public void removeAllItemsFromCart_CartItemCountIsAbsent() {
    logger.info("Test: Removing all items from the cart and verifying the cart item count is absent.");
    int countAddedItem = 2;

    List<Integer> listIndexAddedItems = productPage.addItemsToCartAndReturnListAddedIndexItem(countAddedItem);
    logger.debug("Items added to the cart. Indices: {}", listIndexAddedItems);

    productPage.removeItemsFromCart(listIndexAddedItems);
    logger.debug("All items removed from the cart. Indices: {}", listIndexAddedItems);

    boolean isCartQuantityAbsent = headerPage.isCartQuantityAbsent();
    logger.debug("Verification of cart being empty: {}", isCartQuantityAbsent);

    assertTrue(isCartQuantityAbsent, "The cart is not empty as expected.");
  }

  @Test
  public void clickOnNameLinkItem_NavigatesToProductDetailsPage() {
    logger.info("Test: Clicking on a product name and verifying navigation to the product details page.");
    productPage.clickRandomItemNameLinkAndValidateNavigation();

    String currentUrl = driver.getCurrentUrl();
    logger.debug("Current URL after navigation: {}", currentUrl);

    assertTrue(currentUrl.contains("inventory-item.html?id="),
        formatMessage("Navigation to product details page failed. Current URL: '{}'", currentUrl));
  }

  @Test
  public void clickOnImageLinkItem_NavigatesToProductDetailsPage() {
    logger.info("Test: Clicking on a product image and verifying navigation to the product details page.");
    productPage.clickRandomItemImageLinkAndValidateNavigation();

    String currentUrl = driver.getCurrentUrl();
    logger.debug("Current URL after navigation: {}", currentUrl);

    assertTrue(currentUrl.contains("inventory-item.html?id="),
        formatMessage("Navigation to product details page failed. Current URL: '{}'", currentUrl));
  }
}




