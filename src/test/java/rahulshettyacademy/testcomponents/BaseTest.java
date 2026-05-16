package rahulshettyacademy.testcomponents;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import rahulshettyacademy.pageobjects.LandingPage;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class BaseTest {
    public WebDriver driver;
    public LandingPage landingPage;
    public WebDriver initializeDriver() throws IOException {
        Properties prop=new Properties();
        FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"//src//main//java//rahulshettyacademy//resources//GlobalData.properties");
        prop.load(fis);
        String browserName=prop.getProperty("browser");

        if(browserName.equalsIgnoreCase("chrome")) {
            //WebDriverManager.chromedriver().clearDriverCache().setup();
           WebDriverManager.chromedriver().setup();
           // WebDriverManager.chromedriver().driverVersion("124.0.6367.91").setup();
             driver = new ChromeDriver();
        }
        else if(browserName.equalsIgnoreCase("firefox"))
        {

        }
        else if(browserName.equalsIgnoreCase("edge"))
        {
            System.setProperty("webdriver.edge.driver","edge.exe");
             driver = new EdgeDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent =  FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

        //String to HashMap Jackson Binding

        ObjectMapper mapper=new ObjectMapper();
        List<HashMap<String,String>> data=mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
//            @Override
//            public Type getType() {
//                return super.getType();
//            }

        });
        return data;
    }

    public String getScreenshot(String testCaseName,WebDriver driver) throws IOException {
        TakesScreenshot ts=(TakesScreenshot) driver;
        File source=ts.getScreenshotAs(OutputType.FILE);
        File file=new File(System.getProperty("user.dir")+"//reports//"+testCaseName+".png");
        FileUtils.copyFile(source, file);
        return System.getProperty("user.dir")+"//reports//"+testCaseName+".png";
    }

    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication() throws IOException
    {
        driver = initializeDriver();
        landingPage = new LandingPage(driver);
        landingPage.goTo();
        return landingPage;
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
       driver.close();
    }
}
