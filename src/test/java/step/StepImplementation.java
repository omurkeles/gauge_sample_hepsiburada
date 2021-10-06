package step;

import base.BasePageUtil;
import base.BaseTest;
import com.thoughtworks.gauge.Step;
import helper.StoreHelper;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.*;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StepImplementation extends BaseTest {
    private final static Logger logger = Logger.getLogger(StepImplementation.class.getName());

    BasePageUtil selenium;
    FluentWait<WebDriver> fluentWait;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;
    public static int DEFAULT_MILLISECOND_PAGE_LOAD = 10000;
    public static int DEFAULT_MILLISECOND_POLLING_TIME = 1000;

    public StepImplementation() {
        selenium = new BasePageUtil(driver);
        fluentWait = selenium.fluentWait;
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
        WebElement element = selenium.findElement(key);
        selenium.hoverElement(element);
        selenium.clickElement(element);
        logger.info(key + " elemente tıkladı");
        //waitPage();
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini elemente yaz <key>"})
    public void sendKeys(String text, String key) {
        if (!key.equals("")) {
            selenium.sendKeys(key, text);
            logger.info(text + " değeri" + key + " elementine yazdı ");
        }
    }

    @Step("Wait for page load")
    public void waitPage() {
        selenium.waitForThePage(DEFAULT_MILLISECOND_PAGE_LOAD, DEFAULT_MILLISECOND_POLLING_TIME);
    }

    @Step({"<key> li random elemente tıkla", "Click to random element <key>"})
    public void clickRandom(String key) {
        Random random = new Random();
        By by = selenium.getBy(key);
        List<WebElement> list = selenium.findElements(key);
        int randomNum = random.nextInt(list.size());
        list.get(randomNum == 0 ? 0 : randomNum - 1).click();
    }

    @Step({"Eğer varsa <key> li random elemente tıkla", "Click to random element <key> if exists"})
    public void clickRandomIfExists(String key) {
        Random random = new Random();
        By by = selenium.getBy(key);
        List<WebElement> list = null;
        try {
            list = selenium.findElements(key);
        } catch (Exception e) {
        }

        if (list != null || list.size() != 0) {
            int randomNum = random.nextInt(list.size());
            list.get(randomNum == 0 ? 0 : randomNum - 1).click();
        }
    }

    @Step({"Sayfa başlığı <value> değeri ile eşitliğini kontrol et", "Check <value> in title"})
    public void sayfaTitleKontroluYap(String value) {
        if (selenium.getTitle().equalsIgnoreCase(value)) {
            logger.info("### " + value + " ###" + " page successfully loaded.");
        } else {
            String errMsgTxt = "Gelen sayfa: " + selenium.getTitle() + " Beklenen Sayfa: " + value;
            new Exception("Beklenilen sayfa görüntülenemedi. " + errMsgTxt);
        }
    }

    @Step({"<value> değerini içeren texte tıkla", "Click element in <value> text"})
    public void clickElementIncludeText(String value) {
        try{
            TimeUnit.MILLISECONDS.sleep(2000);
        }catch (Exception e){
        }

        WebElement element = driver.findElement(By.xpath("//*[normalize-space(text())='" + value + "']"));
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[normalize-space(text())='" + value + "']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", element);

        try{
            TimeUnit.MILLISECONDS.sleep(2000);
        }catch (Exception e){
        }

        selenium.clickElement(element);
    }
}
