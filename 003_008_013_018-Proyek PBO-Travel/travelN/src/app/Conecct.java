package app;
import java.sql.DriverManager;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class Conecct {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/travel", "root",
                    "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object statement;
    public Object result;
    public static Statement createStatement() {
        return null;
    }
}
