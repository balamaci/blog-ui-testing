package ro.fortsoft.pippo.demo.integration;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @author Serban Balamaci
 */
public class TestCrudNgDemoAppUI extends PhantomJsWebdriverTest {

    public TestCrudNgDemoAppUI() {
        super(DIMENSION_800x600);
    }

    @Test public void
    forAuthorizedUserLoginSuccessful() throws Exception {
        List<ScreenshotDiffException> differentScreen = Lists.newArrayList();

        WebDriver driver = getDriver();
        driver.get(getBaseUrl() + "login");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until((WebDriver webDriver) -> webDriver.findElement(By.name("username")) != null);
        takeScreenshotAndCompare("LoginPanel").ifPresent(differentScreen::add);

        fillUserLoginData(driver);

        wait.until((WebDriver webDriver) -> webDriver.getTitle().equals("Contacts"));
        takeScreenshotAndCompare("ContactsPanel").ifPresent(differentScreen::add);
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
