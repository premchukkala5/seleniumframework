package base;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import reports.ExtentManager;
import reports.ExtentTestManager;
import utils.ConfigReader;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    @Parameters({"browser","grid","headless"})
    public void setup(String browser, String grid,String headless){
        ExtentTestManager.startTest("Starting test on " + browser);
        boolean isRemote = Boolean.parseBoolean(grid);
        boolean isHeadless = Boolean.parseBoolean(headless);
        driver = DriverManager.initDriver(browser,isRemote,isHeadless);
        driver.get(ConfigReader.getProperty("url"));
        ExtentTestManager.getTest().log(Status.PASS, "Navigated to " + ConfigReader.getProperty("url") + " on " + browser);
    }

    @AfterMethod
    public void tearDown(ITestResult result){
        if(result.getStatus()==ITestResult.FAILURE){
            try {
                String screenshotPath = utils.ScreenshotUtil.takeScreenshot(result.getName());
                reports.ExtentLogger.fail("Test failed: " + result.getThrowable().getMessage(), screenshotPath);
            } catch (Exception e) {
                reports.ExtentLogger.fail("Failed to capture screenshot: " + e.getMessage());
            }
        }
        DriverManager.quitDriver();
        ExtentTestManager.endTest();
    }

    @AfterSuite
    public void flushReport() {
        ExtentManager.getExtent().flush();
    }

}
