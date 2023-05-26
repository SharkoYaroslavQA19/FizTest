package TestNibbd;

import DataBase.DBunits;
import Model.Request;
import Model.RequestRegistr;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class IdentifyClientInSPC {

    protected Integer MSGID;
    private final static String ENDPOINT = "client/identifyClientInSPC";
    private final static String ENDPOINTREGISTR = "client/registerClientInformationFromSPC";
    private final static String ERRORTEXT = "Individual person data not found";
    private final static String ERRORTEXTTWO = "java.lang.RuntimeException: Client doesn't have attribute [ClientId = 19001, AttrRefId = CERTIFICATE_NUM (Номер удостоверения личности)]";
    private final static Integer STATUSCODEOK = 200;
    private final static Integer CLIENTIDPINFL = 18864;
    private final static Integer CLIENTIDDOC = 18892;
    private final static Integer CLIENTIDERROR = 180979;
    private final static Integer CLIENTIDERRORTWO = 19001;
    private final static Integer BRANCHID = 1104;
    private final static Integer STATUSCODEERROR = 500;

    @Test(groups = {"SPC"})
    @Description ("Идентификация ФЛ в ГЦП по пинфл")
    public void identifyClientInSPCPinfl (){
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

        //Регистрация полученого ответа от ГЦП
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr).log().all()
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract();
        Assert.assertEquals(STATUSCODEOK, responseReg.getStatusCode());
    }
    @Test(groups = {"SPC"})
    @Description ("Идентификация ФЛ в ГЦП по удостоверению личности")
    public void identifyClientInSPCPositiveDoc (){
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


        //Регистрация полученого ответа от ГЦП
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr).log().all()
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract();
        Assert.assertEquals(STATUSCODEOK, responseReg.getStatusCode());
        String PERSONALNUM = DBunits.GetPersonalNum(CLIENTIDDOC);
        Assert.assertEquals("31502510360061",PERSONALNUM);
        DBunits.DeletePersonalnum(CLIENTIDDOC);

    }


    @Test(groups = {"SPC"})
    @Description ("Идентификация ФЛ в ГЦП - получение ответа с ошибкой")
    public void identifyClientInSPCNegative (){
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERROR, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then()
                .extract()
                .response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());

        //Регистрация полученого ответа от ГЦП
        RequestRegistr registr = new RequestRegistr(MSGID);
        Response responseReg = (Response) given()
                .body(registr).log().all()
                .when()
                .post(ENDPOINTREGISTR)
                .then().log().all()
                .extract();
        Assert.assertEquals(STATUSCODEERROR, responseReg.getStatusCode());
        Assert.assertEquals(ERRORTEXT,responseReg.path("message"));
    }
    @Test(groups = {"SPC"})
    @Description ("Идентификация ФЛ в ГЦП - отсутствует обязательный атрибут")
    public void identifyClientInSPCNegativeTWO (){
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERRORTWO, BRANCHID);
        Response response = given()
                .body(request)
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        MSGID = response.path("msgId");
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,response.path("message"));
    }
}
