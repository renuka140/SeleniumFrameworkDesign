package rahulshettyacademy.AbstractComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import rahulshettyacademy.pageobjects.CartPage;
import rahulshettyacademy.pageobjects.OrderPage;

import java.time.Duration;

/**
 * AbstractComponent is the base class for all page object classes.
 *
 * It provides shared functionality and common locators that appear across multiple pages,
 * such as navigation headers. All page objects inherit from this class to reuse:
 * - Common locator definitions (@FindBy annotations)
 * - Wait utilities for handling dynamic content
 * - Navigation methods between pages
 *
 * This follows the Page Object Model (POM) design pattern where:
 * - Each page has its own class
 * - Common elements are extracted to a base class
 * - Methods encapsulate user interactions
 *
 * @see CartPage for cart page implementation
 * @see OrderPage for order page implementation
 */
public class AbstractComponent {

    WebDriver driver;

    /**
     * Initializes the AbstractComponent with the provided WebDriver.
     * PageFactory is initialized to support the @FindBy annotations.
     *
     * @param driver the WebDriver instance to be used for browser interactions
     */
    public AbstractComponent(WebDriver driver) {
        // Store driver instance from child class (e.g., LandingPage)
        this.driver = driver;
        // Initialize PageFactory - enables @FindBy annotations to work
        PageFactory.initElements(driver, this);
    }

    // Common locators shared across multiple pages

    /** Locator for Cart navigation link/button in the header */
    @FindBy(css = "[routerlink*='cart']")
    WebElement cartHeader;

    /** Locator for Order/My Orders navigation link/button in the header */
    @FindBy(css = "[routerlink*='myorders']")
    WebElement orderHeader;

    /**
     * Waits for an element located by the given By selector to become visible.
     * Uses a WebDriverWait with 5-second timeout.
     *
     * @param findBy the locator strategy (e.g., By.cssSelector, By.id)
     */
    public void waitForElementToAppear(By findBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }

    /**
     * Waits for a specific WebElement to become visible.
     * Uses a WebDriverWait with 5-second timeout.
     *
     * @param element the WebElement to wait for
     */
    public void waitForWebElementToAppear(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Navigates to the Cart page by clicking the cart header link.
     *
     * @return a new CartPage object representing the cart page
     */
    public CartPage gotoCartPage() {
        cartHeader.click();
        CartPage cartPage = new CartPage(driver);
        return cartPage;
    }

    /**
     * Navigates to the Order/Order History page by clicking the order header link.
     *
     * @return a new OrderPage object representing the order history page
     */
    public OrderPage gotoOrderPage() {
        orderHeader.click();
        OrderPage orderPage = new OrderPage(driver);
        return orderPage;
    }

    /**
     * Waits for an element to disappear/become invisible.
     * Currently implemented with a simple Thread.sleep(1000) as a workaround.
     *
     * TODO: Replace with proper WebDriverWait using ExpectedConditions.invisibilityOf()
     * for more robust and dynamic waiting.
     *
     * @param ele the WebElement to wait for disappearance
     * @throws InterruptedException if the thread is interrupted during sleep
     */
    public void waitForElementToDisappear(WebElement ele) throws InterruptedException {
        Thread.sleep(1000);
        // Commented approach below is more robust and should be preferred:
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // wait.until(ExpectedConditions.invisibilityOf(ele));
    }
}
