# SauceDemo Test Automation Project

## Overview
This project contains automated UI tests for the [SauceDemo](https://www.saucedemo.com/) site. 

## Features
- Automated test cases covering:
    - User login 
    - Adding and removing items from the cart
    - Checkout process steps and validation
    - Verification of product details, totals, tax calculation, and final order summary

## Tech Stack
- **Programming Language:** Java
- **Test Framework:** TestNG 
- **Automation Tools:** Selenium WebDriver
- **Build Tool:** Maven
- **Logging:** SLF4J 
- **Assertions:** Built-in TestNG assertions


## Project Structure
- **models:** Contains POJO classes for ProductCard and Item.
- **pages:** Implements Page Object Model (POM) for various pages (e.g., LoginPage).
- **utils:** Utility classes for reusable functions, like taking screenshots.
- **listeners:** Custom listeners for logging and test reporting.
- **tests:** Contains TestNG test classes.
