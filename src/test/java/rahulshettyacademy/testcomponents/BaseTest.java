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

/**
 * BaseTest is the base class for all TestNG test classes.
 *
 * It provides centralized setup and teardown functionality for browser initialization,
 * WebDriver configuration, and utility methods for common test operations. All test classes
 * should extend BaseTest to inherit:
 * - @BeforeMethod launchApplication() - Initializes WebDriver and navigates to landing page
 * - @AfterMethod tearDown() - Closes the browser after each test
 * - initializeDriver() - Creates and configures a WebDriver instance
 * - getJsonDataToMap() - Reads JSON test data files
 * - getScreenshot() - Captures screenshots for failed tests
 *
 * The browser choice is configured in:
 * {@code src/main/java/rahulshettyacademy/resources/GlobalData.properties}
 *
 * Supported browsers:
 * - chrome (default)
 * - firefox (not yet implemented)
 * - edge
 *
 * @see SubmitOrderTest for usage example
 * @see ErrorValidationsTest for usage example
 */
public class BaseTest {

    // WebDriver instance shared across test methods; initialized before each test
    public WebDriver driver;

    // Landing page object initialized before each test
    public LandingPage landingPage;

    /**
     * Initializes and configures a WebDriver instance based on browser selection.
     *
     * The browser type is read from GlobalData.properties file. This method:
     * - Loads browser configuration from properties file
     * - Sets up the appropriate WebDriver (Chrome, Edge, etc.)
     * - Configures implicit waits (10 seconds)
     * - Maximizes the browser window
     *
     * @return the configured WebDriver instance
     * @throws IOException if the properties file cannot be read
     */
    public WebDriver initializeDriver() throws IOException {
        // Load browser configuration from properties file
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "//src//main//java//rahulshettyacademy//resources//GlobalData.properties");
        prop.load(fis);
        String browserName = prop.getProperty("browser");

        // Initialize appropriate WebDriver based on browser type
        if (browserName.equalsIgnoreCase("chrome")) {
            // Automatically handles ChromeDriver setup
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            // TODO: Implement Firefox WebDriver setup
            // WebDriverManager.firefoxdriver().setup();
            // driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "edge.exe");
            driver = new EdgeDriver();
        }

        // Configure driver settings
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Parses a JSON file and converts it into a list of HashMap objects.
     *
     * This utility method is used for parameterized/data-driven testing.
     * It reads JSON test data and maps it to HashMap<String, String> for easy access.
     *
     * Example JSON structure:
     * [
     *   {"email": "user1@test.com", "password": "pass1", "product": "SHIRT"},
     *   {"email": "user2@test.com", "password": "pass2", "product": "PANTS"}
     * ]
     *
     * @param filePath the absolute path to the JSON file
     * @return a list of HashMap objects, each representing a test data set
     * @throws IOException if the file cannot be read
     */
    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        // Read JSON file content
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

        // Map JSON content to List<HashMap<String, String>> using Jackson
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(jsonContent,
                new TypeReference<List<HashMap<String, String>>>() {
                    // TypeReference helps Jackson understand the target collection type
                });
        return data;
    }

    /**
     * Captures a screenshot of the current browser window and saves it to the reports folder.
     *
     * This method is typically called by the Listeners class when a test fails
     * to capture visual evidence of the failure.
     *
     * @param testCaseName the name to use for the screenshot file
     * @param driver the WebDriver instance to capture from
     * @return the absolute path to the saved screenshot file
     * @throws IOException if screenshot cannot be saved
     */
    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        // Capture screenshot from browser
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Save to reports folder with test case name
        File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
        FileUtils.copyFile(source, file);

        return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
    }

    /**
     * TestNG hook that runs before each test method.
     *
     * This initializes the WebDriver and navigates to the application's landing page.
     * All test methods will have a ready-to-use driver and landingPage object.
     *
     * @return the initialized LandingPage object
     * @throws IOException if WebDriver initialization fails
     */
    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication() throws IOException {
        // Initialize WebDriver with configured browser
        driver = initializeDriver();

        // Create LandingPage object with driver instance
        landingPage = new LandingPage(driver);

        // Navigate to the application URL
        landingPage.goTo();

        return landingPage;
    }

    /**
     * TestNG hook that runs after each test method.
     *
     * This performs cleanup by closing the browser window, ensuring resources
     * are released and no browser instances are left open after tests complete.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // Close the browser window
        driver.close();
    }
}
