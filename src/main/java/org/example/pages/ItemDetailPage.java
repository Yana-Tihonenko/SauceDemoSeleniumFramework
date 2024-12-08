package org.example.pages;

import org.example.models.Item;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ItemDetailPage extends BasePage {

  private final By itemName = By.xpath("//div[@class='inventory_details_desc_container']/div[1]");
  private final By itemDescription = By.xpath("//div[@class='inventory_details_desc_container']/div[2]");
  private final By itemPrice = By.className("inventory_details_price");
  private final By itemImage = By.className("inventory_details_img");

  private final By addOrRemoveButton = By.xpath("//div[@class='inventory_details_desc_container']/child::button");

  private final By backToProductsButton = By.id("back-to-products");

  public ItemDetailPage(WebDriver driver) {
    super(driver);
  }

  public WebElement getItemNameElement() {
    logger.debug("Fetching 'Name Item' field.");
    WebElement ItemNamedElement = findElementIfExist(itemName);
    if (ItemNamedElement == null) {
      logger.error("'Item Name' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return ItemNamedElement;
  }

  public WebElement getItemDescriptionElement() {
    logger.debug("Fetching 'Description Item' field.");
    WebElement descriptionElement = findElementIfExist(itemDescription);
    if (descriptionElement == null) {
      logger.error("'Item Description' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException("'Item Description' field not found");
    }
    return descriptionElement;
  }

  public WebElement getItemPriceElement() {
    logger.debug("Fetching 'Price Item' field.");
    WebElement priceElement = findElementIfExist(itemPrice);
    if (priceElement == null) {
      logger.error("'Item Price' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException("'Item Price' field not found");
    }
    return priceElement;
  }

  public WebElement getItemImageElement() {
    logger.debug("Fetching 'Image Item' field.");
    WebElement imageElement = findElementIfExist(itemImage);
    if (imageElement == null) {
      logger.error("'Item Image' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException("'Item Image' field not found");
    }
    return imageElement;
  }

  public WebElement getAddOrRemoveButtonElement() {
    logger.debug("Fetching 'Add or Remove Button' field.");
    WebElement buttonElement = findElementIfExist(addOrRemoveButton);
    if (buttonElement == null) {
      logger.error("'Add or Remove Button' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException("'Add or Remove Button' field not found");
    }
    return buttonElement;
  }

  public WebElement getBackToProductsButtonElement() {
    logger.debug("Fetching 'Back to Products Button' field.");
    WebElement backButtonElement = findElementIfExist(backToProductsButton);
    if (backButtonElement == null) {
      logger.error("'Back to Products Button' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException("'Back to Products Button' field not found");
    }
    return backButtonElement;
  }


  public String getItemName() {
    return getItemNameElement().getText();
  }

  public String getItemDescription() {
    return getItemDescriptionElement().getText();
  }

  public double getItemPrice() {
    return Double.parseDouble(getItemPriceElement().getText().replace("$", ""));
  }

  public String getItemImageSrc() {
    return getItemImageElement().getAttribute("src");
  }

  public Item getItemDetail() {
    return new Item(getItemName(), getItemDescription(), getItemPrice(), getItemImageSrc());
  }

  public boolean isAddOrRemoveItemButtonDisabled() {
    return getAddOrRemoveButtonElement().isDisplayed();
  }

  public boolean isBackToProductButtonDisabled() {
    return getBackToProductsButtonElement().isDisplayed();
  }


  public void clickAddOrRemoveItemButton() {
    getAddOrRemoveButtonElement().click();
  }

  public void clickBackToProductButton() {
    getBackToProductsButtonElement().click();
  }


}
