package TestNibbd;

import Model.Request;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ChangeDocTest {
    private final static String ENDPOINT = "client/changeDoc";
    private final static Integer BRANCHID = 1104;
    private final static Integer CLIENTIDERROR = 352914;
    private final static Integer CLIENTIDOK = 17978;
    private  final static Integer HTTPSTATUSERROR = 500;
    private  final static Integer HTTPSTATUSOK = 200;
    private final static String ERRORTEXT = "10703: Controls error of the National information base of bank depositors (code 02115, text \"Ошибка в коде клиента\") - the user needs to correct the request.";
    @Test(groups = {"NIBBD"})
    @Description("Ошибочный запрос на регистрацию изменения удостоверяющего документа гражданина РУз в НИББД")
    public void changeTestDocNegative() {
        //Запрос " регистрацию изменения удостоверяющего документа гражданина РУз" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request).log().all()
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(ERRORTEXT, response.path("message"));
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
    }

    @Test(groups = {"NIBBD"})
    @Description("Запрос регистрацию изменения удостоверяющего документа гражданина РУз в НИББД")
    public void changeTestDocPositive() {
        //Запрос " регистрацию изменения удостоверяющего документа гражданина РУз" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDOK, BRANCHID);
        Response response = given()
                .body(request).log().all()
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSOK, response.getStatusCode());
    }
}
