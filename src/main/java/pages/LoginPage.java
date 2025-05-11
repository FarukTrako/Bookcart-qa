package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    private final By navBarLoginButton = By.xpath("//mat-toolbar//button[.//span[contains(text(), 'Login')]]");
    private final By usernameInput = By.cssSelector("input[formcontrolname='username']");
    private final By passwordInput = By.cssSelector("input[formcontrolname='password']");
    private final By formLoginButton = By.xpath("//form//button[.//span[contains(text(), 'Login')]]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void login(String username, String password) {
        try {
            WebElement navLogin = wait.until(ExpectedConditions.elementToBeClickable(navBarLoginButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navLogin);
            navLogin.click();

            WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
            usernameElement.clear();
            usernameElement.sendKeys(username);
            usernameElement.sendKeys(Keys.TAB);

            WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
            passwordElement.clear();
            passwordElement.sendKeys(password);
            passwordElement.sendKeys(Keys.TAB);

            WebElement formLoginElement = wait.until(ExpectedConditions.presenceOfElementLocated(formLoginButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", formLoginElement);

            boolean clickSuccessful = false;
            try {
                new Actions(driver).moveToElement(formLoginElement).pause(Duration.ofMillis(300)).click().perform();
                clickSuccessful = true;
            } catch (Exception e) {
                System.out.println("Actions click failed");
            }

            if (!clickSuccessful) {
                passwordElement.sendKeys(Keys.ENTER);
                clickSuccessful = true;
            }

            if (!clickSuccessful) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", formLoginElement);
            }

            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform login: " + e.getMessage(), e);
        }
    }
}