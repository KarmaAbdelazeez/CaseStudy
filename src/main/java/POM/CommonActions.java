package POM;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.FileReader;

import java.util.Properties;
import java.io.IOException;
import bsh.classpath.BshClassPath.DirClassSource;
import io.github.bonigarcia.wdm.ChromeDriverManager;


public class CommonActions {

	
	public static ArrayList<WebDriver> allDrivers;
	public RemoteWebDriver driver;
	
	
	private static Properties properties = new Properties();

	public void initiateTheWebDriver(String browser) throws Exception,MalformedURLException {
		properties.load(new FileReader(new File("test.properties")));
		String useSeleniumGrid = properties.getProperty("useSeleniumGrid");
		if(useSeleniumGrid.contains("false")){
			ChromeDriverManager.getInstance().setup();
			driver = new ChromeDriver();
		}
		else if(useSeleniumGrid.contains("true")){
			DesiredCapabilities cap=new DesiredCapabilities();
			
			if(browser.contains("chrome")){
			cap = DesiredCapabilities.chrome();
			}
			else if(browser.contains("firefox")){
			cap = DesiredCapabilities.firefox();
			}
			else{
				Assert.fail("Wrong Browser");
			}
			cap.setPlatform(Platform.LINUX);
			cap.setVersion("");
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		}
	    else{
			Assert.fail("Wrong choice of use Selenium Grid Flag "+useSeleniumGrid);
		}	
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}

	public void takeScreenShot(String name) {
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String os = System.getProperty("os.name");
		String directoryName ="";
		if(os.toLowerCase().contains("mac os x ")) {
			directoryName="ScreenShots\\";
		}
		else {
			directoryName="ScreenShots/" ;
		}
		try {
		  FileUtils.copyFile(src, new File(directoryName+name+".png"));
		}
		catch (IOException e)
		 {
		  System.out.println(e.getMessage());
		  
		 }

	}
	public void setText(By b, String text) {
		waitUntil(b, "presenceOfElement");
		//System.out.println(element);
		try {
			driver.findElement(b).clear();
			driver.findElement(b).sendKeys(text);
			String actualValue = ( driver.findElement(b).getAttribute("value")==null) ? driver.findElement(b).getAttribute("innerHTML") : driver.findElement(b).getAttribute("value");
			
			assertEquals(actualValue,text );
		} catch (Exception e) {
			Assert.fail("Couldn't set text because of " + e.getMessage());
		}

	}

	public void clickOn(By b) {
		waitUntil(b, "elementToBeClickable");
		try {
			driver.findElement(b).click();
			Thread.sleep(1000);
		} catch (Exception e) {
			Assert.fail("Couldn't click because of" + e.getMessage());
		}
		
	}
	
	public void mouseOver(By b, By b2, String condition)
	{
		Actions interAction = new Actions(driver);
		WebElement element = driver.findElement(b);
        interAction.moveToElement(element).perform();
        assertNotNull(waitUntil(b2, condition));
	}

	public void navigateToPage(String url,By b) {
		driver.get(url);
		WebElement element = waitUntil(b, "presenceOfElement");
		assertNotNull(element, "Navigation Failed to this Website "+url);
	}
	
	
	public WebElement waitUntil(By b, String condition) {
		try {
			WebElement element = null;
			switch (condition) {

			case "presenceOfElement":
				element = (new WebDriverWait(driver,6)).until(ExpectedConditions.presenceOfElementLocated(b));
				return element;

			case "elementToBeClickable":
				element = (new WebDriverWait(driver, 6)).until(ExpectedConditions.elementToBeClickable(b));
				return element;
				

			default:
				element = null;
				Assert.fail("Wrong condition");
			}
		return element ;
		} catch (Exception e) {
			//Assert.fail("Couldn't find the element because of " + e.getMessage());
			return null;
		}
	}

	public void closeTheBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			Assert.fail("Couldn't close the browser because of " + e.getMessage());
		}
	}
	
	public void erasePastTestData() {
		File screenShot = new File("ScreenShots");
		File[] listFiles = screenShot.listFiles();
		for (File file : listFiles) {
			file.delete();
		}
	}
}
