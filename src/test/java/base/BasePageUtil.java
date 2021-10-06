package base;

import helper.ElementHelper;
import helper.StoreHelper;
import model.ElementInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.Select;

public class BasePageUtil {
    WebDriver driver;
    public FluentWait<WebDriver> fluentWait;

    public BasePageUtil(WebDriver driver) {
        this.driver = driver;
        fluentWait = setFluentWait(3000);
    }

    public FluentWait<WebDriver> setFluentWait(int timeout) {
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
        fluentWait.withTimeout(timeout, TimeUnit.MILLISECONDS)
                .pollingEvery(250, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }


    public WebElement findElement(String key) {
        By infoParam = getBy(key);
        WebElement webElement = fluentWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);

        return webElement;
    }

    public List<WebElement> findElements(String key) {
        By infoParam = getBy(key);
        return driver.findElements(infoParam);
    }

    public void clickElement(WebElement element) {
        element.click();
    }

    public void hoverElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public boolean isDisplayedBy(By by) {
        boolean retValue = false;
        if (this.isElementPresentBy(by)) {
            WebElement element = driver.findElement(by);
            try {
                retValue = element.isDisplayed();
            } catch (Exception e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500L);
                } catch (Exception ex) {
                }

                retValue = element.isDisplayed();
            }
        }

        return retValue;
        //return driver.findElement(by).isDisplayed();
    }

    public boolean isElementPresentBy(By by) {
        WebElement webElement = null;

        try {
            webElement = driver.findElement(by);
        } catch (Exception e) {
        }

        return webElement != null;
    }

    public void sendKeys(String key, String text) {
        findElement(key).sendKeys(text);
    }

    public void sendKeys(String key, Keys keys) {
        findElement(key).sendKeys(keys);
    }

    public String getText(String key) {
        return findElement(key).getText();
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public By getBy(String key) {
        ElementInfo elementInfo = getElementInfo(key);
        return ElementHelper.getElementInfoToBy(elementInfo);
    }

    public ElementInfo getElementInfo(String key) {
        return StoreHelper.INSTANCE.findElementInfoByKey(key);
    }

    public void waitByMilliSeconds(long milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForThePage(int timeout, int pollingTime) {

        boolean pageLoadExceptionOccured = false;
        fluentWait = (new FluentWait(driver)).withTimeout(timeout, TimeUnit.MILLISECONDS).pollingEvery(pollingTime, TimeUnit.MILLISECONDS);
        try {
            fluentWait.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    boolean retVal = false;
                    retVal = ((JavascriptExecutor) driver).executeScript("return document.readyState", new Object[0]).toString().equalsIgnoreCase("complete");
                    return retVal;
                }
            });
        } catch (Exception e) {
            return;
        }
    }

    public void waitForTheElement(String key) {
        By infoParam = getBy(key);
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));
    }

    public void saveValueWithKey(String key, String value) {
        StoreHelper.INSTANCE.saveValue(key, value);
    }

    public String getValueWithKey(String key) {
        return StoreHelper.INSTANCE.getValue(key);
    }

    public void selectValueInCombobox(String key, String value) {
        Select select = new Select(findElement(key));
        select.selectByVisibleText(value);

        String xpathSelect = getValueWithKey(key);
        xpathSelect += "//option[text()='" + value + "']";
        WebElement we = driver.findElement(By.xpath(xpathSelect));
        if (!we.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("var select = arguments[0]; for(var i = 0; i < select.options.length; i++){ if(select.options[i].text == arguments[1]){ select.options[i].selected = true; } }", we, value);
        }
    }

    public String getStoredElementValue(String key) {
        return StoreHelper.INSTANCE.getStoredElementValue(key);
    }

    public void checkElementNumberEquals(String key, int elementNum, int timeout) {
        fluentWait.withTimeout(timeout, TimeUnit.MILLISECONDS).until(ExpectedConditions.numberOfElementsToBe(getBy(key), elementNum));
    }

    public String getTitle() {
        return driver.getTitle().trim();
    }
}