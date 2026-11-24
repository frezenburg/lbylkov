package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreditFormCalc {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String INCOME = "Ежемесячный доход";
    public static final String CURRENT_PAYMENTS = "Платежи по текущим кредитам";
    public static final String LOAN_AMOUNT = "Сумма кредита";
    public static final String TERM_24 = "24";
    public static final String TERM_36 = "36";
    private static final String ROOT = "//*[contains(@class, 'prod-calc__aside-container')]";
    private static final String TERM_BLOCK = ROOT + "//div[contains(@class,'custom-select__label')]" +
            "[contains(normalize-space(),'Срок кредита')]/..";
    private static final String TERM_OPTION_TEMPLATE = TERM_BLOCK + "//div[contains(@class, 'items')]/div[.//span[contains(normalize-space(), '%s')]]";

    private static final By TERM_SELECTED = By.xpath(TERM_BLOCK + "//label[contains(@class, 'selected')]");
    private static final By TERM_CURRENT = By.xpath(TERM_BLOCK + "//label[contains(@class, 'selected')]//span[contains(@class, 'text')]");
    private static final By TERM_ITEMS = By.xpath(TERM_BLOCK + "//div[contains(@class, 'items')]");

    public CreditFormCalc(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public void scrollToForm() {
        WebElement form = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ROOT)));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", form);
    }

    //собиралка локатора
    private By calcInput(String label) {
        return By.xpath(ROOT + String.format( "//div[contains(@class,'range-input__label')]" +
                "[contains(normalize-space(),'%s')]/..//input[@inputmode='decimal']", label));
    }

    //заполняшка поля
    public void setValue(String label, String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(calcInput(label)));
            input.click();
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            input.sendKeys(value);
            input.sendKeys(Keys.TAB);
    }

    //проверялка ожидаемых числовых значений в инпутах
    public String getCalcValue(String label) {
        return driver.findElement(calcInput(label)).getDomProperty("value").replaceAll("\\D", "");
    }

    //опен раскрывающиеся менюшку, плюс выбирает сколько месяцев для кредита
    public void selectTerm (String month) {
        wait.until(ExpectedConditions.elementToBeClickable(TERM_SELECTED)).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(TERM_ITEMS, "class", "select-closed")));

        By option = By.xpath(String.format(TERM_OPTION_TEMPLATE, month));
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(TERM_CURRENT, month));
    }

    // проверяет приколы, какие  установили месяцы
    public String getSelectedTerm () {
        return driver.findElement(TERM_CURRENT).getText();
    }
}
