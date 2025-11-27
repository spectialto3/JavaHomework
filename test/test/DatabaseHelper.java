package test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

public class DatabaseHelper {
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("db.properties")) {
                props.load(fis);
            }
            String db = props.getProperty("database", "train_tickets");
            String baseUrl = props.getProperty("url", "jdbc:mysql://localhost:3306/");
            if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
            url = baseUrl + db + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8";
            user = props.getProperty("user", "root");
            password = props.getProperty("password", "1234");

            try (Connection conn = DriverManager.getConnection(baseUrl + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8", user, password);
                 Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                st.executeUpdate("USE " + db);
                st.executeUpdate("CREATE TABLE IF NOT EXISTS tickets (\n" +
                        "  id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "  name VARCHAR(64) NOT NULL,\n" +
                        "  id_card VARCHAR(32) NOT NULL,\n" +
                        "  start_city VARCHAR(64) NOT NULL,\n" +
                        "  end_city VARCHAR(64) NOT NULL,\n" +
                        "  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_tickets_idcard ON tickets(id_card)");
            } catch (Exception ignore) {}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void insertTicket(Ticket t) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tickets(name,id_card,start_city,end_city) VALUES (?,?,?,?)")) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getIdCard());
            ps.setString(3, t.getStartCity());
            ps.setString(4, t.getEndCity());
            ps.executeUpdate();
        }
    }
}
