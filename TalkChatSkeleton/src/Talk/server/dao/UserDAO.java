package Talk.server.dao;

import java.sql.*;
import Talk.common.*;

public class UserDAO {
    public static class User {
        public int userId; public String loginId; public String name; public String role;
    }

    public User login(String loginId, String password) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT user_id, login_id, name, role FROM users WHERE login_id=? AND password=?")) {
            ps.setString(1, loginId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.userId = rs.getInt(1);
                    u.loginId= rs.getString(2);
                    u.name   = rs.getString(3);
                    u.role   = rs.getString(4);
                    return u;
                }
                return null;
            }
        }
    }

    public String getName(int uid) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT name FROM users WHERE user_id=?")) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : ("user"+uid);
            }
        }
    }
}
