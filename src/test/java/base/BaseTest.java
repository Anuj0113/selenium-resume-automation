package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.DashboardPage;
import pages.LoginPage;
import pages.ResumeBuilderPage;

/**
 * BaseTest — parent class that every test class extends.
 *
 * WHY THIS EXISTS:
 * Every test needs the same setup (launch browser, create page objects)
 * and the same teardown (close browser). Without BaseTest, you would
 * copy-paste @BeforeMethod and @AfterMethod into all 4 test classes.
 * With BaseTest, each test just extends it and gets setup/teardown for free.
 *
 * HOW IT WORKS:
 * @BeforeMethod  → runs BEFORE each @Test method  → opens browser
 * @AfterMethod   → runs AFTER  each @Test method  → closes browser
 */
public class BaseTest {

    // These are protected so all child test classes can use them directly
    protected DriverManager       driverManager;
    protected LoginPage           loginPage;
    protected DashboardPage       dashboard;
    protected ResumeBuilderPage   builder;

    @BeforeMethod
    public void setUp() {
        // 1. Start the browser
        driverManager = new DriverManager();
        driverManager.setUp();

        // 2. Create all Page Objects — pass driver/wait/js into each
        loginPage = new LoginPage(
                driverManager.getDriver(),
                driverManager.getWait(),
                driverManager.getJs()
        );

        dashboard = new DashboardPage(
                driverManager.getDriver(),
                driverManager.getWait(),
                driverManager.getJs()
        );

        builder = new ResumeBuilderPage(
                driverManager.getDriver(),
                driverManager.getWait(),
                driverManager.getJs()
        );

        System.out.println("\n========== TEST STARTED ==========");
    }

    @AfterMethod
    public void tearDown() {
        // Always close the browser — even if the test failed
        driverManager.tearDown();
        System.out.println("========== TEST FINISHED ==========\n");
    }
}