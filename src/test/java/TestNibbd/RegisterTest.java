package TestNibbd;

import DataBase.DBunits;
import Model.Request;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class RegisterTest {
    private final static String ENDPOINT = "client/register";
    private final static Integer BRANCHID = 1104;
    private final static Integer STATUSCODEOK = 200;
    private final static Integer STATUSCODEERROR = 500;
    private final static String ERRORTEXT = "10704: Error of the National information base of bank depositors (code 02209, text \"Ошибка в указанном ПИНФЛ\").";
    private final static String ERRORTEXTTWO = "java.lang.RuntimeException: Client doesn't have attribute [ClientId = 18610, AttrRefId = BIRTHDATE (Дата рождения)]";
    private final static String ERRORTEXTTHREE = "10703: Controls error of the National information base of bank depositors (code 02115, text \"Ошибка в коде клиента\") - the user needs to correct the request.";
    private final static String ERRORTEXTFOUR = "10704: Error of the National information base of bank depositors (code 02015, text \"Ошибка в коде клиента\").";
    private final static Integer CLIENTIDPINFL = 18727;
    private final static Integer CLIENTIDDOC = 18568;
    private final static Integer CLIENTNOTRES = 18507;
    private final static Integer CLIENTIDPINFLERROR = 18637;
    private final static Integer CLIENTIDDOCERROR = 18566;
    private final static Integer CLIENTNOTRESERROR = 18329;
    private final static Integer CLIENTIDERRORATTRIBUTE =18610;


    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по ПИНФЛ в НИББД")
    public void registerTestNibbdPinfl() {
        //Запрос "Регистрация ФЛ по ПИНФЛ" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDPINFL, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());
        DBunits.upDateNibbdnum(99000693,CLIENTIDPINFL);
    }

    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по ПИНФЛ в НИББД - ошибка ниббд")
    public void registerTestNibbdPinflNegative() {
        //Запрос "Регистрация ФЛ по ПИНФЛ" в НИББД, Ошибка в указанном ПИНФЛ
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDPINFLERROR, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT,response.path("message"));
    }


    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по ПИНФЛ в НИББД - не заполнен обязательный атрибут")
    public void registerTestNibbdNegativeTWO() {
        //Запрос "Регистрация ФЛ по ПИНФЛ" в НИББД, не заполнен обязательный атрибут
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDERRORATTRIBUTE, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,response.path("message"));
    }
        @Test(groups = {"NIBBD"})
        @Description("Запрос Регистрация ФЛ по УД резидент в НИББД")
        public void registerTestNibbdResident() {
            //Запрос "Регистрация ФЛ по УД резидент" в НИББД
            Specification.installSpecification(Specification.requestSpecification());
            Request request = new Request(CLIENTIDDOC, BRANCHID);
            Response response = given()
                    .body(request)
                    .when()
                    .post(ENDPOINT)
                    .then()
                    .extract().response();
            Assert.assertEquals(200, response.getStatusCode());
            DBunits.upDateNibbdnum(99000585,CLIENTIDDOC);
        }
    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по УД резидент в НИББД - ошибка в ответе ниббд")
    public void registerTestNibbdResidentNegative() {
        //Запрос "Регистрация ФЛ по УД резидент" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTIDDOCERROR, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTTHREE, response.path("message"));
    }


    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по УД нерезидент в НИББД")
    public void registerTestNibbdNotResident() {
        //Запрос "Регистрация ФЛ по УД нерезидент" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTNOTRES, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then()
                .extract().response();
        Assert.assertEquals(STATUSCODEOK, response.getStatusCode());
        DBunits.upDateNibbdnum(99000570,CLIENTNOTRES);
    }


    @Test(groups = {"NIBBD"})
    @Description("Запрос Регистрация ФЛ по УД нерезидент в НИББД - ошибка в ответе ниббд")
    public void registerTestNibbdNotResidentNegative() {
        //Запрос "Регистрация ФЛ по УД нерезидент" в НИББД
        Specification.installSpecification(Specification.requestSpecification());
        Request request = new Request(CLIENTNOTRESERROR, BRANCHID);
        Response response = given()
                .body(request)
                .when()
                .post(ENDPOINT)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(STATUSCODEERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTFOUR,response.path("message"));
    }
}
