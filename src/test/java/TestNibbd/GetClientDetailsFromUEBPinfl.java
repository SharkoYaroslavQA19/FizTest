package TestNibbd;

import Model.ClientDetails;
import Model.RequestRegistr;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetClientDetailsFromUEBPinfl {

    private final static String ENDPOINT = "client/getClientDetailsFromUEB";
    private final static String ENDPOINTREGISTR = "client/updateClientInformationFromUEB";
    private final static Integer BRANCHID = 1104;
    private final static String CLIENTKEY = "1212121212";
    private final static Integer CLIENTIDOK = 145988;
    private final static Integer CLIENTIDERROR = 54184;
    private final static Integer HTTPSTATUSOK = 200;
    private final static Integer HTTPSTATUSERROR = 500;
    private final static String ERRORTEXT = "Index 0 out of bounds for length 0";

    @Test(groups = {"EBP"})
    @Description("Отправка запроса в ЕБП на идентификацию клиента ИП по ПИНФЛ")
    public void getClientDetailsFromUEBPinflPositive() {
        //Отправка запроса по идентификации ИП в ЕБП
        Specification.installSpecification(Specification.requestSpecification());
        ClientDetails clientDetails = new ClientDetails(CLIENTIDOK, BRANCHID,CLIENTKEY);
        Response response = given()
                .body(clientDetails).log().all()
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
    @Description("Отправка запроса в ЕБП на идентификацию клиента ИП по ПИНФЛ - оишибка по идентификации от ЕБП")
    public void identifyClientInUEBNegative() {
        //Отправка запроса по идентификации ИП в ЕБП
        Specification.installSpecification(Specification.requestSpecification());
        ClientDetails clientDetails = new ClientDetails(CLIENTIDERROR, BRANCHID,CLIENTKEY);
        Response response = given()
                .body(clientDetails).log().all()
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
        Assert.assertEquals(ERRORTEXT,responseReg.path("message"));
    }
}