package com.gamil.ui.base;

import com.gamil.ui.factory.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base UI Test class managing Playwright browser lifecycle via Factory Pattern.
 */
public abstract class BaseUiTest {
    protected Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUpUi() {
        page = PlaywrightFactory.initBrowser();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownUi() {
        PlaywrightFactory.closeBrowser();
    }
}
