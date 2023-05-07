package project_7;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    @Test
    public void getOrdersSuccess() {
        ValidatableResponse response = given()
                .contentType("application/JSON")
                .when()
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders")
                .then();
        response.assertThat().body("orders", notNullValue()).statusCode(200);

    }
}
