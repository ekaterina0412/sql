package ru.netology.bank.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.bank.data.DataHelper;
import ru.netology.bank.data.SQLHelper;
import ru.netology.bank.page.LoginPage;


import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.bank.data.SQLHelper.cleanAuthCode;
import static ru.netology.bank.data.SQLHelper.cleanDataBase;

public class BankAuthTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown1() {
        cleanAuthCode();
    }

    @AfterAll
    static void tearDown2() {
        cleanDataBase();
    }

    @BeforeEach
    void setUp() {
       loginPage = open("http://localhost:9999", LoginPage.class);
    }


    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from SUT test data")
    void shouldSuccessfullyLogin() {
        var authInfo = DataHelper.getAuthInfoWithTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifiVerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void RandomUser() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationIfLoginWithExistInBaseAndActiveUserAndRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifiVerificationPage();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}