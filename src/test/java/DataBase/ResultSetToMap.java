package DataBase;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetToMap {
    public static Map<String, String> resultSetToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, String> map = new HashMap<>(columns);

        for (int i = 1; i <= columns; ++i) {
            map.put(md.getColumnName(i), rs.getString(i));
        }

        return map;
    }
}
