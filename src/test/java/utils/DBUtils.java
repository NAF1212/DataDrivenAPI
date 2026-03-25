package utils;

import config.ConfigManager;
import java.sql.*;

public class DBUtils {

    public static String getRate(String accountId)
            throws Exception {

        Connection con =
                DriverManager.getConnection(
                        ConfigManager.get("db.url"),
                        ConfigManager.get("db.username"),
                        ConfigManager.get("db.password"));

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(
                "SELECT rate FROM pricing_table WHERE account_id="
                        + accountId);

        rs.next();

        String rate = rs.getString("rate");

        con.close();

        return rate;
    }
}