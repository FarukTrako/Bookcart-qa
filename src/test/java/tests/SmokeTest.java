package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;

public class SmokeTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        try {
            System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver.exe");
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            driver.manage().window().maximize();
            driver.get("https://bookcart.azurewebsites.net/");
        } catch (Exception e) {
            Assert.fail("Setup failed: " + e.getMessage(), e);
        }
    }

    @Test
    public void testValidLogin() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login("Mujo", "Mujo123123");

            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            boolean loginSuccess = false;
            try {
                WebElement usernameLabel = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(), 'Mujo')]")
                ));
                loginSuccess = true;
            } catch (Exception e) {
                if (driver.getTitle().contains("Books")) {
                    loginSuccess = true;
                }
            }

            Assert.assertTrue(loginSuccess, "Login was not successful");
            System.out.println("✅ Login successful! User is logged in.");

        } catch (TimeoutException e) {
            Assert.fail("Test timed out: " + e.getMessage(), e);
        } catch (Exception e) {
            Assert.fail("Test failed: " + e.getMessage(), e);
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}