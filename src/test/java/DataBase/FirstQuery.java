package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static DataBase.ResultSetToMap.resultSetToMap;


public class FirstQuery {

    public static Map<String, String> sqlQuery(String sqlString) {

        String url =  "jdbc:oracle:thin:adminuztest/adminuztest@vntxdb.softclub.by:1521/scntuztest";
                // "jdbc:oracle:thin:adminuztest/adminuztest@vntxdb.softclub.by:1521/scntuztest";
                //"jdbc:oracle:thin:adminuz/adminuz@vntxdb.softclub.by:1521/scntuzdev"
        String userName = "adminuztest";
        //"adminuz";
        //"adminuztest";
        String password = "adminuztest";
        //"adminuz";
        //"adminuztest";
        Map<String, String> result = null;
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            Statement stmt = conn.createStatement();
            String sqlAlter = "alter session set NLS_DATE_FORMAT='dd/mm/yyyy' NLS_NUMERIC_CHARACTERS='.,' NLS_TERRITORY='CIS'  NLS_DATE_LANGUAGE='AMERICAN'";
            ResultSet rs = stmt.executeQuery(sqlAlter);
            rs = stmt.executeQuery(sqlString);
            rs.next();
            result = resultSetToMap(rs);
        } catch (SQLException e) {
            System.out.println("Empty results of query \n" + sqlString);
        }
        return result;
    }
}
