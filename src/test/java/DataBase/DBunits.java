package DataBase;

import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.util.Map;

public class DBunits {

    public static String deleteAccountByAccNum(String AccId) {
        Map<String, String> requestInfo;
        String sql = String.format("select pkgTesting.DeleteAccountByAccID('%s') as isExists from dual", AccId);
        requestInfo = FirstQuery.sqlQuery(sql);
        return requestInfo.get("ISEXISTS");
    }

    public static String upDateNibbdnum(Integer NIBBDMUM, Integer Clientid) {
        Map<String, String> requestInfo;
        String sql = String.format("insert into clattrs ca(ca.clattrid, ca.clientid, ca.clattrrefid, ca.clattrvalue, actualdate) \n" +
                "values(seqclattrid.nextval, '%s', 'NIBBDNUM', '%s', getoday(0))", Clientid, NIBBDMUM);
        requestInfo = FirstQuery.sqlQuery(sql);
        return requestInfo.get("ISEXISTS");
    }

    public static String GetPersonalNum(Integer Clientid) {
        Map<String, String> requestInfo;
        String sql = String.format("select ca.clattrvalue from clattrs ca where ca.clientid = %s and ca.clattrrefid = 'PERSONALNUM'", Clientid);
        requestInfo = FirstQuery.sqlQuery(sql);
        return requestInfo.get("CLATTRVALUE");
    }

    public static String DeletePersonalnum(Integer Clientid) {
        Map<String, String> requestInfo;
        String sql = String.format("begin delete from clattrs ca where ca.clientid = %s and ca.clattrrefid = 'PERSONALNUM'; commit; end;", Clientid);
        requestInfo = FirstQuery.sqlQuery(sql);
        return null;
    }
    public static String getOperDay() {
        Map<String, String> requestInfo;
        String sql = String.format("select getoday(0, pkgenv.UserID,1104)from dual ");
        requestInfo = FirstQuery.sqlQuery(sql);
        return requestInfo.get("GETODAY(0,PKGENV.USERID,1104)");
    }
}