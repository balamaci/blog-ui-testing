package ro.fortsoft.pippo.demo.integration.rule;

import com.google.common.collect.Lists;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ro.fortsoft.pippo.demo.integration.ScreenshotDiffException;

import java.util.List;

/**
 * @author Serban Balamaci
 */
public class ThrowErrorOnDiffScreensRule extends TestWatcher {

    private List<ScreenshotDiffException> differentScreen = Lists.newArrayList();

    protected void starting(Description description) {
        differentScreen.clear();
    }

    protected void finished(Description description) {
    }

    public void addScreenDiffException(ScreenshotDiffException screenDifException) {
        differentScreen.add(screenDifException);
    }

}
