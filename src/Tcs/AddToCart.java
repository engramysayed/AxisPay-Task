package Tcs;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AddToCart {
    private WebDriver driver;
    private Properties testData;
    private String filesPath;
    private WebDriverWait wait ;

    
    @BeforeTest
    public void start() throws IOException {

    	getTestData();
    	
        //set driver  
        System.setProperty("webdriver.chrome.driver", filesPath+"\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(testData.getProperty("base.url"));
        
        //set wait time to 10 s 
        wait= new WebDriverWait(driver, 10); 
        
        //accept cookies
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("privacy_pref_optin"))).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("consent_prompt_submit"))).click();

    }

    @Test(priority=1)
	public void LoginWithValidData(){
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
	public void testAddProductToCart() throws InterruptedException{
    	//go to shoes page
    	openShoesSection();
    	
    	//filter by price
    	filterByPrice();
    	
        //Verify ascending order
        boolean descendingArrow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[contains(.,'Set Descending Direction')])[1]"))).isDisplayed();
        Assert.assertEquals(descendingArrow, true,"Shoes are not sorted in ascending order.");
          
        
        //get product details
        shoesDetails(testData.getProperty("product.name"));
        
        Thread.sleep(1000);

        //set color and size
        setPreference(testData.getProperty("product.color"),testData.getProperty("product.size"));
       
        Thread.sleep(1000);
        
        //add to cart 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@title='Add to Cart'])[2]"))).click();

        //Verify success message
        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".success-msg"))).getText();
        Assert.assertEquals(successMessage, testData.getProperty("product.name")+" was added to your shopping cart.","Success message should match");
    }

    
    @AfterClass
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
    
    private void openShoesSection() {
    	//Hover Accessories
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Accessories']") ))).perform();

        //Click on Shoes
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Shoes']"))).click(); 
        
    }
    
    private void filterByPrice() throws InterruptedException {
    	Thread.sleep(1000);
    	
        //Scroll down by 500 pixels 
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500)");
    	
    	//Click on Sort By
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//select[@title='Sort By'])[1]"))).click();
       
        //Choose filter by Price
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//option[contains(.,'Price')])[1]"))).click();
    }
    
    private void shoesDetails(String productName) throws InterruptedException {
    	Thread.sleep(1000);
    	
        //Scroll down by 800 pixels 
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 800)");
    	
        //open product details page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[@title='"+productName+"'])[1]"))).click();        
    }
    
    private void setPreference(String color,String size) {
        //select desired product color
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(color.toLowerCase()))).click();      
        
        //select desired product size 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(size))).click();      

    }
    
}