package Talk.common;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DB {
    private static Connection conn;
    public static Connection get() throws Exception {
        if (conn != null && !conn.isClosed()) return conn;

        Properties p = new Properties();
        String userDir = System.getProperty("user.dir");
        String[] candidates = new String[]{
                "config/db.properties",
                "../config/db.properties",
                userDir + File.separator + "config" + File.separator + "db.properties"
        };
        boolean loaded = false;
        for (String path : candidates) {
            try (FileInputStream fis = new FileInputStream(path)) {
                p.load(fis);
                loaded = true;
                break;
            } catch (Exception ignore) {}
        }
        if (!loaded) throw new RuntimeException("config/db.properties not found");

        String url  = p.getProperty("db.url");
        String user = p.getProperty("db.user");
        String pw   = p.getProperty("db.password");

        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found on classpath. Put mysql-connector-j in lib/.", e);
        }
        conn = DriverManager.getConnection(url, user, pw);
        return conn;
    }
}
