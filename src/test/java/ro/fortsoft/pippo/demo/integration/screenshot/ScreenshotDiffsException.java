package ro.fortsoft.pippo.demo.integration.screenshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sbalamaci
 */
public class ScreenshotDiffsException extends RuntimeException {

    private List<ScreenshotDiff> screenshotDiffs;

    public ScreenshotDiffsException(List<ScreenshotDiff> screenshotDiffs) {
        this.screenshotDiffs = screenshotDiffs;
    }

    @Override
    public String getMessage() {
        return "The following screens differ " + screenshotDiffs.stream()
                .map(ScreenshotDiff::toString)
                .collect(Collectors.joining(", "));
    }
}
