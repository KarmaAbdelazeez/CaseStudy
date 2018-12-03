package POM;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class OunassPageObject {
	CommonActions actions;

	private static Properties properties = new Properties();

	@FindAll({ @FindBy(css = ".login-button"), // userIcon
			@FindBy(css = "#onesignal-popover-cancel-button")// rejcetNewCollectionsPopUp
	})
	ArrayList<WebElement> homePageElements;
	// WebElement userIcon , rejectNewCollectionsPopUp ;

	String amberBtn = ".pull-left>a", facebookBtn = "#btnCreateAccountWithFacebook", userIcon = ".login-button",
			rejectNewCollectionsPopUp = "#onesignal-popover-cancel-button", facebookEmail = "#email",
			facebookPass = "#pass", facebookLoginBtn = "input[name='login']",
			facebookConfirmBtn = "input[name='__CONFIRM__']",
		
			facebookError = "//*[@id='errorData']", account = "a[href='customer']",
			toBeClickable = "elementToBeClickable", loadMoreBtn = "//a[@class='btn btn-success btn-block load-more']",
			//loadMoreBtnLoading = "//a[@class='btn btn-success btn-block load-more loading']",
			facebookWindowTitle = "facebook", parentWindowtitle = "Shop Luxury Clothing Online | Ounass UAE",
			productsList = "//div[@class = 'product-item-wrapper col-md-4 col-sm-4 col-6']",
			designersLink = "more designers ... >",
			designersLinkLocator ="//a[contains(text(),'more designers ... >')]",
			designerName = "//a[@data-filter-value='Alanui']",
			itemBrand = "//a[@data-brand='Alanui']";
	
	public int Counter = 72 ;
	
	public OunassPageObject(CommonActions actions) {
		this.actions = actions;
		try {
			OunassPageObject.properties.load(new FileReader(new File("test.properties")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loginWithFacebook(String username, String pass) throws InterruptedException {
		goToUsers();
		selectAmber();
		amberLoginWithFacebook(username, pass);
		verifyLogin();
	}

	public void goToHomePage(String url) {
		actions.navigateToPage(url, By.cssSelector(userIcon));
		handleCollectionsPopUp();
	}

	public void handleCollectionsPopUp() {
		if (actions.waitUntil(By.cssSelector(rejectNewCollectionsPopUp), toBeClickable) != null) {
			actions.clickOn(By.cssSelector(rejectNewCollectionsPopUp));
		}
	}

	public void goToUsers() {
		actions.clickOn(By.cssSelector(userIcon));
	}

	public void selectAmber() {
		actions.clickOn(By.cssSelector(amberBtn));
	}

	public void amberLoginWithFacebook(String username, String pass) throws InterruptedException {

		actions.driver.switchTo().defaultContent();
		actions.driver.switchTo().frame(0);
		actions.clickOn(By.cssSelector(facebookBtn));
		Thread.sleep(2000);
		Set<String> handler = actions.driver.getWindowHandles();
		Iterator<String> loop = handler.iterator();
		String parentWindow = loop.next();
		String childWindow = loop.next();
		actions.driver.switchTo().window(childWindow);
		try {
			if (actions.driver.getTitle().toLowerCase().equals(facebookWindowTitle)) {
				actions.setText(By.cssSelector(facebookEmail), username);
				actions.setText(By.cssSelector(facebookPass), pass);
				actions.clickOn(By.cssSelector(facebookLoginBtn));
			}
			Thread.sleep(2000);
			actions.driver.switchTo().window(parentWindow);
			if (actions.waitUntil(By.cssSelector(facebookError), "presenceOfElement") != null) {
				Assert.fail("This Account can't be connected to Amber via Facebook please contact the support");
			}

			else {
				Assert.fail("Couldn't Open Facebook Window");
			}
		} catch (Exception e) {

		}
	}

	public void verifyLogin() {
		goToHomePage(properties.getProperty("OunassWebsite") + "customer/login");
		if (actions.waitUntil(By.cssSelector(account), "presenceOfElement") == null) {
			Assert.fail("Login Failed");
		}
	}

	public void loadMoreProducts() {
	
		actions.waitUntil(By.xpath(loadMoreBtn), "presenceOfElement");
		actions.clickOn(By.xpath(loadMoreBtn));
		if (actions.waitUntil(By.xpath(loadMoreBtn), "presenceOfElement") == null) {
			Assert.fail("Loading Failed or no more items to load ");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int countProductsListed() throws InterruptedException
	{
		List<WebElement> list ;
		JavascriptExecutor js = (JavascriptExecutor)actions.driver;
		int itemsCount = 0;
		WebElement LoadMoreSection = actions.driver.findElement(By.xpath("//div[@class='row listing-meta pagination-wrapper m-b-3']"));
		
		do {
		js.executeScript("arguments[0].scrollIntoView(true);",LoadMoreSection);
		list = actions.driver.findElements(By.xpath(productsList));
		itemsCount = list.size();
		}
		while (itemsCount < Counter );
		js.executeScript("scroll(0, -10);");
		
		

		return itemsCount;
		
	}

	
	
	public void filterAlanui(int results) {
		int counter = 0;
		//JavascriptExecutor js = (JavascriptExecutor)actions.driver;
		//js.executeScript("arguments[0].scrollIntoView(true);",actions.driver.findElement(By.partialLinkText(designersLink)));
		actions.waitUntil(By.xpath(designersLinkLocator), "presenceOfElement");
		//actions.mouseOver(By.xpath(designersLinkLocator));
		actions.clickOn((By.xpath(designersLinkLocator)));
		actions.clickOn((By.xpath(designerName)));
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<WebElement> itemsList;
		itemsList = actions.driver.findElements(By.xpath(itemBrand));
		counter = itemsList.size();
		String designer = actions.driver.findElement(By.xpath(designerName)).getText();
		assertEquals(actions.driver.findElement(By.xpath(designerName)).getText(),"Alanui");
		assertEquals(counter,results,"Number of Products is wrong");
		
		
	}
	
	

	

}
