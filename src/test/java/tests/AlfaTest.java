package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import pages.CreditFormCalc;
import pages.AlfaPage;

public class AlfaTest {

    private WebDriver driver;
    private AlfaPage page;
    private CreditFormCalc calc;




    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        calc = new CreditFormCalc(driver);
        page = new AlfaPage(driver);
    }

    @Test

    void fullFlow() {
        page.goToCreditCalculator();

        calc.scrollToForm();
        calc.setValue(CreditFormCalc.INCOME, "3000");
        calc.setValue(CreditFormCalc.LOAN_AMOUNT, "5000");
        calc.selectTerm(CreditFormCalc.TERM_36);

        page.clickOformit();
        page.fillPhone("297777777");
        page.clickSend();
    }

    void incomeField() {
        calc.setValue(CreditFormCalc.INCOME, "3000");
        String actual = calc.getCalcValue(CreditFormCalc.INCOME);
        Assertions.assertEquals("3000", actual);
    }



    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
