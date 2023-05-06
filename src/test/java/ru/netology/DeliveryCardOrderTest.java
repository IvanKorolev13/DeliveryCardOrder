package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.Integer.parseInt;

public class DeliveryCardOrderTest {

    private String changeCurrentDateByDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate newDate = LocalDate.now().plusDays(days);
        return newDate.format(formatter);
    }

    private HashMap<String, String> changeCurrentDateByDaysMap(int days) {
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMM");
        DateTimeFormatter formatterMonthD = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyyy");

        LocalDate newDate = LocalDate.now().plusDays(days);

        HashMap<String, String> result = new HashMap<>();
        result.put("day", newDate.format(formatterDay));
        result.put("month", newDate.format(formatterMonth));
        result.put("monthDigit", newDate.format(formatterMonthD));
        result.put("year", newDate.format(formatterYear));

        return result;
    }

    String errorMessageEmptyField = "обязательно для заполнения";
    String errorMessageIncorrectCity = "Доставка в выбранный город недоступна";
    String errorMessageIncorrectName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы";
    String errorMessageIncorrectDate = "Заказ на выбранную дату невозможен";
    String errorMessageIncorrectPhone = "Телефон указан неверно";

    @Test
    public void testValidData() {
        String city = "Москва";
        String deliveryDate = changeCurrentDateByDays(4);
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

        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }
/*
//еще сырой тест
    @Test
    public void testValidDataListChose() {
        String city = "Нальчик";
        String cityShot = "На";
        String personFullName = "Петров-Сидоров Петя";
        String phone = "+12345678901";

        HashMap<String, String> deliveryData = changeCurrentDateByDaysMap(180);
        String deliveryDay = deliveryData.get("day");
        int deliveryMonthDigit = parseInt(deliveryData.get("monthDigit"));
        int deliveryYear = parseInt(deliveryData.get("year"));

        HashMap<String,String> currentData = changeCurrentDateByDaysMap(0);
        int currentMonthDigit = parseInt(currentData.get("monthDigit"));
        String currentMonthStr = currentData.get("month");

        open("http://localhost:9999/");
        $("span[data-test-id='city'] input").setValue(cityShot);
        $x("//span[text()='" + city + "']/..").click();
        SelenideElement dateInput =
                $x("//button[@class='icon-button icon-button_size_m icon-button_theme_alfa-on-white']");
        dateInput.click();

        String[] monthYear = $("div.calendar__name").text().split(" ", 2);
        String calendarMonthStr = monthYear[0].toLowerCase();
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

        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }
*/
    @Test
    public void testInvalidCity() {
        String city = "Нью Йорк";
        String deliveryDate = changeCurrentDateByDays(5);
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
        String deliveryDate = changeCurrentDateByDays(5);
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
        String deliveryDate = changeCurrentDateByDays(5);
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

        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }

    @Test
    public void testLatinSymbolInCityField() {
        String city = "Moscow";
        String deliveryDate = changeCurrentDateByDays(5);
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
        String deliveryDate = changeCurrentDateByDays(5);
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
        String deliveryDate = changeCurrentDateByDays(0);
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
        String deliveryDate = changeCurrentDateByDays(-1);
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
        String deliveryDate = changeCurrentDateByDays(2);
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
        String deliveryDate = changeCurrentDateByDays(3);
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

        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }

    @Test
    public void testCurrentPlusOneYearInDateField() {
        String city = "Нижний Новгород";
        String deliveryDate = changeCurrentDateByDays(365);
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

        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }

    @Test
    public void testLatinSymbolInNameField() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(180);
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
        String deliveryDate = changeCurrentDateByDays(180);
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
        String deliveryDate = changeCurrentDateByDays(180);
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
        String deliveryDate = changeCurrentDateByDays(180);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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
        String deliveryDate = changeCurrentDateByDays(60);
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

    //ввести все поля невалидные, исправлять поля снизу

    @Test
    public void testValidationFieldsAfterCorrectingWrongInput() {
        String city = "Ханты-Мансийск";
        String deliveryDate = changeCurrentDateByDays(70);
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
        String incorrectDate = changeCurrentDateByDays(-1);
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
        $(withText("Успешно")).shouldBe(appear, Duration.ofSeconds(15));
    }

}