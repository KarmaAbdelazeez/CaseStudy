package TestAutomationTasks;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.Properties;

import org.testng.annotations.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import POM.CommonActions;
import POM.NisnassPageObject;
import POM.NisnassProduct;

public class NisnassTest {

	public CommonActions actions = new CommonActions();
	public static Properties properties = new Properties();
	NisnassPageObject Nisnass;
	NisnassProduct Product;

	@BeforeTest
	public void suiteSetUp() {
		actions.erasePastTestData();
	}

	@BeforeClass
	@Parameters({ "browser" })
	public void setUp(String browser) throws Exception, MalformedURLException {
		System.out.println(browser);
		properties.load(new FileReader(new File("test.properties")));
		actions.initiateTheWebDriver(browser);
		Nisnass = new NisnassPageObject(actions);
		Product = new NisnassProduct(actions);
	}

	@AfterClass
	public void tearDown() throws Exception {
		actions.closeTheBrowser();
	}

	@Test(priority = 1, enabled = true)
	public void invalidSignUp() throws InterruptedException {
		String Email = properties.getProperty("invalidEmail"), FirstName = properties.getProperty("FirstName"),
				LastName = properties.getProperty("LastName"), CountryCode = properties.getProperty("CountryCode"),
				PhoneNumber = properties.getProperty("PhoneNumber"), Password = properties.getProperty("Password");

		Nisnass.navigateToHomePage(properties.getProperty("NisnassWebsite"));
		assertFalse(Nisnass.createAccount(FirstName, LastName, CountryCode, PhoneNumber, Email, Password),
				"SignUp Error");

	}

	@Test(priority = 2, enabled = true)
	public void invalidSignIn() throws InterruptedException {
		String Password = properties.getProperty("wrongPassword");
		String Email = properties.getProperty("invalidEmail");
		Nisnass.navigateToHomePage(properties.getProperty("NisnassWebsite"));
		assertFalse(Nisnass.signIn(Email, Password), "SignIn Error");

	}

	@Test(priority = 3, enabled = true)
	public void SignUpandEdit() throws InterruptedException {
		String FirstName = properties.getProperty("FirstName"), LastName = properties.getProperty("LastName"),
				CountryCode = properties.getProperty("CountryCode"),
				PhoneNumber = properties.getProperty("PhoneNumber"), Password = properties.getProperty("Password"),
				Email = properties.getProperty("email"), NewPhone = properties.getProperty("NewPhone"),
				NewCode = properties.getProperty("NewCode"), newClient = properties.getProperty("newClient");

		Nisnass.navigateToHomePage(properties.getProperty("NisnassWebsite"));
		if (newClient.equals("true")) {
			assertTrue(Nisnass.createAccount(FirstName, LastName, CountryCode, PhoneNumber, Email, Password),
					"Sign Up Error");
			properties.setProperty("newClient", "false");
		} else {
			assertTrue(Nisnass.signIn(Email, Password), "SignIn Error");
		}
		Nisnass.navigateToHomePage(properties.getProperty("NisnassWebsite"));
		Nisnass.EditAndVerifyAccountSettings(Email, NewPhone, NewCode);

	}

	@Test(priority = 4, enabled = true)
	public void AddToBag() {
		String[] items = { properties.getProperty("firstItemVariant"), properties.getProperty("secondItemVariant"),
				properties.getProperty("thirdItemVariant") };

		Product.navigateToItem(properties.getProperty("firstItemLink"));
		Product.addToBag();

		 Product.navigateToItem(properties.getProperty("secondItemLink"));
		 Product.chooseColor(properties.getProperty("secondItemColor"));
		 Product.chooseSize(properties.getProperty("secondItemSize"));
		 Product.addToBag();

		Product.navigateToItem(properties.getProperty("thirdItemLink"));
		Product.chooseSize(properties.getProperty("thirdItemSize"));
		Product.addToBag();
        Product.verifyBag(items);
	}

}
