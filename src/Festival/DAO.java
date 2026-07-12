package Festival;

import java.sql.Connection;
import java.sql.DriverManager;
public class DAO {
	private static final String URL = "jdbc:mysql://localhost:3307/festivali";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  
        return DriverManager.getConnection(URL, USER, PASS);
    }
    }

