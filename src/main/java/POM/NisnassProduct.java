package POM;

import static org.junit.Assert.fail;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class NisnassProduct {

	public CommonActions actions;

	public NisnassProduct(CommonActions actions) {
		this.actions = actions;

	}

	String AddToBag = "//button[@class = 'AddToBag']", SelectSizeLoactor = "//button[text()='",
			ColorLocator = "//img[@alt='", bagLocator = "//div[text()='BAG']",
			bagItems = "//a[@class='CartItem-pdpLink']";

	public void navigateToItem(String URL) {
		actions.navigateToPage(URL, By.xpath(AddToBag));

	}

	public void addToBag() {
		actions.clickOn(By.xpath(AddToBag));
	}

	public void chooseSize(String size) {
		String selectedSize;
		WebElement sizeLocator = actions.driver.findElement(By.xpath(SelectSizeLoactor + size.toUpperCase() + "']"));
		try {
			if (sizeLocator.getText().toUpperCase().contains(size.toUpperCase()))
				;
			{
				selectedSize = sizeLocator.getText();
				sizeLocator.click();
				assertTrue(selectedSize.equals(size));
			}

		} catch (Exception e) {
			Assert.fail("Couldn't select size cause of " + e.getMessage());

		}
	}

	public void chooseColor(String color) {
		color = ColorLocator + color + "']";
		actions.clickOn(By.xpath(color));

	}
	
	public void verifyBag(String[] goods) {
	    actions.clickOn(By.xpath(bagLocator));
	    List<WebElement> listItems = actions.driver.findElements(By.xpath(bagItems));
	    List <String> actualItems = new ArrayList<String>();
	    for(int i=0;i<listItems.size();i++)
	    {
	        actualItems.add(listItems.get(i).getAttribute("href"));
	    }
	    //List<String> goodsList = Arrays.asList(goods);
	    List<String> goodsList = new ArrayList(Arrays.asList(goods));
	    goodsList.removeAll(actualItems);
	    if(goodsList.size() == 0) {
	        Assert.assertTrue(true, "All goods from provided goods list Added to bag ");
	    }
	    else {
	        Assert.assertFalse(false, "Some items didnt math");
	    }
	}
}
/*
	public void verifyBag(String[] goods) {
		actions.clickOn(By.xpath(bagLocator));
		Arrays.sort(goods);
		List<WebElement> listItems = actions.driver.findElements(By.xpath(bagItems));
		// List <String> actualItems = new ArrayList<String>();
		List<String> actualItems = listItems.stream().map(listItem -> listItem.getAttribute("href"))
				.collect(Collectors.toList());
		IntStream.range(0, goods.length).forEach(i -> assertEquals(goods[goods.length - i - 1], actualItems.get(i)));
		assertEquals(listItems.size(), goods.length, "Assert Number of Items in the Bag");

	}
*/

/*
 * List <String> actualItems = new ArrayList<String>(); for(int
 * i=0;i<listItems.size();i++) {
 * actualItems.add(listItems.get(i).getAttribute("href")); } int j =
 * goods.length-1; for(int i=0;i<goods.length;i++) {
 * 
 * String actualItem = actualItems.get(i); String product = goods[j];
 * System.out.println(product);
 * //assertTrue(actualItems.get(i).contains(goods[j]));
 * assertTrue(actualItem.equals(product)); j--; }
 */

// assertEquals(listItems.size(), goods.length,"Assert Number of Items in the
// Bag");
// }
