package TestNibbd;

import Model.Request;
import Model.RequestRegistr;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class IdentifyTest {
    protected Integer MSGID;
    private final static String ENDPOINT = "client/identify";
    private final static String ENDPOINTREGISTR = "client/registerClientInformation";
    private final static String ERRORTEXT = "java.lang.RuntimeException: Client doesn't have attribute [ClientId = 16929, AttrRefId = CERTIFICATE_NUM (Номер удостоверения личности)]";
    private final static String ERRORTEXTTWO = "Error occurred during client registration number changing";
    private final static Integer STATUSCODEOK = 200;
    private final static Integer CLIENTIDPINFL = 17071;
    private final static Integer CLIENTIDDOC = 17047;
    private final static Integer CLIENTIDERROR = 16929;
    private final static Integer CLIENTIDERRORTWO = 16870;
    private final static Integer BRANCHID = 1104;
    private final static Integer STATUSCODEERROR = 500;


    @Test(groups = {"NIBBD"})
    @Description("Идентификация физического лица по ПИНФЛ")
    public void identifyInNIBBDPinflPositive() {
        ////Отправка запроса в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDPINFL, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then()
                .extract()
                .response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());

        //Регистрация клиента в БД по информации из ответа на запрос по идентификации клиента в НИББД
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr).log().all()
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract();
        Assert.assertEquals(STATUSCODEOK, responseReg.getStatusCode());
    }


    @Test(groups = {"NIBBD"})
    @Description("Идентификация физического лица по УД")
    public void identifyNIBBDDocPositive() {
        //Отправка запроса в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDDOC, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then()
                .extract()
                .response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());

        //Регистрация клиента в БД по информации из ответа на запрос по идентификации клиента в НИББД
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr)
                .when()
                .post(ENDPOINTREGISTR)
                .then()
                .extract();
        Assert.assertEquals(STATUSCODEOK, responseReg.getStatusCode());

    }

    @Test(groups = {"NIBBD"})
    @Description("Идентификация физического лица в НИББД")
    public void identifyNIBBDNegative() {
        //Отправка запроса в НИББД, у клиента отсутствует ПИНФЛ и УД
        //Ошибка на уровне отправки запроса
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT, response.path("message"));

    }

    @Test(groups = {"NIBBD"})
    @Description("Идентификация физического лица в НИББД, обработка ответа от ниббд с ошибкой")
    public void identifyNIBBDRegistrNegative() {
        ////Отправка запроса в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERRORTWO, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then().log().all()
                .extract()
                .response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());

        //Регистрация клиента в БД по информации из ответа на запрос по идентификации клиента в НИББД
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr).log().all()
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract();
        Assert.assertEquals(STATUSCODEERROR, responseReg.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,responseReg.path("message"));
    }
}
