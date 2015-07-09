package ro.fortsoft.pippo.demo.integration;

import org.openqa.selenium.Dimension;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.browser.PhantomJsBrowser;

/**
 * @author sbalamaci
 */
public class PhantomJsWebdriverTest extends BaseUIWebdriverTest {

    public PhantomJsWebdriverTest(Dimension dimension) {
        super(dimension);
    }

    @Override
    public Browser initBrowser() {
        return new PhantomJsBrowser();
    }
}
