package ro.fortsoft.pippo.demo.integration;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static ro.fortsoft.pippo.demo.integration.util.UrlUtil.appendSlashOnRightSide;

/**
 * @author Serban Balamaci
 */
public abstract class BaseUIWebdriverTest {

    private Browser browser;

    protected static String serverUrl;
    protected static String appContext;

    @BeforeClass
    public static void init() throws Exception {
        serverUrl = "http://" + System.getProperty("container.host");
        appContext = System.getProperty("webapp.deploy.context");
    }

    public abstract Browser initBrowser();

    @Before
    public void before() {
        browser = initBrowser();
    }

    @After
    public void after() {
        browser.shutDown();
    }

    protected WebDriver getDriver() {
        return browser.getDriver();
    }

    private void takeScreenshot(String pageName) throws Exception {
        String screenshotFilename = pageName;
        Path originalScreenshot = screenshotPath.resolve(screenshotFilename + ".png");
        Path screen = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE).toPath();
    }


    @AfterClass
    public static void globalTearDown() {
    }

    public String getBaseUrl() {
        String baseUrl = appendSlashOnRightSide(serverUrl) + appContext;
        return appendSlashOnRightSide(baseUrl);
    }

}
