package ro.fortsoft.pippo.demo.integration;

/**
 * @author sbalamaci
 */
public class ScreenshotDiffException extends Exception {

    private String name;

    public ScreenshotDiffException(String name) {
        super("Encountered image diff for scenario " + name);
        this.name = name;
    }
}
