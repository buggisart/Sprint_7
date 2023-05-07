package project_7;

import com.google.gson.Gson;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import porject_7.OrderData;
import io.restassured.RestAssured;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @DataProvider(name = "orderData")
    public Object[][] getOrderData() {
        return new Object[][]{
                {new OrderData()
                {{
                    firstName = "Naruto";
                    lastName = "Uchiha";
                    address = "Konoha, 142 apt.";
                    metroStation = 4;
                    phone = "+7 800 355 35 35";
                    rentTime = 5;
                    deliveryDate = "2020-06-06";
                    comment = "Saske, come back to Konoha";
                    color = new String[]{"BLACK"};
                }}, "BLACK"},
                {new OrderData() {{
                    firstName = "Sakura";
                    lastName = "Haruno";
                    address = "Konoha, 15 apt.";
                    metroStation = 8;
                    phone = "+7 800 355 35 35";
                    rentTime = 10;
                    deliveryDate = "2020-06-08";
                    comment = "Naruto, I believe in you";
                    color = new String[]{"BLACK", "GREY"};
                }}, "BLACK,GREY"},
                {new OrderData() {{
                    firstName = "Hinata";
                    lastName = "Hyuga";
                    address = "Konoha, 23 apt.";
                    metroStation = 1;
                    phone = "+7 800 355 35 35";
                    rentTime = 3;
                    deliveryDate = "2020-06-10";
                    comment = "Naruto, I'm always cheering for you";
                    color = new String[]{};
                }}, ""},
        };
    }

    @Test(dataProvider = "orderData")
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
