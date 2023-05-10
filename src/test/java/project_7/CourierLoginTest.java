package project_7;

import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @Description("Позитивный кейс")
    public void courierLoginSuccess() {
        courier = CourierGenerator.getRandomWithAllParams();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse.assertThat().body("id",  greaterThanOrEqualTo(1)).statusCode(200);
    }
    @Test
    @Description("Логин курьера без логина")
    public void courierLoginNoParamError() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(CourierGenerator.getRandomWithoutLogin()));
        loginResponse.assertThat().body("message", equalTo("Недостаточно данных для входа")).body("code", equalTo(400)).statusCode(400);
    }
    @Test
    @Description("Логин курьера без пароля")
    public void courierPasswordNoParamError() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(CourierGenerator.getRandomWithoutPassword()));
        loginResponse.assertThat().body("message",  equalTo("Недостаточно данных для входа")).body("code", equalTo(400)).statusCode(400);
    }
    @Test
    @Description("Логина курьера с неправильными кредами")
    public void courierCredentialsIncorrectError() {
        CourierCredentials courierCredentials = new CourierCredentials("f@ы", "тест");
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        loginResponse.assertThat().body("message",  equalTo("Учетная запись не найдена")).body("code", equalTo(404)).statusCode(404);
    }


    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
