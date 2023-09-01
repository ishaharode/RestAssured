package org.example;

import files.jsonParser;
import io.restassured.path.json.JsonPath;
import jdk.jfr.ContentType;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class GraphQLscrip {
public static void main(String[] args){

    //graphQl Query to get data
    String response = given().log().all().header("Content-Type","application/json").
            body("{\n" +
                    "  \"query\": \"query($charcterId: Int!, $episodeId: Int!)\\n{\\n  character(characterId: $charcterId) {\\n    name\\n    gender\\n    status\\n    id\\n  }\\n  location(locationId: 177) {\\n    name\\n    type\\n    dimension\\n  }\\n  episode(episodeId: $episodeId) {\\n    episode\\n    air_date\\n  }\\n  characters(filters: {name: \\\"Rahul\\\"}) {\\n    info {\\n      count\\n    }\\n    result\\n    {\\n      name\\n      type\\n    }\\n  }\\n}\\n\",\n" +
                    "  \"variables\": {\n" +
                    "    \"charcterId\": 168,\n" +
                    "    \"episodeId\": 1\n" +
                    "  }\n" +
                    "}").
            when().post("https://rahulshettyacademy.com/gq/graphql").
            then().assertThat().statusCode(200).extract().response().asString();

    JsonPath js = jsonParser.parseJson(response);
    String name = js.getString("data.character.name");
    System.out.println(name);


    //Mutation in graphQL to create data

    String mutation = given().log().all().header("Content-Type","application/json").
            body("{\n" +
                    "  \"query\": \"mutation($locationName:String!,$characterName:String!)\\n{\\n  createLocation(location:{name:$locationName,type:\\\"office\\\",dimension:\\\"254\\\"})\\n  {\\n    id\\n  }\\n  createCharacter(character:{name:$characterName,type:\\\"mocho\\\",status:\\\"alive\\\",species:\\\"human\\\",gender:\\\"female\\\",image:\\\"dan.png\\\",originId:177,locationId:177})\\n  {\\n    id\\n  }\\n  deleteLocations(locationIds:[150,151])\\n  {\\n    locationsDeleted\\n  }\\n}\",\n" +
                    "  \"variables\": {\n" +
                    "    \"locationName\": \"new york\",\n" +
                    "    \"characterName\": \"yoda\"\n" +
                    "  }\n" +
                    "}").
            when().post("https://rahulshettyacademy.com/gq/graphql").
            then().assertThat().statusCode(200).extract().response().asString();


    System.out.println(mutation);

}

}
