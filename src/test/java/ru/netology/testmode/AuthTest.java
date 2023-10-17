package ru.netology.testmode;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.DataHelper.Registration.getAuthInfo;
import static ru.netology.testmode.DataHelper.Registration.getRegisteredAuthInfo;
import static ru.netology.testmode.DataHelper.getLogin;
import static ru.netology.testmode.DataHelper.getPassword;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void successAuthorizationWithRegistratedUser() {//успешная авторизация зарегистрированного клиента
        var registeredUser = getRegisteredAuthInfo("active");
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("#root").shouldBe(visible)
                .shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    void errorWithNotRegistratedUser() { //ошибка при отправке данных незарегистрированного клиента
        var notRegisteredUser = getAuthInfo("active");
        $("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(visible, Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void invalidLogin() { //неправильно введенный логин
        var registeredUser = getRegisteredAuthInfo("active");
        var invalidLogin = getLogin();
        $("[data-test-id=login] .input__control").setValue(invalidLogin);
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void invalidPassword() { //неправильно введенный пароль
        var registeredUser = getRegisteredAuthInfo("active");
        var invalidPassword = getPassword();
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(invalidPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void blockedUser() { //заблокированный пользователь
        var blockedUser = getRegisteredAuthInfo("blocked");
        $("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible)
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

}
