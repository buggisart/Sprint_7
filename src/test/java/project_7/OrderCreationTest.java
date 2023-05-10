package project_7;

import com.google.gson.Gson;
import jdk.jfr.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @DataProvider(name = "orderData")
    public Object[][] getOrderData() {
        return new Object[][]{
                {new OrderData()
                {{
                    setFirstName("Naruto");
                    setLastName("Uchiha");
                    setAddress("Konoha, 142 apt.");
                    setMetroStation("4");
                    setPhone("+7 800 355 35 35");
                    setRentTime(5);
                    setDeliveryDate("2020-06-06");
                    setComment("Saske, come back to Konoha");
                    setColor(new String[]{"BLACK"});
                }}, "BLACK"},
                {new OrderData() {{
                    setFirstName("Sakura");
                    setLastName("Haruno");
                    setAddress("Konoha, 15 apt.");
                    setMetroStation("8");
                    setPhone("+7 800 355 35 35");
                    setRentTime(10);
                    setDeliveryDate("2020-06-08");
                    setComment("Naruto, I believe in you");
                    setColor(new String[]{"BLACK", "GREY"});
                }}, "BLACK,GREY"},
                {new OrderData() {{
                    setFirstName("Hinata");
                    setLastName("Hyuga");
                    setAddress("Konoha, 23 apt.");
                    setMetroStation("1");
                    setPhone("+7 800 355 35 35");
                    setRentTime(3);
                    setDeliveryDate("2020-06-10");
                    setComment("Naruto, I'm always cheering for you");
                    setColor(new String[]{});
                }}, ""},
        };
    }

    @Test(dataProvider = "orderData")
    @Description("Создание ордера")
    public void testCreateOrder(OrderData orderData, String expectedColor) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(orderData);

        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("track", not(empty()))
                .body("color", equalTo(expectedColor));
    }

}
