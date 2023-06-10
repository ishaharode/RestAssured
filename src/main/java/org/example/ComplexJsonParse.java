package org.example;
import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParse {
    public static void main(String [] args){
        JsonPath js = new JsonPath(payload.coursePrice());

        //1. Print No of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);

        //2.Print Purchase Amount
        int purchaseAmt= js.getInt("dashboard.purchaseAmount");
        System.out.println(purchaseAmt);

        //3. Print Title of the first course
        String courseTitle = js.get("courses[0].title");
        System.out.println(courseTitle);


        //4. Print All course titles and their respective Prices
        for(int i = 0;i<count;i++) {
           String title = js.get("courses["+i+"].title");
           String price = js.get("courses["+i+"].price").toString();
           System.out.println(title +"  "+price);
        }

        //5. Print no of copies sold by RPA Course
        for(int i = 0;i<count;i++) {
            String title = js.get("courses["+i+"].title");
            if(title.equals("RPA")) {
                String Copies = js.get("courses[" + i + "].copies").toString();
                System.out.println("no of copies sold by RPA Course"+title + "  " + Copies);
                break;
            }
        }

        //6. Verify if Sum of all Course prices matches with Purchase Amount




    }

}
