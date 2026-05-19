package tests;

import base.BaseTest;
import data.TestData;
import org.testng.annotations.Test;

/**
 * Test: Create a brand-new resume from scratch.
 *
 * Flow:
 *   Login → Dashboard → Create Resume →
 *   Fill all sections (Personal, Summary, Experience, Education, Projects, Skills)
 *
 * extends BaseTest:
 *   - @BeforeMethod in BaseTest opens the browser before this test runs
 *   - @AfterMethod  in BaseTest closes the browser after this test finishes
 */
public class CreateResumeTest extends BaseTest {

    @Test(description = "Create a new resume and fill all sections")
    public void createResumeTest() throws Exception {

        loginPage.login();
        dashboard.goToDashboard();

        // Create a new resume — navigates straight to the builder
        dashboard.createResume(TestData.RESUME_TITLE);

        // Fill every section (AI enhance OFF for plain create flow)
        builder.uploadPhoto(TestData.PHOTO_PATH);
        builder.fillPersonalInfo();
        builder.fillSummary(false);
        builder.fillExperience(false);
        builder.fillEducation();
        builder.fillProjects();
        builder.fillSkills();

        System.out.println("\n✅✅✅ CREATE RESUME TEST COMPLETED SUCCESSFULLY! ✅✅✅");
    }
}