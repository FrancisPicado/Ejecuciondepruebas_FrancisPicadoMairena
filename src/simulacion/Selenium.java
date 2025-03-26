package simulacion;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium {
	
	private WebDriver driver;
	
	@Before
	public void setup() {
	
		System.setProperty("Webdriver.chrome.drive","./src/Driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/v1/");
	}
	
	@Test
	public void test() {
		
		WebElement TxtUsername = driver.findElement(By.id("user-name"));
		WebElement TxtUpassword = driver.findElement(By.id("password"));
		WebElement BtnLogin = driver.findElement(By.id("login-button"));
		
		TxtUsername.sendKeys("standard_user");
		TxtUpassword.sendKeys("secret_sauce");
		BtnLogin.click();
	
	}
	
	
	

}
