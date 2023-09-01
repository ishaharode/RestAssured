package Pojo;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class SerializeTest {
    public static void main(String[] args){

        RestAssured.baseURI = "https://rahulshettyacademy.com";

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

        Response res = given().log().all().queryParam("key","qaclick123").body(ap).when().log().all().post(" /maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response();

        String responsestr = res.asString();
        System.out.println(responsestr);


    }



}
