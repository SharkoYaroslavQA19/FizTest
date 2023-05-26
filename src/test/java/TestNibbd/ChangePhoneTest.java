package TestNibbd;

import Model.Request;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ChangePhoneTest {
    private final static String ENDPOINT = "client/changePhone";
    private final static Integer BRANCHID = 1104;
    private final static Integer CLIENTIDERROR = 17688;
    private final static Integer CLIENTIDERRORTWO = 352914;
    private final static Integer CLIENTIDOK = 17754;
    private  final static Integer HTTPSTATUSERROR = 500;
    private  final static Integer HTTPSTATUSOK = 200;
    private final static String ERRORTEXTTWO = "10704: Error of the National information base of bank depositors (code 02363, text \"Запрос отклонён из-за отсутствия связи с системой МВД в межведомственной платформе\").";

    private final static String ERRORTEXT = "java.lang.RuntimeException: Client doesn't have composite attribute [ClientId = 17688, AttrRefId = Мобильный телефон, CompAttrRefId = STATEPHONENUMBER]";


    @Test(groups = {"NIBBD"})
    @Description("Запрос на перерегистрацию номера мобильного телефона физического лица по ПИНФЛ в НИББД - по клиенту отсутствует атрибут моб. номер")
    public void changeTestPhoneNegative() {
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT,response.path("message"));
    }

    @Test(groups = {"NIBBD"})
    @Description("Запрос на перерегистрацию номера мобильного телефона физического лица по ПИНФЛ в НИББД - ошибка ниббд")
    public void changeTestPhoneNegativeTwo() {
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERRORTWO, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,response.path("message"));
    }

    @Test(groups = {"NIBBD"})
    @Description("Запрос на перерегистрацию номера мобильного телефона физического лица по ПИНФЛ в НИББД")
    public void changeTestPhonePositive() {
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
}
