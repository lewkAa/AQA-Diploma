package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;


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

    private SelenideElement success = $("div.notification_status_ok");
    private SelenideElement fail = $("div.notification_status_error");
    private SelenideElement failX = fail.$(".icon_name_close");
    private SelenideElement successX = success.$(".icon_name_close");

    Map<String, SelenideElement> elements = Map.of("numberSub", numberSub, "monthSub", monthSub,
            "yearSub", yearSub, "holderSub", holderSub, "cvvSub", cvvSub, "number", number,
            "month", month, "year", year, "cvv", cvv, "holder", holder);

    Map<String, String> messages = Map.of(
            "format", "Неверный формат",
            "empty", "Поле обязательно для заполнения",
            "outdated", "Истёк срок действия карты",
            "expiration", "Неверно указан срок действия карты",
            "fail", "Ошибка! Банк отказал в проведении операции.",
            "success", "Операция одобрена Банком.");


    public BuyPage() {
        SelenideElement[] elementsToVerify = {number, month, year, cvv, holder, sendButton};
        for (SelenideElement element : elementsToVerify) {
            element.shouldBe(visible);
        }
    }

    public void formFill(DataHelper.Card card) {
        number.setValue(card.getNumber());
        month.setValue(card.getMonth());
        year.setValue(card.getYear());
        cvv.setValue(card.getCvc());
        holder.setValue(card.getHolder());
        sendButton.click();
    }

    public void fieldErrorCheck(DataHelper.Card card, String fieldSub, String msgType) {
        formFill(card);
        elements.get(fieldSub)
                .shouldBe(visible).shouldHave(text(messages.get(msgType)));
    }


    public void noInputCheck(String field, String input) {
        elements.get(field).setValue(input).shouldBe(empty);
    }

    public void extraInputCheck(String field, String input, int maxLength) {
        String expectedValue = input.substring(0, Math.min(input.length(), maxLength));
        elements.get(field)
                .setValue(input)
                .shouldHave(value(expectedValue));
    }


    public void errorCheck(boolean isSuccess) {
        SelenideElement message = isSuccess ? success : fail;
        SelenideElement oppositeMessage = isSuccess ? fail : success;
        SelenideElement closeButton = isSuccess ? successX : failX;
        String messageKey = isSuccess ? "success" : "fail";

        message.shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text(messages.get(messageKey)), Duration.ofSeconds(2));
        closeButton.click();
        message.shouldNotBe(visible, Duration.ofSeconds(2));
        oppositeMessage.shouldNotBe(visible, Duration.ofSeconds(2));
    }

}
