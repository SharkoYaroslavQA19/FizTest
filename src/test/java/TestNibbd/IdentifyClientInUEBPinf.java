package TestNibbd;

import Model.Request;
import Model.RequestRegistr;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class IdentifyClientInUEBPinf {


    private final static String ENDPOINT = "client/identifyClientInUEB";
    private final static String ENDPOINTREGISTR = "client/registerClientInformationFromUEB";
    private final static String ERRORTEXT = "java.lang.RuntimeException: Client doesn't have attribute [ClientId = 56224, AttrRefId = PERSONALNUM (Личный номер физического лица)]";
    private final static String ERRORTEXTTWO = "Business entity data not found";
    private final static Integer BRANCHID = 1104;
    private final static Integer CLIENTIDOK = 82004;
    private final static Integer CLIENTIDERROR = 56224;
    private final static Integer CLIENTIDERRORTWO = 60349;
    private final static Integer HTTPSTATUSOK = 200;
    private final static Integer HTTPSTATUSERROR = 500;

    @Test(groups = {"EBP"})
    @Description("Отправка запроса в ЕБП на идентификацию клиента ИП по ПИНФЛ")
    public void identifyClientInUEBPositive() {
        //Отправка запроса по идентификации ИП в ЕБП
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDOK, BRANCHID);
        Response response = given()
                .body(request).log().all()
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Integer msgId = response.path("msgId");
        Assert.assertEquals(HTTPSTATUSOK, response.getStatusCode());
        //Выполнение регистрация ответа от ЕБП
        RequestRegistr requestRegistr =new RequestRegistr(msgId);
        Response responseReg = given()
                .body(requestRegistr)
                .when()
                .post(ENDPOINTREGISTR)
                .then()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSOK,responseReg.getStatusCode());
    }

    @Test(groups = {"EBP"})
    @Description("Отправка запроса в ЕБП на идентификацию клиента ИП по ПИНФЛ - по клиенту отсутсвуют обязательные атрибуты ПИНФЛ")
    public void identifyClientInUEBNegative() {
        //Запрос " регистрацию изменения удостоверяющего документа гражданина РУз" в НИББД, Контроль на обязательные атрибуты
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request).log().all()
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Integer msgId = response.path("msgId");
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT,response.path("message"));
    }

    @Test(groups = {"EBP"})
    @Description("Отправка запроса в ЕБП на идентификацию клиента ИП по ПИНФЛ - оишибка по идентификации от ЕБП")
    public void identifyClientInUEBNegativeTwo() {
        //Отправка запроса по идентификации ИП в ЕБП
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERRORTWO, BRANCHID);
        Response response = given()
                .body(request).log().all()
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Integer msgId = response.path("msgId");
        Assert.assertEquals(HTTPSTATUSOK, response.getStatusCode());
        //Выполнение регистрация ответа от ЕБП с ошибкой
        RequestRegistr requestRegistr =new RequestRegistr(msgId);
        Response responseReg = given()
                .body(requestRegistr)
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR,responseReg.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,responseReg.path("message"));
    }
}
