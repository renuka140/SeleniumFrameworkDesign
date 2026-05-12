package rahulshettyacademy.tests;

import org.apache.hc.core5.util.Asserts;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.CartPage;
import rahulshettyacademy.pageobjects.CheckoutPage;
import rahulshettyacademy.pageobjects.ConfirmationPage;
import rahulshettyacademy.pageobjects.ProductCatalogue;
import rahulshettyacademy.testcomponents.BaseTest;
import java.io.IOException;
import java.util.List;

public class ErrorValidationsTest extends BaseTest
{
    @Test(groups= {"ErrorHandling"})
    public void LoginErrorValidation() throws IOException, InterruptedException
    {
      landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$123451");
        Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
    }

    @Test
    public void ProductErrorValidation() throws IOException, InterruptedException
    {
        String productName = "ZARA COAT 3";
        ProductCatalogue productCatalogue=landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$1234");
        List<WebElement> productList =productCatalogue.getProductList();
        productCatalogue.addProductToCart(productName);
        CartPage cartPage=productCatalogue.gotoCartPage();
        Boolean match= cartPage.VerifyProductDisplay("ZARA COAT 3");
        Asserts.check(match,"ZARA COAT 3");

    }
}
