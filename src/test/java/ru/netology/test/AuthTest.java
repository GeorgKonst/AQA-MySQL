package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.AccountPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    DataHelper dataHelper = new DataHelper();

    @AfterAll
    public static void cleanTables() throws SQLException {
        DataHelper dataHelper = new DataHelper();
        dataHelper.cleanData();
    }

    @Test
    void shouldValidData() throws SQLException {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = dataHelper.getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        val codeVerify = dataHelper.getVerificationCode();
        verificationPage.validVerify(codeVerify);
        val accountPage = new AccountPage();
        accountPage.checkIfVisible();
    }

    @Test
    void shouldNotValidLogin() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = dataHelper.getInvalidLogin();
        loginPage.invalidAuth(authInfo);
    }

    @Test
    void shouldNotValidPassword() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = dataHelper.getInvalidPassword();
        loginPage.invalidAuth(authInfo);
    }

    @Test
    void shouldNotValidAuthCode() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = dataHelper.getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        verificationPage.invalidVerify(dataHelper.getInvalidVerificationCode());
    }

    @Test
    void shouldBlockWhenThreeInvalidPasswords() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = dataHelper.getInvalidPassword();
        loginPage.invalidAuth(authInfo);
        val invalidPassword = dataHelper.invalidPassword();
        loginPage.sendInvalidPassword(invalidPassword);
        loginPage.sendInvalidPasswordThirdTime(invalidPassword);

    }
}
