package Specification;

import DataBase.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specification {
    private  static String URL =
            System.getenv().getOrDefault("URL", PropertyReader.getProperty("cfgendpoint"));
    //String.valueOf(System.getProperty("url","http://192.168.166.204/srcntint/integration-nibbd/"));
    //http://192.168.166.204/ntint/integration-nibbd/
    //http://192.168.166.204/srcntint/integration-nibbd/
    public static RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static void installSpecification(RequestSpecification request){
        RestAssured.requestSpecification = request;
    }


}