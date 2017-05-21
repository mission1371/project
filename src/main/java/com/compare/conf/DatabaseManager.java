package com.compare.conf;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
public class DatabaseManager {

    private MysqlConnectionPoolDataSource dataSource = null;
    private Connection connection;

    {
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("deneme");
        dataSource.setPort(3306);
    }

    protected Connection getConnection() {

        if (null == connection) {
            return createConnection();
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
        String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789 ";

        for (int i = 0; i < len; i++) {
            text += chars.charAt((int) Math.floor(Math.random() * chars.length()));
        }

        return text;
    }

    /**
     * ---------------------------- Private ----------------------
     **/


    private synchronized Connection createConnection() {
        try {
            connection = dataSource.getPooledConnection().getConnection();
        } catch (SQLException e) {
            System.out.println("failed to connect");
            e.printStackTrace();
        }
        return connection;
    }

}



