package TestNibbd;

import Model.Request;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class ChangeForeignerTest {
    private final static String ENDPOINT = "client/changeForeigner";
    private final static Integer BRANCHID = 1104;
    private final static Integer CLIENTIDERROR = 17688;
    private final static Integer CLIENTIDOK = 17754;
    private  final static Integer HTTPSTATUSERROR = 500;
    private  final static Integer HTTPSTATUSOK = 200;
    private final static String ERRORTEXT = "10704: Error of the National information base of bank depositors (code 02015, text \"Ошибка в коде клиента\").";



    @Test(groups = {"NIBBD"})
    @Description("Запрос регистрацию изменений удостоверяющего документа иностранного гражданина в НИББД")
    public void changeTestForeignerPositive() {
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDOK, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSOK, response.getStatusCode());
    }

    @Test(groups = {"NIBBD"})
    @Description("Запрос \"регистрацию изменений удостоверяющего документа иностранного гражданина\" в НИББД")
    public void changeTestForeignerNegative() {
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT, response.path("message"));

    }
}
