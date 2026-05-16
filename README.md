# Selenium Test Automation Framework

A robust Selenium WebDriver-based end-to-end test automation framework built with Java and TestNG, demonstrating best practices including the **Page Object Model (POM)** design pattern.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Architecture](#test-architecture)
- [Page Objects](#page-objects)
- [Debugging & Troubleshooting](#debugging--troubleshooting)
- [Best Practices](#best-practices)

---

## 🎯 Project Overview

This framework automates end-to-end testing of an e-commerce web application. It includes:

- **End-to-End Tests**: Complete order workflow (login → browse → add to cart → checkout)
- **Error Handling Tests**: Validation of error messages and edge cases
- **Parameterized Testing**: Data-driven tests using JSON and DataProviders
- **Page Object Model**: Separation of test logic from page interactions
- **Screenshot Capture**: Automatic screenshots on test failure
- **Extent Reports**: Test execution reporting with test results
- **Cross-browser Support**: Chrome, Edge browsers (Firefox ready for implementation)

### Key Application Under Test
- **URL**: https://rahulshettyacademy.com/client
- **Type**: E-commerce web application
- **Technologies**: Angular (frontend), REST API (backend)

---

## 📁 Project Structure

```
TestSeleniumFrameworkDesign/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── rahulshettyacademy/
│   │           ├── AbstractComponents/
│   │           │   └── AbstractComponent.java      # Base class for all page objects
│   │           ├── pageobjects/                    # Page Object Model classes
│   │           │   ├── LandingPage.java            # Login page
│   │           │   ├── ProductCatalogue.java       # Product listing page
│   │           │   ├── CartPage.java               # Shopping cart page
│   │           │   ├── CheckoutPage.java           # Checkout/payment page
│   │           │   ├── ConfirmationPage.java       # Order confirmation page
│   │           │   └── OrderPage.java              # Order history page
│   │           └── resources/
│   │               ├── GlobalData.properties        # Browser configuration
│   │               └── ExtentReporterNG.java        # Test reporting
│   │
│   └── test/
│       ├── java/
│       │   └── rahulshettyacademy/
│       │       ├── testcomponents/
│       │       │   ├── BaseTest.java                # Base test class with setup/teardown
│       │       │   └── Listeners.java               # TestNG listeners for reports
│       │       ├── tests/
│       │       │   ├── SubmitOrderTest.java         # Main order flow test
│       │       │   ├── ErrorValidationsTest.java    # Error handling tests
│       │       │   └── StandAloneTest.java          # Standalone test (legacy)
│       │       └── data/
│       │           ├── PurchaseOrder.json           # Test data in JSON format
│       │           └── DataReader.java              # JSON data utility
│       │
│       └── testSuites/
│           ├── testng.xml                           # Main TestNG suite
│           ├── purchase.xml                         # Purchase flow suite
│           └── ErrorValidationsTests.xml            # Error tests suite
│
├── reports/                                         # Test execution reports
│   ├── index.html                                   # Extent report
│   └── *.png                                        # Screenshots on failure
│
├── pom.xml                                          # Maven dependencies & configuration
├── README.md                                        # This file
└── target/                                          # Build artifacts
    ├── classes/                                     # Compiled classes
    ├── test-classes/                                # Compiled test classes
    ├── surefire-reports/                            # Test reports
    └── *.jar                                        # Built JAR files
```

---

## 📦 Prerequisites

Ensure you have the following installed:

- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **Git**: For version control (optional)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (optional)

### Verify Installation

```powershell
# Check Java version
java -version

# Check Maven version
mvn -version
```

---

## 🚀 Setup & Installation

### 1. Clone/Download the Project

```powershell
# If using Git
git clone <repository-url>
cd TestSeleniumFrameworkDesign
```

### 2. Install Dependencies

```powershell
# Download all Maven dependencies (including Selenium, TestNG, Jackson, etc.)
mvn clean install
```

This command:
- Cleans previous build artifacts
- Downloads dependencies from Maven central repository
- Compiles source code
- Runs tests (can be skipped with `-DskipTests`)

### 3. Verify Setup

```powershell
# List downloaded dependencies
mvn dependency:tree
```

---

## ⚙️ Configuration

### Browser Configuration

Edit `GlobalData.properties` to set the browser:

```properties
# File: src/main/java/rahulshettyacademy/resources/GlobalData.properties

# Supported values: chrome, edge, firefox
browser=chrome
```

### Supported Browsers

- **Chrome**: Fully implemented and tested (default)
- **Edge**: Implemented
- **Firefox**: Partially implemented (requires WebDriverManager setup)

---

## 🧪 Running Tests

### Run All Tests

```powershell
# Run all tests using Maven
mvn test
```

### Run Specific Test Suite

```powershell
# Run from TestNG xml suite
mvn test -Dsuite=src/testSuites/testng.xml

# Run purchase order tests only
mvn test -Dsuite=src/testSuites/purchase.xml

# Run error validation tests only
mvn test -Dsuite=src/testSuites/ErrorValidationsTests.xml
```

### Run Specific Test Class

```powershell
# Run a single test class
mvn -Dtest=SubmitOrderTest test

# Run a specific test method
mvn -Dtest=SubmitOrderTest#submitOrder test

# Run with custom thread count (parallel execution)
mvn -Dtest=SubmitOrderTest -DthreadCount=3 test
```

### Run with Different Browser

```powershell
# Override browser via Maven (requires property support in code)
mvn test -Dbrowser=edge
```

### Skip Tests During Build

```powershell
# Build without running tests
mvn clean install -DskipTests
```

---

## 🏗️ Test Architecture

### Page Object Model (POM)

The framework uses POM design pattern:

```
User Action (Test) → Page Object → Web Element Locator → WebDriver
```

**Benefits:**
- Separation of concerns: Test logic separate from locators
- Easy maintenance: Change locator once, updates everywhere
- Reusability: Common methods across multiple tests
- Readability: Clear, business-like test steps

### Example: Test Flow

```java
// Test code (business-like)
ProductCatalogue productCatalogue = landingPage.loginApplication("email", "password");
productCatalogue.addProductToCart("ZARA COAT 3");
CartPage cartPage = productCatalogue.gotoCartPage();
Boolean match = cartPage.VerifyProductDisplay("ZARA COAT 3");
Assert.assertTrue(match);
```

**vs low-level code without POM:**

```java
// Without POM (hard to maintain)
driver.findElement(By.id("userEmail")).sendKeys("email");
driver.findElement(By.id("userPassword")).sendKeys("password");
driver.findElement(By.id("login")).click();
// ... many more findElement calls ...
```

---

## 📄 Page Objects

### LandingPage (`LandingPage.java`)
- **Purpose**: Login page interactions
- **Key Methods**:
  - `goTo()`: Navigate to application
  - `loginApplication(email, password)`: Perform login
  - `getErrorMessage()`: Get login error message

### ProductCatalogue (`ProductCatalogue.java`)
- **Purpose**: Product listing and shopping interactions
- **Key Methods**:
  - `getProductList()`: Retrieve all products
  - `addProductToCart(productName)`: Add item to cart
  - `gotoCartPage()`: Navigate to cart
  - `gotoOrderPage()`: Navigate to order history

### CartPage (`CartPage.java`)
- **Purpose**: Shopping cart interactions
- **Key Methods**:
  - `VerifyProductDisplay(productName)`: Check if product exists in cart
  - `goToCheckout()`: Proceed to checkout

### CheckoutPage (`CheckoutPage.java`)
- **Purpose**: Checkout/payment flow
- **Key Methods**:
  - `selectCountry(countryName)`: Select shipping country
  - `submitOrder()`: Submit order

### ConfirmationPage (`ConfirmationPage.java`)
- **Purpose**: Order confirmation verification
- **Key Methods**:
  - `getConfirmationMessage()`: Get order confirmation text

### OrderPage (`OrderPage.java`)
- **Purpose**: Order history/My Orders page
- **Key Methods**:
  - `verifyOrderDisplay(productName)`: Check if order exists in history

### AbstractComponent (`AbstractComponent.java`)
- **Purpose**: Base class with shared functionality
- **Key Methods**:
  - `waitForElementToAppear(By)`: Wait for element visibility
  - `waitForWebElementToAppear(element)`: Wait for WebElement visibility
  - `gotoCartPage()`: Navigate to cart (shared across pages)
  - `gotoOrderPage()`: Navigate to orders (shared across pages)
  - `waitForElementToDisappear(element)`: Wait for element invisibility

---

## 🧬 Test Classes

### SubmitOrderTest (`SubmitOrderTest.java`)
**Status**: Main test class - PRODUCTION

**Tests**:
1. `submitOrder()` - End-to-end order placement
2. `OrderHistoryTest()` - Verify order appears in history

**Features**:
- Data-driven (parametrized with JSON)
- Multiple test datasets
- Screenshot on failure

**Run**:
```powershell
mvn -Dtest=SubmitOrderTest test
```

### ErrorValidationsTest (`ErrorValidationsTest.java`)
**Status**: Error handling - PRODUCTION

**Tests**:
1. `LoginErrorValidation()` - Incorrect credentials
2. `ProductErrorValidation()` - Product name mismatch

**Features**:
- Grouped under "ErrorHandling"
- Validates error messages
- Tests edge cases

**Run**:
```powershell
mvn -Dtest=ErrorValidationsTest test
```

### StandAloneTest (`StandAloneTest.java`)
**Status**: Legacy - Can run standalone

**Purpose**: Quick validation without TestNG framework

**Run as Java application**:
```powershell
# Compile if needed
mvn compile

# Run standalone (requires manually setting classpath)
java -cp target/classes:target/test-classes rahulshettyacademy.tests.StandAloneTest
```

---

## 🧩 Test Data

### JSON Data Format (`PurchaseOrder.json`)

```json
[
  {
    "email": "renukreddy47@gmail.com",
    "password": "Rn#ti$1234",
    "product": "ZARA COAT 3"
  },
  {
    "email": "renukreddy47@gmail.com",
    "password": "Rn#ti$1234",
    "product": "ADIDAS ORIGINAL"
  }
]
```

### Data-Driven Testing

The `@DataProvider` annotation in test classes provides data:

```java
@DataProvider
public Object[][] getData() throws IOException {
    List<HashMap<String,String>> data = getJsonDataToMap("path/to/file.json");
    return new Object[][] { {data.get(0)}, {data.get(1)} };
}

@Test(dataProvider = "getData")
public void submitOrder(HashMap<String,String> input) {
    // Each dataset triggers a separate test run
}
```

---

## 📊 Test Reporting

### Extent Reports

Reports are generated in: `reports/index.html`

**To view reports**:
1. After test execution, open `reports/index.html` in a browser
2. Report includes:
   - Test pass/fail status
   - Execution time
   - Screenshots on failure
   - Test logs

### Console Output

TestNG prints summary to console:
```
===== Test Summary =====
Tests run: 4, Failures: 0, Skipped: 0
Success Rate: 100%
```

---

## 🐛 Debugging & Troubleshooting

### Enable Debug Logging

```powershell
# Run with verbose output
mvn test -X
```

### Common Issues

#### 1. **ChromeDriver Version Mismatch**
**Error**: `SessionNotCreatedException: session not created`

**Solution**:
```powershell
# WebDriverManager automatically handles driver, but if issues persist:
# Verify Chrome browser version (chrome://version)
# WebDriverManager will match driver to installed Chrome version
```

#### 2. **Element Not Found (NoSuchElementException)**
**Error**: `NoSuchElementException: no such element`

**Solution**:
- Check CSS/XPath selectors in page object class
- Use browser DevTools to inspect element
- Wait using `waitForElementToAppear()` before interacting

#### 3. **Timeout Waiting for Element (TimeoutException)**
**Error**: `TimeoutException: Timed out after 5 seconds`

**Solution**:
- Element might not be visible on page
- CSS selector might be incorrect
- Increase wait time in `AbstractComponent.java`
- Check if page navigated correctly

#### 4. **Test Data File Not Found**
**Error**: `FileNotFoundException: PurchaseOrder.json`

**Solution**:
```powershell
# Verify file path in DataProvider
# Example correct path:
System.getProperty("user.dir") + "//src//test//java//rahulshettyacademy//data//PurchaseOrder.json"
```

### Debug with IDE

**IntelliJ IDEA**:
1. Right-click test method → "Debug <test-name>"
2. Set breakpoints on lines of interest
3. F8 to step over, F7 to step into
4. View variable values in "Variables" pane

**Eclipse**:
1. Right-click test method → "Debug As" → "TestNG Test"
2. Set breakpoints and debug similar to IntelliJ

### Print Statements for Debugging

```java
// Add debugging output
System.out.println("Current URL: " + driver.getCurrentUrl());
System.out.println("Page title: " + driver.getTitle());
System.out.println("Product list size: " + productList.size());
```

---

## 🎓 Best Practices

### 1. **Wait Strategies**
- Use **Explicit Waits** (WebDriverWait) over Implicit Waits when possible
- Implicit Wait: Once set, applies to every element lookup (global)
- Explicit Wait: Applied per element lookup (more control)

```java
// Good - Explicit wait
waitForElementToAppear(By.cssSelector(".product"));

// Avoid - Implicit wait only
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

### 2. **Selectorscomm**
- Prefer **CSS selectors** over XPath (faster)
- Make selectors specific to avoid false matches
- Avoid hardcoding index-based selectors (brittle)

```java
// Good
By productName = By.cssSelector(".product-name");

// Avoid
By firstProduct = By.xpath("//div[1]");
```

### 3. **Page Objects**
- One page object per page/component
- Method names should be business-readable
- Encapsulate UI details (locators private)
- Don't assert in page objects (assertion is test concern)

```java
// Good page object method
public CartPage addProductToCart(String productName) {
    // Find and interact with product
    // Return next page
}

// Bad - Assertion in page object
public void addProductToCart(String productName) {
    // ... add product ...
    Assert.assertTrue(...); // No!
}
```

### 4. **Test Independence**
- Each test should be independent
- Don't rely on test execution order
- Use `@BeforeMethod` to set up test data
- Use `@AfterMethod` for cleanup

### 5. **Naming Conventions**
- Test methods: `test` + `Action` + `Result` (e.g., `testAddProductToCart_Success`)
- Page objects: `<PageName>Page.java`
- Package names: lowercase

### 6. **Avoid Hard-coded Values**
- Use external configuration files (properties)
- Use page objects or base test data readers
- Make tests data-driven when possible

### 7. **Handle Exceptions**
- Catch specific exceptions, not generic `Exception`
- Log meaningful error messages
- Clean up resources in finally blocks

### 8. **Screenshot Strategy**
- Auto-capture on test failure (handled by Listeners)
- Manual capture for specific assertions if needed
- Organize screenshots with clear naming

---

## 📚 Dependencies

Key libraries used (see `pom.xml`):

| Library | Version | Purpose |
|---------|---------|---------|
| Selenium WebDriver | 4.x | Browser automation |
| TestNG | 7.x | Test framework |
| WebDriverManager | 5.x | Driver management |
| Jackson | 2.x | JSON processing |
| Apache Commons | 2.x | File utilities |
| Extent Reports | 5.x | Test reporting |

---

## 🔄 CI/CD Integration

### GitHub Actions Example

```yaml
name: Run Selenium Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: mvn clean test
      - uses: actions/upload-artifact@v2
        if: failure()
        with:
          name: test-reports
          path: reports/
```

---

## 📞 Support & Questions

For issues or questions:
1. Check this README
2. Review test code comments (JavaDoc)
3. Check exception messages and stack traces
4. Review Extent report screenshots
5. Debug using IDE breakpoints

---

## 📝 License

This project is provided as-is for educational purposes.

---

## ✅ Checklist for Running Tests Locally

- [ ] Java 11+ installed
- [ ] Maven 3.6+ installed
- [ ] Project downloaded/cloned
- [ ] Run `mvn clean install`
- [ ] Verify Chrome browser installed (for default test run)
- [ ] Update browser in `GlobalData.properties` if desired
- [ ] Run `mvn test`
- [ ] Check `reports/index.html` for results

---

**Last Updated**: May 2026  
**Framework Version**: 1.0  
**Java Version**: 11+  
**Selenium Version**: 4.x


