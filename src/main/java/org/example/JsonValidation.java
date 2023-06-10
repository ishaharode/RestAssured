package org.example;

import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonValidation {
    @Test
    public void CalculatePrice(){
        JsonPath js = new JsonPath(payload.coursePrice());
        int count = js.getInt("courses.size()");
        int purchaseAmt= js.getInt("dashboard.purchaseAmount");
        int totalCalculatedPrice = 0;
        for(int i = 0;i<count;i++) {
            int price = js.get("courses["+i+"].price");
            int Copies = js.get("courses["+i+"].copies");
            totalCalculatedPrice = totalCalculatedPrice + (price*Copies);

        }
        System.out.println("calculated price amount ="+totalCalculatedPrice);
        Assert.assertEquals(purchaseAmt,totalCalculatedPrice);
    }

}
