/* Name: Andrea Marquez Tavera
Course: CNT 4714 – Fall 2024 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 1, 2024
*/
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/RootUser")
public class RootUser extends HttpServlet {
    private Connection conn;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // sqlstmt from query
        String sqlStatement = request.getParameter("sql");
        // make it lowercase lol 
        String lowerCaseStatement = sqlStatement != null ? sqlStatement.toLowerCase().trim() : "";
        String resultMessage = "";

        // business logic :D
        String[] businessLogicSQL = {
            "DROP TABLE IF EXISTS replicate;",
            "CREATE TABLE replicate LIKE shipments;",
            "INSERT INTO replicate SELECT * FROM shipments;",
            "UPDATE suppliers SET status = status + 5 WHERE suppliers.snum IN (SELECT DISTINCT snum FROM shipments WHERE shipments.quantity >= 100 AND NOT EXISTS (SELECT * FROM replicate WHERE shipments.snum = replicate.snum AND shipments.pnum = replicate.pnum AND shipments.jnum = replicate.jnum AND shipments.quantity = replicate.quantity));",
            "DROP TABLE replicate;"
        };

        try {
            establishDatabaseConnection();// kept crashing inside here so had to make a new function 
            resultMessage = processSQL(lowerCaseStatement, sqlStatement, businessLogicSQL);
        } catch (SQLException e) {
            resultMessage = formatErrorMessage(e.getMessage());
        } finally {
            closeDatabaseConnection();
        }

        HttpSession session = request.getSession();
        session.setAttribute("sql", sqlStatement);
        session.setAttribute("tableResults", resultMessage);
        response.sendRedirect("rootHome.jsp");
    }

    private String processSQL(String lowerCaseStatement, String sqlStatement, String[] businessLogicSQL) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            if (lowerCaseStatement.startsWith("select")) {
                return executeSelectQuery(statement, sqlStatement);
            } else {
                return executeNonSelectQuery(statement, lowerCaseStatement, sqlStatement, businessLogicSQL);
            }
        }
    }

    private String executeSelectQuery(Statement statement, String sqlStatement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        return outputTable.getHtml(resultSet);
    }

    private String executeNonSelectQuery(Statement statement, String lowerCaseStatement, String sqlStatement, String[] businessLogicSQL) throws SQLException {
        for (int i = 0; i < 3; i++) {
            statement.executeUpdate(businessLogicSQL[i]);
        }

        int affectedRows = statement.executeUpdate(sqlStatement);

        if (affectedRows > 0) {
            if (lowerCaseStatement.startsWith("insert into shipments") || lowerCaseStatement.startsWith("update shipments")) {
                int updatedSuppliers = statement.executeUpdate(businessLogicSQL[3]);
                return formatBusinessLogicMessage(affectedRows, updatedSuppliers);
            } else {
                return formatSuccessMessage(affectedRows);
            }
        }

        statement.executeUpdate(businessLogicSQL[4]);
        return formatSuccessMessage(affectedRows);
    }

    private void establishDatabaseConnection() throws SQLException {
        Properties dbProperties = new Properties();
        ServletContext context = getServletContext();

        try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/lib/root.properties")) {
            if (inputStream == null) {
                throw new IOException("Database properties file not found!");
            }

            dbProperties.load(inputStream);

            String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
            String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
            String dbUser = dbProperties.getProperty("MYSQL_DB_USERNAME");
            String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");

            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new SQLException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }

    private void closeDatabaseConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    private String formatBusinessLogicMessage(int affectedRows, int updatedSuppliers) {
        return String.format(
            "<table><tr style='background-color: #46FF00;'><th style='text-align:center; background-color: #46FF00;'><font color=#000000><b>The statement executed successfully.<br>%d row(s) affected.<br><br>Business Logic Detected! - Updating Supplier Status.<br><br>Business Logic updated %d supplier status marks.</b></font></th></tr></table>",
            affectedRows, updatedSuppliers
        );
    }

    private String formatSuccessMessage(int affectedRows) {
        return String.format(
            "<table><tr style='background-color: #46FF00;'><th style='text-align:center; background-color: #46FF00;'><font color=#000000><b>The statement executed successfully.<br>%d row(s) affected.<br><br>Business Logic Not Triggered</b></font></th></tr></table>",
            affectedRows
        );
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