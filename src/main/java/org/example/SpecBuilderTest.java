package org.example;
import Pojo.AddPlace;
import Pojo.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.*;


public class SpecBuilderTest {
    public static void main(String[] args){

        AddPlace ap = new AddPlace();
        ap.setAccuracy(50);
        ap.setAddress("29, side layout, cohen 09");
        ap.setLanguage("French-IN");
        ap.setName("Frontline house");
        ap.setPhone_number("(+91) 983 893 3937");
        ap.setWebsite("http://google.com");

        List<String> T = new ArrayList<String>();
        T.add("shoe park");
        T.add("shop");
        ap.setTypes(T);

        Location L = new Location();
        L.setLat(-38.383494);
        L.setLng(33.427362);
        ap.setLocation(L);

       RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key","qaclick123").setContentType(ContentType.JSON).build();
       ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        Response res = given().spec(reqSpec).body(ap).when().log().all().post(" /maps/api/place/add/json")
                .then().spec(resSpec).extract().response();

        String responseStr = res.asString();
        System.out.println(responseStr);



    }
}
