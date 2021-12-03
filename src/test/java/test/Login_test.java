package test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import page.LoginPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

public class Login_test {
    @BeforeEach
    void beforeEach() {
        open("https://lmslite47vr.demo.mirapolis.ru/mira");
    }

    @AfterEach
    public void addAttachments() {
        Selenide.closeWebDriver();
    }

    public String validLogin() {
        return "fominaelena";
    }

    public String validPass() {
        return "1P73BP4Z";
    }

    public String invalidLogin() {
        return "NEfominaelena";
    }

    public String invalidPass() {
        return "qwert1234";
    }

    public String emptyLogin() {
        return "";
    }

    public String emptyPass() {
        return "";
    }

    public String longPass150() {
        return "Тестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случай";
    }

    public String longPass151() {
        return "1Тестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случайТестовый_случай";
    }

    LoginPage loginPage = new LoginPage();

    @Test
    @DisplayName("Вход в систему с корректными данными")
    void shouldAutoValidData() {
        step("Ввод корректного логина и пароля", () -> {
            loginPage.login(validLogin(), validPass());
        });
        step("Проверка успешной авторизации", () -> {
            LoginPage.CheckAvatar("Фомина Елена Сергеевна");
        });
    }

    @Test
    @DisplayName("Вход в систему с некорректным паролем")
    void shouldAutoInvalidPassword() {
        step("Ввод корректного логина и не неверного пароля", () -> {
            loginPage.login(validLogin(), invalidPass());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Вход в систему с неверным логином")
    void shouldAutoInvalidLogin() {
        step("Ввод неверного логина и корректного пароля", () -> {
            loginPage.login(invalidLogin(), validPass());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Вход в систему только с логином")
    void shouldAutoWithoutPassword() {
        step("Ввод только логина", () -> {
            loginPage.login(invalidLogin(), emptyPass());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();

        });
    }

    @Test
    @DisplayName("Вход в систему только с паролем")
    void shouldAutoWithoutLogin() {
        step("Ввод только пароля", () -> {
            loginPage.login(emptyLogin(), validPass());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Вход в систему без ввода данных")
    void shouldAutoWithoutData() {
        step("Ничего не вводим, нажимаем Войти ", () -> {
            loginPage.login(emptyLogin(), emptyPass());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Восстановления пароля")
    void shouldPasswordRecoveryCheck() {
        step("Нажимаем кнопку Забыли пароль?", () -> {
            $(".mira-default-login-page-link").click();
        });
        step("Проверяем страницу после перехода", () -> {
            $(".info-title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Восстановление пароля"));
        });
    }

    @Test
    @DisplayName("Ввод 150 символов в поле пароль")
    void shouldInvalidNumberOfCharactersInPassword_150() {
        step("Ввод 150 символов в поле пароль", () -> {
            loginPage.login(validLogin(), longPass150());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Ввод 151 символов в поле пароль")
    void shouldInvalidNumberOfCharactersInPassword_151() {
        step("Ввод 151 символ в поле пароль", () -> {
            loginPage.login(validLogin(), longPass151());
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            getWebDriver().switchTo().alert().getText().contains("Логин или пароль слишком длинные");
            getWebDriver().switchTo().alert().accept();
            String error = loginPage.errorNotification.getText();
            String errorForm = "Вход в систему";
            Assertions.assertEquals(error, errorForm);
        });
    }

    @Test
    @DisplayName("Ввод только пробелов в форму авторизации")
    void shouldInputSpaces() {
        step("Ввод пробелов в логин и пароль", () -> {
            loginPage.login(" ", " ");
        });
        step("Проверяем модальное окно и страницу после закрытия", () -> {
            loginPage.errorIncorrectFormData();
        });
    }

    @Test
    @DisplayName("Работа функциональности Скрыть/Раскрыть пароль")
    void shouldHideRevealPassword() {
        step("Ввод пароля", () -> {
            loginPage.passwordInput.setValue(validPass());
            $("[id=show_password]").click();
        });
        step("Проверяем отображения пароля(цвета бордюра кнопки, по CSS атрибуту)", () -> {
            String par = $("[id=show_password]").getCssValue("border");
            Assertions.assertEquals( "1px solid rgb(125, 141, 145)", par);
        });
    }
    @Test
    @DisplayName("Отображение текста стартовой страницы")
    void shouldVisibleStartPage() {
        step("Проверка отображения текста стартовой страницы ", () -> {
            $("[class=mira-page-login-right-side-content-title]").shouldBe(visible).shouldHave(exactText("Добро пожаловать в систему дистанционного обучения Mirapolis LMS!"));
        });
    }
}