/*
Name: Andrea Marquez Tavera
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: October 20, 2024

Class: p3A.java
*/
package cnt471403Pack;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class p3A extends JFrame {
    private JTextArea commands;
    private JTextField usrF;
    private JPasswordField pF;
    private JButton connectButton, disconnectButton, clearSQLButton, executeButton, clearResultButton, closeButton;
    private JLabel csL, cL, erwL, cmdL, usrL, pwdL, dbL, usrSL;
    private JTable resultsT;
    private JScrollPane resultSP, commandSP;
    private Connection connection;
  
    public p3A() {
        Interface();
    }

    private void Interface() {
        setTitle("SPECIALIZED ACCOUNTANT APPLICATION - (MJL - CNT 4714 - FALL 2024 - PROJECT 3 - ANDREA :D)");
        setSize(830, 645);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // top panel labels
        cL = new JLabel("Connection Details");
        cL.setForeground(Color.blue);
        cL.setBounds(20, 10, 200, 25);
        
        dbL = new JLabel(" DB URL Properties");
        dbL.setBounds(20, 40, 130, 25);
        dbL.setOpaque(true);
        dbL.setBackground(Color.gray);
        JTextField dbField = new JTextField("operationslog.properties");
        dbField.setEditable(false);
        dbField.setBounds(150, 40, 230, 25);

        usrSL = new JLabel(" User Properties");
        usrSL.setOpaque(true);
        usrSL.setBackground(Color.gray);
        usrSL.setBounds(20, 80, 130, 25);
        JTextField userField = new JTextField("theaccountant.properties");
        userField.setEditable(false);
        userField.setBounds(150, 80, 230, 25);
        
        usrL = new JLabel("Username");
        usrL.setOpaque(true);
        usrL.setBackground(Color.gray);
        usrL.setBounds(20, 120, 130, 25);
        usrF = new JTextField("theaccountant");
        usrF.setEditable(false);
        usrF.setBounds(150, 120, 230, 25);

        pwdL = new JLabel("Password");
        pwdL.setOpaque(true);
        pwdL.setBackground(Color.gray);
        pwdL.setBounds(20, 160, 130, 25);
        pF = new JPasswordField("theaccountant");
        pF.setEditable(false);
        pF.setBounds(150, 160, 230, 25);
        
        connectButton = new JButton("Connect to Database");
        connectButton.setBounds(20, 200, 150, 35);
        connectButton.setBackground(Color.BLUE);
        connectButton.setForeground(Color.white); 
        connectButton.addActionListener(new ConnectListener());
        
        disconnectButton = new JButton("Disconnect From Database");
        disconnectButton.setBounds(170, 200, 200, 35);
        disconnectButton.setBackground(Color.RED);
        disconnectButton.setForeground(Color.white);
        disconnectButton.addActionListener(new DisconnectListener());

        // sql command panel
        cmdL = new JLabel("Enter An SQL Command");
        cmdL.setForeground(Color.blue);
        cmdL.setBounds(400, 10, 200, 25);
        
        commands = new JTextArea(5, 40);
        commands.setLineWrap(true);
        commands.setWrapStyleWord(true);
        commandSP = new JScrollPane(commands);
        commandSP.setBounds(400, 40, 400, 130);

        clearSQLButton = new JButton("Clear SQL Command");
        clearSQLButton.setBounds(420, 180, 180, 35);
        clearSQLButton.setBackground(Color.WHITE);
        clearSQLButton.setForeground(Color.RED);
        clearSQLButton.addActionListener(e -> commands.setText(""));

        executeButton = new JButton("Execute SQL Command");
        executeButton.setBounds(620, 180, 180, 35);
        executeButton.setBackground(Color.GREEN);
        executeButton.addActionListener(new ExecuteListener());
        
        // Connection label
        csL = new JLabel("NO CONNECTION ESTABLISHED");
        csL.setOpaque(true);
        csL.setBackground(Color.BLACK);
        csL.setForeground(Color.RED);
        csL.setBounds(20, 250, 780, 35);

        // sql execution result window panel
        erwL = new JLabel("SQL Execution Result Window");
        erwL.setForeground(Color.blue);
        erwL.setBounds(20, 300, 200, 25);
        
        resultsT = new JTable(new DefaultTableModel());
        resultsT.setBorder(null);
        resultSP = new JScrollPane(resultsT);
        resultSP.setBounds(20, 325, 780, 230);
        
        clearResultButton = new JButton("Clear Result Window");
        clearResultButton.setBounds(20, 560, 180, 35);
        clearResultButton.setBackground(Color.YELLOW);
        clearResultButton.addActionListener(e -> {
        	resultsT.setModel(new DefaultTableModel());
        });
        
        closeButton = new JButton("Close Application");
        closeButton.setBounds(620, 560, 180, 35);
        closeButton.setBackground(Color.RED); 
        closeButton.addActionListener(new CloseListener());
        
        // connection details panel
        add(cL);
        add(dbL);
        add(dbField);
        add(usrSL);
        add(userField);
        add(usrL);
        add(usrF);
        add(pwdL);
        add(pF);
        add(connectButton);
        add(disconnectButton);
        // sql command panel
        add(cmdL);
        add(clearSQLButton);
        add(executeButton);
        // sql execution result window panel
        add(erwL);
        add(clearResultButton);
        add(closeButton);
        add(commandSP);
        add(csL);
        add(resultSP);
    }
    
    // actionListener
    private class ConnectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String pwd = new String(pF.getPassword());
            try {
                Properties propertiesDB = new Properties();
                InputStream inDB = getClass().getResourceAsStream("operationslog.properties");

                if (inDB == null) {
                    // operations log properties file not found
                	
                	return;
                }
                // if successfully found operations log properties file, get info
                propertiesDB.load(inDB);
                String url = propertiesDB.getProperty("db.url");
                String driver = propertiesDB.getProperty("db.driver");

                Properties propertiesUser = new Properties();
                InputStream inUser = getClass().getResourceAsStream("theaccountant.properties");

                if (inUser == null) {
                    // couldn't find the accountant properties file
                	
                	return;
                }
                
                // if successfully found accountant properties file, get info
                propertiesUser.load(inUser);
                
                // if credentials aren't correct, say so
                if (!pwd.equals(propertiesUser.getProperty("db.password"))) {
                	csL.setText("NOT CONNECTED - User Credentials Do Not Match Properties File!");
                	csL.setBackground(Color.black);
                    csL.setForeground(Color.red);
                    return;
                }

                Class.forName(driver);
                connection = DriverManager.getConnection(url, "theaccountant", pwd);
                csL.setText("CONNECTED TO: " + url);
                csL.setBackground(Color.black);
                csL.setForeground(Color.yellow);
            } catch (Exception e1) {
            	// error!
            }
        }
    }
  
    private class DisconnectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {// make sure there even is a connection lol
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    csL.setText("NO CONNECTION ESTABLISHED");
                    csL.setBackground(Color.BLACK);
                    csL.setForeground(Color.RED);
                }
            }
            
            catch (SQLException ex) {
                // error!
            }
        }
    }
    
    private class ExecuteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (connection == null) {
                // no connection right now, ignore actiion
            	
            	return;
            }

            // otherwise, execute command
            String sql = commands.getText();
            sql = sql.replaceAll("where login_username = \"(\\w+)\"", "where login_username LIKE \"$1%\"");

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                boolean resutlbool = stmt.execute();
                if (resutlbool) {
                    ResultSet rs = stmt.getResultSet();
                    displayResultSet(rs);
                } else {
                    // error
                }
            } catch (SQLException ex){
            	// check if the error message contains "unknown column"
            	String errorMessage = ex.getMessage();
            	if (errorMessage.contains("Unknown column")) {
            		JOptionPane.showMessageDialog(p3A.this, "Error: " + errorMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
                } else if (errorMessage.contains("command denied")) {
                	JOptionPane.showMessageDialog(p3A.this, errorMessage, "Database error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // general error
                }
            }
        }
    }
    
    private class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Close connection if open
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                // Error closing connections
            	
            } finally {
                // Ensure the application exits regardless
                System.exit(0);
            }
        }
    }
    
    private void displayResultSet(ResultSet rs) throws SQLException {
        DefaultTableModel TM = new DefaultTableModel();
        
        try {
        	ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();

            // Add columns according to metadata
            for (int i = 1; i <= count; i++) {
                TM.addColumn(metaData.getColumnName(i));
            }
            
            // Add rows to TM
            while (rs.next()) {
                Object[] row = new Object[count];
                for (int i = 1; i <= count; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                TM.addRow(row);
            }
            
            // Set the model to resultsT
            resultsT.setModel(TM);

            DefaultTableCellRenderer hr = new DefaultTableCellRenderer();
            hr.setHorizontalAlignment(SwingConstants.LEFT);
            
            for (int i = 0; i < resultsT.getColumnModel().getColumnCount(); i++) {
            	resultsT.getColumnModel().getColumn(i).setHeaderRenderer(hr);
            }
        } catch (SQLException e) {
        	// error! couldn't display results
        }
    }
  
    public static void main(String[] args) {
    	// for some reason I couldn't get the colors to show on my computer so this was the only way 
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new p3A().setVisible(true));
    }
}