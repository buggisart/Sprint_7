package project_7;

import com.google.gson.Gson;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

public class OrderCreationTest {

    @DataProvider(name = "orderData")
    public Object[][] getOrderData() {
        return new Object[][]{
                {new OrderData("HIn","uga","Konoha, 3 apt.", "1","+7 810 355 35 35", 3, "2021-06-10","I'm always cheering for you", new String[]{"BLACK,GREY"}), 201},
                {new OrderData("Test","tesg","Konoha, 5 apt.", "4","+7 860 355 35 35", 4, "2023-06-10","always cheering for you", new String[]{"BLACK"}), 201},
                {new OrderData("Hata","Hga","Konoha, 1 apt.", "2","+7 820 355 35 35", 2, "2022-06-10","Naruto", new String[]{"GREY"}), 201},
                {new OrderData("Hinata","Hyuga","Konoha, 23 apt.", "3","+7 800 355 35 35", 1, "2020-06-10","Naruto, I'm always cheering for you", new String[]{""}), 201},
        };
    }

    @Test(dataProvider = "orderData")
    @Description("Создание ордера")
    public void testCreateOrder(OrderData orderData, int expectedStatus) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(orderData);
        CreateOrder createOrder = new CreateOrder();
        ValidatableResponse response = createOrder.createOrder(jsonBody);
        response.body("track", not(empty()));
        int actualStatus = response.extract().statusCode();
        assertEquals(expectedStatus,actualStatus);
    }

}
