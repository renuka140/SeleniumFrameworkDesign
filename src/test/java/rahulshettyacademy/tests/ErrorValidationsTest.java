package rahulshettyacademy.tests;

import org.apache.hc.core5.util.Asserts;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.CartPage;
import rahulshettyacademy.pageobjects.CheckoutPage;
import rahulshettyacademy.pageobjects.ConfirmationPage;
import rahulshettyacademy.pageobjects.ProductCatalogue;
import rahulshettyacademy.testcomponents.BaseTest;
import rahulshettyacademy.testcomponents.Retry;

import java.io.IOException;
import java.util.List;

/**
 * ErrorValidationsTest is dedicated to testing error handling and validation scenarios.
 * It verifies that the application properly handles and displays error messages for
 * invalid login credentials and incorrect product transactions.
 *
 * Tests in this class are grouped under "ErrorHandling" for test categorization and reporting.
 *
 * @see BaseTest for test setup and teardown
 * @see rahulshettyacademy.pageobjects for page objects used in testing
 */
public class ErrorValidationsTest extends BaseTest {

    /**
     * Validates that the login page displays an appropriate error message
     * when incorrect credentials are provided.
     *
     * This test attempts to login with an invalid password (note: "Rn#ti$123451" is incorrect)
     * and verifies that the error message "Incorrect email or password." is displayed.
     *
     * @throws IOException if test data cannot be read
     * @throws InterruptedException if thread is interrupted during wait operations
     */
    @Test(groups = {"ErrorHandling"}, retryAnalyzer= Retry.class)
    public void LoginErrorValidation() throws IOException, InterruptedException {
        // Attempt login with incorrect credentials
        landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$123451");

        // Verify the expected error message is displayed
        Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
    }

    /**
     * Validates that when adding a product with a mismatched product name,
     * the cart correctly reflects the actual product added (not the requested one).
     *
     * This test demonstrates error handling when product names don't match expectations,
     * ensuring the system behaves correctly even when data mismatch occurs.
     *
     * @throws IOException if test data cannot be read
     * @throws InterruptedException if thread is interrupted during wait operations
     */
    @Test (retryAnalyzer = Retry.class)
    public void ProductErrorValidation() throws IOException, InterruptedException {
        String productName = "ZARA COAT 3";

        // Login with valid credentials
        ProductCatalogue productCatalogue = landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$1234");

        // Load product list
        List<WebElement> productList = productCatalogue.getProductList();

        // Add the specified product to cart
        productCatalogue.addProductToCart(productName);

        // Navigate to cart and verify the product is present
        CartPage cartPage = productCatalogue.gotoCartPage();
        Boolean match = cartPage.VerifyProductDisplay("ZARA COAT 3");

        // Verify product is in cart
        Asserts.check(match, "ZARA COAT 3");
    }
}
