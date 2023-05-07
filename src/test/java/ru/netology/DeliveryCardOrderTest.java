package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.Integer.parseInt;

public class DeliveryCardOrderTest {

    private String changeCurrentDateByDays(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    String errorMessageEmptyField = "обязательно для заполнения";
    String errorMessageIncorrectCity = "Доставка в выбранный город недоступна";
    String errorMessageIncorrectName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы";
    String errorMessageIncorrectDate = "Заказ на выбранную дату невозможен";
    String errorMessageIncorrectPhone = "Телефон указан неверно";
    String successMessage = "Встреча успешно забронирована на ";

    @Test
    public void testValidData() {
        String city = "Москва";
        String deliveryDate = changeCurrentDateByDays(4, "dd.MM.yyyy");
        String personFullName = "Петров-Сидоров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $("div.notification__content")
                .shouldHave(Condition.text(deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void testValidDataListChoice() {
        String city = "Нальчик";
        String cityShot = "На";
        String personFullName = "Петров-Сидоров Петя";
        String phone = "+12345678901";

        String deliveryDate = changeCurrentDateByDays(180, "dd.MM.yyyy");
        String deliveryDay = changeCurrentDateByDays(180, "d");
        int deliveryMonthDigit = parseInt(changeCurrentDateByDays(180, "MM"));
        int deliveryYear = parseInt(changeCurrentDateByDays(180, "yyyy"));

        int currentMonthDigit = parseInt(changeCurrentDateByDays(0, "MM"));

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(cityShot);
        $x("//span[text()='" + city + "']/..").click();
        SelenideElement dateInput =
                $x("//span[@class='input__icon']/button");
        dateInput.click();

        String[] monthYear = $("div.calendar__name").text().split(" ", 2);
        int calendarYear = parseInt(monthYear[1]);

        while (calendarYear < deliveryYear) {
            $x("//div[@class='calendar__arrow calendar__arrow_direction_right calendar__arrow_double']")
                    .click();
            calendarYear++;
        }
        while (currentMonthDigit < deliveryMonthDigit) {
            $x("//div[@class='calendar__arrow calendar__arrow_direction_right']").click();
            currentMonthDigit++;
        }
        while (currentMonthDigit > deliveryMonthDigit) {
            $x("//div[@class='calendar__arrow calendar__arrow_direction_left']").click();
            currentMonthDigit--;
        }
        $x("//td[text()='" + deliveryDay + "']").click();

        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $("div.notification__content")
                .shouldHave(Condition.text(successMessage + deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void testInvalidCity() {
        String city = "Нью Йорк";
        String deliveryDate = changeCurrentDateByDays(5, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='city']//span[contains(text(), '" + errorMessageIncorrectCity + "')]")
                .should(appear);
    }

    @Test
    public void testEmptyCityField() {
        String city = "";
        String deliveryDate = changeCurrentDateByDays(5, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='city']//span[contains(text(), '" + errorMessageEmptyField + "')]")
                .should(appear);
    }

    @Test
    public void testValidCityInCapslock() {
        String city = "МОСКВА";
        String deliveryDate = changeCurrentDateByDays(5, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $("div.notification__content")
                .shouldHave(Condition.text(successMessage + deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void testLatinSymbolInCityField() {
        String city = "Moscow";
        String deliveryDate = changeCurrentDateByDays(5, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='city']//span[contains(text(), '" + errorMessageIncorrectCity + "')]")
                .should(appear);
    }

    @Test
    public void testSpecSymbolInCityField() {
        String city = "Нальчик!";
        String deliveryDate = changeCurrentDateByDays(5, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='city']//span[contains(text(), '" + errorMessageIncorrectCity + "')]")
                .should(appear);
    }

    @Test
    public void testCurrentDateInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(0, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='date']//span[contains(text(), '" + errorMessageIncorrectDate + "')]")
                .should(appear);
    }

    @Test
    public void testEmptyDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = "";
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='date']//span[contains(text(), 'Неверно введена')]").should(appear);
    }

    @Test
    public void testPresentDateInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(-1, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='date']//span[contains(text(), '" + errorMessageIncorrectDate + "')]")
                .should(appear);
    }

    @Test
    public void testCurrentPlusTwoDaysInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(2, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='date']//span[contains(text(), '" + errorMessageIncorrectDate + "')]")
                .should(appear);
    }

    @Test
    public void testCurrentPlusThreeDaysInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(3, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $("div.notification__content")
                .shouldHave(Condition.text(successMessage + deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void testCurrentPlusOneYearInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(365, "dd.MM.yyyy");
        String personFullName = "Петров Петя";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $("div.notification__content")
                .shouldHave(Condition.text(successMessage + deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void testLatinSymbolInNameField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(180, "dd.MM.yyyy");
        String personFullName = "Petrov Petr";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='name']//span[contains(text(), '" + errorMessageIncorrectName + "')]")
                .should(appear);
    }

    @Test
    public void testDigitSymbolInNameField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(180, "dd.MM.yyyy");
        String personFullName = "Петр1";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='name']//span[contains(text(), '" + errorMessageIncorrectName + "')]")
                .should(appear);
    }

    @Test
    public void testSpecSymbolInNameField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(180, "dd.MM.yyyy");
        String personFullName = "Петр+Мария";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='name']//span[contains(text(), '" + errorMessageIncorrectName + "')]")
                .should(appear);
    }

    @Test
    public void testEmptyNameField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(180, "dd.MM.yyyy");
        String personFullName = "";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='name']//span[contains(text(), '" + errorMessageEmptyField + "')]")
                .should(appear);
    }

    @Test
    public void testEmptyPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageEmptyField + "')]")
                .should(appear);
    }

    @Test
    public void testInputWithoutPlusInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testInputLess11DigitInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+1234567890";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testInputMore11DigitInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+123456789012";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testInputSymbolInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+1234567890A";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testInputWithDashInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+1-234-567-89-01";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testInputWithBracketsInPhoneField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+1(234)5678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);
        $("label[data-test-id='agreement']").click();
        $x("//span[@class='button__text']/../../../button").click();

