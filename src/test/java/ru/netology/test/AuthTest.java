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

    @AfterAll
    public static void cleanTables() throws SQLException {
        DataHelper.cleanData();
    }

    @Test
    void shouldValidData() throws SQLException {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        val codeVerify = DataHelper.getVerificationCode();
        verificationPage.validVerify(codeVerify);
        val accountPage = new AccountPage();
        accountPage.checkIfVisible();
    }

    @Test
    void shouldNotValidLogin() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidLogin();
        loginPage.invalidAuth(authInfo);
    }

    @Test
    void shouldNotValidPassword() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidAuth(authInfo);
    }

    @Test
    void shouldNotValidAuthCode() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        verificationPage.invalidVerify(DataHelper.getInvalidVerificationCode());
    }

    @Test
    void shouldBlockWhenThreeInvalidPasswords() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidAuth(authInfo);
        val invalidPassword = DataHelper.invalidPassword();
        loginPage.sendInvalidPasswordThirdTime(invalidPassword);

    }
}
