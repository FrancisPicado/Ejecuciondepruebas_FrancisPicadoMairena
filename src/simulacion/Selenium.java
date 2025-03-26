package simulacion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.netty.handler.timeout.TimeoutException;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.NoSuchElementException;
public class Selenium {
	
	private WebDriver driver;
	
	@Before
	public void setup() {
	
		System.setProperty("Webdriver.chrome.drive","./src/Driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/v1/");
	}
	
	@Test
    public void testLoginSuccess() {
        WebElement txtUsername = driver.findElement(By.id("user-name"));
        WebElement txtPassword = driver.findElement(By.id("password"));
        WebElement btnLogin = driver.findElement(By.id("login-button"));

        assertNotNull("El campo de usuario no se encontró", txtUsername);
        assertNotNull("El campo de contraseña no se encontró", txtPassword);
        assertNotNull("El botón de login no se encontró", btnLogin);
        
        txtUsername.sendKeys("standard_user");
        txtPassword.sendKeys("secret_sauce");
        btnLogin.click();
        
        WebElement inventoryContainer = driver.findElement(By.id("inventory_container"));
        assertNotNull("No se encontró la página de inventario después del login", inventoryContainer);
        assertTrue("La página de inventario no está visible", inventoryContainer.isDisplayed());
    }


	@Test
	public void testAddItemToCart() {
	    testLoginSuccess(); 
	    
	    WebElement addToCartButton = driver.findElement(By.cssSelector("button.btn_primary.btn_inventory"));
	    
	    WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));

	    assertNotNull("El botón de agregar al carrito no se encontró", addToCartButton);
	    addToCartButton.click();  	    
	 
	    WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
	    assertNotNull("No se encontró el icono del carrito", cartBadge);
	    assertEquals("El carrito no muestra la cantidad correcta", "1", cartBadge.getText());
	    	  
	    cartIcon.click();
	    	    
	    WebElement cartItem = driver.findElement(By.className("cart_item"));
	    assertNotNull("No se encontró el producto en el carrito", cartItem);
	}
    
	@Test
	public void testLogout() throws InterruptedException {
	    testLoginSuccess();  // Asegúrate de que el usuario haya iniciado sesión antes de realizar logout
	    
	    Thread.sleep(5000);  // Espera para asegurarse de que la página se haya cargado correctamente

	    // Localizamos y tocamos el botón del menú
	    WebElement menuButton = driver.findElement(By.xpath("//div[@class='bm-burger-button']//button[text()='Open Menu']"));
	    assertNotNull("No se encontró el botón de menú", menuButton);
	    menuButton.click();

	    Thread.sleep(2000);  // Esperamos para asegurarnos de que el menú se haya desplegado

	    // Ahora localizamos y tocamos el enlace de Logout dentro del menú
	    WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
	    assertNotNull("No se encontró el enlace de Logout", logoutLink);
	    logoutLink.click();  // Hacemos clic en el enlace de Logout

	    // Verificamos que hemos sido redirigidos a la página de login
	    WebElement loginButton = driver.findElement(By.id("login-button"));
	    assertNotNull("No se redirigió correctamente a la página de login", loginButton);
	    assertTrue("El botón de login no está visible", loginButton.isDisplayed());
	}





    
    @Test
    public void testNavigationToAboutPage() throws InterruptedException {
        testLoginSuccess();  
        
        Thread.sleep(5000);  
        WebElement menuButton = driver.findElement(By.xpath("//div[@class='bm-burger-button']//button[text()='Open Menu']"));

        assertNotNull("No se encontró el botón de menú", menuButton);
        menuButton.click();

        Thread.sleep(2000); 

        WebElement aboutLink = driver.findElement(By.id("about_sidebar_link"));

        assertNotNull("No se encontró el enlace a About", aboutLink);
        aboutLink.click(); 

        assertTrue("La URL de la página About no es la esperada", driver.getCurrentUrl().contains("saucelabs.com"));
    }
    
    @Test
    public void testFilterProducts() {
        testLoginSuccess();
        WebElement filterDropdown = driver.findElement(By.className("product_sort_container"));
        assertNotNull("No se encontró el selector de filtro", filterDropdown);
        filterDropdown.click();
        
        WebElement filterOption = driver.findElement(By.cssSelector("option[value='lohi']"));
        assertNotNull("No se encontró la opción de filtro de menor a mayor precio", filterOption);
        filterOption.click();
        
        WebElement firstItem = driver.findElement(By.className("inventory_item_price"));
        assertNotNull("No se encontró el primer producto tras aplicar el filtro", firstItem);
    }
    
    @Test
    public void testRemoveItemFromCart() {
        // Asegúrate de que se haya agregado un producto al carrito previamente
        testAddItemToCart();

        // Esperamos 2 segundos antes de intentar eliminar el artículo
        try {
            Thread.sleep(2000);  // Espera de 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Localizamos y tocamos el botón REMOVE dentro del carrito
        WebElement removeButton = driver.findElement(By.cssSelector(".cart_item .btn_secondary.cart_button"));

        assertNotNull("No se encontró el botón de remover del carrito", removeButton);
        removeButton.click();

        // Esperamos 5 segundos para asegurarnos de que el carrito se actualice
        try {
            Thread.sleep(5000);  // Espera de 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ahora, localizamos y hacemos clic en "Continue Shopping"
        WebElement continueShoppingButton = driver.findElement(By.cssSelector("a.btn_secondary[href='./inventory.html']"));

        assertNotNull("No se encontró el botón 'Continue Shopping'", continueShoppingButton);
        continueShoppingButton.click();

        // Esperamos que la página de inventario se cargue
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Intentamos localizar el span del carrito con la clase "shopping_cart_badge"
            WebElement cartBadge = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shopping_cart_badge")));
            
            // Verificamos que el número de artículos sea 0
            assertEquals("El carrito debería estar vacío después de remover el producto", "0", cartBadge.getText());
        } catch (TimeoutException e) {
            // Si no se encuentra el badge, significa que el carrito está vacío
            try {
                WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
                
                // Verificamos que el icono del carrito no tenga cantidad, indicando que está vacío
                assertTrue("El carrito debería estar vacío, pero tiene artículos", cartIcon.getText().isEmpty() || cartIcon.getText().equals("0"));
            } catch (NoSuchElementException ex) {
                // Si tampoco se encuentra el icono, algo ha fallado
                fail("No se encontró el icono del carrito");
            }
        }
    }









    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
