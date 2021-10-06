package base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected static WebDriver driver;
    private static final String BROWSER = System.getenv("browser");

    @BeforeScenario
    public void setUp() throws Exception {
        Class<? extends WebDriver> driverClass = null;
        switch (BROWSER) {
            case "":
            case "chrome":
                driverClass = ChromeDriver.class;
                break;
            case "firefox":
                driverClass = FirefoxDriver.class;
                break;
        }

        String baseUrl = "https://www.hepsiburada.com/cozummerkezi";
        WebDriverManager.getInstance(driverClass).setup();
        driver = driverClass.newInstance();

        driver.get(baseUrl);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(24, TimeUnit.SECONDS);
    }

    @AfterScenario
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}