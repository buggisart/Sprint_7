package project_7;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class    CourierClient extends RestClient {
    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_PATH = "api/v1/courier/login/";
    private static final String DELETE_COURIER_PATH = "api/v1/courier/{id}";



    public ValidatableResponse create (Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(credentials)
                .post(LOGIN_PATH)
                .then();

    }

    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .pathParam("id", id)
                .delete(DELETE_COURIER_PATH)
                .then();

    }
}
