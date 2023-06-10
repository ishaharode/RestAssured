import files.jsonParser;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String [] args){
        //Post API

        //given
        //when
        //Then

        RestAssured.baseURI= "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.AddPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope",equalTo("APP")).header("server","Apache/2.4.41 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);
        JsonPath js = new JsonPath(response); //parsing json
        String placeId= js.getString("place_id");
        System.out.println(placeId);

        // Update place with address----PUT place API
        String newAddress= "70 Summer walk, USA";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body(payload.UpdatePlace(placeId,newAddress)).when().put("/maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        // Get Place Api to validate the updated address
        String GetResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).header("Content-Type","application/json")
                .when().get("/maps/api/place/get/json")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath getjs = jsonParser.parseJson(GetResponse); //parsing json
        String updtAddress= getjs.getString("address");
        System.out.println(updtAddress);
        Assert.assertEquals(newAddress,updtAddress);

    }
}
