package Tcs;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class Registration {
    private WebDriver driver;
    private Properties testData;
    private String filesPath;
    private WebDriverWait wait ;

    
    @BeforeMethod
    public void start() throws IOException {

    	getTestData();
    	
        //set driver  
        System.setProperty("webdriver.chrome.driver", filesPath+"\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(testData.getProperty("base.url"));
        
        //set wait time to 10 s 
        wait= new WebDriverWait(driver, 10); 
        
        //accept cookies
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("privacy_pref_optin"))).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("consent_prompt_submit"))).click();

    }

    @Test(priority=1)
	public void testRegistrationWithValidData(){
        // Navigate to registration page
        navigateToRegistration();

        
        // Register with valid data
        register(
                testData.getProperty("first.name"),
                testData.getProperty("middle.name"),
                testData.getProperty("last.name"),
                testData.getProperty("valid.email"),
                testData.getProperty("valid.password")
        );
        
        // Verify success
        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".success-msg"))).getText();
        Assert.assertEquals(successMessage, "Thank you for registering with Tealium Ecommerce.","Success message should match");
    }

    
    @Test(priority=2)
	public void testRegistrationWithEmptyEmail(){
        // Navigate to registration page
        navigateToRegistration();
                
        // Register with invalid email
        register(
                testData.getProperty("first.name"),
                testData.getProperty("middle.name"),
                testData.getProperty("last.name"),
                testData.getProperty("invalid.email"),
                testData.getProperty("valid.password")
        );
        
        // Verify error
        String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("advice-required-entry-email_address"))).getText();
        Assert.assertTrue(errorMessage.contains("This is a required field."),"Required field error should display");
    }

    @AfterMethod
    public void closeDriver() {
            driver.quit();
    }

  
    private void navigateToRegistration() {

    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(.,'Account')])[1]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Register']"))).click();
        
    }

   private void getTestData() throws IOException {
	   filesPath = new File(".").getCanonicalPath();
   	   testData = new Properties();
       FileInputStream fis = new FileInputStream(filesPath+"\\resources\\test-data.properties");
       testData.load(fis);  
   }
    
    
    private void register(String firstName, String middleName, String lastName, String validMail, String password) {
    	    	
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
        driver.findElement(By.id("firstname")).sendKeys(firstName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("middlename")));
        driver.findElement(By.id("middlename")).sendKeys(middleName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastname")));
        driver.findElement(By.id("lastname")).sendKeys(lastName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        driver.findElement(By.name("email")).sendKeys(validMail);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        driver.findElement(By.id("password")).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmation")));
        driver.findElement(By.id("confirmation")).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[title='Register']")));
        driver.findElement(By.cssSelector("button[title='Register']")).click();
        
    }
}