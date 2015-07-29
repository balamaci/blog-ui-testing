package ro.fortsoft.pippo.demo.integration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.rule.ThrowErrorOnDiffScreensRule;
import ro.fortsoft.pippo.demo.integration.screenshot.ScreenshotDiff;
import ro.fortsoft.pippo.demo.integration.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static ro.fortsoft.pippo.demo.integration.util.UrlUtil.appendSlashOnRightSide;

/**
 * Base class for all UI WebDriver tests that provides the functionality of taking
 * printscreens
 *
 * @author Serban Balamaci
 */
public abstract class BaseUIWebdriverTest {

    private Browser browser;
    private Dimension dimension;

    protected static String serverUrl;
    protected static String appContext;

    private static Path screenshotReferencePath;
    private static Path screenshotPath;
    private static Path screenshotDiffPath;

    private static Boolean flagUpdateReferenceScreenshots;

    public static Dimension DIMENSION_800x600 = new Dimension(800, 600);
    public static Dimension DIMENSION_1024x768 = new Dimension(1024, 768);

    @Rule
    public ThrowErrorOnDiffScreensRule throwErrorOnDiffScreensRule = new ThrowErrorOnDiffScreensRule();

    public BaseUIWebdriverTest(Dimension dimension) {
        this.dimension = dimension;
    }



    @BeforeClass
    public static void init() throws Exception {
        serverUrl = "http://" + System.getProperty("container.host");
        appContext = System.getProperty("webapp.deploy.context");

        flagUpdateReferenceScreenshots = Boolean.valueOf(System.getProperty("flagUpdateReferenceScreenshots"));

        Config conf = ConfigFactory.load();
        screenshotPath = Paths.get(conf.getString("screenshot.path"));
        screenshotReferencePath = Paths.get(conf.getString("screenshot.referencePath"));
        screenshotDiffPath = Paths.get(conf.getString("screenshot.diffPath"));
    }

    @Before
    public void before() {
        browser = initBrowser();

        browser.changeWindowSize(dimension);
    }

    protected abstract Browser initBrowser();

    @After
    public void after() {
        browser.shutDown();
    }

    protected WebDriver getDriver() {
        return browser.getDriver();
    }

    private Path takeScreenshot(String scenarioName, boolean isReference) {
        Path currentScreenshotPath;
        if (isReference) {
            currentScreenshotPath = getScreenshotReferencePath(scenarioName);
        } else {
            currentScreenshotPath = getCurrentScreenshotPath(scenarioName);
        }

        Path screen = browser.getDriver().getScreenshotAs(OutputType.FILE).toPath();
        try {
            Files.copy(screen, currentScreenshotPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error saving image path=%s",currentScreenshotPath.toString()), e);
        }
        return currentScreenshotPath;
    }

    private Path getScreenshotReferencePath(String scenarioName) {
        return screenshotReferencePath.resolve(getScreenshotBaseFilename(scenarioName) + ".ref.png");
    }

    private Path getScreenshotDiffPath(String scenarioName) {
        return screenshotDiffPath.resolve(getScreenshotBaseFilename(scenarioName) + ".diff.png");
    }

    private Path getCurrentScreenshotPath(String scenarioName) {
        return screenshotPath.resolve(getScreenshotBaseFilename(scenarioName) + ".png");
    }

    private String getScreenshotBaseFilename(String scenarioName) {
        return scenarioName + "_" + dimension.getWidth() + "_" + dimension.getHeight();
    }

    /**
     * Takes a printscreen and adds it to the list of screen diffs
     *
     * @param scenarioName scenarioName
     */
    protected void takeScreenshotCompareAndCollectDiff(String scenarioName) {
        takeScreenshotAndCompare(scenarioName).ifPresent(throwErrorOnDiffScreensRule::addScreenDiff);
    }

    /**
     * Takes a screenshot and returns an Optional {@link ScreenshotDiff}
     * if the screens are different from the reference.
     *
     * Considers the flagUpdateReferenceScreenshots which can be set at runtime to just
     * update the printscreens instead of creating a diff image and return a diff
     * @param scenarioName how the file will be called
     * @return Optional {@link ScreenshotDiff}
     */
    private Optional<ScreenshotDiff> takeScreenshotAndCompare(String scenarioName) {
        if (flagUpdateReferenceScreenshots) {
            takeScreenshot(scenarioName, true);
        } else {
            Path screenshot = takeScreenshot(scenarioName, false);
            Path reference = getScreenshotReferencePath(scenarioName);
            if (!ImageUtil.isEqual(screenshot, reference)) {
                Path imageDiff = getScreenshotDiffPath(scenarioName);
                ImageUtil.createImageDiff(screenshot, reference, imageDiff);

                return Optional.of(new ScreenshotDiff(scenarioName, dimension));
            }
        }
        return Optional.empty();
    }



    @AfterClass
    public static void globalTearDown() {
    }

    public String getWebAppBaseUrl() {
        String baseUrl = appendSlashOnRightSide(serverUrl) + appContext;
        return appendSlashOnRightSide(baseUrl);
    }

    protected void navigateTo(String pageName) {
        getDriver().get(getWebAppBaseUrl() + pageName);
    }

    protected void waitForElementWithName(String name, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeoutSeconds);
        wait.until((WebDriver webDriver) -> webDriver.findElement(By.name(name)) != null);
    }

    protected void waitForElementWithName(String name) {
        waitForElementWithName(name, 5);
    }
}
