package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlfaPage  {
    private final WebDriver driver;
    private final WebDriverWait wait;


    private final By acceptButtonCookie = By.xpath("//span[contains(@class, 'button__content') and normalize-space()='Принять']");
    private final By buttonCredits = By.xpath("//button[contains(@class, 'rounded-filter__item') and normalize-space()='Кредиты']");
    private final By nextSlider = By.xpath("(//button[contains(@class, 'slider__control--next') and contains(@aria-label, 'следующий слайд')])[1]");
    private final By moreDetails = By.xpath("//a[contains(@href, '/credits/online/credit')]//span[normalize-space()='Подробнее']");
    private final By oformit = By.xpath("//span[contains(@class, 'button__content') and normalize-space()='Оформить']");
    private final By inputNumber = By.name("phone");
    private final By send = By.xpath("//span[contains(@class, 'button__content') and normalize-space()='Отправить']");
    private static final String URL = "https://www.alfabank.by/";


    public AlfaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    private void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // найти, проскроллить к центру экрана, дождаться кликабельности и кликнуть
    private void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollToCenter(element);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }


    public void open() {
        driver.get(URL);
    }

    public void acceptCookiesIfPresent() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(acceptButtonCookie)).click();
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(acceptButtonCookie));
        } catch (TimeoutException ignored) {
            // баннера нет, идём дальше
        }
    }

    public void openCreditsTab() {
        scrollAndClick(buttonCredits);
    }

    public void nextSlide() {
        scrollAndClick(nextSlider);
    }

    public void openMoreDetails() {
        scrollAndClick(moreDetails);
        wait.until(ExpectedConditions.urlContains("/credits/online/credit"));
    }

    public void clickOformit() {
        scrollAndClick(oformit);
    }

    public void fillPhone(String phone) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputNumber));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        input.sendKeys(phone);
    }

    public void clickSend() {
        scrollAndClick(send);
    }

    // удобный составной шаг: от главной до страницы с калькулятором
    public void goToCreditCalculator() {
        open();
        acceptCookiesIfPresent();
        openCreditsTab();
        nextSlide();
        openMoreDetails();
    }
}
