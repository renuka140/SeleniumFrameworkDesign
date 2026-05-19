package rahulshettyacademy.tests;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.*;
import rahulshettyacademy.testcomponents.BaseTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * SubmitOrderTest contains end-to-end tests for placing an order and
 * verifying the order is visible in the order history (Order Page).
 *
 * Tests use the Page Object Model classes in the package
 * {@code rahulshettyacademy.pageobjects} and the browser setup in
 * {@code BaseTest}.
 */
public class SubmitOrderTest extends BaseTest {

    // Reusable product name used in the Order history verification test
    String productName = "ZARA COAT 3";

    /**
     * end-to-end test that performs login, adds a product to cart,
     * completes checkout and verifies the confirmation message.
     *
     * The test uses a DataProvider (getData) which supplies different
     * sets of credentials and product names from a JSON file.
     *
     * @param input map containing keys: email, password, product
     */
    @Test(dataProvider = "getData", groups = {"Purchase"})
    public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {

        // 1) Login to the application using credentials from test data
        ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));

        // 2) Load available products (wait until product elements are present)
        List<WebElement> productList = productCatalogue.getProductList();

        // 3) Add the requested product to cart
        productCatalogue.addProductToCart(input.get("product"));

        // 4) Navigate to Cart page and verify the product is present
        CartPage cartPage = productCatalogue.gotoCartPage();
        Boolean match = cartPage.VerifyProductDisplay(input.get("product"));
        Assert.assertTrue(match, "Expected product not present in cart: " + input.get("product"));

        // 5) Proceed to checkout, select country and submit the order
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();

        // 6) Verify the order confirmation message
        String message = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(message.equalsIgnoreCase("THANKYOU FOR THE ORDER."), "Confirmation message mismatch.");
    }

    /**
     * Verifies that an order placed earlier is visible in the Order History page.
     * This test depends on {@link #submitOrder(HashMap)} because it expects an
     * order to exist for the given user.
     */
    @Test(dependsOnMethods = {"submitOrder"})
    public void OrderHistoryTest() {
        // Login with known user credentials (could be moved to test data)
        ProductCatalogue productCatalogue = landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$1234");

        // Navigate to Order page and verify our product is listed in past orders
        OrderPage orderPage = productCatalogue.gotoOrderPage();
        Assert.assertTrue(orderPage.verifyOrderDisplay(productName), "Product not found in order history: " + productName);
    }

    /**
     * Utility method used by listeners to capture a screenshot when a test fails.
     *
     * @param testCaseName name to use for the screenshot file
     * @return absolute path to the saved screenshot
     * @throws IOException if file operations fail
     */
//    public String getScreenshot(String testCaseName) throws IOException {
//        TakesScreenshot ts = (TakesScreenshot) driver;
//        File source = ts.getScreenshotAs(OutputType.FILE);
//        File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
//        System.out.println("file path "+file);
//        FileUtils.copyFile(source, file);
//        return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
//    }

    /**
     * DataProvider that reads test data from PurchaseOrder.json and
     * returns two sets of input maps. The JSON reading helper is in
     * {@code BaseTest#getJsonDataToMap}.
     *
     * @return Object[][] where each element is a single-argument array
     * containing a HashMap<String,String> with keys: email, password, product
     * @throws IOException if reading the JSON file fails
     */
    @DataProvider
    public Object[][] getData() throws IOException {
        List<HashMap<String, String>> data = getJsonDataToMap(System.getProperty("user.dir") + "//src//test//java//rahulshettyacademy//data//PurchaseOrder.json");

        // Return first two datasets. If the JSON changes, update this accordingly.
        return new Object[][]{{data.get(0)}, {data.get(1)}};
    }

    // The commented-out alternate DataProviders below are retained for reference.
    // They show other ways to provide test data (hardcoded maps and simple arrays).

    // @DataProvider
    // public Object[][] getData() {
    //     HashMap<String,String> map=new HashMap<String,String>();
    //     map.put("email","renukreddy47@gmail.com");
    //     map.put("password","Rn#ti$1234");
    //     map.put("product","ZARA COAT 3");
    //
    //     HashMap<String,String> map1=new HashMap<String,String>();
    //     map1.put("email","renukreddy47@gmail.com");
    //     map1.put("password","Rn#ti$1234");
    //     map1.put("product","ADIDAS ORIGINAL");
    //
    //     return new Object[][] {{map}, {map1}};
    // }

    // @DataProvider
    // public Object[][] getData() {
    //     return new Object[][] {{"renukreddy47@gmail.com", "Rn#ti$1234","ZARA COAT 3"}, {"renukreddy47@gmail.com", "Rn#ti$1234","ADIDAS ORIGINAL"}};
    // }

}
