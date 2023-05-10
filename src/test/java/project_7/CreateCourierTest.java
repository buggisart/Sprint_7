package project_7;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CreateCourierTest {

    private CourierClient courierClient;
    private int courierId;
    private Courier courier;
    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @Description("Создание курьера с корректными данными")
    public void createCourierWithAllParamsSuccess() {
        // Тестовые данные
        courier = CourierGenerator.getRandomWithAllParams();

        // Вызвать эндпоинт создания курьера
        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("ok", equalTo(true)).statusCode(201);
        // Проверить что курьер реально создался
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id", greaterThanOrEqualTo(1));

    }

    @Test
    @Description("Созданеи идентичного курьера")
    public void createTheSameCourierError() {

        // Тестовые данные
        courier = CourierGenerator.getRandomWithAllParams();
        // Вызвать эндпоинт и записать ответ ручки создания курьера
        ValidatableResponse response = courierClient.create(courier);



        // Проверить статус код ответа и боди ответа
        response.assertThat().body("ok", equalTo(true)).statusCode(201);

        // Проверить что курьер реально создался
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id", greaterThanOrEqualTo(1));

        // Проверить что снова создать нельзя такого же курьера
        ValidatableResponse responseOfSecondRequest = courierClient.create(courier);
        responseOfSecondRequest.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).body("code", equalTo(409)).statusCode(409);
    }

    @Test
    @Description("Создание курьера без пароля")
    public void createCourierWithoutPasswordError() {

        // Тестовые данные

        courier = CourierGenerator.getRandomWithoutPassword();
        // Вызвать эндпоинт и записать ответ создания курьера
        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).body("code", equalTo(400)).statusCode(400);

    }

    @Test
    @Description("Создание курьера без логина")
    public void createCourierWithoutLoginError() {

        courier = CourierGenerator.getRandomWithoutLogin();
        // Тестовые данные
        // Вызвать эндпоинт и записать ответ создания курьера
        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).body("code", equalTo(400)).statusCode(400);
    }
    @Test
    @Description("Создание курьера только с обязательными параметрами")
    public void createCourierWithRequiredParams() {

        courier = CourierGenerator.getRandomWithRequiredParams();

        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("ok", equalTo(true)).statusCode(201);

        // Проверить что курьер реально создался
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id", any(int.class));
    }

    @Test
    @Description("Создание двух курьеров с одинаковым логином, но разными паролями и именами")
    public void createTheSameLoginCourierError() {
        // Тестовые данные
        Courier courierFirst = new Courier(CourierGenerator.getRandomWithRequiredParams().getLogin(), CourierGenerator.getRandomWithRequiredParams().getPassword(),
        CourierGenerator.getRandomWithRequiredParams().getFirstName());

        // Вызвать эндпоинт создания курьера
        ValidatableResponse response = courierClient.create(courierFirst);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("ok", equalTo(true)).statusCode(201);
        // Проверить что курьер реально создался
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierFirst));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id", greaterThanOrEqualTo(1));

        // Проверить что нельзя создать курьера с таким же логином
        ValidatableResponse responseForTheSameLogin = courierClient.create(new Courier(courierFirst.getLogin(),CourierGenerator.getRandomWithRequiredParams().getPassword(),
                CourierGenerator.getRandomWithRequiredParams().getFirstName()));
        responseForTheSameLogin.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).body("code", equalTo(409)).statusCode(409);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
