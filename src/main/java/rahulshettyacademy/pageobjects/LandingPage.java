package rahulshettyacademy.pageobjects;

import rahulshettyacademy.AbstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage extends AbstractComponent {
    WebDriver driver;

    public LandingPage(WebDriver driver) {
        // with super we are sending driver from child class(Landing Page) to Parent class(Abstract Component page)
        super(driver);
        this.driver = driver;
        // we are catching driver from SubmitOrderTest class where initialization is done.
        PageFactory.initElements(driver, this);
    }

    //  WebElement userEmail = driver.findElement(By.id("userEmail"));
    //page factory
    @FindBy(id = "userEmail")
    WebElement userEmail;

    @FindBy(id = "userPassword")
    WebElement passWordEle;

    @FindBy(id = "login")
    WebElement login;

    @FindBy(css = "[class*='flyInOut']")
    WebElement errorMessage;

    public void goTo() {
        driver.get("https://rahulshettyacademy.com/client");
    }

    public String getErrorMessage()
    {
        waitForWebElementToAppear(errorMessage);
        return errorMessage.getText();
    }

    public ProductCatalogue loginApplication(String userName, String passWord) {
        userEmail.sendKeys(userName);
        passWordEle.sendKeys(passWord);
        login.submit();
        ProductCatalogue productCatalogue=new ProductCatalogue(driver);
        return productCatalogue;
    }

}
