package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HeaderPage extends BasePage {

  private final By titlePageElement = By.className("title");
  private final By shoppingCartElement = By.id("shopping_cart_container");
  private final By cartQuantityElement = By.className("shopping_cart_badge");


  public HeaderPage(WebDriver driver) {
    super(driver);
  }

  public String getTitlePage() {
    WebElement titleCartPage = findElementIfExist(titlePageElement);
    if (titleCartPage == null) {
      logger.error("Title element is not found.");
      throw new IllegalStateException();
    }
    return titleCartPage.getText();
  }

  public WebElement getShoppingCartButton() {
    WebElement shoppingCart = findElementIfExist(shoppingCartElement);
    if (shoppingCart == null) {
      logger.error("Shopping cart element is not found.");
      throw new IllegalStateException();
    }
    return shoppingCart;
  }

  public String getCartQuantity(boolean isMandatory) {
    List<WebElement> cartQuantity = driver.findElements(cartQuantityElement);
    if (isMandatory && cartQuantity.isEmpty()) {
      logger.error("Cart quantity element is not found.");
      throw new IllegalStateException("Cart quantity element is missing but required.");
    }
    if (cartQuantity.isEmpty()) {
      return null;
    }
    return cartQuantity.get(0).getText();
  }

  public void openCart() {
    getShoppingCartButton().click();
  }

  public boolean isCartQuantityAbsent() {
    return getCartQuantity(false) == null;

  }
}
