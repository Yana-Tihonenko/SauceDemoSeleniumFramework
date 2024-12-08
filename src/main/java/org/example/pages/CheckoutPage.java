package org.example.pages;

import org.example.constants.BusinessSetting;
import org.example.models.Item;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CheckoutPage extends BasePage {

  //step one
  private final By firstNameField = By.id("first-name");
  private final By lastNameField = By.id("last-name");
  private final By postalCodeField = By.id("postal-code");
  private final By continueButton = By.id("continue");
  //step two
  private final By listProductCard = By.className("cart_item");
  private final By itemName = By.className("inventory_item_name");
  private final By itemDescription = By.className("inventory_item_desc");
  private final By itemPrice = By.className("inventory_item_price");
  private final By itemTotal = By.xpath("//*[@class=\"summary_info\"]/div[6]");
  private final By itemTax = By.xpath("//*[@class=\"summary_info\"]/div[7]");
  private final By total = By.xpath("//*[@class=\"summary_info\"]/div[8]");
  //step three
  private final By completeHeader = By.className("complete-header");
  private final By completeText = By.className("complete-text");
  private final By backToProductButton = By.id("back-to-products");

  //through pages
  private final By finishButton = By.id("finish");
  private final By cancelButton = By.id("cancel");

  public CheckoutPage(WebDriver driver) {
    super(driver);
  }


  public WebElement getFirstNameField() {
    logger.debug("Fetching 'First Name' field.");
    WebElement firstNameFieldElement = findElementIfExist(firstNameField);
    if (firstNameFieldElement == null) {
      logger.error("'First Name' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return firstNameFieldElement;
  }

  public boolean isFirstNameDisplayed() {
    logger.debug("Checking if 'First Name' field is displayed.");
    return getFirstNameField().isDisplayed();
  }

  public void enterFirstName(String firstName) {
    logger.info("Entering 'First Name': {}", firstName);
    getFirstNameField().sendKeys(firstName);
  }

  public String getPlaceholderFieldFirstName() {
    logger.debug("Fetching placeholder for 'First Name' field.");
    WebElement placeholderField = findElementIfExist(firstNameField);
    return placeholderField.getAttribute("placeholder");
  }

  public String getPlaceholderFieldLastName() {
    logger.debug("Fetching placeholder for 'Last Name' field.");
    WebElement placeholderField = findElementIfExist(lastNameField);
    return placeholderField.getAttribute("placeholder");
  }

  public String getPlaceholderFieldPostCode() {
    logger.debug("Fetching placeholder for 'Postal Code' field.");
    WebElement placeholderField = findElementIfExist(postalCodeField);
    return placeholderField.getAttribute("placeholder");
  }

  public WebElement getLastNameField() {
    logger.debug("Fetching 'Last Name' field.");
    WebElement lastNameFieldElement = findElementIfExist(lastNameField);
    if (lastNameFieldElement == null) {
      logger.error("'Last Name' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return lastNameFieldElement;
  }

  public void enterLastName(String lastName) {
    logger.info("Entering 'Last Name': {}", lastName);
    getLastNameField().sendKeys(lastName);
  }

  public boolean isLastNameDisplayed() {
    logger.debug("Checking if 'Last Name' field is displayed.");
    return getLastNameField().isDisplayed();
  }

  public WebElement getPostalCodeField() {
    logger.debug("Fetching 'Postal Code' field.");
    WebElement postalCodeFieldField = findElementIfExist(postalCodeField);
    if (postalCodeFieldField == null) {
      logger.error("'Postal Code' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return postalCodeFieldField;
  }

  public boolean isPostalCodeDisplayed() {
    logger.debug("Checking if 'Postal Code' field is displayed.");
    return getPostalCodeField().isDisplayed();
  }

  public void enterPostalCode(String postalCode) {
    logger.info("Entering 'Postal Code': {}", postalCode);
    getPostalCodeField().sendKeys(postalCode);
  }

  public WebElement getCancelButtonElement() {
    logger.debug("Fetching 'Cancel' button.");
    WebElement cancelButtonElement = findElementIfExist(cancelButton);
    if (cancelButtonElement == null) {
      logger.error("'Cancel' button not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return cancelButtonElement;
  }

  public boolean isCancelButtonDisplayed() {
    logger.debug("Checking if 'Cancel' button is displayed.");
    return getCancelButtonElement().isDisplayed();
  }

  public void clickCancelButton() {
    logger.info("Clicking 'Cancel' button.");
    getCancelButtonElement().click();
  }

  public WebElement getContinueButtonElement() {
    logger.debug("Fetching 'Continue' button.");
    WebElement continueButtonElement = findElementIfExist(continueButton);
    if (continueButtonElement == null) {
      logger.error("'Continue' button not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return continueButtonElement;
  }

  public void clickContinueButton() {
    logger.info("Clicking 'Continue' button.");
    getContinueButtonElement().click();
  }

  public boolean isContinueButtonDisplayed() {
    logger.debug("Checking if 'Continue' button is displayed.");
    return getContinueButtonElement().isDisplayed();
  }

  public void generateAndInputClientInformation() {
    int randomInt = ThreadLocalRandom.current().nextInt(0, 1000);
    String randomFirstName = "FirstNameAutoTest" + randomInt;
    String randomLastName = "LastNameAutoTest" + randomInt;
    String randomPostalCode = "11" + randomInt;

    logger.info("Generated random client information:");
    logger.info("First Name: {}", randomFirstName);
    logger.info("Last Name: {}", randomLastName);
    logger.info("Postal Code: {}", randomPostalCode);

    enterFirstName(randomFirstName);
    enterLastName(randomLastName);
    enterPostalCode(randomPostalCode);
  }

  public List<WebElement> getListItemDetailElements() {
    logger.debug("Fetching list of item detail elements.");
    List<WebElement> listItemsElements = findElementsIfExists(listProductCard);
    if (listItemsElements == null) {
      logger.warn("No item detail elements found.");
      return Collections.emptyList();
    }
    return listItemsElements;
  }

  public List<Item> getListItemInOrder() {
    logger.debug("Retrieving list of items in order.");
    List<WebElement> listItemsElements = getListItemDetailElements();
    return listItemsElements.stream().map(item -> {
      logger.trace("Processing item element.");
      String name = findElementIfExist(item, itemName).getText();
      String description = findElementIfExist(item, itemDescription).getText();
      Double price = Double.parseDouble(findElementIfExist(item, itemPrice).getText().replace("$", ""));
      return new Item(name, description, price);
    }).collect(Collectors.toList());
  }

  public WebElement getItemTotalElement() {
    logger.debug("Fetching 'Item Total' field.");
    WebElement itemTotalElement = findElementIfExist(itemTotal);
    if (itemTotalElement == null) {
      logger.error("'Item Total' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return itemTotalElement;
  }

  public double getItemTotalOrder() {
    logger.debug("Calculating total item order.");
    return extractDigits(getItemTotalElement().getText());
  }

  public WebElement getTaxElement() {
    logger.debug("Fetching 'Tax' field.");
    WebElement taxElement = findElementIfExist(itemTax);
    if (taxElement == null) {
      logger.error("'Tax' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return taxElement;
  }

  public double getTaxOrder() {
    logger.debug("Calculating tax order.");
    return extractDigits(getTaxElement().getText());
  }

  public WebElement getTotalElement() {
    logger.debug("Fetching 'Total' field.");
    WebElement totalElement = findElementIfExist(total);
    if (totalElement == null) {
      logger.error("'Total' field not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return totalElement;
  }

  public double getTotalOrder() {
    logger.debug("Calculating total order.");
    return extractDigits(getTotalElement().getText());
  }

  public Double calculateSumItemOrder() {
    logger.debug("Calculating sum of items in order.");
    List<Item> listItemOrder = getListItemInOrder();
    double sumItem = listItemOrder.stream().mapToDouble(Item::getPrice).sum();
    return formatToTwoDecimalPlaces(sumItem);
  }

  public Double calculateTaxOrder() {
    logger.debug("Calculating tax for the order.");
    return formatToTwoDecimalPlaces(calculateSumItemOrder() * Double.parseDouble(BusinessSetting.TAX));
  }

  public Double calculateTotal() {
    logger.debug("Calculating total for the order.");
    return formatToTwoDecimalPlaces(calculateSumItemOrder() + calculateTaxOrder());
  }

  public WebElement getFinishButtonElement() {
    logger.debug("Fetching 'Finish' button.");
    WebElement finishButtonElement = findElementIfExist(finishButton);
    if (finishButtonElement == null) {
      logger.error("'Finish' button not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return finishButtonElement;
  }

  public void clickFinishButton() {
    logger.info("Clicking 'Finish' button.");
    getFinishButtonElement().click();
  }

  public WebElement getCompleteHeaderElement() {
    logger.debug("Fetching 'Complete Header'.");
    WebElement completeHeaderElement = findElementIfExist(completeHeader);
    if (completeHeaderElement == null) {
      logger.error("'Complete Header' not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return completeHeaderElement;
  }

  public String getTextCompleteHeader() {
    logger.debug("Fetching text from 'Complete Header'.");
    return getCompleteHeaderElement().getText();
  }

  public WebElement getCompleteTextElement() {
    logger.debug("Fetching 'Complete Text'.");
    WebElement completeTextElement = findElementIfExist(completeText);
    if (completeTextElement == null) {
      logger.error("'Complete Text' not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return completeTextElement;
  }

  public String getTextComplete() {
    logger.debug("Fetching text from 'Complete Text'.");
    return getCompleteTextElement().getText();
  }

  public WebElement getBackToProductButtonElement() {
    logger.debug("Fetching 'Back to Product' button.");
    WebElement backToProductButtonElement = findElementIfExist(backToProductButton);
    if (backToProductButtonElement == null) {
      logger.error("'Back to Product' button not found on page: {}", driver.getCurrentUrl());
      throw new IllegalStateException();
    }
    return backToProductButtonElement;
  }

  public String getBackToProductButtonContent() {
    logger.debug("Fetching content of 'Back to Product' button.");
    return getBackToProductButtonElement().getText();
  }

  public void clickBackToProductButton() {
    logger.info("Clicking 'Back to Product' button.");
    getBackToProductButtonElement().click();
  }

  public boolean isBackToProductButtonDisplayed() {
    logger.debug("Checking if 'Back to Product' button is displayed.");
    return getBackToProductButtonElement().isDisplayed();
  }


}
