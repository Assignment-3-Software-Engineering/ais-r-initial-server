package org.aisr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectionManager {

    private static DbConnectionManager instance = null;
    private Connection connection;

    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    public static final String DATABASE_NAME = "ais-r-db";
    public static final String DATABASE_URL = MYSQL_URL + DATABASE_NAME;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    private DbConnectionManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            statement.addBatch( "CREATE DATABASE IF NOT EXISTS `" + DATABASE_NAME + "`");
            statement.addBatch( "USE `" + DATABASE_NAME + "`");
            statement.executeBatch();
            statement.close();

            System.out.println("Database connection successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DbConnectionManager getInstance() {
        if (instance == null) {
            instance = new DbConnectionManager();
        }
        return instance;
    }

    private void createTablesIfNotExists(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        String createUserTable = "CREATE TABLE IF NOT EXISTS user ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "reference_id INT, "
                + "role VARCHAR(50) NOT NULL, "
                + "status VARCHAR(50) NOT NULL, "
                + "username VARCHAR(50) UNIQUE, "
                + "password VARCHAR(255), "
                + "token VARCHAR(100) UNIQUE, "
                + "created_at DATETIME  DEFAULT CURRENT_TIMESTAMP, "
                + "updated_at DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (id))";

        String createStaffTable = "CREATE TABLE IF NOT EXISTS staff ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "role VARCHAR(50) NOT NULL, "
                + "full_name VARCHAR(100) NOT NULL, "
                + "address VARCHAR(255), "
                + "phone_number VARCHAR(15), "
                + "email VARCHAR(100) NOT NULL UNIQUE, "
                + "username VARCHAR(50) NOT NULL UNIQUE, "
                + "staff_id VARCHAR(50), "
                + "branch VARCHAR(50), "
                + "user_id INT, "
                + "position_type VARCHAR(50), "
                + "management_level VARCHAR(50), "
                + "FOREIGN KEY (user_id) REFERENCES user(id), "
                + "PRIMARY KEY (id))";

        String createRecruitsTable = "CREATE TABLE IF NOT EXISTS recruits ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "full_name VARCHAR(100) NOT NULL, "
                + "address VARCHAR(255) NOT NULL, "
                + "phone_number VARCHAR(15), "
                + "email VARCHAR(100) NOT NULL, "
                + "username VARCHAR(50) NOT NULL UNIQUE, "
                + "interview_date DATE, "
                + "highest_qualification_level VARCHAR(50), "
                + "department VARCHAR(50), "
                + "created_by INT, "
                + "user_id INT, "
                + "created_at DATETIME  DEFAULT CURRENT_TIMESTAMP, "
                + "updated_at DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (id), "
                + "FOREIGN KEY (user_id) REFERENCES user(id), "
                + "FOREIGN KEY (created_by) REFERENCES staff(id))";

        statement.execute(createUserTable);
        statement.execute(createStaffTable);
        statement.execute(createRecruitsTable);
        statement.close();
    }
    public void initializeDatabase() {
        try {
            createTablesIfNotExists(getConnection());
            System.out.println("Database initialize successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
