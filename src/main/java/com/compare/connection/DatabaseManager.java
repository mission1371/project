package com.compare.connection;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
public class DatabaseManager {

    private Connection connection;

    protected Connection getConnection() {

        if (null == connection) {
            connection = createConnection();
        }
        return connection;
    }

    protected void closeConnection() {

        try {
            System.out.println("trying to close connection");
            connection.close();
            connection = null;
        } catch (SQLException e) {
            System.out.println("failed to close connection");
            e.printStackTrace();
        }

    }

    protected synchronized String generateOid() {
        return UUID.randomUUID().toString();
    }


    /**
     * ---------------------------- Private ----------------------
     **/


    private Connection createConnection() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("deneme");
        dataSource.setPort(3306);

        Connection conn = null;
        try {
            System.out.println("trying to connect");
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("failed to connect");
            e.printStackTrace();
        }
        return conn;
    }

}
