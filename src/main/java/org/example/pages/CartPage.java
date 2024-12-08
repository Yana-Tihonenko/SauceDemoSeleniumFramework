package org.example.pages;

import org.example.models.Item;
import org.example.models.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertTrue;

public class CartPage extends BasePage {
  private final By listItemElements = By.className("cart_item");
  private final By itemNameElement = By.className("inventory_item_name");
  private final By itemDescriptionElement = By.className("inventory_item_desc");
  private final By itemPriceElement = By.className("inventory_item_price");
  private final By removeButtonElement = By.tagName("button");
  private final By continueShoppingButtonElement = By.id("continue-shopping");
  private final By checkoutButtonElement = By.id("checkout");

  public CartPage(WebDriver driver) {
    super(driver);
  }


  public List<WebElement> getListItemDetailElements() {
    List<WebElement> listItemsElements = findElementsIfExists(listItemElements);
    return listItemsElements != null ? listItemsElements : Collections.emptyList();
  }

  public WebElement getContinueShoppingButtonElement() {
    return getElementOrThrow(continueShoppingButtonElement, "Continue Shopping");
  }

  public WebElement getCheckoutButtonElement() {
    return getElementOrThrow(checkoutButtonElement, "Checkout");
  }

  public void clickContinueShoppingButton() {
    getContinueShoppingButtonElement().click();
  }

  public void clickCheckoutButton() {
    getCheckoutButtonElement().click();
  }

  public List<ProductCard> getListProductCard() {
    return getListItemDetailElements().stream()
        .map(this::buildProductCard)
        .collect(Collectors.toList());
  }

  public ProductCard getProductCard(int indexItem) {
    List<WebElement> productCards = getListItemDetailElements();
    if (indexItem < 0 || indexItem >= productCards.size()) {
      logger.error("Invalid index: {}. Product card does not exist.", indexItem);
      throw new IndexOutOfBoundsException("Product card index is out of bounds.");
    }
    return buildProductCard(productCards.get(indexItem));
  }

  public void verifyRemoveButtonForAllItemsClickable(List<ProductCard> productCardList) {
    productCardList.forEach(productCard -> {
      assertTrue(productCard.isAddOrRemoveButtonClickable(), "Remove button is not clickable.");
      logger.debug("Verified remove button is clickable for product card: {}", productCard);
    });
  }

  private WebElement getElementOrThrow(By locator, String elementName) {
    WebElement element = findElementIfExist(locator);
    if (element == null) {
      logger.error("Element '{}' not found on page: {}", elementName, driver.getCurrentUrl());
      throw new IllegalStateException(elementName + " element not found.");
    }
    return element;
  }

  private ProductCard buildProductCard(WebElement productCardElement) {
    String name = findElementIfExist(productCardElement, itemNameElement).getText();
    String description = findElementIfExist(productCardElement, itemDescriptionElement).getText();
    Double price = extractDigits(findElementIfExist(productCardElement, itemPriceElement).getText());
    Item itemDetails = new Item(name, description, price);
    WebElement button = findElementIfExist(productCardElement, removeButtonElement);
    WebElement nameLink = findElementIfExist(productCardElement, itemNameElement);
    return new ProductCard(itemDetails, nameLink, button);
  }


}
