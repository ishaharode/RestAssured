package org.example;

import Pojo.EcomLoginReq;
import Pojo.EcomLoginResponse;
import Pojo.OrderDetail;
import Pojo.Orders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceApiTest {
    public static void main(String[] args){

        //Login Ecommerce api  with spec builder, serialize and deserialize concept
        //Bypass SSL certification

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

        EcomLoginReq loginPojo = new EcomLoginReq();
        loginPojo.setUserEmail("ishaharode207@gmail.com");
        loginPojo.setUserPassword("Test@2023");
        RequestSpecification loginReq = given().relaxedHTTPSValidation().log().all().spec(reqSpec).body(loginPojo);
        EcomLoginResponse loginResponse = loginReq.when().post("/api/ecom/auth/login").then().log().all().extract().as(EcomLoginResponse.class);

        String Token = loginResponse.getToken();
        String userID = loginResponse.getUserId();

        System.out.println(loginResponse.getToken());
        System.out.println(loginResponse.getUserId());

        //Create Order for Form Data Payload (No JSON)

        RequestSpecification reqSpec1 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",Token).build();
        RequestSpecification createProductReq =given().log().all().spec(reqSpec1).param("productName","Dressberry").param("productAddedBy",userID).param("productCategory","fashion")
                .param("productSubCategory","longTop").param("productPrice","11000").param("productDescription","Originals")
                .param("productFor","Women").multiPart("productImage",new File("C://Users//ishah//Postman//files//productImage_1.jpg"));

        String CreateProductRes = createProductReq.when().log().all().post("/api/ecom/product/add-product").then().log().all().assertThat().statusCode(201).extract().response().asString();
        JsonPath js = new JsonPath(CreateProductRes);
        String productID = js.get("productId");
        System.out.println(js.getString("message"));

        //Create Order-------payload is json Array

        RequestSpecification reqSpec2 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",Token).setContentType(ContentType.JSON).build();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("Argentina");
        orderDetail.setProductOrderedId(productID);

        List<OrderDetail> orderdetailList = new ArrayList<OrderDetail>();
        orderdetailList.add(orderDetail);


        Orders order = new Orders();
        order.setOrders(orderdetailList);

       RequestSpecification createOrder = given().log().all().spec(reqSpec2).body(order);
        String orderRes = createOrder.when().post("/api/ecom/order/create-order").then().log().all().assertThat().statusCode(201).extract().response().asString();

        System.out.println(orderRes);


        //Delete Order
        RequestSpecification deleteOrderReq = given().log().all().spec(reqSpec2).pathParams("productID",productID);
        String DeleteResponse = deleteOrderReq.when().delete("/api/ecom/product/delete-product/{productID}").then().log().all().extract().response().asString();
        JsonPath js1 = new JsonPath(DeleteResponse);
        System.out.println(js1.getString("message"));






    }
}