        $x("//span[@data-test-id='phone']//span[contains(text(), '" + errorMessageIncorrectPhone + "')]")
                .should(appear);
    }

    @Test
    public void testUncheckedAgreement() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(60, "dd.MM.yyyy");
        String personFullName = "Сидоров-Иванов Петр";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(city);
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        $("span[data-test-id='name'] input").setValue(personFullName);
        $("span[data-test-id='phone'] input").setValue(phone);

        $x("//span[@class='button__text']/../../../button").click();

        $x("//label[@data-test-id='agreement'][contains(@class, 'input_invalid')]").should(appear);
    }

    @Test
    public void testValidationFieldsAfterCorrectingWrongInput() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(70, "dd.MM.yyyy");
        String personFullName = "Петр";
        String phone = "+12345678901";

        open("http://localhost:9999/");
        SelenideElement cityInput = $("span[data-test-id='city'] input");
        SelenideElement dateInput = $("span[data-test-id='date'] input");
        SelenideElement nameInput = $("span[data-test-id='name'] input");
        SelenideElement phoneInput = $("span[data-test-id='phone'] input");
        SelenideElement agreementCheckbox = $("label[data-test-id='agreement']");
        SelenideElement buttonOK = $x("//span[@class='button__text']/../../../button");

        cityInput.setValue(city + "@");
        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        String incorrectDate = changeCurrentDateByDays(-1, "dd.MM.yyyy");
        dateInput.setValue(incorrectDate);
        nameInput.setValue(personFullName + "W");
        phoneInput.setValue(phone + "2");
        buttonOK.click();
        $x("//span[@data-test-id='city'][contains(@class, 'input_invalid')]").should(appear);

        cityInput.clear();
        cityInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        cityInput.setValue(city);
        buttonOK.click();
        $x("//span[@data-test-id='date']//span[contains(@class, 'input_invalid')]").should(appear);

        dateInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        dateInput.setValue(deliveryDate);
        buttonOK.click();
        $x("//span[@data-test-id='name'][contains(@class, 'input_invalid')]").should(appear);

        nameInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        nameInput.setValue(personFullName);
        buttonOK.click();
        $x("//span[@data-test-id='phone'][contains(@class, 'input_invalid')]").should(appear);

        phoneInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        phoneInput.setValue(phone);
        buttonOK.click();
        $x("//label[@data-test-id='agreement'][contains(@class, 'input_invalid')]").should(appear);

        agreementCheckbox.click();
        buttonOK.click();
        $("div.notification__content")
                .shouldHave(Condition.text(successMessage + deliveryDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

}