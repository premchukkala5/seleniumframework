package base;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {


    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    public static WebDriver getDriver(){
        return driver.get();
    }

    public static WebDriver initDriver(String browser, boolean isRemote,boolean isHeadless){

        MutableCapabilities capabilities;
        if(isRemote) {
            if (browser.toLowerCase().equalsIgnoreCase("chrome")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                if(isHeadless)
                chromeOptions.addArguments("--headless=new");
                capabilities=chromeOptions;
                //capabilities.setCapability("se:recordVideo", true);
            } else if (browser.toLowerCase().equalsIgnoreCase("firefox")) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if(isHeadless)
                firefoxOptions.addArguments("--headless");
                capabilities=firefoxOptions;
            } else if (browser.toLowerCase().equalsIgnoreCase("edge")) {
                EdgeOptions edgeOptions = new EdgeOptions();
                if(isHeadless)
                edgeOptions.addArguments("--headless=new");
                capabilities=edgeOptions;
            } else {
                throw new IllegalArgumentException("Browser not supported: " + browser);
            }
            try {
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
                driver.set(remoteWebDriver);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver.set(new ChromeDriver());
                    break;
                case "edge":
                    driver.set(new EdgeDriver());
                    break;
                default:
                    System.out.println("Browser not supported");
                    break;
            }
            getDriver().manage().window().maximize();
        }
        return getDriver();
    }

    public static void quitDriver(){
        if(getDriver() != null){
            getDriver().quit();
            driver.remove();
        }
    }

}
