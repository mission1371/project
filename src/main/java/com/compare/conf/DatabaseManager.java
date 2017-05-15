package com.compare.conf;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.joda.time.DateTime;

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

    public synchronized String generateOid() {
        return UUID.randomUUID().toString();
    }

    public int generateNumber(int precision) {
        precision = precision > 0 ? precision : 100000;
        return ((int) (Math.random() * 100000) % precision) + 1;
    }

    public float generateDecimal() {
        int numPrecision = ((int) ((Math.random() * 100) % 10)) + 1;
        int numScale = ((int) ((Math.random() * 10) % 4)) + 1;

        String precision = "";
        String scale = "";
        for (int i = 0; i < numPrecision; i++) {
            precision = precision + (int) (Math.random() * 10);
        }

        for (int k = 0; k < numScale; k++) {
            scale = scale + (int) (Math.random() * 10);
        }
        return Float.valueOf(precision + "." + scale);
    }

    public String generateTimestamp() {
        return DateTime.now().toString("yyyyMMddHHmmss");
    }

    public String getDate() {
        return DateTime.now().toString("yyyyMMdd");
    }


    public String generateRandomString(int len) {
        String text = "";
        String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789!^+%&/()=?_@.,<> ";

        for (int i = 0; i < len; i++) {
            text += chars.charAt((int) Math.floor(Math.random() * chars.length()));
        }

        return text;
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



