package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class BasePage {

  protected WebDriver driver;

  protected final Logger logger = LogManager.getLogger(getClass());

  public BasePage(WebDriver driver) {
    this.driver = driver;
  }

  protected WebElement findElementIfExist(By locator) {
    List<WebElement> element = driver.findElements(locator);
    return element.isEmpty() ? null : element.get(0);
  }

  protected WebElement findElementIfExist(WebElement element, By locator) {
    try {
      return element.findElement(locator);
    } catch (NoSuchElementException e) {
      logger.warn("Element with locator {} not found.", locator);
      return null;
    }
  }

  protected List<WebElement> findElementsIfExists(By locator) {
    List<WebElement> elements = driver.findElements(locator);
    return elements.isEmpty() ? null : elements;
  }

  public static int getRandomItem(List<ProductCard> listProductCard) {
    return ThreadLocalRandom.current().nextInt(0, listProductCard.size());
  }

  public Double extractDigits(String str) {
    return Double.parseDouble(str.replaceAll("[^0-9.]", ""));

  }

  public static double formatToTwoDecimalPlaces(double totalSum) {
    BigDecimal bd = new BigDecimal(Double.toString(totalSum));
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}

