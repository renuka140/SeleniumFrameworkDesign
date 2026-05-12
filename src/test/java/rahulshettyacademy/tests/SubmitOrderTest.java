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

public class SubmitOrderTest extends BaseTest
{
    String productName = "ZARA COAT 3";
    @Test(dataProvider = "getData",groups={"Purchase"})
    public void submitOrder(HashMap<String,String> input) throws IOException, InterruptedException
    {

        ProductCatalogue productCatalogue=landingPage.loginApplication(input.get("email"),input.get("password"));
        List<WebElement> productList =productCatalogue.getProductList();
        productCatalogue.addProductToCart(input.get("product"));
        CartPage cartPage=productCatalogue.gotoCartPage();
        Boolean match= cartPage.VerifyProductDisplay(input.get("product"));
        Assert.assertTrue(match,input.get("product"));
        CheckoutPage checkoutPage=cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        ConfirmationPage confirmationPage=checkoutPage.submitOrder();
        String message= confirmationPage.getConfirmationMessage();
        Assert.assertTrue(message.equalsIgnoreCase("THANKYOU FOR THE ORDER."), "Thankyou for the order. ");
    }
@Test(dependsOnMethods = {"submitOrder"})
 public void OrderHistoryTest()
 {
     ProductCatalogue productCatalogue=landingPage.loginApplication("renukreddy47@gmail.com", "Rn#ti$1234");
        OrderPage orderPage=productCatalogue.gotoOrderPage();
        Assert.assertTrue(orderPage.verifyOrderDisplay(productName));

 }

 public String getScreenshot(String testCaseName) throws IOException {
     TakesScreenshot ts=(TakesScreenshot) driver;
     File source=ts.getScreenshotAs(OutputType.FILE);
     File file=new File(System.getProperty("user.dir")+"//reports//"+testCaseName+".png");
     FileUtils.copyFile(source, file);
     return System.getProperty("user.dir")+"//reports//"+testCaseName+".png");
 }

    @DataProvider
    public Object[][] getData() throws IOException {
        List<HashMap<String,String>> data=getJsonDataToMap(System.getProperty("user.dir")+"//src//test//java//rahulshettyacademy//data//PurchaseOrder.json");

        return new Object[][] { {data.get(0)}, {data.get(1)} };
    }

//@DataProvider
// public Object[][] getData()
// {
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
//return new Object[][] {{map}, {map1}};
// }

//    @DataProvider
//    public Object[][] getData()
//    {
//        return new Object[][] {{"renukreddy47@gmail.com", "Rn#ti$1234","ZARA COAT 3"}, {"renukreddy47@gmail.com", "Rn#ti$1234","ADIDAS ORIGINAL"}};
//    }
}
