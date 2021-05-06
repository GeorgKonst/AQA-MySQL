package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import javax.xml.crypto.Data;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");

    public void setValue(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
    }
    public VerificationPage validAuth(DataHelper.AuthInfo info) {
        setValue(info);
        return new VerificationPage();
    }

    public void invalidAuth(DataHelper.AuthInfo info) {
        setValue(info);
        errorMessage.shouldBe(Condition.visible);
    }

    public void sendInvalidPassword(String password) {
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        passwordField.doubleClick().sendKeys(Keys.DELETE);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public void sendInvalidPasswordThirdTime(String password) {
        sendInvalidPassword(password);
        sendInvalidPassword(password);
        loginButton.shouldBe(Condition.disabled);
    }
}