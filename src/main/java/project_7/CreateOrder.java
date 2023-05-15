package project_7;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class CreateOrder extends RestClient {
    private static final String CREATE_ORDER_PATH = "api/v1/orders";

    public ValidatableResponse createOrder(String jsonBody) {
        return given()
                .spec(getBaseSpec())
                .body(jsonBody)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();

    }
}
