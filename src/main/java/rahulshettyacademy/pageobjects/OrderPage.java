package rahulshettyacademy.pageobjects;

import rahulshettyacademy.AbstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class OrderPage extends AbstractComponent {
    WebDriver driver;

    public OrderPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //page factory
    @FindBy(css = ".order-summary")
    List<WebElement> orderList;

    @FindBy(css = "tr td:nth-child(3)")
    private List<WebElement> productNames;

    public List<WebElement> getOrderList() {
        return orderList;
    }

    public Boolean verifyOrderDisplay(String productName) {
        Boolean match = productNames.stream().anyMatch(product -> product.getText().equalsIgnoreCase(productName));
        return match;
    }

}
