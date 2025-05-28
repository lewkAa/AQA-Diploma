package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.ApiHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTest {
    public static final String APPROVED_CARD = "1111 2222 3333 4444";
    public static final String DECLINED_CARD = "5555 6666 7777 8888";

    @BeforeAll
    static void AllureStart() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void AllureStop() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if (!testInfo.getDisplayName().contains("NoSetup")) {
            open("http://localhost:8080");
        }
    }

    @AfterEach
    void clean() {
        SQLHelper.cleanDB();
    }

    /* Web Тесты */

    @Test
    @DisplayName("Success buy with APPROVED Card")
    void shouldSucessWithApprovedCard() {
        var card = DataHelper.genCard(APPROVED_CARD);
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.formFill(card);
        buyPg.errorCheck(true);

        assertEquals("APPROVED", SQLHelper.getStatus()); // Проверка записи в БД.
    }


    @Test
    @DisplayName("Failed buy with DECLINED Card")
    void shouldFailWithDeclinedCard() {
        var card = DataHelper.genCard(DECLINED_CARD);
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.formFill(card);
        buyPg.errorCheck(false);

        assertEquals("DECLINED", SQLHelper.getStatus()); // Проверка записи в БД.
    }

    @Test
    @DisplayName("Failed buy with random Card")
    void shouldFailWithRandomCard() {
        var card = DataHelper.genCard(DataHelper.genRndCardNum());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.formFill(card);
        buyPg.errorCheck(false);
    }


    @Test
    @DisplayName("Should get error when send form with empty field 'number'")
    void shouldErrorWithEmptyNumberField() {
        var card = DataHelper.genCard(""); // Генерируем карту без номера
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "numberSub", "empty");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'cvv'")
    void shouldErrorWithEmptyCvvField() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), "");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "cvvSub", "empty");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'holder'")
    void shouldErrorWithEmptyHolderField() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                "", DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "holderSub", "empty");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'month'")
    void shouldErrorWithEmptyMonthField() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), "",
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "monthSub", "empty");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'Year'")
    void shouldErrorWithEmptyYearField() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                "", date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "yearSub", "empty");
    }

    @Test
    @DisplayName("Should get error when send form with short field 'number'")
    void shouldErrorWithShortNumber() {
        var card = DataHelper.genCard(DataHelper.genShortCardNum());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "numberSub", "format");
    }

    @Test
    @DisplayName("Should not input digits above max in field 'number'")
    void shouldNoExtraInputWithLongNumber() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.extraInputCheck("number", DataHelper.genLongCardNum(), 19);
    }

    @Test
    @DisplayName("Should not input special chars  in field 'number'")
    void shouldNotInputSpecCharsInNumberField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("number", DataHelper.genSpeсChars());
    }

    @Test
    @DisplayName("Should not input latin chars  in field 'number'")
    void shouldNotInputLatinCharsInNumberField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("number", DataHelper.genLatinStr(1, 10));
    }

    @Test
    @DisplayName("Should not input cirill text  in field 'number'")
    void shouldNotInputCirillCharsInNumberField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("number", DataHelper.genCyrillStr());
    }

    @Test
    @DisplayName("Should get error when send form with invalid field 'month'")
    void shouldErrorWithInvalidMonthField() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genNumber(13, 99),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "monthSub", "expiration");
    }

    @Test
    @DisplayName("Should get error when send form with '0'' in field 'month'")
    void shouldErrorWithZeroInMonth() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), "0",
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "monthSub", "format");

    }

    @Test
    @DisplayName("Should get error when send form with bad format in field 'month'")
    void shouldErrorWithBadFormatMonth() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genNumber(0, 9),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "monthSub", "format");

    }

    @Test
    @DisplayName("Should not input special chars  in field 'month'")
    void shouldNotInputSpecCharsInMonthField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("month", DataHelper.genSpeсChars());
    }

    @Test
    @DisplayName("Should not input latin chars  in field 'month'")
    void shouldNotInputLatinCharsInMonthField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("month", DataHelper.genLatinStr(1, 10));
    }

    @Test
    @DisplayName("Should not input cirill text  in field 'month'")
    void shouldNotInputCirillCharsInMonthField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("month", DataHelper.genCyrillStr());
    }

    @Test
    @DisplayName("Should get error when send form with '0'' in field 'year'")
    void shouldErrorWithZeroInYear() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                "0", date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "yearSub", "format");

    }

    @Test
    @DisplayName("Should get error when send form with bad format in field 'year'")
    void shouldErrorWithBadFormatYear() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genNumber(0, 9), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "yearSub", "format");

    }

    @Test
    @DisplayName("Should not input special chars  in field 'year'")
    void shouldNotInputSpecCharsInYearField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("year", DataHelper.genSpeсChars());
    }

    @Test
    @DisplayName("Should not input latin chars  in field 'year'")
    void shouldNotInputLatinCharsInYearField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("year", DataHelper.genLatinStr(1, 10));
    }

    @Test
    @DisplayName("Should not input cirill text  in field 'year'")
    void shouldNotInputCirillCharsInYearField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("year", DataHelper.genCyrillStr());
    }

    @Test
    @DisplayName("Should get error when send form with outdated in field 'year'")
    void shouldErrorWithOutdatedYear() {
        var date = DataHelper.genDate(false);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "yearSub", "outdated");

    }

    @Test
    @DisplayName("Should get error when send form with outdated in field 'month'")
    void shouldErrorWithOutdatedMonth() {
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.getCurrYear(), DataHelper.genInvalidMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "monthSub", "expiration");
    }

    @Test
    @DisplayName("Should get error when send form with value below min length in field 'holder'")
    void shouldErrorWithTooShortYHolder() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genLatinStr(1, 1), DataHelper.genValidCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "holderSub", "format");

    }

    @Test
    @DisplayName("Should not input value above max length in field 'holder'")
    void shouldNoExtraInputLongHolder() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.extraInputCheck("holder", DataHelper.genLatinStr(31, 50), 19);
    }

    @Test
    @DisplayName("Should not input special chars  in field 'holder'")
    void shouldNotInputSpecCharsInHolderField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("year", DataHelper.genSpeсChars());
    }

    @Test
    @DisplayName("Should not input digits  in field 'holder'")
    void shouldNotInputDigitsInHolderField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("holder", DataHelper.genDigits(1, 10));
    }

    @Test
    @DisplayName("Should not input cirill text  in field 'holder'")
    void shouldNotInputCirillCharsInHolderField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("holder", DataHelper.genCyrillStr());
    }

    @Test
    @DisplayName("Should get error when send form with '0'' in field 'CVV'")
    void shouldErrorWithZeroInCVV() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), "0");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "cvvSub", "format");

    }

    @Test
    @DisplayName("Should get error when send form with short value in field 'CVV'")
    void shouldErrorWithShortCVV() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genShortCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.fieldErrorCheck(card, "cvvSub", "format");

    }

    @Test
    @DisplayName("Should not input value above max length in field 'CVV'")
    void shouldNotInputExtraLongCVV() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genLongCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.extraInputCheck("cvv", DataHelper.genLatinStr(4, 10), 3);

    }

    @Test
    @DisplayName("Should not input special chars  in field 'CVV'")
    void shouldNotInputSpecCharsInCVVField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("cvv", DataHelper.genSpeсChars());
    }

    @Test
    @DisplayName("Should not input latin chars  in field 'CVV'")
    void shouldNotInputLatinCharsInCVVField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("cvv", DataHelper.genLatinStr(1, 10));
    }

    @Test
    @DisplayName("Should not input cirill text  in field 'CVV'")
    void shouldNotInputCirillCharsInCVVField() {
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.noInputCheck("cvv", DataHelper.genCyrillStr());
    }

    /* API Тесты */

    @Test
    @DisplayName("Success buy with APPROVED Card via API (NoSetup)")
    void shouldSuccessWithApprovedCardApi() {
        ApiHelper.successSend(DataHelper.genCard(APPROVED_CARD));
        assertEquals("APPROVED", SQLHelper.getStatus());
        ;
    }

    @Test
    @DisplayName("Failed buy with DECLINED Card via API (NoSetup)")
    void shouldFailWithDeclinedCardApi() {

        ApiHelper.failSend(DataHelper.genCard(DECLINED_CARD));
        assertEquals("DECLINED", SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Failed buy with random card via API (NoSetup)")
    void shouldFailWithRndCardApi() {
        ApiHelper.sendCard(DataHelper.genCard(DataHelper.genRndCardNum()));
    }

    @Test
    @DisplayName("Should respond 'Bad request' with  too long 'year' API (NoSetup)")
    void shouldFailWithLongYearApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genDigits(3, 8), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'year' API (NoSetup)")
    void shouldFailWithShortYearApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genDigits(1, 1), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'year' API (NoSetup)")
    void shouldFailWithSpecCharYearApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genSpeсChars(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'year' API (NoSetup)")
    void shouldFailWithLatinCharYearApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genLatinStr(2, 2), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'year' API (NoSetup)")
    void shouldFailWithCirillCharYearApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                DataHelper.genCyrillStr(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with  too long 'month' API (NoSetup)")
    void shouldFailWithLongMonthApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genDigits(3, 8),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'month' API (NoSetup)")
    void shouldFailWithShortMonthApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genDigits(1, 1),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'month' API (NoSetup)")
    void shouldFailWithSpecCharMonthApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genSpeсChars(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'month' API (NoSetup)")
    void shouldFailWithLatinCharMonthApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genLatinStr(2, 2),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'month' API (NoSetup)")
    void shouldFailWithCirillCharMonthApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), DataHelper.genCyrillStr(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with  too long 'cvc' API (NoSetup)")
    void shouldFailWithLongCvcApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genLongCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'сvc' API (NoSetup)")
    void shouldFailWithShortCvcApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genShortCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'сvc' API (NoSetup)")
    void shouldFailWithSpecCharCvcApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genSpeсChars());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'сvc' API (NoSetup)")
    void shouldFailWithLatinCharCvcApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genLatinStr(2, 2));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'сvc' API (NoSetup)")
    void shouldFailWithCirillCharCvcApi() {
        var date = DataHelper.genDate(true);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genCyrillStr());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should decline with APPROVED Card and outdated 'year' via API (NoSetup)")
    void shouldFailWithApprovedCardAndBadYearApi() {
        var date = DataHelper.genDate(false);
        var card = new DataHelper.Card(
                APPROVED_CARD,
                date.getYear(), date.getMonth(),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        ApiHelper.failSend(card);
        assertEquals("DECLINED", SQLHelper.getStatus());
    }


}
