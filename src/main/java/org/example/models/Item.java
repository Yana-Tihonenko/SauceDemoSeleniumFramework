package org.example.models;

import java.util.Objects;

public class Item {

  private final String name;
  private final String description;
  private final Double price;
  private String imageSrc;


  public Item(String name, String description, Double price, String image) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.imageSrc = image;
  }


  public Item(String name, String description, Double price) {
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Double getPrice() {
    return price;
  }

  public String getImageSrc() {
    return imageSrc;
  }

  @Override
  public String toString() {
    return String.format("Name: %s, Description: %s, Price: %.2f, ImageSrc: %s",
        name, description, price, imageSrc);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Item that)) return false;
    return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(imageSrc, that.imageSrc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, price, imageSrc);
  }
}
