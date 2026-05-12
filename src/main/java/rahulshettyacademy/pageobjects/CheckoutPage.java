package rahulshettyacademy.pageobjects;

import rahulshettyacademy.AbstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage extends AbstractComponent {
    WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //page factory
    @FindBy(css = "[placeholder='Select Country']")
    WebElement country;
    @FindBy(css = ".action__submit")
    WebElement submit;
    @FindBy(css = ".ta-item:nth-of-type(2)")
    WebElement selectCountry;


    By results = By.cssSelector(".ta-results");

    public void selectCountry(String countryName)
    {
        Actions a = new Actions(driver);
        a.sendKeys(country, countryName).build().perform();
        waitForElementToAppear(results);
        selectCountry.click();

    }
    public ConfirmationPage submitOrder()
    {
        submit.click();
     return new ConfirmationPage(driver);
    }

}



