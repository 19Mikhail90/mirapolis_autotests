package page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class LoginPage {
    public LoginPage() {
    }

    public SelenideElement loginInput = $("[name=user]");
    public SelenideElement passwordInput = $("[name=password]");
    public SelenideElement buttonActionLogin = $("#button_submit_login_form");
    public static SelenideElement checkAvatar = $(".avatar-full-name");
    public SelenideElement errorNotification = $(".info-title");


    public void login (String login, String password) {
        loginInput.setValue(login);
        passwordInput.setValue(password);
        buttonActionLogin.click();
    }

    public void errorIncorrectFormData(){
        getWebDriver().switchTo().alert().getText().contains("Неверные данные для авторизации");
        getWebDriver().switchTo().alert().accept();
        String error = errorNotification.getText();
        String errorForm = "Вход в систему";
        Assertions.assertEquals(error, errorForm);

    }
    public static void CheckAvatar (String name){
        String assertName = checkAvatar.getText();
        Assertions.assertEquals(name, assertName);
    }

}