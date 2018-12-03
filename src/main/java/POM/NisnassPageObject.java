package POM;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class NisnassPageObject {

	public CommonActions actions;

	String homePageLoadedLocator = "//div[@class = 'ProductNavigation']", AccountLocator = "//div[text()='ACCOUNT']",
			LoginPopUp = "//div[@class='CustomerPopup-guestActions']",
			SignUpBtnLocator = "//a[@class = 'CustomerPopup-signUpButton']",
			FirstNameLocator = "//input[@class = 'Profile-firstName']",
			LastNameLocator = "//input[@class = 'Profile-lastName']",
			EmailLocator = "//input[@class = 'Profile-email']",
			PasswordLocator = "//input[@class = 'Profile-password no-tracking']",
			CountryLocator = "//*[@class='Select-value']", //CountryDropDown = "//*[@id='react-select-3--list']",
			PhoneLocator = "//input[@class = 'Profile-phoneNumber']", PromoTickBox = "//input[@type='checkbox']",
			RegisterBtn = "//button[@class='Profile-signUpButton']",
			SignInBtn = "//button[@class = 'CustomerPopup-signInButton']",
			SignInEmail = "//input[@class = 'SignInForm-email']", SignInPassword = "//input[@type= 'password']",
			SubmitBtn = "//Button[@class= 'SignInForm-signInButton']",
			//CustomerAccount = "//div[@class ='Popup CustomerPopup']",
			CustomerAccount = "//div[contains(text(),'ACCOUNT')]",
			//CustomerAccount = "//a[@class ='Popup-iconLink']",
			CustomerLoggedin = "//div[@class='CustomerPopup-loggedIn']", MyAccount = "//a[contains(text(),'My Account')]",
			MyAccountPage = "//div[@class='MyAccountPage-title']", Edit = "Edit",
			MyProfileLocator = "//h3[@class = 'MyProfile-header']",
			MyAccountEmailLocator = "//input[@type ='email' and @class='Profile-email']",
			ProfileCountryDropDown = "//*[@id='react-select-3--list']",
			updateAccountCountry = "//*[@id='react-select-2--list']",
			updateProfileBtn = "//button[@class='Profile-updateDetailsButton']",

			presenceOfElement = "presenceOfElement";

	public NisnassPageObject(CommonActions actions) {
		this.actions = actions;

	}

	public void navigateToHomePage(String url) {
		actions.navigateToPage(url, By.xpath(homePageLoadedLocator));

	}

	public boolean createAccount(String FirstName, String LastName, String CountryCode, String PhoneNumber,
			String Email, String Password) throws InterruptedException {
		goToSignIn_SignUp(By.xpath(AccountLocator), By.xpath(LoginPopUp));
		actions.clickOn(By.xpath(SignUpBtnLocator));
		setName(FirstName, LastName);
		setEmail(Email);
		setPassword(Password);
		setCountryCode(CountryCode,ProfileCountryDropDown );
		setPhone(PhoneNumber);
		actions.clickOn(By.xpath(PromoTickBox));

		if (register())
			return true;
		else {
			actions.takeScreenShot("SignUp Error");
			return false;
		}
	}

	void goToSignIn_SignUp(By b, By b2) {
		actions.mouseOver(b,b2,presenceOfElement );
		//assertNotNull(actions.waitUntil(b2, presenceOfElement));
	}

	void setName(String UserFirstName, String UserLastName) {
		WebElement Fname, Lname;
		Fname = actions.driver.findElement(By.xpath(FirstNameLocator));
		Lname = actions.driver.findElement(By.xpath(LastNameLocator));
		Fname.sendKeys(UserFirstName);
		Lname.sendKeys(UserLastName);
		assertTrue(Fname.getAttribute("value").toString().contains(UserFirstName));
		assertTrue(Lname.getAttribute("value").toString().contains(UserLastName));
	}

	void setEmail(String email) {

		WebElement element;
		element = actions.driver.findElement(By.xpath(EmailLocator));
		element.sendKeys(email);
		assertTrue(element.getAttribute("value").toString().contains(email));
	}

	void setSignInEmail(String email) {
		WebElement element;
		element = actions.driver.findElement(By.xpath(SignInEmail));
		element.sendKeys(email);
		assertTrue(element.getAttribute("value").toString().contains(email));
	}

	void setSignInPassword(String pass) {
		WebElement Password;
		Password = actions.driver.findElement(By.xpath(SignInPassword));
		Password.sendKeys(pass);
		assertTrue(Password.getAttribute("value").toString().contains(pass));
	}

	void setPassword(String pass) {
		WebElement Password;
		Password = actions.driver.findElement(By.xpath(PasswordLocator));
		Password.sendKeys(pass);
		assertTrue(Password.getAttribute("value").toString().contains(pass));
	}

	void setCountryCode(String CountryCode, String dropDownLocator) {

		actions.clickOn(By.xpath(CountryLocator));
		assertNotNull(actions.waitUntil(By.xpath(dropDownLocator), presenceOfElement));
		WebElement dropdownValue = actions.driver
				.findElement(By.xpath("//div[contains(text(),'" + CountryCode + "')]"));
		dropdownValue.click();
	}

	void setPhone(String PhoneNumber) {
		WebElement Phone;
		Phone = actions.driver.findElement(By.xpath(PhoneLocator));
		Phone.clear();
		//Phone.sendKeys(Keys.chord(Keys.COMMAND, "a", Keys.DELETE));
		Phone.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT,Keys.END),PhoneNumber);
		
		assertTrue(Phone.getAttribute("value").toString().contains(PhoneNumber));

	}

	boolean register() throws InterruptedException {
		WebElement element;
		element = actions.driver.findElement(By.xpath(RegisterBtn));
		element.click();
		Thread.sleep(5000);
		//assertNotNull(actions.waitUntil(By.xpath(CustomerAccount), presenceOfElement));
		//actions.mouseOver(By.xpath(CustomerAccount));
		//if (actions.waitUntil(By.xpath(CustomerLoggedin), presenceOfElement) == null) {
		//	actions.takeScreenShot("SignIn Error" + String.valueOf((int) (Math.random() * 50 + 1)));
		//	return false;
		//}
		return true;
	}

	boolean login() throws InterruptedException {
		WebElement element;
		element = actions.driver.findElement(By.xpath(SubmitBtn));
		element.click();
		Thread.sleep(2000);
		if (actions.waitUntil(By.xpath(SubmitBtn), presenceOfElement) == null) {
			if (actions.waitUntil(By.xpath(SubmitBtn), presenceOfElement) != null) {
				return true;
			}
		}
		return false;
	}

	public boolean signIn(String email, String pass) throws InterruptedException {
		goToSignIn_SignUp(By.xpath(AccountLocator), By.xpath(LoginPopUp));
		actions.clickOn(By.xpath(SignInBtn));
		setSignInEmail(email);
		setSignInPassword(pass);
		login();
		actions.mouseOver(By.xpath(CustomerAccount), By.xpath(CustomerLoggedin),presenceOfElement);
		if (actions.waitUntil(By.xpath(CustomerLoggedin), presenceOfElement) == null) {
			actions.takeScreenShot("SignIn Error" + String.valueOf((int) (Math.random() * 50 + 1)));
			return false;
		}
		return true;
	}

	public void EditAndVerifyAccountSettings(String email, String PhoneNumber, String CountryCode) throws InterruptedException
	 {
		    
			goToAccountSettings();
			verifyEditabilityOfEmail(email);
			editPhone(CountryCode, PhoneNumber);
			actions.clickOn(By.xpath(updateProfileBtn));
			WebElement PhoneField = actions.driver.findElement(By.xpath(PhoneLocator));
			Thread.sleep(2000);
			
			String actualValue = (PhoneField.getAttribute("value") == null) ? PhoneField.getAttribute("innerHTML")
					: PhoneField.getAttribute("value");
			assertEquals(actualValue.contains(PhoneNumber),true);
			
		}

	private void editPhone(String countryCode, String phoneNumber) {
		setCountryCode(countryCode, updateAccountCountry);
		setPhone(phoneNumber);
	}

	private void verifyEditabilityOfEmail(String email) {
		WebElement element = actions.driver.findElement(By.xpath(MyAccountEmailLocator));
		try {
			element.sendKeys("");
			Assert.fail("The Email must be non-Editable");
		} catch (Exception e) {
			String actualValue = (element.getAttribute("value") == null) ? element.getAttribute("innerHTML")
					: element.getAttribute("value");
			assertEquals(actualValue.contains(email),true);

		}

	}

	void goToAccountSettings() {
		actions.mouseOver(By.xpath(CustomerAccount),By.xpath(CustomerLoggedin),presenceOfElement);
		actions.clickOn(By.xpath(MyAccount));
		assertNotNull(actions.waitUntil(By.xpath(MyAccountPage), presenceOfElement));
		actions.clickOn(By.linkText(Edit));
		assertNotNull(actions.waitUntil(By.xpath(MyProfileLocator), presenceOfElement));

	}
}
