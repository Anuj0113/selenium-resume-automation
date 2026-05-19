package data;

/**
 * Central place for all test data used across test scripts.
 * Change values here — no need to touch any test file.
 */
public class TestData {

    // ── App URLs ──────────────────────────────────────────────────────────────
    public static final String BASE_URL      = "https://ai-resume-builder-frontend-hqwn.onrender.com";
        public static final String DASHBOARD_URL = BASE_URL + "/app";

    // ── Login credentials ─────────────────────────────────────────────────────
    public static final String EMAIL    = "anuj.patel@project-kiwiqa.com";
    public static final String PASSWORD = "admin123";

    // ── Resume titles ─────────────────────────────────────────────────────────
    public static final String RESUME_TITLE         = "Anuj Automation Resume";
    public static final String RESUME_TITLE_UPDATED = "Anuj Automation Resume Updated";
    public static final String UPLOAD_RESUME_TITLE  = "Anuj QA Resume";

    // ── Personal info ─────────────────────────────────────────────────────────
    public static final String FULL_NAME   = "Anuj Patel";
    public static final String EMAIL_FIELD = "anuj@test.com";
    public static final String PHONE       = "9876543210";
    public static final String LOCATION    = "Ahmedabad";
    public static final String PROFESSION  = "QA Engineer";

    // ── Summary ───────────────────────────────────────────────────────────────
    public static final String SUMMARY_TEXT =
            "Experienced QA Engineer with expertise in Selenium automation and regression testing.";

    // ── Experience ────────────────────────────────────────────────────────────
    public static final String COMPANY        = "KiwiQA Technologies";
    public static final String JOB_TITLE      = "QA Engineer";
    public static final String EXP_START_DATE = "2023-06";
    public static final String EXP_END_DATE   = "2024-12";
    public static final String EXP_DESCRIPTION =
            "Led automation testing projects and built Selenium frameworks.";

    // ── Education ─────────────────────────────────────────────────────────────
    public static final String INSTITUTE        = "Gujarat Technological University";
    public static final String DEGREE           = "B.Tech";
    public static final String FIELD_OF_STUDY   = "Computer Engineering";
    public static final String GRADUATION_DATE  = "2022-05";
    public static final String GPA              = "3.9/4.0";

    // ── Projects ──────────────────────────────────────────────────────────────
    public static final String PROJECT_NAME        = "Selenium Automation Framework";
    public static final String PROJECT_TECH_STACK  = "Java, Selenium, TestNG";
    public static final String PROJECT_DESCRIPTION =
            "Built an end-to-end Selenium automation framework for resume builder application.";

    // ── Skills ────────────────────────────────────────────────────────────────
    public static final String[] SKILLS_TO_ADD    = {"Selenium Automation", "Java", "Automation Testing", "SQL"};
    public static final String[] SKILLS_TO_DELETE = {"Java", "SQL", "Automation Testing"};

    // ── Templates & Colors ────────────────────────────────────────────────────
    public static final String[] TEMPLATES     = {"Classic", "Modern", "Minimal Image", "Minimal"};
    public static final String[] ACCENT_COLORS = {"Blue", "Indigo", "Purple", "Green", "Red",
                                                   "Orange", "Teal", "Pink", "Gray", "Black"};

    // ── File paths ────────────────────────────────────────────────────────────
    public static final String PHOTO_PATH      = "E:/all docs/photo.jpg";
    public static final String UPLOAD_PDF_PATH = "C:\\Users\\Anuj Patel\\Downloads\\anuj resume.pdf";
}