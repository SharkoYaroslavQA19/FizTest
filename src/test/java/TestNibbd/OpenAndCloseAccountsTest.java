package TestNibbd;

import Model.OpenAccount;
import Model.CloseAccount;
import Specification.Specification;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import DataBase.DBunits;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;


public class OpenAndCloseAccountsTest {
    private final static String ENDPOINTOpen = "account/register";
    private final static String ENDPOINTClose = "account/close";

    private final static Integer CLIENTIDOK = 136593;
    private final static Integer BRANCHID = 1104;
    private final static Integer PLANID = 20406;
    private final static Integer CURRENCYID = 840;
    private final static Integer NUMBERID = 1;
    private static String CLOSEDATE= null;//"2023-03-08";
    private final static Integer HTTPSTATUSOK = 200;


    @Test(groups = {"NIBBD"})
    @Description("открытие лицевого счета ФЛ через НИББД")
    public void openAccountTestPositive() {
        //Отправка запроса на открытие счета
        Specification.installSpecification(Specification.requestSpecification());
        OpenAccount openAccount = new OpenAccount(CLIENTIDOK, BRANCHID, PLANID, CURRENCYID, NUMBERID);
        Response response = given()
                .body(openAccount)
                .post(ENDPOINTOpen)
                .then().log().all()
                .extract().response();
        Integer accId = response.path("accId");
        Assert.assertEquals(HTTPSTATUSOK, response.getStatusCode());

        //Отправка запроса на закрытие счета
        CLOSEDATE = DBunits.getOperDay(BRANCHID);
        System.out.println(CLOSEDATE);
        CloseAccount closeAccount = new CloseAccount(accId,BRANCHID,CLOSEDATE);
        Response response1 = given()
                .body(closeAccount)
                .post(ENDPOINTClose)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSOK,response1.getStatusCode());
        //Удаление из БД счета
        DBunits.deleteAccountByAccNum(String.valueOf(accId));


    }
}
