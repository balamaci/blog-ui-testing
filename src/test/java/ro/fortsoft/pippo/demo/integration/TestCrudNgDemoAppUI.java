package ro.fortsoft.pippo.demo.integration;

import com.google.common.base.Function;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Serban Balamaci
 */
public class TestCrudNgDemoAppUI extends PhantomJsWebdriverTest {

    public TestCrudNgDemoAppUI() {
        super(DIMENSION_800x600);
    }

    @Test public void
    forAuthorizedUserLoginSuccessful() throws Exception {
        WebDriver driver = getDriver();
        driver.get(getBaseUrl() + "login");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                return webDriver.findElement(By.name("username")) != null;
            }
        });

        takeScreenshotAndCompare("LoginPanel");
        fillUserLoginData(driver);

        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                return webDriver.getTitle().equals("Contacts");
            }
        });
        takeScreenshotAndCompare("ContactsPanel");
    }

    private void fillUserLoginData(WebDriver driver) {
        String username = "admin";
        WebElement txtUsername = driver.findElement(By.name("username"));
        txtUsername.sendKeys(username);

        String password = "password";
        WebElement txtPassword = driver.findElement(By.name("password"));
        txtPassword.sendKeys(password);

        WebElement btnLogin = driver.findElement(By.name("login"));
        btnLogin.click();
    }
}
