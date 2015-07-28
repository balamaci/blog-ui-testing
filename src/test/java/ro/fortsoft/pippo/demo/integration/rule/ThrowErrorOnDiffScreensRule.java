package ro.fortsoft.pippo.demo.integration.rule;

import com.google.common.collect.Lists;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ro.fortsoft.pippo.demo.integration.screenshot.ScreenshotDiff;
import ro.fortsoft.pippo.demo.integration.screenshot.ScreenshotDiffsException;

import java.util.List;

/**
 * TestWatcher implementation that at the end of a tests looks for the existence
 * of screen shots diffs and signals as a failed test because by throwing an exception
 *
 *
 * @author Serban Balamaci
 */
public class ThrowErrorOnDiffScreensRule extends TestWatcher {

    private List<ScreenshotDiff> differentScreen = Lists.newArrayList();

    protected void starting(Description description) {
        System.out.println("** Started ......");
        differentScreen.clear();
    }

    protected void finished(Description description) {
        System.out.println("Checcking finished ......");
        if(! differentScreen.isEmpty()) {
            throw new ScreenshotDiffsException(differentScreen);
        }
    }

    public void addScreenDiffException(ScreenshotDiff screenDifException) {
        differentScreen.add(screenDifException);
    }

}
