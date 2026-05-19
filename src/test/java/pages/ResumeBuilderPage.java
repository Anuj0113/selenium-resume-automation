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
 * Page Object for the Resume Builder screen.
 *
 * Each builder section (Personal Info, Summary, Experience, Education,
 * Projects, Skills, Templates, Accent Colors, ATS Score) has its own method.
 * Test classes compose these methods — no section logic is repeated anywhere.
 */
public class ResumeBuilderPage {

    private final WebDriver        driver;
    private final WebDriverWait    wait;
    private final JavascriptExecutor js;

    public ResumeBuilderPage(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) {
        this.driver = driver;
        this.wait   = wait;
        this.js     = js;
    }

    // ── Photo upload ──────────────────────────────────────────────────────────

    /**
     * Uploads a profile photo.  Skips gracefully if the file input is not found.
     *
     * @param photoPath Absolute path to the image file
     */
    public void uploadPhoto(String photoPath) throws InterruptedException {
        System.out.println("\n---------- IMAGE UPLOAD ----------");
        try {
            WebElement photoInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            js.executeScript("arguments[0].style.display='block';", photoInput);
            photoInput.sendKeys(photoPath);
            SeleniumUtils.pause(1000);
            System.out.println("✅ Image Uploaded");
        } catch (Exception e) {
            System.out.println("⚠️ Image upload skipped: " + e.getMessage());
        }
    }

    // ── Personal Info ─────────────────────────────────────────────────────────

