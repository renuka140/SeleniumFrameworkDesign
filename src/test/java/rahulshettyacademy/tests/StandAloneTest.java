package rahulshettyacademy.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.hc.core5.util.Asserts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import rahulshettyacademy.pageobjects.LandingPage;

import java.time.Duration;
import java.util.List;

/**
 * StandAloneTest demonstrates a standalone (non-TestNG) end-to-end test that can be
 * executed directly as a Java application (via main method).
 *
 * This test serves as a quick validation script without the Page Object Model pattern,
 * using direct WebDriver calls. It performs the full order flow:
 * - Launches browser and navigates to the application
 * - Logs in with credentials
 * - Searches for and adds a product to cart
 * - Completes checkout
 * - Verifies order confirmation
 * - Closes the browser
 *
 * Note: This is a legacy style test. For newer tests, use the Page Object Model approach
 * in SubmitOrderTest.java or ErrorValidationsTest.java instead, which are managed by TestNG.
 *
 * To run this test:
 * - Execute: java -cp target/classes:lib/* rahulshettyacademy.tests.StandAloneTest
 */
public class StandAloneTest {

    public static void main(String[] args) throws InterruptedException {

        String productName = "ZARA COAT 3";

        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rahulshettyacademy.com/client");
        driver.manage().window().maximize();
        LandingPage landingPage = new LandingPage(driver);

        // Login to application
        driver.findElement(By.id("userEmail")).sendKeys("renukreddy47@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Rn#ti$1234");
        driver.findElement(By.id("login")).click();

        // Wait for products to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));

        // Find and add product to cart
        List<WebElement> productList = driver.findElements(By.cssSelector(".mb-3"));
        WebElement prod = productList.stream()
                .filter(product -> product.findElement(By.cssSelector("b")).getText().equals("ZARA COAT 3"))
                .findFirst()
                .orElse(null);
        prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();

        // Wait for toast notification and spinner to disappear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));

        // Navigate to cart
        driver.findElement(By.cssSelector("[routerlink*='cart']")).click();
        List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));

        // Verify product in cart
        Boolean match = cartProducts.stream()
                .anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
        driver.findElement(By.cssSelector(".totalRow button")).click();

        // Complete checkout - select country
        Actions a = new Actions(driver);
        a.sendKeys(driver.findElement(By.cssSelector("[placeholder='Select Country']")), "India").build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));

        driver.findElement(By.cssSelector(".ta-item:nth-of-type(2)")).click();

        // Submit order
        driver.findElement(By.cssSelector(".action__submit")).click();

        // Verify confirmation message
        String confirmMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
        Asserts.check(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."), "Thankyou for the order. ");

        // Cleanup
        driver.close();

    }

}
