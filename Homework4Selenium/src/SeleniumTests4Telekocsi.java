import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

@RunWith(JUnit4.class)
public class SeleniumTests4Telekocsi {
	private static WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver","d:\\programs\\geckodriver-master\\geckodriver.exe");
		
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8088/Homework4Telekocsi/user/self-manager.xhtml";
	}

	@Test
	public void testForras() throws Exception {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);

		driver.get(baseUrl);
		driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
		
		
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("papp.zoltan@integrity.hu");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("q");
		driver.findElement(By.id("submit")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		By exit = By.cssSelector(".icon-logout");
		driver.findElement(exit).click();
		if (!isElementPresent(By.cssSelector("body"))) {
			fail("body not present!");
		}
		
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	protected static WebElement findElement(By by) {
		return findElement(driver, by);
	}

	protected static WebElement findElement(SearchContext container, By by) {
		boolean wait = true;
		WebElement e = wait ? waitElement(container, by) : container.findElement(by);
		assertNotNull(e);
		return e;
	}

	protected static WebElement waitElement(SearchContext container, By by) {
		WebElement we = null;

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// no way
		}

		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			we = container.findElement(by);
			if (we != null) {
				break;
			}
		}
		assertNotNull(we);
		return we;
	}

	protected static void clickButtonByText(String text) {
		clickButtonByText(driver, text);
	}

	protected static void clickButtonByText(SearchContext container, String text) {
		WebElement element = findElement(container, By.xpath("//button[contains(., '" + text + "')]"));
		element.click();
	}

	protected static void clickLinkByText(String text) {
		clickLinkByText(driver, text);
	}

	protected static void clickLinkByText(SearchContext container, String text) {
		WebElement element = findElement(container, By.partialLinkText(text));
		element.click();
	}

	protected static void clickLinkByTitle(String string) {
		// http://stackoverflow.com/questions/6554067/xpath-1-0-closest-preceding-and-or-ancestor-node-with-an-attribute-in-a-xml-tree
		WebElement element = findElement(By.xpath("//ancestor::*[img[@title='English']]"));
		String tagname = element.getTagName();
		element.click();
		// element.sendKeys(Keys.RETURN);
		// JavascriptExecutor executor = (JavascriptExecutor) driver;
		// executor.executeScript("arguments[0].click();", element);
		// executor.executeScript("PrimeFaces.addSubmitParam('j_idt11',{'j_idt11:j_idt16':'j_idt11:j_idt16'}).submit('j_idt11');return
		// false;");
	}

	protected static void clickMenuById(String text) {
		clickMenuById(driver, text);
	}

	protected static void clickMenuById(SearchContext container, String id) {
		WebElement element = findElement(container, By.xpath("//*[@id=\"" + id + "\"]"));
		element.click();
	}

}
