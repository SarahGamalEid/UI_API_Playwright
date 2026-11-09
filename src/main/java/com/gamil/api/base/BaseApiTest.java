package com.gamil.api.base;

import com.gamil.ui.config.ConfigReader;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

// Only starts and closes Playwright. Login and assertions are in the test classes.
public class BaseApiTest {
    private Playwright playwright;
    // protected lets test classes that extend this class use request.
    protected APIRequestContext request;

    @BeforeClass // TestNG runs this before the tests in each class.
    public void setUp() {
        Map<String, String> env = new HashMap<>(System.getenv());
        env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(env));

        // Use baseUrl from System Property, ConfigReader, or default to https://reqres.in/api
        String defaultApiUrl = ConfigReader.getInstance().getProperty("api.baseUrl", "https://reqres.in/api");
        String baseUrl = System.getProperty("baseUrl", defaultApiUrl);

        // A request context is the HTTP client. The timeout is 10 seconds.
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl).setTimeout(10000));
    }

    @AfterClass(alwaysRun = true) // Also attempt cleanup when a test fails.
    public void tearDown() {
        try {
            if (request != null) {
                request.dispose();
            }
        } finally { // Still close Playwright if disposing the context fails.
            if (playwright != null) {
                playwright.close();
            }
        }
    }
}
