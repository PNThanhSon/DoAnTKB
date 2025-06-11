package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // --- THÔNG SỐ KẾT NỐI CSDL ORACLE CỦA BẠN ---
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"; // jdbc:oracle:thin:@localhost:1521:orcl nếu là SID | Lưu ý: Service Name như XEPDB1 và SID như orcl có thể khác tùy máy của bạn!!!
    private static final String DB_USER = "TKB"; // Tên người dùng của bạn
    private static final String DB_PASSWORD = "pass"; // Mật khẩu của bạn
    // --- KẾT THÚC PHẦN THÔNG SỐ ---

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } catch (SQLException e) {
            System.err.println("LỖI KẾT NỐI CƠ SỞ DỮ LIỆU:");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            System.err.println("Message: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args) { //Có thể chạy hàm main này để kiểm tra kết nối, không liên quan code chính
        System.out.println("Đang thực hiện kiểm tra kết nối đến Oracle Database...");
        System.out.println("Thông tin kết nối sử dụng:");
        System.out.println("  DB URL: " + DB_URL);
        System.out.println("  DB User: " + DB_USER);
        // Không nên in mật khẩu ra console trong môi trường production

        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("THÀNH CÔNG! Kết nối tới Oracle Database đã được thiết lập.");
            try {
                conn.close();
                System.out.println("Kết nối đã được đóng.");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("THẤT BẠI! Không thể kết nối tới Oracle Database. Vui lòng kiểm tra lại:");
            System.err.println("1. Thông tin DB_URL, DB_USER, DB_PASSWORD trong file util.DatabaseConnection.java có chính xác không.");
            System.err.println("2. Dịch vụ Oracle Database (và Listener) có đang chạy trên máy chủ và cổng đã chỉ định không.");
            System.err.println("3. Firewall có đang chặn kết nối đến cổng của Oracle Database không.");
            System.err.println("4. File JAR của Oracle JDBC driver đã được thêm vào thư viện (classpath) của project chưa.");
        }
    }
}
