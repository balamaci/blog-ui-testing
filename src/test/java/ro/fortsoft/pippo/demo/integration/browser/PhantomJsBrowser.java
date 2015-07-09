package ro.fortsoft.pippo.demo.integration.browser;

import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

/**
 * @author sbalamaci
 */
public class PhantomJsBrowser extends Browser {

    /**
     * Constructor.
     *
     */
    public PhantomJsBrowser() {
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, Boolean.TRUE);

        LoggingPreferences prefs = new LoggingPreferences();
        prefs.enable(LogType.BROWSER, Level.WARNING);
        prefs.enable(LogType.DRIVER, Level.INFO);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, prefs);

        start(new PhantomJSDriver(PhantomJSDriverService.createDefaultService(), capabilities));
    }



}
