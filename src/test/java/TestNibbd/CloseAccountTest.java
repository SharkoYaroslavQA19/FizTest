package TestNibbd;

import Model.CloseAccount;
import Specification.Specification;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class CloseAccountTest {
    private final static String ENDPOINTClose = "account/close";
    private final static Integer BRANCHID = 1104;
    private final static String CLOSEDATE = "2023-03-07";
    private final static String ERROTEXTTHREE = "10704: Error of the National information base of bank depositors (code 02015, text \"Не найден код клиента\").";
    private final static String ERROTEXTFFOUR = "Error checking account before close: На счете ненулевые остатки.";
    private final static Integer HTTPSTATUSERROR = 500;
    private final static Integer ACCIDERROR = 104164;
    private final static Integer ACCIDERRORTWO = 19969;


    //закрытие лицевого счета ФЛ через НИББД
    @Test(groups = {"NIBBD"})
    @Description("закрытие лицевого счета ФЛ через НИББД - на счете не нулевые остатки, ошибка системе NT")
    public void CloseAccountTestNegative() {
        //открытие лицевого счета ФЛ через НИББД
        Specification.installSpecification(Specification.requestSpecification());
        CloseAccount closeAccount = new CloseAccount(ACCIDERROR,BRANCHID,CLOSEDATE);
        Response response = given()
                .body(closeAccount)
                .post(ENDPOINTClose)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR,response.getStatusCode());
        Assert.assertEquals(ERROTEXTFFOUR,response.path("message"));

    }

    //закрытие лицевого счета ФЛ через НИББД
    @Test(groups = {"NIBBD"})
    @Description("закрытие лицевого счета ФЛ через НИББД - ошибка ниббд")
    public void CloseAccountTestNegativeTWO() {
        //открытие лицевого счета ФЛ через НИББД
        Specification.installSpecification(Specification.requestSpecification());
        CloseAccount closeAccount = new CloseAccount(ACCIDERRORTWO,BRANCHID,CLOSEDATE);
        Response response = given()
                .body(closeAccount)
                .post(ENDPOINTClose)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(HTTPSTATUSERROR,response.getStatusCode());
        Assert.assertEquals(ERROTEXTTHREE,response.path("message"));

    }
}

