package org.example;

import files.jsonParser;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    //Add Book
    @Test(dataProvider = "BooksData")
    public void addBook(String aisle, String isbn){
        RestAssured.baseURI="http://216.10.245.166";
        String Response = given().log().all().header("Content-Type","application/json").
                body(payload.AddBookAPI(aisle,isbn)).
                when().post("/Library/Addbook.php").
                then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = jsonParser.parseJson(Response);
        String ID = js.getString("ID");
        System.out.println(ID);
    }
    //to avoid redundancy
    // 1. parameterised with random unique data
    // 2. delete the same data after the test to use the same again.

    //DataProvider
    @DataProvider(name = "BooksData")
    public Object[][] getData(){
        //array --collection of elements
        //multidimentional array -- collection of arrays.

        return new Object[][] {{"512","qaz"},{"851","wert"},{"753","rgdt"}};
    }


    //Delete Book to avoid error
    @Test(dataProvider = "BooksData")
    public void deleteBook(String aisle, String isbn){
        String ID = isbn + aisle;
        RestAssured.baseURI="http://216.10.245.166";
        String Response = given().log().all().header("Content-Type","application/json").
                body(payload.deleteBookAPI(ID)).
                when().post("/Library/DeleteBook.php").
                then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = jsonParser.parseJson(Response);
        String message = js.getString("msg");
        System.out.println(ID + "   " + message);

    }
}
