package ro.fortsoft.pippo.demo.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Serban Balamaci
 */
@RunWith(Parameterized.class)
public abstract class CrudNgDemoAppTest extends BaseUIWebdriverTest {

    @Parameterized.Parameters
    public static Collection<Object[]> getDimensions() {
        return Arrays.asList(new Object[][] {
                {DIMENSION_800x600}, {DIMENSION_1024x768}});
    }

    public CrudNgDemoAppTest(Dimension dimension) {
        super(dimension);
    }


    @Test public void
    loginPage() throws Exception {
        navigateTo("login");
        waitForElementWithName("username");
        takeScreenshotCompareAndCollectDiff("LoginPage");
    }

    @Test public void
    contactsPanel() throws Exception {
        login();

        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        wait.until((WebDriver webDriver) -> webDriver.getTitle().equals("Contacts"));

        takeScreenshotCompareAndCollectDiff("ContactsPage");
    }

    private void login() {
        navigateTo("login");

        waitForElementWithName("username");

        fillUserLoginData();
    }

    private void fillUserLoginData() {
        WebDriver driver = getDriver();

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
