package project_7;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import org.junit.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @Test
    @Description("Получение списка ордеров")
    public void getOrdersSuccess() {
        ValidatableResponse response = given()
                .contentType("application/JSON")
                .when()
                .get(BASE_URL)
                .then();
        response.assertThat().body("orders", notNullValue()).statusCode(200);

    }
}