    /**
     * Fills in Personal Info fields with values from {@link TestData} and saves.
     */
    public void fillPersonalInfo() throws InterruptedException {
        System.out.println("\n---------- PERSONAL INFO ----------");
        SeleniumUtils.clickTab(wait, js, "Personal");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(),'Personal Information')]")));
        System.out.println("✅ Personal Info section loaded");
        SeleniumUtils.debugAllInputs(driver, "PERSONAL INFO");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. John Smith']")),
                TestData.FULL_NAME);
        System.out.println("✅ Full Name typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. john@gmail.com']")),
                TestData.EMAIL_FIELD);
        System.out.println("✅ Email typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. +91 9876543210']")),
                TestData.PHONE);
        System.out.println("✅ Phone typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. Mumbai, India']")),
                TestData.LOCATION);
        System.out.println("✅ Location typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. Full Stack Developer']")),
                TestData.PROFESSION);
        System.out.println("✅ Profession typed");

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Summary ───────────────────────────────────────────────────────────────

    /**
     * Fills in the summary textarea, optionally AI-enhances it, and saves.
     *
     * @param aiEnhance  If true, clicks the "AI Enhance" button after typing
     */
    public void fillSummary(boolean aiEnhance) throws InterruptedException {
        System.out.println("\n---------- SUMMARY ----------");
        SeleniumUtils.clickTab(wait, js, "Summary");
        SeleniumUtils.debugAllInputs(driver, "SUMMARY");

        WebElement summary = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea")));
        SeleniumUtils.setReactTextarea(js, summary, TestData.SUMMARY_TEXT);
        System.out.println("✅ Summary typed");

        if (aiEnhance) {
            System.out.println("\n---------- AI ENHANCE SUMMARY ----------");
            SeleniumUtils.clickAIEnhanceAndWait(driver, wait, js, "AI Enhance");
        }

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Experience ────────────────────────────────────────────────────────────

    /**
     * Adds one experience entry with values from {@link TestData},
     * optionally AI-enhances the description, and saves.
     *
     * @param aiEnhance If true, clicks "Enhance with AI" after typing the description
     */
    public void fillExperience(boolean aiEnhance) throws InterruptedException {
        System.out.println("\n---------- EXPERIENCE ----------");
        SeleniumUtils.clickTab(wait, js, "Experience");
        SeleniumUtils.debugAllInputs(driver, "EXPERIENCE BEFORE ADD");

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'bg-green-600') and contains(.,'Add')]")));
        js.executeScript("arguments[0].click();", addBtn);
        SeleniumUtils.pause(1500);
        System.out.println("✅ Clicked Add Experience");
        SeleniumUtils.debugAllInputs(driver, "EXPERIENCE AFTER ADD");

        SeleniumUtils.setReactInput(js,
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='e.g. Google']"))),
                TestData.COMPANY);
        System.out.println("✅ Company typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. Software Engineer']")),
                TestData.JOB_TITLE);
        System.out.println("✅ Job Title typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("(//input[@type='month'])[1]")),
                TestData.EXP_START_DATE);
        System.out.println("✅ Start Date set");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("(//input[@type='month'])[2]")),
                TestData.EXP_END_DATE);
        System.out.println("✅ End Date set");

        SeleniumUtils.setReactTextarea(js,
                driver.findElement(By.xpath(
                        "//textarea[@placeholder='Describe your key responsibilities and achievements...']")),
                TestData.EXP_DESCRIPTION);
        System.out.println("✅ Experience Description typed");

        if (aiEnhance) {
            System.out.println("\n---------- AI ENHANCE EXPERIENCE JD ----------");
            SeleniumUtils.clickAIEnhanceAndWait(driver, wait, js, "Enhance with AI");
        }

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Education ─────────────────────────────────────────────────────────────

    /**
     * Adds one education entry with values from {@link TestData} and saves.
     */
    public void fillEducation() throws InterruptedException {
        System.out.println("\n---------- EDUCATION ----------");
        SeleniumUtils.clickTab(wait, js, "Education");
        SeleniumUtils.pause(1000);
        SeleniumUtils.debugAllInputs(driver, "EDUCATION BEFORE ADD");

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'bg-green-600') and contains(.,'Add')]")));
        js.executeScript("arguments[0].click();", addBtn);
        SeleniumUtils.pause(2000);
        System.out.println("✅ Clicked Add Education");

        js.executeScript("window.scrollBy(0, 400)");
        SeleniumUtils.pause(1000);
        SeleniumUtils.debugAllInputs(driver, "EDUCATION AFTER ADD");

        SeleniumUtils.setReactInput(js,
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='e.g. MIT, Stanford University']"))),
                TestData.INSTITUTE);
        System.out.println("✅ Institute typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. Bachelors']")),
                TestData.DEGREE);
        System.out.println("✅ Degree typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. Computer Science']")),
                TestData.FIELD_OF_STUDY);
        System.out.println("✅ Field of Study typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@type='month']")),
                TestData.GRADUATION_DATE);
        System.out.println("✅ Graduation Date set");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. 3.8/4.0']")),
                TestData.GPA);
        System.out.println("✅ GPA typed");

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Projects ──────────────────────────────────────────────────────────────

    /**
     * Adds one project entry with values from {@link TestData} and saves.
     */
    public void fillProjects() throws InterruptedException {
        System.out.println("\n---------- PROJECTS ----------");
        SeleniumUtils.clickTab(wait, js, "Projects");
        SeleniumUtils.pause(1000);
        SeleniumUtils.debugAllInputs(driver, "PROJECTS BEFORE ADD");

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'bg-green-600') and contains(.,'Add')]")));
        js.executeScript("arguments[0].click();", addBtn);
        SeleniumUtils.pause(1500);
        System.out.println("✅ Clicked Add Project");

        js.executeScript("window.scrollBy(0, 300)");
        SeleniumUtils.pause(500);
        SeleniumUtils.debugAllInputs(driver, "PROJECTS AFTER ADD");

        SeleniumUtils.setReactInput(js,
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='e.g. E-commerce App']"))),
                TestData.PROJECT_NAME);
        System.out.println("✅ Project Name typed");

        SeleniumUtils.setReactInput(js,
                driver.findElement(By.xpath("//input[@placeholder='e.g. React, Node.js']")),
                TestData.PROJECT_TECH_STACK);
        System.out.println("✅ Technologies typed");

        SeleniumUtils.setReactTextarea(js,
                driver.findElement(By.xpath(
                        "//textarea[@placeholder='Describe what you built and your role...']")),
                TestData.PROJECT_DESCRIPTION);
        System.out.println("✅ Project Description typed");

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Skills ────────────────────────────────────────────────────────────────

    /**
     * Adds all skills from {@link TestData#SKILLS_TO_ADD},
     * then deletes those in {@link TestData#SKILLS_TO_DELETE}, and saves.
     */
    public void fillSkills() throws InterruptedException {
        System.out.println("\n---------- SKILLS ----------");
        SeleniumUtils.clickTab(wait, js, "Skills");
        SeleniumUtils.debugAllInputs(driver, "SKILLS");

        WebElement skillInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter a skill (e.g., Javascript, Communication)']")));
        js.executeScript("arguments[0].scrollIntoView(true);", skillInput);
        SeleniumUtils.pause(800);

        for (String skill : TestData.SKILLS_TO_ADD) {
            SeleniumUtils.setReactInput(js, skillInput, skill);
            SeleniumUtils.pause(800);
            WebElement addSkillBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class,'bg-blue-600')]")));
            js.executeScript("arguments[0].click();", addSkillBtn);
            SeleniumUtils.pause(1000);
            System.out.println("✅ Skill added: " + skill);
        }

        System.out.println("\n---------- DELETING SKILLS ----------");
        for (String skillToDelete : TestData.SKILLS_TO_DELETE) {
            try {
                WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(),'" + skillToDelete + "')]//button")));
                js.executeScript("arguments[0].click();", deleteBtn);
                SeleniumUtils.pause(800);
                System.out.println("✅ Deleted skill: " + skillToDelete);
            } catch (Exception e) {
                System.out.println("⚠️ Skill not found to delete: " + skillToDelete);
            }
        }

        SeleniumUtils.saveResume(wait, js);
    }

    // ── Templates ─────────────────────────────────────────────────────────────

    /**
     * Opens the Template panel, applies each template from {@link TestData#TEMPLATES}.
     */
    public void applyTemplates() throws InterruptedException {
        System.out.println("\n---------- TEMPLATES ----------");
        WebElement templateTabDebug = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='Template']/ancestor::button")));
        js.executeScript("arguments[0].click();", templateTabDebug);
        SeleniumUtils.pause(1500);
        SeleniumUtils.debugAvailableTemplates(driver);
        js.executeScript("arguments[0].click();", templateTabDebug);
        SeleniumUtils.pause(800);

        for (String template : TestData.TEMPLATES) {
            WebElement templateTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='Template']/ancestor::button")));
            js.executeScript("arguments[0].click();", templateTab);
            SeleniumUtils.pause(1500);

            WebElement templateOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//h4[text()='" + template + "']")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", templateOption);
            SeleniumUtils.pause(800);

            wait.until(ExpectedConditions.elementToBeClickable(templateOption));
            js.executeScript("arguments[0].click();", templateOption);
            SeleniumUtils.pause(2000);
            System.out.println("✅ Template selected: " + template);
        }
    }

    // ── Accent Colors ─────────────────────────────────────────────────────────

    /**
     * Opens the Accent colour panel and applies each colour from
     * {@link TestData#ACCENT_COLORS}.
     */
    public void applyAccentColors() throws InterruptedException {
        System.out.println("\n---------- ACCENT COLORS ----------");
        WebElement accentTabDebug = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Accent')]")));
        js.executeScript("arguments[0].click();", accentTabDebug);
        SeleniumUtils.pause(1500);
        SeleniumUtils.debugAvailableAccentColors(driver);
        js.executeScript("arguments[0].click();", accentTabDebug);
        SeleniumUtils.pause(800);

        for (String colorName : TestData.ACCENT_COLORS) {
            WebElement accentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Accent')]")));
            js.executeScript("arguments[0].click();", accentBtn);
            SeleniumUtils.pause(800);

            WebElement colorDiv = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//p[text()='" + colorName + "']/parent::div")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", colorDiv);
            SeleniumUtils.pause(500);
            js.executeScript("arguments[0].click();", colorDiv);
            SeleniumUtils.pause(1500);
            System.out.println("✅ Accent color applied: " + colorName);
        }
    }

    // ── ATS Score ─────────────────────────────────────────────────────────────

    /**
     * Opens the ATS Score modal, waits for the score to load, then closes it.
     */
    public void checkATSScore() throws InterruptedException {
        System.out.println("\n---------- ATS SCORE ----------");
        WebElement atsBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'ATS Score')]")));
        js.executeScript("arguments[0].scrollIntoView(true);", atsBtn);
        SeleniumUtils.pause(500);
        js.executeScript("arguments[0].click();", atsBtn);
        System.out.println("✅ Clicked ATS Score button");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(.,'Analyzing')]")));
            System.out.println("✅ ATS Score loading...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//p[contains(.,'Analyzing')]")));
            System.out.println("✅ ATS Score loaded");
        } catch (Exception e) {
            System.out.println("⚠️ ATS loading indicator not detected, waiting 5s: " + e.getMessage());
            SeleniumUtils.pause(5000);
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(.,'Re-analyze')]")));
            System.out.println("✅ ATS Score displayed successfully");
        } catch (Exception e) {
            System.out.println("⚠️ Re-analyze button not found: " + e.getMessage());
        }

        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h2[contains(.,'ATS')]" +
                             "/ancestor::div[contains(@class,'flex items-center justify-between')]//button")));
            js.executeScript("arguments[0].click();", closeBtn);
            SeleniumUtils.pause(100);
            System.out.println("✅ Closed ATS Score modal");
        } catch (Exception e) {
            System.out.println("⚠️ Could not close ATS modal: " + e.getMessage());
        }
    }
}