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


public class Login {
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
	public void testLoginWithValidData(){
        //Navigate to login page
        navigateToLogin();

        
        //login with valid data
        login(testData.getProperty("valid.email"),testData.getProperty("valid.password") );
        
        String fullName=testData.getProperty("first.name")+" "+testData.getProperty("middle.name")+" "+testData.getProperty("last.name");
        
        // Verify success
        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='hello']"))).getText();
        Assert.assertEquals(successMessage, "Hello, "+fullName+"!","Success message should match");
        
    }

    
    @Test(priority=2)
	public void testLoginWithInValidData(){
        //Navigate to login page
    	navigateToLogin();
                
        //login with valid data
    	login(testData.getProperty("valid.email"), testData.getProperty("invalid.password")  );
        
        // Verify error
        String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(@class,'error-msg')]"))).getText();
        Assert.assertTrue(errorMessage.contains("Invalid login or password."),"Error message should display");
    }

    @AfterMethod
    public void closeDriver() {
            driver.quit();
    }

  
    private void navigateToLogin() {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(.,'Account')])[1]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Log In']"))).click();
        
    }

   private void getTestData() throws IOException {
	   filesPath = new File(".").getCanonicalPath();
   	   testData = new Properties();
       FileInputStream fis = new FileInputStream(filesPath+"\\resources\\test-data.properties");
       testData.load(fis);  
   }
    
    
    private void login(String Mail, String password) {
    	    	
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        driver.findElement(By.id("email")).sendKeys(Mail);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
        driver.findElement(By.id("pass")).sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("send2")));
        driver.findElement(By.id("send2")).click();
        
    }
}