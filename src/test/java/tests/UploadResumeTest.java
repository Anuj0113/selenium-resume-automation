package tests;

import base.BaseTest;
import base.SeleniumUtils;
import data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Test: Upload an existing PDF resume, fill all sections,
 *       apply templates & colours, check ATS, delete resume, logout.
 *
 * Flow:
 *   Login → Dashboard → Upload PDF →
 *   Fill all sections (AI Enhance ON) →
 *   Apply Templates → Apply Accent Colors → ATS Score →
 *   Back to Dashboard → Delete Resume → Logout
 */
public class UploadResumeTest extends BaseTest {

    @Test(description = "Upload PDF resume, fill all sections, apply templates and cleanup")
    public void uploadResumeTest() throws Exception {

        loginPage.login();
        dashboard.goToDashboard();

        // ── Upload PDF ─────────────────────────────────────────────────────────
        System.out.println("\n---------- UPLOAD PDF ----------");
        SeleniumUtils.debugAllInputs(driverManager.getDriver(), "DASHBOARD");

        WebElement uploadPdfBtn = driverManager.getWait().until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Upload PDF') " +
                                 "or contains(.,'Upload Resume') " +
                                 "or contains(.,'Import')]")));
        driverManager.getJs().executeScript("arguments[0].scrollIntoView(true);", uploadPdfBtn);
        SeleniumUtils.pause(1000);
        driverManager.getJs().executeScript("arguments[0].click();", uploadPdfBtn);
        System.out.println("✅ Clicked Upload PDF button");
        SeleniumUtils.pause(2000);

        // ── Resume title modal ─────────────────────────────────────────────────
        System.out.println("\n---------- RESUME TITLE MODAL ----------");
        SeleniumUtils.debugAllInputs(driverManager.getDriver(), "UPLOAD MODAL");

        WebElement resumeTitle = driverManager.getWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder=\"e.g. My Uploaded Resume\" " +
                                 "or @placeholder='Resume Title' " +
                                 "or @placeholder='e.g. Software Engineer Resume']")));
        SeleniumUtils.pause(1000);
        SeleniumUtils.setReactInput(driverManager.getJs(), resumeTitle, TestData.UPLOAD_RESUME_TITLE);
        SeleniumUtils.pause(500);
        System.out.println("✅ Resume title entered: " + TestData.UPLOAD_RESUME_TITLE);

        // ── File input ─────────────────────────────────────────────────────────
        System.out.println("\n---------- UPLOAD FILE ----------");
        WebElement fileInput = driverManager.getWait().until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@id='resume-input']")));
        driverManager.getJs().executeScript(
                "arguments[0].removeAttribute('hidden');" +
                "arguments[0].style.display='block';" +
                "arguments[0].style.visibility='visible';" +
                "arguments[0].style.opacity='1';",
                fileInput);
        SeleniumUtils.pause(500);
        fileInput.sendKeys(TestData.UPLOAD_PDF_PATH);
        System.out.println("✅ Resume PDF file selected");
        SeleniumUtils.pause(2000);

        String fileName = (String) driverManager.getJs().executeScript(
                "return arguments[0].files[0]?.name", fileInput);
        System.out.println("✅ File confirmed in input: " + fileName);

        // ── Parse the PDF ──────────────────────────────────────────────────────
        WebElement finalUploadBtn = driverManager.getWait().until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Upload & Parse Resume') and not(@disabled)]")));
        driverManager.getJs().executeScript("arguments[0].scrollIntoView(true);", finalUploadBtn);
        SeleniumUtils.pause(1000);
        driverManager.getJs().executeScript("arguments[0].click();", finalUploadBtn);
        System.out.println("✅ Clicked Upload & Parse Resume button");

        System.out.println("⏳ Waiting for AI parsing and redirect to builder...");
        WebDriverWait longWait = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(60));
        longWait.until(ExpectedConditions.urlContains("builder"));
        SeleniumUtils.pause(3000);
        System.out.println("✅ Resume Uploaded & Parsed | URL: " + driverManager.getDriver().getCurrentUrl());

        // ── Fill / verify sections ─────────────────────────────────────────────
        builder.uploadPhoto(TestData.PHOTO_PATH);
        builder.fillPersonalInfo();
        builder.fillSummary(true);        // ← AI Enhance ON
        builder.fillExperience(true);     // ← AI Enhance ON
        builder.fillEducation();
        builder.fillProjects();
        builder.fillSkills();

        // ── Templates, colours, ATS ────────────────────────────────────────────
        builder.applyTemplates();
        builder.applyAccentColors();
        builder.checkATSScore();

        // ── Clean up ───────────────────────────────────────────────────────────
        dashboard.backToDashboard();
        dashboard.deleteResume(TestData.UPLOAD_RESUME_TITLE);
        loginPage.logout();

        System.out.println("\n✅✅✅ UPLOAD RESUME TEST COMPLETED SUCCESSFULLY! ✅✅✅");
    }
}