import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardApplicationValidationCheckTest {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldNotSendFormWithEmptyNameField() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithEmptyPhoneField() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithEmptyCheckbox() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".checkbox__text")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithLatinSymbolsInName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Lebedeva Olga");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithJapaneseSymbolsInName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("れべでヴぁおりが");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNonAlphabeticSymbolsInName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys(";)");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNumbersInName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("3256");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNumberWithoutPlus() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("79998887766");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNumberOf10numbers() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+7999888776");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNumberOf12numbers() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("799988877666");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithLettersInNumber() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("котики");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text);
    }

    @Test
    public void shouldNotSendFormWithNonAlphabeticSymbolsInNumber() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Лебедева Ольга");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys(":(");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text);
    }


}
