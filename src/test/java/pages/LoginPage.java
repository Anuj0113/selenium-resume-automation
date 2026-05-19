package pages;

import base.SeleniumUtils;
import data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the Login / Logout flow.
 *
 * Both login and logout were copy-pasted verbatim into every test class.
 * Now they live here and each test simply calls:
 *
 *   LoginPage loginPage = new LoginPage(driver, wait, js);
 *   loginPage.login();
 *   ...
 *   loginPage.logout();
 */
public class LoginPage {

    private final WebDriver        driver;
    private final WebDriverWait    wait;
    private final JavascriptExecutor js;

    public LoginPage(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) {
        this.driver = driver;
        this.wait   = wait;
        this.js     = js;
    }

    /**
     * Navigates to the app root, performs a full login with credentials
     * from {@link TestData}, and waits until the dashboard URL is reached.
     */
    public void login() throws InterruptedException {
        System.out.println("---------- LOGIN ----------");
        driver.get(TestData.BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Login']"))).click();
        System.out.println("✅ Clicked Login button");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='email']")));
        emailField.sendKeys(TestData.EMAIL);
        System.out.println("✅ Email entered");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='password']")));
        passwordField.sendKeys(TestData.PASSWORD);
        System.out.println("✅ Password entered");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Login')]"))).click();
        System.out.println("✅ Clicked Login Submit");

        wait.until(ExpectedConditions.urlContains("app"));
        System.out.println("✅ Login Successful! URL: " + driver.getCurrentUrl());
    }

    /**
     * Opens the profile dropdown and clicks "Sign out".
     */
    public void logout() throws InterruptedException {
        System.out.println("\n---------- LOGOUT ----------");
        WebElement avatarBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//div[contains(@class,'rounded-full') " +
                         "and contains(@class,'bg-gradient-to-br')]]")));
        js.executeScript("arguments[0].click();", avatarBtn);
        System.out.println("✅ Opened profile dropdown");

        WebElement signOutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Sign out')]")));
        js.executeScript("arguments[0].click();", signOutBtn);
        SeleniumUtils.pause(200);
        System.out.println("✅ Logged out | URL: " + driver.getCurrentUrl());
    }
}