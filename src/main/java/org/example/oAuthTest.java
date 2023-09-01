package org.example;

import Pojo.Api;
import Pojo.GetCourseDeSerialization;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class oAuthTest {
    public static void main(String[] args) throws InterruptedException {

        //to hit the URL in browser to get the code
        // Gmail does not allow automation to perform login. Manually perform the below step and get the URL directly.

//        System.setProperty("webdriver.chrome.driver","C:\\Users\\ishah\\Workspace\\chromedriver_win32");
//        WebDriver driver =new ChromeDriver();
//        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//        driver.findElement(By.xpath("//input[@type =\"email\" and @class=\"whsOnd zHQkBf\"]")).sendKeys("Username");
//        driver.findElement(By.xpath("//input[@type =\"email\" and @class=\"whsOnd zHQkBf\"]")).sendKeys(Keys.ENTER);
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Password");
//        driver.findElement(By.xpath("//input[@type =\"email\" and @class=\"whsOnd zHQkBf\"]")).sendKeys(Keys.ENTER);
//        Thread.sleep(1000);
//        String url = driver.getCurrentUrl();

        String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0Adeu5BXtxuuRFQH7NDLlAyEZtGwWuFUVWyazLaDmN5Qah0oG69Ocw4ZioHTkK6TXkNg-cQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none";
        String partialString = url.split("=")[1];
        String code = partialString.split("&scope")[0];
        System.out.println(code);


        //use the code to get the access token
        String getAccessToken = given().urlEncodingEnabled(false).queryParams("code",code)
                .queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .queryParam("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type","authorization_code").header("Content-Type","application/json")
                .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath js = new JsonPath(getAccessToken);
        String accessToken = js.getString("access_token");


        //use token to get the user details

//        String getResponse = given().queryParam("access_token",accessToken).header("Content-Type","application/json")
//                .when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
//        System.out.println(getResponse);

        //get user details using deserialization
        GetCourseDeSerialization gc = given().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourseDeSerialization.class);
        System.out.println(gc.getLinkedIn());
        System.out.println(gc.getInstructor());
        System.out.println(gc.getCourses().getApi().get(1).getCourseTitle()); // do not rely on indexes. iterate the whole loop and get the price based on course name

        List<Api> apiCourselst = gc.getCourses().getApi();
        for(int i =0; i<apiCourselst.size();i++)
        {
            if(apiCourselst.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java"))
            {
                System.out.println(apiCourselst.get(i).getPrice());
            }
        }

        String [] courseTitle ={"Selenium Webdriver Java","Cypress","Protractor"};
        //get the courses name of webautomation
        List<Pojo.Webautomation> webautomationlst = gc.getCourses().getWebAutomation();
        ArrayList<String> webAutoCourses = new ArrayList<String>();

        for(int j =0; j<webautomationlst.size();j++)
        {
            webAutoCourses.add(webautomationlst.get(j).getCourseTitle());
            System.out.println(webautomationlst.get(j).getCourseTitle());
            System.out.println(webautomationlst.get(j).getPrice());
        }

        List<String> expectedlst = Arrays.asList(courseTitle);
        Assert.assertTrue(webAutoCourses.equals(expectedlst));

    }
}
