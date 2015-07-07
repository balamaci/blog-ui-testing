package ro.fortsoft.pippo.demo.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ro.fortsoft.pippo.demo.integration.browser.Browser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ro.fortsoft.pippo.demo.integration.util.UrlUtil.appendSlashOnRightSide;

/**
 * @author Serban Balamaci
 */
public abstract class BaseUIWebdriverTest {

    private Browser browser;

    protected static String serverUrl;
    protected static String appContext;

    private static Path screenshotReferencePath;
    private static Path screenshotPath;
    private static Path screenshotDiffPath;

    @BeforeClass
    public static void init() throws Exception {
        serverUrl = "http://" + System.getProperty("container.host");
        appContext = System.getProperty("webapp.deploy.context");

        screenshotPath = Paths.get(System.getProperty("screenshot.path"));
        screenshotReferencePath = Paths.get(System.getProperty("screenshot.reference.path"));
        screenshotDiffPath = Paths.get(System.getProperty("screenshot.diff.path"));
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

    public void takeScreenshot(String pageName, boolean isReference) throws Exception {
        String screenshotFilename = pageName;

        Path currentScreenshotPath;
        if (isReference) {
            currentScreenshotPath = screenshotReferencePath.resolve(screenshotFilename + ".ref.png");
        } else {
            currentScreenshotPath = screenshotPath.resolve(screenshotFilename + ".png");
        }

        Path screen = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE).toPath();
        Files.copy(screen, currentScreenshotPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void takeScreenshotAndCompare(String pageName, boolean isReference) throws Exception {

    }

    @AfterClass
    public static void globalTearDown() {
    }

    public String getBaseUrl() {
        String baseUrl = appendSlashOnRightSide(serverUrl) + appContext;
        return appendSlashOnRightSide(baseUrl);
    }

}
