package dk.cubing.liveresults.web.test.selenium;

import com.thoughtworks.selenium.Selenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Created with IntelliJ IDEA.
 * User: mohr
 * Date: 9/27/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexTest {

    protected Selenium selenium;

    @Before
    public void setUp() {
        FirefoxProfile profile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(profile);
        String baseUrl = "http://live.speedcubing.dk";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
    }

    @After
    public void tearDown() {
        selenium.close();
    }

    @Test
    public void testIndex() {
        selenium.open("/index.action");
    }
}
