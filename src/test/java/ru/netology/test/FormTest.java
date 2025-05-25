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
        buyPg.validBuy(card);

        assertEquals("APPROVED", SQLHelper.getStatus()); // Проверка записи в БД.
    }



    @Test
    @DisplayName("Failed buy with DECLINED Card")
    void shouldFailWithDeclinedCard() {
        var card = DataHelper.genCard(DECLINED_CARD);
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.invalidBuy(card);

        assertEquals("DECLINED", SQLHelper.getStatus()); // Проверка записи в БД.
    }

    @Test
    @DisplayName("Failed buy with random Card")
    void shouldFailWithRandomCard() {
        var card = DataHelper.genCard(DataHelper.genRndCardNum());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.invalidBuy(card);
    }


    @Test
    @DisplayName("Should get error when send form with empty field 'number'")
    void shouldErrorWithEmptyNumberField() {
        var card = DataHelper.genCard(""); // Генерируем карту без номера
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.emptyFieldCheck(card, "numberSub");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'cvv'")
    void shouldErrorWithEmptyCvvField() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc("");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.emptyFieldCheck(card, "cvvSub");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'holder'")
    void shouldErrorWithEmptyHolderField() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setHolder("");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.emptyFieldCheck(card, "holderSub");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'month'")
    void shouldErrorWithEmptyMonthField() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth("");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.emptyFieldCheck(card, "monthSub");
    }

    @Test
    @DisplayName("Should get error when send form with empty field 'Year'")
    void shouldErrorWithEmptyYearField() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear("");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.emptyFieldCheck(card, "yearSub");
    }

    @Test
    @DisplayName("Should get error when send form with short field 'number'")
    void shouldErrorWithShortNumber() {
        var card = DataHelper.genCard(DataHelper.genShortCardNum());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "numberSub");
    }

    @Test
    @DisplayName("Should not input digits above max in field 'number'")
    void shouldNoExtraInputWithLongNumber() {
        var card = DataHelper.genCard(DataHelper.genLongCardNum());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.lengthCheck(card, "number", "numberSub", 19);
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
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genNumber(13, 99));
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.monthInvalidCheck(card);
    }

    @Test
    @DisplayName("Should get error when send form with '0'' in field 'month'")
    void shouldErrorWithZeroInMonth() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth("0");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "monthSub");

    }

    @Test
    @DisplayName("Should get error when send form with bad format in field 'month'")
    void shouldErrorWithBadFormatMonth() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genNumber(0, 9));
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "monthSub");

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
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear("0");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "yearSub");

    }

    @Test
    @DisplayName("Should get error when send form with bad format in field 'year'")
    void shouldErrorWithBadFormatYear() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genNumber(0, 9));
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "yearSub");

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
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genOutdateYear());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.yearInvalidCheck(card);

    }

    @Test
    @DisplayName("Should get error when send form with outdated in field 'month'")
    void shouldErrorWithOutdatedMonth() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.getCurrYear());
        card.setMonth(DataHelper.genInvalidMonth());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.monthInvalidCheck(card);
       /*Надо сказать, что данный тест не сработает, если сейчас текущий месяц январь.
        Этот тест в принципе невозможно провести в январе, поскольку условие выдачи ошибки это текущий год +
        прошедший месяц.*/
    }

    @Test
    @DisplayName("Should get error when send form with value below min length in field 'holder'")
    void shouldErrorWithTooShortYHolder() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setHolder(DataHelper.genLatinStr(1, 1));
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "holderSub");

    }

    @Test
    @DisplayName("Should not input value above max length in field 'holder'")
    void shouldNoExtraInputLongHolder() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setHolder(DataHelper.genLatinStr(31, 50));
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.lengthCheck(card, "holder", "holderSub", 30);
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
        buyPg.noInputCheck("holder", DataHelper.genDigits(1,10));
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
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc("0");
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "cvvSub");

    }

    @Test
    @DisplayName("Should get error when send form with short value in field 'CVV'")
    void shouldErrorWithShortCVV() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genShortCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.badFormatCheck(card, "cvvSub");

    }

    @Test
    @DisplayName("Should not input value above max length in field 'CVV'")
    void shouldNotInputExtraLongCVV() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genLongCVC());
        var startPg = new StartPage();
        var buyPg = startPg.clickBuy();
        buyPg.lengthCheck(card, "cvv", "cvvSub", 3);
        ;
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
        assertEquals("APPROVED", SQLHelper.getStatus());;
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
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genDigits(3,8));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'year' API (NoSetup)")
    void shouldFailWithShortYearApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genDigits(1,1));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'year' API (NoSetup)")
    void shouldFailWithSpecCharYearApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genSpeсChars());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'year' API (NoSetup)")
    void shouldFailWithLatinCharYearApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genLatinStr(2,2));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'year' API (NoSetup)")
    void shouldFailWithCirillCharYearApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genCyrillStr());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with  too long 'month' API (NoSetup)")
    void shouldFailWithLongMonthApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genDigits(3,8));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'month' API (NoSetup)")
    void shouldFailWithShortMonthApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genDigits(1,1));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'month' API (NoSetup)")
    void shouldFailWithSpecCharMonthApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genSpeсChars());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'month' API (NoSetup)")
    void shouldFailWithLatinCharMonthApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genLatinStr(2,2));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'month' API (NoSetup)")
    void shouldFailWithCirillCharMonthApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setMonth(DataHelper.genCyrillStr());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with  too long 'cvc' API (NoSetup)")
    void shouldFailWithLongCvcApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genLongCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with too short 'сvc' API (NoSetup)")
    void shouldFailWithShortCvcApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genShortCVC());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with special chars in 'сvc' API (NoSetup)")
    void shouldFailWithSpecCharCvcApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genSpeсChars());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with latin chars in 'сvc' API (NoSetup)")
    void shouldFailWithLatinCharCvcApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genLatinStr(2,2));
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should respond 'Bad request' with cirill chars in 'сvc' API (NoSetup)")
    void shouldFailWithCirillCharCvcApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setCvc(DataHelper.genCyrillStr());
        ApiHelper.sendCard(card);
    }

    @Test
    @DisplayName("Should decline with APPROVED Card and outdated 'year' via API (NoSetup)")
    void shouldFailWithApprovedCardAndBadYearApi() {
        var card = DataHelper.genCard(APPROVED_CARD);
        card.setYear(DataHelper.genOutdateYear());
        ApiHelper.failSend(card);
        assertEquals("DECLINED", SQLHelper.getStatus());
    }



}
