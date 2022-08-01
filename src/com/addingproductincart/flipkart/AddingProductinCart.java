package com.addingproductincart.flipkart;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AddingProductinCart {

	private static final String URL = "https://www.flipkart.com/";
	WebDriver driver;
	String actualText;
	String expectedText;

	@BeforeTest
	public void setup() {
		setupDriver();
		initilizeApp();
		Reporter.log("SetupDriver and InitilizeApplication was Successfully.");
	}
	
	@AfterTest
	public void tearDown() {
		driver.close();
		driver.quit();
		Reporter.log("Browser Closed Done.");
	}

	@Test
	public void searchProduct() throws AWTException {
		Actions act = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//div/a[@class='_1_3w1N']"));
		act.moveToElement(element).perform();
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ESCAPE);
		r.keyRelease(KeyEvent.VK_ESCAPE);

		WebElement userProduct = driver.findElement(By.name("q"));
		userProduct.clear();
		userProduct.sendKeys("boAt Rockerz wireless headset bluetooth");
		WebElement searchBtn = driver.findElement(By.className("L0Z3Pu"));
		LibraryUtils.waitForElementToBeVisible(driver, searchBtn, 5);
		searchBtn.click();
		Reporter.log("Search user product Successfully.");
	}
	
	@Test(dependsOnMethods = {"searchProduct"})
	public void chooseProduct() {
		
		//WebElement element1 = driver.findElements(By.cssSelector("._36fx1h > div > div:nth-of-type(2)")).get(2);
		WebElement element2 = driver.findElement(By.cssSelector("._36fx1h > div > div:nth-of-type(2) >div:nth-of-type(3) > div > div:nth-of-type(3)"));  
		LibraryUtils.waitForElementToBeVisible(driver, element2, 5);
		element2.click();
		Reporter.log("Select the Product in the featured list successfully.");
		
		Set<String> windowhandles = driver.getWindowHandles();
		List<String> list = new ArrayList<String>(windowhandles);
		driver.switchTo().window(list.get(1));
		Reporter.log("Successfully switched the tab.");
	}
	
	@Test(dependsOnMethods = {"chooseProduct"})
	public void addToCart() {
		WebElement e_link = driver.findElement(By.cssSelector(".B_NuCI"));
		LibraryUtils.waitForElementToBeVisible(driver, e_link, 10);
		expectedText = e_link.getText();
		
		WebElement addToCartBTN = driver.findElement(By.xpath("//button[text()='Add to Cart']"));
		addToCartBTN.click();
		Reporter.log("Product added and navigate to Cart.");
	}
	
	@Test(dependsOnMethods = {"addToCart"})
	public void viewAddedProduct() {
		WebElement addedProduct = driver.findElement(By.cssSelector("._2Kn22P"));
		addedProduct.click();
		WebElement a_link = driver.findElement(By.cssSelector(".B_NuCI"));
		actualText = a_link.getText();
		Reporter.log("Successfully viewing product in cart.");
	}
	
	@Test(dependsOnMethods = { "viewAddedProduct" })
	public void verifytheProduct() {
		boolean isEqual = actualText.equals(expectedText);
		Assert.assertTrue(isEqual);
//		System.out.println(actualText);
//		System.out.println(expectedText);
		Reporter.log("Searched and added Product are same.");
	}
	
	
	private void initilizeApp() {
		driver.manage().window().maximize();
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	private void setupDriver() {
		System.setProperty("webdriver.chrome.driver", "./lib/chromedriver.exe");
		this.driver = new ChromeDriver();
	}
	
}
