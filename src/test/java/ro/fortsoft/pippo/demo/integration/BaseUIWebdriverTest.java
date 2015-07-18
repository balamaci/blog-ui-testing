package ro.fortsoft.pippo.demo.integration;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ro.fortsoft.pippo.demo.integration.util.UrlUtil.appendSlashOnRightSide;

/**
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

    private static Boolean updateReferenceImages;

    public static Dimension DIMENSION_800x600 = new Dimension(800, 600);
    public static Dimension DIMENSION_1024x768 = new Dimension(1024, 768);

    List<ScreenshotDiffException> differentScreen;

    public BaseUIWebdriverTest(Dimension dimension) {
        this.dimension = dimension;
    }

    @BeforeClass
    public static void init() throws Exception {
        serverUrl = "http://" + System.getProperty("container.host");
        appContext = System.getProperty("webapp.deploy.context");

        updateReferenceImages = Boolean.valueOf(System.getProperty("screenshotUpdateReferences"));
        System.out.println("******** UPDATE " + updateReferenceImages);
        Config conf = ConfigFactory.load();
        screenshotPath = Paths.get(conf.getString("screenshot.path"));
        screenshotReferencePath = Paths.get(conf.getString("screenshot.referencePath"));
        screenshotDiffPath = Paths.get(conf.getString("screenshot.diffPath"));
    }

    @Before
    public void before() {
        differentScreen = Lists.newArrayList();
        this.browser = initBrowser();

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
            throw new RuntimeException("Error saving image", e);
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
     * Takes a screenshot and returns an
     *
     * @param scenarioName
     * @return
     * @throws Exception
     */
    private Optional<ScreenshotDiffException> takeScreenshotAndCompare(String scenarioName) {
        if (updateReferenceImages) {
            takeScreenshot(scenarioName, true);
        } else {
            Path screenshot = takeScreenshot(scenarioName, false);
            Path reference = getScreenshotReferencePath(scenarioName);
            if (!ImageUtil.isEqual(screenshot, reference)) {
                Path imageDiff = getScreenshotDiffPath(scenarioName);
                ImageUtil.createImageDiff(screenshot, reference, imageDiff);

                return Optional.of(new ScreenshotDiffException(scenarioName));
            }
        }
        return Optional.empty();
    }

    protected void takeScreenshotCompareAndCollect(String scenarioName) {
        takeScreenshotAndCompare(scenarioName).ifPresent(differentScreen::add);
    }

    public static <T> List<T> collect(Optional<T> option, List<T> previousValues) {
        previousValues.addAll(option.
                                  map(Collections::singletonList).
                                  orElse(Collections.emptyList()));
        return previousValues;
    }

    @AfterClass
    public static void globalTearDown() {
    }

    public String getBaseUrl() {
        String baseUrl = appendSlashOnRightSide(serverUrl) + appContext;
        return appendSlashOnRightSide(baseUrl);
    }

    protected void navigateTo(String pageName) {
        getDriver().get(getBaseUrl() + pageName);
    }

    protected void waitForElementWithName(String name, long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeoutSeconds);
        wait.until((WebDriver webDriver) -> webDriver.findElement(By.name(name)) != null);
    }

    protected void waitForElementWithName(String name) {
        waitForElementWithName(name, 5);
    }
}
