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

public class SheinPageObject {
	public CommonActions actions ;
	
	public SheinPageObject(CommonActions actions){
		this.actions = actions;
	}
	int login_index = 0, signUp_Index=1,phone_index=2;
	String homePageLoadedLocator = ".first-cate-ctn" , //css Selector
		   coockiesAcceptBtnLocator = ".accept-btn",
		   userIconBtnLocator = "a[href*='auth/login'] > i" ,
		   signInLabelLocaor = "h4[innerText='SignIn']",
		   signUpEmailTextFieldLocator = "input[name='email']",
		   signUpPasswordTextFieldLocator = "input[name='password']",
		   signUpConfirmPasswordTextFieldLocator = "input[name='cfPassword']",
		   registerBtnLocator = ".col-xs-offset-1.col-xs-3 > div > button",
		   loginBtnLocator = ".j-login-con.col-xs-3.col-xs-offset-2 > div > button",
		   accountSettingsLocator = "a[href='https://www.shein.com/user/account_setting']",
		   accSettingsEmailLocator = "input[name='email']",
		   accSettingsPhoneLocator = ".c-input>input",
		   accSettingsDayLocator = "select[name='day']",
		   accSettingsMonthLocator = "select[name='month']",
		   accSettingsYearLocator = "select[name='year']",
		   accSettingsCountryLocator = "select[name='country']",
		   accSettingsUpdateBtn = "body > div.c-outermost-ctn > div.container-fluid-1200 > div > div.col-xs-9 > div > div.col-xs-8.col-xs-offset-1 > div:nth-child(8) > button" , 
		   accSettingsUdpateConfirmationBtn = "body > div.c-outermost-ctn > div.container-fluid-1200 > div > div.col-xs-9 > div > div:nth-child(3) > div.c-modal > div > div > div.modal-footer > div > button",
		   
		   presenceOfElement = "presenceOfElement" ,
		   elementToBeClickable = "elementToBeClickable";
	
	
	public void navigateToHomePage(String url) {
		actions.navigateToPage(url, By.cssSelector(homePageLoadedLocator));
		handleCookies();
	}
	
	public boolean createAccount(String email,String pass) {
		goToSignIn_SignUp();
		setEmail(signUp_Index,email);
		setPassword(signUp_Index,pass);
		setConfirmPassword(signUp_Index, pass);
		if(register(0))return true;
		else {
			actions.takeScreenShot("SignUp Error"+String.valueOf ((int)(Math.random() * 50 + 1)));
			return false;
		}
	}
	
	public boolean signIn(String email,String pass) {
		goToSignIn_SignUp();
		setEmail(login_index,email);
		setPassword(login_index,pass);
		login(login_index);
		if(actions.waitUntil(By.cssSelector(accountSettingsLocator), presenceOfElement)==null) {
			actions.takeScreenShot("SignIn Error"+String.valueOf((int)(Math.random() * 50 + 1)));
			return false ;
		}
		return true;
		
	}
	
	public void EditAndVerifyAccountSettings(String email,String phone,
			String date,String country) {
		String[] fullDate = date.split("-");
		goToAccountSettings();
		verifyEditabilityOfEmail(email);
		editDate(fullDate[0], fullDate[1], fullDate[2]);
		editCountry(country);	
		editPhone(phone_index,phone);
		updateDate();
	}
	
	void handleCookies() {
		try {
			if(actions.driver.findElement(By.cssSelector(coockiesAcceptBtnLocator)).isDisplayed() ) {
				actions.clickOn(By.cssSelector(coockiesAcceptBtnLocator));
		}}
		catch(Exception e) {}
	}
	
	void goToSignIn_SignUp() {
		actions.clickOn(By.cssSelector(userIconBtnLocator));
		assertNotNull(actions.waitUntil(By.cssSelector(signUpEmailTextFieldLocator),presenceOfElement));
	}
	
	void setEmail(int i,String email) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = actions.driver.findElements(By.cssSelector(signUpEmailTextFieldLocator));
		elements.get(i).sendKeys(email);
		assertTrue(elements.get(i).getAttribute("value").toString().contains(email));
	}
	
	void setPassword(int i,String pass) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = actions.driver.findElements(By.cssSelector(signUpPasswordTextFieldLocator));
		elements.get(i).sendKeys(pass);
		assertTrue(elements.get(i).getAttribute("value").toString().contains(pass));
	}
	
	void setConfirmPassword(int i,String pass) {
		actions.setText(By.cssSelector(signUpConfirmPasswordTextFieldLocator), pass);
	}
	
	boolean register(int i) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = actions.driver.findElements(By.cssSelector(registerBtnLocator));
		elements.get(i).click();
		if(actions.waitUntil(By.cssSelector(registerBtnLocator), presenceOfElement)==null) {
			return true;
		}
		else return false;
	}
	
	boolean login(int i) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = actions.driver.findElements(By.cssSelector(loginBtnLocator));
		elements.get(i).click();
		if(actions.waitUntil(By.cssSelector(loginBtnLocator), presenceOfElement)==null) {
			if(actions.waitUntil(By.cssSelector(accountSettingsLocator), presenceOfElement)!=null) {
				return true;
			}
		}
		return false;
	}
	
	void goToAccountSettings() {
		actions.clickOn(By.cssSelector(accountSettingsLocator));
		assertNotNull(actions.waitUntil(By.cssSelector(accSettingsEmailLocator), presenceOfElement));
	}
	
	void verifyEditabilityOfEmail(String email) {
		WebElement ele = actions.driver.findElement(By.cssSelector(accSettingsEmailLocator));
		try {
			ele.sendKeys("");
			Assert.fail("The Email must be non-Editable");
		}
		catch(Exception e) {
			String actualValue = ( ele.getAttribute("value")==null) ? ele.getAttribute("innerHTML") : ele.getAttribute("value");			
			assertEquals(actualValue,email);
		}
	}
	
	void editPhone(int i, String p) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = actions.driver.findElements(By.cssSelector(accSettingsPhoneLocator));
		elements.get(i).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		elements.get(i).sendKeys(p);
		assertTrue(elements.get(i).getAttribute("value").toString().contains(p));
	
	}
	
	void editDate(String day,String month,String year) {
		actions.driver.findElement(By.cssSelector(accSettingsDayLocator)).sendKeys(day);
		actions.driver.findElement(By.cssSelector(accSettingsMonthLocator)).sendKeys(month);
		actions.driver.findElement(By.cssSelector(accSettingsYearLocator)).sendKeys(year);
	}
	
	void editCountry(String country) {
		actions.driver.findElement(By.cssSelector(accSettingsCountryLocator)).sendKeys(country);
	}
	
	void updateDate() {
		actions.clickOn(By.cssSelector(accSettingsUpdateBtn));
		assertNotNull(actions.waitUntil(By.cssSelector(accSettingsUdpateConfirmationBtn), elementToBeClickable));
	}
}