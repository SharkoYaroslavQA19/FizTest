package TestNibbd;

import Model.OpenAccount;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class OpenAccountTest {
    private final static String ENDPOINTOpen = "account/register";
    private final static Integer CLIENTIDERROR = 136593;
    private final static Integer CLIENTIDERRORTWO = 17116;
    private final static Integer BRANCHID = 1104;
    private final static Integer PLANID = 20206;
    private final static Integer CURRENCYID = 840;
    private final static Integer NUMBERID = 05;
    private final static String ERRORTEXT= "java.lang.Exception: Account opening error: ORA-20101  Указанный номер счета уже есть в картотеке счетов.";
    private final static String ERRORTEXTTWO= "10704: Error of the National information base of bank depositors (code 02015, text \"Ошибка в коде клиента\").";
    private final static Integer HTTPSTATUSERROR = 500;



    @Test(groups = {"NIBBD"})
    @Description("открытие лицевого счета ФЛ через НИББД - счет уже открыт с системе NT")
    public void openAccountTestNegative() {
        //открытие лицевого счета ФЛ через НИББД
        Specification.installSpecification(Specification.requestSpecification());
        OpenAccount openAccount = new OpenAccount(CLIENTIDERROR, BRANCHID, PLANID, CURRENCYID, NUMBERID);
        Response response = given()
                .body(openAccount)
                .post(ENDPOINTOpen)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXT,response.path("message"));
    }

    @Test(groups = {"NIBBD"})
    @Description("открытие лицевого счета ФЛ через НИББД - ошибка ниббд")
    public void openAccountTestNegativeTWO() {
        //открытие лицевого счета ФЛ через НИББД
        Specification.installSpecification(Specification.requestSpecification());
        OpenAccount openAccount = new OpenAccount(CLIENTIDERRORTWO, BRANCHID, PLANID, CURRENCYID, NUMBERID);
        Response response = given()
                .body(openAccount)
                .post(ENDPOINTOpen)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR, response.getStatusCode());
        Assert.assertEquals(ERRORTEXTTWO,response.path("message"));
    }
}

