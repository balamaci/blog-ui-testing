package ro.fortsoft.pippo.demo.integration;

import org.openqa.selenium.Dimension;
import ro.fortsoft.pippo.demo.integration.browser.Browser;
import ro.fortsoft.pippo.demo.integration.browser.PhantomJsBrowser;

/**
 * @author Serban Balamaci
 */
public class TestPhantomJsCrudNgDemoApp extends CrudNgDemoAppTest {

    public TestPhantomJsCrudNgDemoApp(Dimension dimension) {
        super(dimension);
    }

    @Override
    protected Browser initBrowser() {
        return new PhantomJsBrowser();
    }
}
