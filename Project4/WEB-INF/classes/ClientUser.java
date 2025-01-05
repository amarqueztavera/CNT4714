/* Name: Andrea Marquez Tavera
Course: CNT 4714 – Fall 2024 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 1, 2024
*/

import java.io.*;
import java.sql.*;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ClientUser")
public class ClientUser extends HttpServlet {
    private Connection conn;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // getting sql stmt
        String sqlStatement = request.getParameter("sql");
        // lower stmt
        String lowerCaseStatement = sqlStatement != null ? sqlStatement.toLowerCase().trim() : "";


        String finalMess = "";

        try {
            // Load database properties
            Properties dbProperties = new Properties();
            ServletContext context = getServletContext();

            try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/lib/client.properties")) {
                if (inputStream == null) {
                    // Handle missing properties file if needed
                }

                dbProperties.load(inputStream);

                Class.forName(dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS"));
                conn = DriverManager.getConnection(
                        dbProperties.getProperty("MYSQL_DB_URL"),
                        dbProperties.getProperty("MYSQL_DB_USERNAME"),
                        dbProperties.getProperty("MYSQL_DB_PASSWORD"));

            } catch (IOException | ClassNotFoundException | SQLException e) {
                finalMess = "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                        + "Database connection failed: " + e.getMessage() + "</b></font></th></tr></table>";
                request.setAttribute("tableResults", finalMess);
                request.getRequestDispatcher("clientHome.jsp").forward(request, response);
                return;
            }

            if (lowerCaseStatement.startsWith("select")) {
                // Handle SELECT queries
                try (Statement statement = conn.createStatement();
                     ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                    finalMess = outputTable.getHtml(resultSet); // Call static method to generate HTML table
                }
            } else if (lowerCaseStatement.startsWith("insert") || lowerCaseStatement.startsWith("update")
                    || lowerCaseStatement.startsWith("delete") || lowerCaseStatement.startsWith("replace")) {
                // Handle DML (insert, update, delete, replace) queries
                try (Statement statement = conn.createStatement()) {
                    statement.executeUpdate(sqlStatement);
                    finalMess = "<table><tr bgcolor='green'><th style='text-align:center; background-color: green;'><font color='#ffffff'><b>" 
                            + "The statement executed successfully." + "</b></font></th></tr></table>";
                } catch (SQLException e) {
                    // Check if the SQLException is related to privilege denial
                    if (e.getMessage().contains("Access denied for user")) {
                        finalMess = "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                                + "You do not have the required privileges to execute this SQL command." + "</b></font></th></tr></table>";
                    } else {
                        finalMess = "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                                + "Error executing SQL statement: " + e.getMessage() + "</b></font></th></tr></table>";
                    }
                }
            } else {
                // Invalid SQL command
                finalMess = "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                        + "Invalid SQL command. Only SELECT, INSERT, UPDATE, DELETE, or REPLACE are allowed." + "</b></font></th></tr></table>";
            }
        } catch (SQLException e) {
            finalMess = "<table><tr bgcolor='red'><th style='text-align:center; width: fit-content; background-color: red;'><font color='#ffffff'><b>" 
                    + "Error executing the SQL Statement: " + e.getMessage() + "</b></font></th></tr></table>";
        } finally {
            // Close database connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    finalMess = "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                            + "Error closing database connection: " + e.getMessage() + "</b></font></th></tr></table>";
                }
            }
        }

        // Set result message in session
        HttpSession session = request.getSession();
        session.setAttribute("tableResults", finalMess);
        session.setAttribute("sql", sqlStatement);
        response.sendRedirect("clientHome.jsp");
    }

    public static class outputTable {
        public static synchronized String getHtml(ResultSet rs) throws SQLException {
            if (rs == null) {
                return "<table><tr><td>No results found.</td></tr></table>";
            }

            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            StringBuilder html = new StringBuilder();
            
            html.append("<table border='1' style='border-collapse: collapse; width: fit-content;'>");
            html.append("<tr style='background-color: #f2f2f2;'>");

            // Add column headers
            for (int i = 1; i <= colCount; i++) {
                String columnName = metaData.getColumnName(i);
                html.append("<th style='padding: 8px; text-align: left;'>").append(columnName).append("</th>");
            }
            html.append("</tr>");

            // Add rows from ResultSet
            while (rs.next()) {
                html.append("<tr>");
                for (int i = 1; i <= colCount; i++) {
                    String cellValue = rs.getString(i);
                    html.append("<td style='padding: 8px;'>")
                        .append(cellValue != null ? cellValue : "")
                        .append("</td>");
                }
                html.append("</tr>");
            }

            html.append("</table>");
            return html.toString();
        }
    }
}
