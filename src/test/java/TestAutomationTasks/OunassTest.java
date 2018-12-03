package TestAutomationTasks;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import java.net.MalformedURLException;
import org.testng.annotations.*;

import POM.CommonActions;
import POM.OunassPageObject;

public class OunassTest {
	public CommonActions actions = new CommonActions();
	private static Properties properties = new Properties();
	OunassPageObject ounass;
	@BeforeClass
	@Parameters({"browser"})
	public void setUp(String browser) throws Exception,MalformedURLException {
		properties.load(new FileReader(new File("test.properties")));
		actions.initiateTheWebDriver(browser);
		ounass = new OunassPageObject(actions);
	}

	@AfterClass
	public void tearDown() throws Exception {
	actions.closeTheBrowser();
	}

	@Test(priority = 3, enabled = true)
	public void loginWithFacebook() throws InterruptedException {
		ounass.goToHomePage(properties.getProperty("OunassWebsite"));
		ounass.loginWithFacebook(properties.getProperty("OunassEmail"), properties.getProperty("OunassPass"));
		
	}
	
	@Test(priority = 1, enabled = true)
	public void VerifyProductsPageLoadMoreBtn() throws InterruptedException {
		int counter = 0;
		actions.driver.get(properties.getProperty("OunassListing"));
		ounass.handleCollectionsPopUp();
		counter = ounass.countProductsListed();
		ounass.loadMoreProducts();
	
	}
	
	@Test(priority = 2, enabled = true)
	public void VerifyAlanuiFilter() {
		actions.driver.get(properties.getProperty("OunassListing"));
		ounass.filterAlanui(Integer.parseInt(properties.getProperty("ExpectedNoOfResults")));
	}
	
	
	
	
	
	
	
	
	}
