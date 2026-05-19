package tests;

import base.BaseTest;
import data.TestData;
import org.testng.annotations.Test;

/**
 * Test: Full AI Resume Builder flow — edit title, fill all sections with AI,
 *       templates, accent colours, ATS Score, duplicate, delete both, logout.
 *
 * Flow:
 *   Login → Dashboard →
 *   Edit title of existing first card →
 *   Open Resume → Fill all sections (AI Enhance ON) →
 *   Apply Templates → Apply Accent Colors → ATS Score →
 *   Back to Dashboard → Duplicate Resume →
 *   Delete both (original + copy) → Logout
 */
public class AIResumeBuilderTest extends BaseTest {

    @Test(description = "Full AI builder flow with templates, ATS score, duplicate and cleanup")
    public void aiResumeBuilderTest() throws Exception {

        loginPage.login();
        dashboard.goToDashboard();

        // Rename the first card on the dashboard
        dashboard.editFirstResumeTitle(TestData.RESUME_TITLE_UPDATED);

        // Open the renamed resume in the builder
        dashboard.openResume(TestData.RESUME_TITLE_UPDATED);

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

        // Check ATS score and close modal
        builder.checkATSScore();

        // Back to dashboard → duplicate → delete both → logout
        dashboard.backToDashboard();
        dashboard.duplicateResume(TestData.RESUME_TITLE_UPDATED);

        dashboard.deleteResumes(
            TestData.RESUME_TITLE_UPDATED,
            "Copy of " + TestData.RESUME_TITLE_UPDATED
        );

        loginPage.logout();

        System.out.println("\n✅✅✅ AI RESUME BUILDER TEST COMPLETED SUCCESSFULLY! ✅✅✅");
    }
}