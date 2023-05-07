package project_7;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import porject_7.Courier;
import porject_7.CourierClient;
import porject_7.CourierCredentials;
import porject_7.CourierGenerator;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CreateCourierTest {

    private CourierClient courierClient;
    private int courierId;
    private Courier courier;
    @Before
    public void setUp() {
        courierClient = new CourierClient();
//        RestAssured.filters(new ResponseLoggingFilter(LogDetail.ALL));
    }

    @Test
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
    public void createCourierWithoutPasswordError() {

        // Тестовые данные

        courier = CourierGenerator.getRandomWithoutPassword();
        // Вызвать эндпоинт и записать ответ создания курьера
        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).body("code", equalTo(400)).statusCode(400);

    }

    @Test
    public void createCourierWithoutLoginError() {

        courier = CourierGenerator.getRandomWithoutLogin();
        // Тестовые данные
        // Вызвать эндпоинт и записать ответ создания курьера
        ValidatableResponse response = courierClient.create(courier);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).body("code", equalTo(400)).statusCode(400);
    }
    @Test
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
    public void createTheSameLoginCourierError() {
        // Тестовые данные
        Courier courierFirst = new Courier("LoginTesttt","pass", "name");
        // Вызвать эндпоинт создания курьера
        ValidatableResponse response = courierClient.create(courierFirst);

        // Проверить статус код ответа и боди ответа
        response.assertThat().body("ok", equalTo(true)).statusCode(201);
        // Проверить что курьер реально создался
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierFirst));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id", greaterThanOrEqualTo(1));

        // Проверить что нельзя создать курьера с таким же логином
        ValidatableResponse responseForTheSameLogin = courierClient.create(new Courier("LoginTesttt", "NewPass", "newName"));
        responseForTheSameLogin.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).body("code", equalTo(409)).statusCode(409);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
