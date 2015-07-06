package ro.fortsoft.pippo.demo.integration.browser;

/**
 * @author sbalamaci
 */

import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Generic Browser class
 */
public abstract class Browser {

    private RemoteWebDriver driver;

    /**
     * Start the browser
     * @param driver the driver
     */
    protected void start(RemoteWebDriver driver) {
        this.driver = driver;
    }

    /**
     * Change the window size
     *
     * @param dimension the dimension of the window
     */
    public void changeWindowSize(Dimension dimension) {
        driver.manage().window().setSize(dimension);
    };

    /**
     * Returns the browser name with version.
     *
     * @return the name of the browser
     */
    public String getBrowserName() {
        return driver.getCapabilities().getBrowserName() + "_" + driver.getCapabilities().getVersion();
    };

    /**
     * Shuts down the webdriver.
     */
    public void shutDown() {
        driver.quit();
    }

    /**
     * @return the webdriver
     */
    public RemoteWebDriver getDriver() {
        return driver;
    }
}
