package ro.fortsoft.pippo.demo.integration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.util.ImageUtil;

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

    public BaseUIWebdriverTest(Dimension dimension) {
        this.dimension = dimension;
    }

    @BeforeClass
    public static void init() throws Exception {
        serverUrl = "http://" + System.getProperty("container.host");
        appContext = System.getProperty("webapp.deploy.context");

        updateReferenceImages = Boolean.valueOf(System.getProperty("screenshot.updateReferences"));

        Config conf = ConfigFactory.load();
        screenshotPath = Paths.get(conf.getString("screenshot.path"));
        screenshotReferencePath = Paths.get(conf.getString("screenshot.referencePath"));
        screenshotDiffPath = Paths.get(conf.getString("screenshot.diffPath"));
    }

    public abstract Browser initBrowser();

    @Before
    public void before() {
        browser = initBrowser();

        browser.changeWindowSize(dimension);
    }

    @After
    public void after() {
        browser.shutDown();
    }

    protected WebDriver getDriver() {
        return browser.getDriver();
    }

    private Path takeScreenshot(String scenarioName, boolean isReference) throws Exception {
        Path currentScreenshotPath;
        if (isReference) {
            currentScreenshotPath = getScreenshotReferencePath(scenarioName);
        } else {
            currentScreenshotPath = getCurrentScreenshotPath(scenarioName);
        }

        Path screen = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE).toPath();
        Files.copy(screen, currentScreenshotPath, StandardCopyOption.REPLACE_EXISTING);

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
     * @param scenarioName
     * @return
     * @throws Exception
     */
    public Optional<ScreenshotDiffException> takeScreenshotAndCompare(String scenarioName) throws Exception {
        if(updateReferenceImages) {
            takeScreenshot(scenarioName, true);
        } else {
            Path screenshot = takeScreenshot(scenarioName, false);
            Path reference = getScreenshotReferencePath(scenarioName);
            if(! ImageUtil.isEqual(screenshot, reference)) {
                Path imageDiff = getScreenshotDiffPath(scenarioName);
                ImageUtil.createImageDiff(screenshot, reference, imageDiff);

                return Optional.of(new ScreenshotDiffException(scenarioName));
            }
        }
        return Optional.empty();
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

}
