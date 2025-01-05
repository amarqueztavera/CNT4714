/* Name: Andrea
 Course: CNT 4714 – Fall 2024 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 1, 2024
*/
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Authentication")
public class Authentication extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
    
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);
    
        if (username == null || password == null) {
            response.sendRedirect("/Project4/errorpage.html");
            return;
        }
    
        Properties dbProperties = new Properties();
        ServletContext context = getServletContext();
        InputStream inputStream = context.getResourceAsStream("/WEB-INF/lib/systemapp.properties");
    
        if (inputStream == null) {
            throw new IOException("Database properties file not found!");
        }
    
        dbProperties.load(inputStream);
    
        String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
        String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
        String dbUser = dbProperties.getProperty("MYSQL_DB_USERNAME");
        String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isAuthenticated = false;
    
        HttpSession session = request.getSession();
        session.setAttribute("user", username);
    
        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    
            String query = "SELECT login_password FROM usercredentials WHERE LOWER(login_username) = LOWER(?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String cur_pass = resultSet.getString("login_password");
                System.out.println("Password from database: " + cur_pass);
                if (cur_pass != null && password.equals(cur_pass)) {
                    isAuthenticated = true;
                } else {
                    System.out.println("Password mismatch or null password.");
                }
            } else {
                System.out.println("No user found for username: " + username);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection or query error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    
        if (isAuthenticated) {
            System.out.println("Authentication successful for user: " + username);
            switch (username.toLowerCase()) {
                case "root":
                    response.sendRedirect("/Project4/rootHome.jsp");
                    break;
                case "client":
                    response.sendRedirect("/Project4/clientHome.jsp");
                    break;
                case "theaccountant":
                    response.sendRedirect("/Project4/accountantHome.jsp");
                    break;
                default:
                    response.sendRedirect("/Project4/errorpage.html");
                    break;
            }
        } else {
            System.out.println("Authentication failed for user: " + username);
            response.sendRedirect("/Project4/errorpage.html");
        }
    }
}