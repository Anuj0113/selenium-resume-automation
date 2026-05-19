package tests;

import base.BaseTest;
import data.TestData;
import org.testng.annotations.Test;

/**
 * Test: Login, create resume with full builder flow including AI enhance,
 *       templates, accent colours, then delete resume and logout.
 *
 * Flow:
 *   Login → Dashboard → Create Resume →
 *   Fill all sections (AI Enhance ON for Summary & Experience) →
 *   Apply Templates → Apply Accent Colors →
 *   Back to Dashboard → Delete Resume → Logout
 */
public class LoginAndCreateTest extends BaseTest {

    @Test(description = "Full create resume flow with AI enhance, templates and cleanup")
    public void loginAndCreateTest() throws Exception {

        loginPage.login();
        dashboard.goToDashboard();

        // Create the resume
        dashboard.createResume(TestData.RESUME_TITLE);

        // Fill all sections — AI enhance ON for Summary and Experience
        builder.uploadPhoto(TestData.PHOTO_PATH);
        builder.fillPersonalInfo();
        builder.fillSummary(true);        // ← AI Enhance ON
        builder.fillExperience(true);     // ← AI Enhance ON
        builder.fillEducation();
        builder.fillProjects();
        builder.fillSkills();

        // Apply templates and accent colours
        builder.applyTemplates();
        builder.applyAccentColors();

        // Go back, clean up, log out
        dashboard.backToDashboard();
        dashboard.deleteResume(TestData.RESUME_TITLE);
        loginPage.logout();

        System.out.println("\n✅✅✅ LOGIN AND CREATE TEST COMPLETED SUCCESSFULLY! ✅✅✅");
    }
}