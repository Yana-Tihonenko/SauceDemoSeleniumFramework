package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {
  private final WebDriver driver;

  public ScreenshotUtil(WebDriver driver) {
    this.driver = driver;
  }

  public void takeScreenshot(String fileName) {
    File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    File destinationFile = new File("screenshots/" + fileName + ".png");
    try {
      Files.createDirectories(destinationFile.getParentFile().toPath());
      Files.copy(screenshotFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      System.out.println("Скриншот сохранен: " + destinationFile.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
