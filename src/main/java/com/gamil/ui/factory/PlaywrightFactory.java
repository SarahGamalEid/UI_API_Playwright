package com.gamil.ui.factory;

import com.gamil.ui.config.ConfigReader;
import com.microsoft.playwright.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Design Pattern: Factory Pattern
 * Encapsulates the instantiation and configuration of Playwright, Browser, BrowserContext, and Page objects.
 */
public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    /**
     * Initializes the browser and returns a new Page instance.
     * Uses locally installed Google Chrome or specified browser.
     */
    public static Page initBrowser() {
        ConfigReader config = ConfigReader.getInstance();
        String browserType = config.getProperty("ui.browser", "chrome").toLowerCase();
        boolean headless = config.getBooleanProperty("ui.headless", true);
        double slowMo = config.getIntProperty("ui.slowMo", 0);

        // Configure Playwright to skip downloading remote browser binaries if using installed channels
        Map<String, String> env = new HashMap<>(System.getenv());
        env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
        Playwright.CreateOptions createOptions = new Playwright.CreateOptions().setEnv(env);

        Playwright playwright = Playwright.create(createOptions);
        tlPlaywright.set(playwright);

        Browser browser;
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo)
                .setArgs(Collections.singletonList("--start-maximized"));

        try {
            switch (browserType) {
                case "firefox":
                    browser = playwright.firefox().launch(launchOptions);
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(launchOptions);
                    break;
                case "msedge":
                    launchOptions.setChannel("msedge");
                    browser = playwright.chromium().launch(launchOptions);
                    break;
                case "chromium":
                    try {
                        browser = playwright.chromium().launch(launchOptions);
                    } catch (PlaywrightException pe) {
                        launchOptions.setChannel("chrome");
                        browser = playwright.chromium().launch(launchOptions);
                    }
                    break;
                case "chrome":
                default:
                    try {
                        launchOptions.setChannel("chrome");
                        browser = playwright.chromium().launch(launchOptions);
                    } catch (PlaywrightException pe) {
                        launchOptions = new BrowserType.LaunchOptions()
                                .setHeadless(headless)
                                .setSlowMo(slowMo);
                        browser = playwright.chromium().launch(launchOptions);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch browser [" + browserType + "]: " + e.getMessage(), e);
        }

        tlBrowser.set(browser);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080);
        
        BrowserContext context = browser.newContext(contextOptions);
        tlContext.set(context);

        Page page = context.newPage();
        int timeout = config.getIntProperty("ui.timeout", 30000);
        page.setDefaultTimeout(timeout);
        page.setDefaultNavigationTimeout(timeout);
        tlPage.set(page);

        return page;
    }

    /**
     * Clean up and close Playwright resources.
     */
    public static void closeBrowser() {
        try {
            if (getPage() != null) {
                getPage().close();
                tlPage.remove();
            }
            if (getContext() != null) {
                getContext().close();
                tlContext.remove();
            }
            if (getBrowser() != null) {
                getBrowser().close();
                tlBrowser.remove();
            }
        } finally {
            if (getPlaywright() != null) {
                getPlaywright().close();
                tlPlaywright.remove();
            }
        }
    }
}
