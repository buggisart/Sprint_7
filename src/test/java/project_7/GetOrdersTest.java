package project_7;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    @Test
    @Description("Получение списка ордеров")
    public void getOrdersSuccess() {
        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getAllOrders();
        response.assertThat().body("orders", notNullValue()).statusCode(200);
    }
}
