# Tealium Ecommerce Automation Testing

# Overview
This project automates test cases for the Tealium Ecommerce website (`https://ecommerce.tealiumdemo.com/`) using Selenium WebDriver and TestNG. The test cases cover registration, login, and shopping cart functionality.

# Project Structure
  - src/Tcs : Contains the Java test classes.
  - Registration.java: Tests for registration (AX-T1, AX-T2).
  - Login.java: Tests for login (AX-T3, AX-T4).
  - AddToCart.java: Tests for shopping cart (AX-T5).
- *resources*:
  - test-data.properties: Test data for automation.
  - Test_Cases.xlsx: Test case documentation.
- *chromedriver.exe*: ChromeDriver executable.

# Setup Instructions
1. Clone the repository.
2. Ensure Java and Eclipse are installed.
3. Add the JARs in to the project build path(Selenium, TestNG).

# Test Cases
- **AX-T1**: Register with valid data.
- **AX-T2**: Register with invalid email.
- **AX-T3**: Login with valid credentials.
- **AX-T4**: Login with invalid credentials.
- **AX-T5**: Add product to cart and verify.

# Dependencies
- Selenium WebDriver 3.141.59
- TestNG 7.11.0
