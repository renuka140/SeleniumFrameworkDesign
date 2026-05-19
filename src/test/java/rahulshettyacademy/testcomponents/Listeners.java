package rahulshettyacademy.testcomponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import rahulshettyacademy.resources.ExtentReporterNG;

import java.io.IOException;

public class Listeners extends BaseTest implements ITestListener {
ExtentReports extent=ExtentReporterNG.getReportObject();
ThreadLocal<ExtentTest> extentTest=new ThreadLocal<ExtentTest>();

ExtentTest test;
    public void onTestStart(ITestResult result) {
           // ITestListener.super.onTestStart(result);
        test=extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    public void onTestSuccess(ITestResult result) {
       // ITestListener.super.onTestSuccess((ITestResult) result);
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    public void onTestFailure(ITestResult result) {
        // ITestListener.super.onTestFailure(result);
        extentTest.get().fail(result.getThrowable());
        try {
            driver=(WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String filePath=null;
        //screenshot
try
{
     filePath=getScreenshot(result.getMethod().getMethodName(),driver);

} catch (IOException e) {
e.printStackTrace();
}
extentTest.get().addScreenCaptureFromPath(filePath,result.getMethod().getMethodName());


           }
    public void onTestSkipped(ITestResult result) {
       // ITestListener.super.onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();
    }

}
