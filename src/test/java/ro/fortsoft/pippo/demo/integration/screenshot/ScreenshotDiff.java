package ro.fortsoft.pippo.demo.integration.screenshot;

import org.openqa.selenium.Dimension;

/**
 * @author sbalamaci
 */
public class ScreenshotDiff {

    private String screenName;
    private Dimension dimension;

    /**
     * Constr.
     * @param screenName screenName
     * @param dimension dimension
     */
    public ScreenshotDiff(String screenName, Dimension dimension) {
        this.screenName = screenName;
        this.dimension = dimension;
    }

    public String getScreenName() {
        return screenName;
    }

    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        return screenName + dimension;
    }
}
