package project_7;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetOrders extends RestClient {

    private static final String GET_ORDERS_PATH = "api/v1/orders";

    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_ORDERS_PATH)
                .then();
    }
}
