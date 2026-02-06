package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import reports.ExtentLogger;

public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    private By userName = By.id("user-name");
    private By password = By.id("password");
    private By loginButton = By.id("login-button");

    private void enterUserName(String username){
        driver.findElement(userName).sendKeys(username);
    }
    private void enterPassword(String password){
        driver.findElement(this.password).sendKeys(password);
    }
    private void clickLoginButton(){
        driver.findElement(loginButton).click();
    }
    public void getTitle(){
       System.out.println( driver.getTitle());
        ExtentLogger.pass("Title is: "+driver.getTitle());
    }

    public void loginToApplication(String username, String password){
        System.out.println("Login with: "+username+" and password: "+password);
        ExtentLogger.info("Login with: "+username+" and password: "+password);

    }


}
