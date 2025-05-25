package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.Card;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BuyPage {
    private SelenideElement number = $("[placeholder^='0000']");
    private SelenideElement month = $("[placeholder='08']");
    private SelenideElement year = $("[placeholder='22']");
    private SelenideElement cvv = $("[placeholder='999']");
    private SelenideElement holder = $("input:not([placeholder])");
    private SelenideElement sendButton = $("div.form-field .button");

    private SelenideElement numberSub = number.parent().sibling(0);
    private SelenideElement monthSub = month.parent().sibling(0);
    private SelenideElement yearSub = year.parent().sibling(0);
    private SelenideElement cvvSub = cvv.parent().sibling(0);
    private SelenideElement holderSub = holder.parent().sibling(0);

    private SelenideElement successMsg = $("div.notification_status_ok");
    private SelenideElement errorMsg = $("div.notification_status_error");
    private SelenideElement errorClose = errorMsg.$(".icon_name_close");
    private SelenideElement successClose = successMsg.$(".icon_name_close");

    Map<String, SelenideElement> elements = Map.of("numberSub", numberSub, "monthSub", monthSub,
            "yearSub", yearSub, "holderSub", holderSub, "cvvSub", cvvSub, "number", number,
            "month", month, "year", year, "cvv", cvv, "holder", holder);

    public BuyPage() {
        SelenideElement[] elementsToVerify = {number, month, year, cvv, holder, sendButton};
        for (SelenideElement element : elementsToVerify) {
            element.shouldBe(visible);
        }
    }


    public void formFill(Card card) {
        number.setValue(card.getNumber());
        month.setValue(card.getMonth());
        year.setValue(card.getYear());
        cvv.setValue(card.getCvc());
        holder.setValue(card.getHolder());
    }

    public void emptyFieldCheck(Card card, String fieldSub) {
        formFill(card);
        sendButton.click();
        elements.get(fieldSub)
                .shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    public void badFormatCheck(Card card, String fieldSub) {
        formFill(card);
        sendButton.click();
        elements.get(fieldSub)
                .shouldBe(visible).shouldHave(text("Неверный формат"));
    }

    public void noInputCheck(String field, String input) {
        elements.get(field).setValue(input)
                .shouldBe(empty);
    }

    public void lengthCheck(Card card, String field, String fieldSub, int maxLength) {
        formFill(card);
        int fieldValue = elements.get(field).shouldHave((attribute("value"))).getValue().length();
        assertThat(fieldValue).isNotNull().isEqualTo(maxLength);
        sendButton.click();
        elements.get(fieldSub)
                .shouldNot(visible);
    }

    public void monthInvalidCheck(Card card) {
        formFill(card);
        sendButton.click();
        monthSub.shouldBe(visible).shouldHave(text("Неверно указан срок действия карты"));
    }

    public void yearInvalidCheck(Card card) {
        formFill(card);
        sendButton.click();
        yearSub.shouldBe(visible).shouldHave(text("Истёк срок действия карты"));
    }

    public void validBuy(Card card) {
        formFill(card);
        sendButton.click();
        successCheck();
    }

    public void invalidBuy(Card card) {
        formFill(card);
        sendButton.click();
        errorCheck();
    }

    /* В этих проверках пришлось сделать столько задержек, поскольку тесты ловили баг через-раз*/
    private void errorCheck() {
        errorMsg.shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(2));
        errorClose.click();
        errorMsg.shouldNotBe(visible, Duration.ofSeconds(2));
        successMsg.shouldNotBe(visible, Duration.ofSeconds(2));
    }

    private void successCheck() {
        successMsg.should(visible, Duration.ofSeconds(10))
                .shouldHave(text("Операция одобрена Банком."), Duration.ofSeconds(2));
        successClose.click();
        successMsg.shouldNotBe(visible, Duration.ofSeconds(2));
        errorMsg.shouldNotBe(visible, Duration.ofSeconds(2));
    }
}
