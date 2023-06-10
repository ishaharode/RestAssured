package files;

import io.restassured.path.json.JsonPath;

public class jsonParser {
    public static JsonPath parseJson(String reponse){
        JsonPath js = new JsonPath(reponse);
        return js;
    }
}
