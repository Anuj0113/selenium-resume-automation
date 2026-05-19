package pages;

import base.SeleniumUtils;
import data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the Dashboard (resume listing page).
 *
 * Groups all card-level actions (create, edit title, open, duplicate, delete)
 * that were previously inlined and duplicated inside every test's main() method.
 */
public class DashboardPage {

    private final WebDriver        driver;
    private final WebDriverWait    wait;
    private final JavascriptExecutor js;

    public DashboardPage(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) {
        this.driver = driver;
        this.wait   = wait;
        this.js     = js;
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Navigates directly to the dashboard URL and waits for it to load.
     */
    public void goToDashboard() throws InterruptedException {
        driver.get(TestData.DASHBOARD_URL);
        SeleniumUtils.pause(3000);
        System.out.println("✅ Dashboard Loaded | URL: " + driver.getCurrentUrl());
    }

    // ── Create resume ─────────────────────────────────────────────────────────

    /**
     * Clicks "Create Resume", types a title in the modal, and submits.
     * Waits until the builder URL is reached before returning.
     *
     * @param title The resume title to enter
     */
    public void createResume(String title) throws InterruptedException {
        System.out.println("\n---------- CREATE RESUME ----------");
        WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Create Resume')]")));
        js.executeScript("arguments[0].click();", createBtn);
        SeleniumUtils.pause(2000);
        System.out.println("✅ Clicked Create Resume button");

        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='e.g. Software Engineer Resume']")));
        SeleniumUtils.pause(1000);
        SeleniumUtils.setReactInput(js, titleInput, title);
        SeleniumUtils.pause(1000);
        System.out.println("✅ Resume title entered: " + title);

        // Re-find to avoid stale element, then press Enter to submit
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='e.g. Software Engineer Resume']")))
            .sendKeys(Keys.RETURN);

        wait.until(ExpectedConditions.urlContains("builder"));
        SeleniumUtils.pause(3000);
        System.out.println("✅ Resume Created | URL: " + driver.getCurrentUrl());
    }

    // ── Edit title ────────────────────────────────────────────────────────────

    /**
     * Finds the first resume card, reveals its action buttons, clicks the
     * pencil/edit-title button, types a new title, and presses Enter.
     *
     * @param newTitle New title to set
     */
    public void editFirstResumeTitle(String newTitle) throws InterruptedException {
        System.out.println("\n---------- EDIT RESUME TITLE ----------");
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[contains(@class,'group') and contains(@class,'rounded-2xl')])[1]")));
        js.executeScript("arguments[0].scrollIntoView(true);", card);
        SeleniumUtils.pause(1000);

        SeleniumUtils.revealCardActions(js, card);
        SeleniumUtils.pause(800);

        // First SVG button = edit-title icon
        WebElement editTitleBtn = card.findElement(By.xpath(".//button[.//*[name()='svg']][1]"));
        js.executeScript("arguments[0].click();", editTitleBtn);
        SeleniumUtils.pause(1500);
        System.out.println("✅ Clicked Edit Title button");

        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter new title']")));
        SeleniumUtils.pause(500);
        SeleniumUtils.setReactInput(js, titleInput, newTitle);
        titleInput.sendKeys(Keys.RETURN);
        SeleniumUtils.pause(2000);
        System.out.println("✅ Title updated to: " + newTitle);
    }

    // ── Open resume ───────────────────────────────────────────────────────────

    /**
     * Finds a resume card by partial title text and clicks to open it in the builder.
     *
     * @param titleContains Partial text of the resume title to look for
     */
    public void openResume(String titleContains) throws InterruptedException {
        System.out.println("\n---------- OPENING RESUME ----------");
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'" + titleContains +
                         "')]/ancestor::div[contains(@class,'group')]")));
        js.executeScript("arguments[0].click();", card);
        wait.until(ExpectedConditions.urlContains("builder"));
        SeleniumUtils.pause(3000);
        System.out.println("✅ Opened Resume | URL: " + driver.getCurrentUrl());
    }

    // ── Duplicate resume ──────────────────────────────────────────────────────

    /**
     * Finds a resume card by partial title and clicks the duplicate (second SVG) button.
     *
     * @param titleContains Partial resume title
     */
    public void duplicateResume(String titleContains) throws InterruptedException {
        System.out.println("\n---------- DUPLICATE RESUME ----------");
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'" + titleContains +
                         "')]/ancestor::div[contains(@class,'group')]")));
        js.executeScript("arguments[0].scrollIntoView(true);", card);
        SeleniumUtils.pause(1000);

        SeleniumUtils.revealCardActions(js, card);
        SeleniumUtils.pause(800);

        WebElement dupBtn = card.findElement(By.xpath(".//button[.//*[name()='svg']][2]"));
        js.executeScript("arguments[0].click();", dupBtn);
        SeleniumUtils.pause(2000);
        System.out.println("✅ Duplicated resume: " + titleContains);
    }

    // ── Delete resume ─────────────────────────────────────────────────────────

    /**
     * Deletes a single resume card identified by partial title text.
     * Handles both browser alert and on-page confirm button.
     *
     * @param titleContains Partial resume title
     */
    public void deleteResume(String titleContains) throws InterruptedException {
        System.out.println("\n---------- DELETE RESUME: " + titleContains + " ----------");
        try {
            WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'" + titleContains +
                             "')]/ancestor::div[contains(@class,'group')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", card);
            SeleniumUtils.pause(800);

            SeleniumUtils.revealCardActions(js, card);
            SeleniumUtils.pause(500);

            // Last SVG button = delete icon
            WebElement delBtn = card.findElement(By.xpath(".//button[.//*[name()='svg']][last()]"));
            js.executeScript("arguments[0].click();", delBtn);
            SeleniumUtils.pause(1000);
            System.out.println("✅ Clicked Delete for: " + titleContains);

            SeleniumUtils.confirmDeletion(driver, wait, js);
            SeleniumUtils.pause(1000);
            System.out.println("✅ Deleted: " + titleContains);

        } catch (Exception e) {
            System.out.println("⚠️ Resume not found to delete: " + titleContains);
        }
    }

    /**
     * Convenience method — deletes multiple resumes in order.
     *
     * @param titles Array of partial resume titles to delete
     */
    public void deleteResumes(String... titles) throws InterruptedException {
        for (String title : titles) {
            deleteResume(title);
        }
    }

    // ── Back to dashboard ─────────────────────────────────────────────────────

    /**
     * Clicks the "Dashboard" link from inside the builder and waits to land back.
     */
    public void backToDashboard() throws InterruptedException {
        System.out.println("\n---------- BACK TO DASHBOARD ----------");
        WebElement backBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(.,'Dashboard')]")));
        js.executeScript("arguments[0].click();", backBtn);
        wait.until(ExpectedConditions.urlContains("/app"));
        SeleniumUtils.pause(2000);
        System.out.println("✅ Back to Dashboard | URL: " + driver.getCurrentUrl());
    }
}