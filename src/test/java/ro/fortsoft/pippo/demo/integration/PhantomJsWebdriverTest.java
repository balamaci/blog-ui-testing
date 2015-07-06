package ro.fortsoft.pippo.demo.integration;

import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.browser.PhantomJsBrowser;

/**
 * @author sbalamaci
 */
public class PhantomJsWebdriverTest extends BaseUIWebdriverTest {


    @Override
    public Browser initBrowser() {
        return new PhantomJsBrowser();
    }
}
