package org.example.pages;

import org.example.models.Item;
import org.example.models.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductPage extends BasePage {

  private final By productItemElements = By.className("inventory_item");
  private final By SortDropdownElement = By.className("product_sort_container");
  private final By itemPriceElement = By.className("inventory_item_price");
  private final By itemNameElement = By.className("inventory_item_name");
  private final By addOrRemoveButtonElement = By.tagName("button");
  private final By itemImageElement = By.tagName("img");
  private final By itemDescriptionElement = By.className("inventory_item_desc");


  public ProductPage(WebDriver driver) {
    super(driver);
  }

  public List<WebElement> getProductCardElements() {
    logger.debug("Fetching list of product items from the page.");
    return driver.findElements(productItemElements);
  }

  public WebElement getSortDropdown() {
    logger.debug("Fetching the sort dropdown element.");
    return driver.findElement(SortDropdownElement);
  }

  public List<ProductCard> getListProductCard() {
    logger.debug("Building a list of product cards from the page.");

    List<WebElement> listProductCard = getProductCardElements();

    return listProductCard.stream()
        .map(this::buildProductCard)
        .collect(Collectors.toList());
  }

  public List<ProductCard> getListProductCardByIndexItemList(List<Integer> indexItemList) {
    logger.debug("Building a list of product cards from the page by item index list.");

    List<WebElement> listProductCard = getProductCardElements();

    Stream<WebElement> filteredStream = indexItemList == null || indexItemList.isEmpty()
        ? listProductCard.stream()
        : indexItemList.stream().map(listProductCard::get);

    return filteredStream
        .map(this::buildProductCard)
        .collect(Collectors.toList());

  }

  private ProductCard buildProductCard(WebElement productCard) {
    try {
      logger.trace("Processing product item to create a ProductCard object.");
      String name = productCard.findElement(itemNameElement).getText();
      String description = productCard.findElement(itemDescriptionElement).getText();
      Double price = extractDigits(productCard.findElement(itemPriceElement).getText());
      String imageSrc = productCard.findElement(itemImageElement).getAttribute("src");

      Item itemDetails = new Item(name, description, price, imageSrc);

      WebElement button = productCard.findElement(addOrRemoveButtonElement);
      WebElement nameLink = productCard.findElement(itemNameElement);
      WebElement imageLink = productCard.findElement(itemImageElement);

      return new ProductCard(itemDetails, nameLink, imageLink, button);
    } catch (Exception e) {
      logger.error("Failed to process product item. Skipping this item.", e);
      return null;
    }
  }

  public int getNumberOfProducts() {
    logger.debug("Fetching the number of products available on the page.");
    return getListProductCard().size();
  }

  public void sortProductsCarsListBy(String sortName) {
    logger.info("Sorting product list by criteria: {}", sortName);
    Select listOfSortProduct = new Select(getSortDropdown());
    listOfSortProduct.selectByValue(sortName);
  }

  public void navigateToProductDetailsPage(int itemNumber) {
    logger.info("Navigating to product details page for item number: {}", itemNumber);
    if (itemNumber < 0 || itemNumber >= getListProductCard().size()) {
      logger.error("Invalid item number: {}. Cannot navigate to details page.", itemNumber);
      throw new IllegalArgumentException("Invalid item number: " + itemNumber);
    }
    getListProductCard().get(itemNumber).clickNameLink();
  }

  public int addItemToCardAndReturnItemIndex() {
    logger.info("Adding a random item to the cart.");
    List<ProductCard> productCardList = getListProductCard();
    int randomIndex = getRandomItem(productCardList);
    logger.debug("Random item index generated: {}", randomIndex);
    productCardList.get(randomIndex).clickAddOrRemoveButton();
    logger.info("Product added to cart: {}", productCardList.get(randomIndex));
    return randomIndex;
  }

  public void addItemToCart() {
    logger.info("Adding a random item to the cart.");
    ProductCard randomItem = selectRandomProductCard();
    randomItem.clickAddOrRemoveButton();
    logger.info("Product added to cart: {}", randomItem);
  }

  public void addItemToCartByIndexItem(int indexItem) {
    logger.info("Adding item to the cart at index: {}", indexItem);
    List<ProductCard> productCardList = getListProductCard();
    productCardList.get(indexItem).clickAddOrRemoveButton();
    logger.info("Product added to cart: {}", productCardList.get(indexItem));
  }

  public void addItemsToCart(int countAddedItem) {
    logger.info("Adding {} items to the cart.", countAddedItem);
    List<ProductCard> productCardList = getListProductCard();
    if (validateItemCount(countAddedItem, productCardList)) {
      return;
    }

    for (int i = 0; i < countAddedItem; i++) {
      ProductCard randomItem = selectRandomProductCard();
      randomItem.clickAddOrRemoveButton();
      logger.info("Item added to cart. Item: {}", randomItem);
    }
  }

  public List<Integer> addItemsToCartAndReturnListAddedIndexItem(int countAddedItem) {
    logger.info("Adding {} items to the cart and returning their indices.", countAddedItem);
    List<Integer> listIndexAddedItems = new ArrayList<>();
    List<ProductCard> productCardList = getListProductCard();
    if (validateItemCount(countAddedItem, productCardList)) {
      return listIndexAddedItems;
    }

    for (int i = 0; i < countAddedItem; i++) {
      int indexItem = getRandomIndexItem(productCardList);
      productCardList.get(indexItem).clickAddOrRemoveButton();
      logger.info("Item added to cart. Index: {}", indexItem);
      productCardList = getListProductCard();
      listIndexAddedItems.add(indexItem);
    }
    return listIndexAddedItems;
  }

  private boolean validateItemCount(int countAddedItem, List<ProductCard> productCardList) {
    if (countAddedItem > productCardList.size()) {
      logger.error(
          "Cannot add {} items to the cart. Available items: {}.",
          countAddedItem, productCardList.size()
      );
      return false;
    }
    return true;
  }

  private static int getRandomIndexItem(List<ProductCard> productCardList) {
    return ThreadLocalRandom.current().nextInt(0, productCardList.size());
  }

  public void removeItemFromCard(int indexItem) {
    logger.info("Removing item from the cart at index: {}", indexItem);
    List<ProductCard> productCardList = getListProductCard();
    productCardList.get(indexItem).clickAddOrRemoveButton();
    logger.info("Product removed from cart: {}", productCardList.get(indexItem));
  }

  public void removeItemsFromCart(List<Integer> listIndexItems) {
    logger.info("Removing items from the cart. Indices: {}", listIndexItems);
    for (Integer listIndexItem : listIndexItems) {
      List<ProductCard> productCardList = getListProductCard();
      productCardList.get(listIndexItem).clickAddOrRemoveButton();
      logger.info("Item removed from cart. Index: {}", listIndexItem);
    }
  }

  public String getActualNameButton(int indexItem) {
    logger.debug("Fetching the button name for item at index: {}", indexItem);
    List<ProductCard> productCardList = getListProductCard();
    return productCardList.get(indexItem).getActualNameButton();
  }

  public void clickRandomItemNameLinkAndValidateNavigation() {
    logger.info("Clicking on a random item's name link to navigate.");
    ProductCard randomItem = selectRandomProductCard();
    randomItem.clickNameLink();
  }

  public void clickRandomItemImageLinkAndValidateNavigation() {
    logger.info("Clicking on a random item's image link to navigate.");
    ProductCard randomItem = selectRandomProductCard();
    randomItem.clickImageNameLink();
  }

  public ProductCard selectRandomProductCard() {
    logger.info("Selecting a random product card.");
    List<ProductCard> productCardList = getListProductCard();
    int randomIndex = getRandomIndexItem(productCardList);
    ProductCard randomItem = productCardList.get(randomIndex);
    logger.info("Random product selected: {}", randomItem);
    return randomItem;
  }

}
