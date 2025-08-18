package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CareMatch?useUnicode=true&characterEncoding=UTF-8";
    private static final String DB_USER = "root"; 		// 본인 환경에 맞게 변경
    private static final String DB_PASSWORD = "1234"; 	// 본인 환경에 맞게 변경

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("오류: MySQL JDBC 드라이버를 찾을 수 없습니다.", e);
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("데이터베이스 연결 성공");
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 실패");
            e.printStackTrace();
        }
        return conn;
    }
    /* 연결 테스트용 
    public static void main(String[] args) {
        System.out.println("DB 연결 테스트");
        Connection conn = getConnection();

        if (conn != null) {
            try {
                conn.close();
                System.out.println("연결을 성공적으로 닫았습니다.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/
}