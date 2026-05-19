package base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Reusable Selenium helper methods shared across ALL test files.
 *
 * Previously these methods were copy-pasted into every class
 * (setReactInput, setReactTextarea, pause, clickTab, saveResume,
 * clickAIEnhanceAndWait, debugAllInputs, debugAvailableTemplates,
 * debugAvailableAccentColors).  They now live here once.
 */
public class SeleniumUtils {

    // ── React-aware input setters ─────────────────────────────────────────────

    /**
     * Sets a value on a React-controlled <input> element and fires
     * the synthetic input/change events React needs to register the change.
     */
    public static void setReactInput(JavascriptExecutor js, WebElement element, String value) {
        js.executeScript(
            "const setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            element, value
        );
    }

    /**
     * Same as {@link #setReactInput} but for <textarea> elements.
     */
    public static void setReactTextarea(JavascriptExecutor js, WebElement element, String value) {
        js.executeScript(
            "const setter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            element, value
        );
    }

    // ── Timing helpers ────────────────────────────────────────────────────────

    /**
     * Simple pause in milliseconds.  Wrap Thread.sleep so callers
     * don't need to handle the checked exception themselves.
     */
    public static void pause(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    // ── Common UI actions ─────────────────────────────────────────────────────

    /**
     * Clicks a builder tab (Personal, Summary, Experience, Education, etc.)
     * and waits for the tab transition to complete.
     *
     * @param wait    WebDriverWait instance
     * @param js      JavascriptExecutor instance
     * @param tabName Visible text of the tab button (partial match is fine)
     */
    public static void clickTab(WebDriverWait wait, JavascriptExecutor js, String tabName)
            throws InterruptedException {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'rounded-xl') and contains(.,'" + tabName + "')]")));
        js.executeScript("arguments[0].click();", tab);
        Thread.sleep(1500);
        System.out.println("✅ Switched to tab: " + tabName);
    }

    /**
     * Clicks the green "Save" button and waits for the save to complete.
     */
    public static void saveResume(WebDriverWait wait, JavascriptExecutor js)
            throws InterruptedException {
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'bg-green-600') " +
                         "and contains(.,'Save') " +
                         "and not(contains(.,'Saving'))]")));
        js.executeScript("arguments[0].click();", saveBtn);
        Thread.sleep(2000);
        System.out.println("✅ Resume Saved");
    }

    /**
     * Clicks an AI-enhancement button and waits until the textarea content
     * changes (indicating the AI has responded) or a 30-second timeout expires.
     *
     * @param driver  WebDriver instance
     * @param wait    WebDriverWait instance
     * @param js      JavascriptExecutor instance
     * @param btnText Visible text on the AI enhance button (e.g. "AI Enhance",
     *                "Enhance with AI")
     */
    public static void clickAIEnhanceAndWait(WebDriver driver, WebDriverWait wait,
                                              JavascriptExecutor js, String btnText)
            throws InterruptedException {
        System.out.println("⏳ Clicking AI Enhance button: [" + btnText + "]");

        // Capture current textarea content so we can detect when it changes
        String beforeText = "";
        try {
            WebElement ta = driver.findElement(By.xpath("//textarea"));
            beforeText = ta.getAttribute("value");
            System.out.println("📝 Text before enhance: " +
                    beforeText.substring(0, Math.min(80, beforeText.length())) + "...");
        } catch (Exception e) {
            System.out.println("⚠️ Could not read textarea before enhance");
        }

        // Click the AI button
        WebElement enhanceBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'" + btnText + "')]")));
        js.executeScript("arguments[0].click();", enhanceBtn);
        System.out.println("✅ Clicked: " + btnText);

        pause(1500); // let the API call start

        // Poll until textarea content differs from what it was before
        System.out.println("⏳ Waiting for AI to enhance...");
        String finalBefore = beforeText;
        WebDriverWait aiWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            aiWait.until(d -> {
                try {
                    String current = d.findElement(By.xpath("//textarea")).getAttribute("value");
                    return current != null
                            && !current.trim().equals(finalBefore.trim())
                            && current.length() > 10;
                } catch (Exception e) {
                    return false;
                }
            });

            String afterText = driver.findElement(By.xpath("//textarea")).getAttribute("value");
            System.out.println("✅ AI enhancement complete!");
            System.out.println("📝 Enhanced text: " +
                    afterText.substring(0, Math.min(120, afterText.length())) + "...");
        } catch (Exception e) {
            System.out.println("⚠️ AI enhancement timeout or text did not change: " + e.getMessage());
        }

        pause(1000);
    }

    /**
     * Reveals the hidden action-button overlay on a resume card by removing
     * the opacity-0 class via JavaScript (hover is not reliable in automation).
     *
     * @param js   JavascriptExecutor instance
     * @param card The resume card WebElement
     */
    public static void revealCardActions(JavascriptExecutor js, WebElement card) {
        js.executeScript(
            "var btns = arguments[0].querySelector('div.opacity-0');" +
            "if(btns){ btns.classList.remove('opacity-0'); btns.classList.add('opacity-100'); }",
            card);
    }

    /**
     * Confirms a delete operation by first trying a browser alert,
     * then falling back to a confirm/delete button in the DOM.
     *
     * @param driver WebDriver instance
     * @param wait   WebDriverWait instance
     * @param js     JavascriptExecutor instance
     */
    public static void confirmDeletion(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) {
        try {
            driver.switchTo().alert().accept();
            System.out.println("✅ Confirmed deletion via alert");
        } catch (Exception e) {
            System.out.println("⚠️ No alert, trying confirm button");
            try {
                WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Confirm') " +
                                 "or contains(.,'Delete') " +
                                 "or contains(.,'Yes')]")));
                js.executeScript("arguments[0].click();", confirmBtn);
                System.out.println("✅ Confirmed via button");
            } catch (Exception e2) {
                System.out.println("⚠️ No confirm button found: " + e2.getMessage());
            }
        }
    }

    // ── Debug helpers ─────────────────────────────────────────────────────────

    /**
     * Prints a snapshot of all visible inputs, textareas, and buttons.
     * Useful when an XPath fails — run this to see what is actually on the page.
     */
    public static void debugAllInputs(WebDriver driver, String section) {
        System.out.println("\n=== DEBUG INPUTS: " + section + " ===");

        List<WebElement> inputs = driver.findElements(By.xpath("//input"));
        System.out.println("Total inputs: " + inputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            try {
                System.out.println(i + ": type=[" + inputs.get(i).getAttribute("type") +
                        "] placeholder=[" + inputs.get(i).getAttribute("placeholder") +
                        "] visible=" + inputs.get(i).isDisplayed());
            } catch (Exception ex) {
                System.out.println(i + ": STALE");
            }
        }

        List<WebElement> textareas = driver.findElements(By.xpath("//textarea"));
        System.out.println("Total textareas: " + textareas.size());
        for (int i = 0; i < textareas.size(); i++) {
            try {
                System.out.println("textarea " + i + ": placeholder=[" +
                        textareas.get(i).getAttribute("placeholder") +
                        "] visible=" + textareas.get(i).isDisplayed());
            } catch (Exception ex) {
                System.out.println("textarea " + i + ": STALE");
            }
        }

        List<WebElement> buttons = driver.findElements(By.xpath("//button"));
        System.out.println("Total buttons: " + buttons.size());
        for (WebElement b : buttons) {
            try {
                String txt = b.getText().replace("\n", " ").trim();
                if (!txt.isEmpty()) {
                    System.out.println("button: [" + txt + "] visible=" + b.isDisplayed() +
                            " class=" + b.getAttribute("class")
                                         .substring(0, Math.min(40, b.getAttribute("class").length())));
                }
            } catch (Exception ex) {
                System.out.println("button: STALE");
            }
        }

        System.out.println("=== END DEBUG: " + section + " ===\n");
    }

    /** Prints all visible h4 template names found on the page. */
    public static void debugAvailableTemplates(WebDriver driver) {
        System.out.println("\n=== DEBUG: AVAILABLE TEMPLATES ===");
        driver.findElements(By.xpath("//h4")).forEach(h4 -> {
            try {
                if (h4.isDisplayed()) System.out.println("  Template: [" + h4.getText() + "]");
            } catch (Exception ignored) {}
        });
        System.out.println("=== END DEBUG: AVAILABLE TEMPLATES ===\n");
    }

    /** Prints all visible accent-colour labels found on the page. */
    public static void debugAvailableAccentColors(WebDriver driver) {
        System.out.println("\n=== DEBUG: AVAILABLE ACCENT COLORS ===");
        driver.findElements(By.xpath("//p[ancestor::div[contains(@class,'grid')]]")).forEach(p -> {
            try {
                if (p.isDisplayed() && !p.getText().trim().isEmpty())
                    System.out.println("  Color: [" + p.getText().trim() + "]");
            } catch (Exception ignored) {}
        });
        System.out.println("=== END DEBUG: AVAILABLE ACCENT COLORS ===\n");
    }
}