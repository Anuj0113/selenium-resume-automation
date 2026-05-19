package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Handles WebDriver initialisation and teardown.
 * Called by BaseTest's @BeforeMethod and @AfterMethod.
 * No changes needed here — same as before.
 */
public class DriverManager {

    private WebDriver          driver;
    private WebDriverWait      wait;
    private JavascriptExecutor js;

    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        js     = (JavascriptExecutor) driver;
        System.out.println("✅ Browser launched");
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed");
        }
    }

    public WebDriver           getDriver() { return driver; }
    public WebDriverWait       getWait()   { return wait;   }
    public JavascriptExecutor  getJs()     { return js;     }
}