package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {
    SelenideElement buyButton = $("button:not(.button_view_extra)");
    SelenideElement creditButton = $("button.button_view_extra");

    public StartPage() {
        buyButton.shouldBe(visible);
        creditButton.shouldBe(visible);
    }

    public BuyPage clickBuy() {
        buyButton.click();
        return new BuyPage();
    }
}



