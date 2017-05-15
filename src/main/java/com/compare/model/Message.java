package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class Message {

    private String id;
    private String createdDateTime;
    private String sendDateTime;
    private String from;
    private String to;
    private String content;
    private int isRead = 0;

    public String getId() {
        return id;
    }

    public Message generate(DatabaseManager connection) {
        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.sendDateTime = connection.generateTimestamp();
        this.from = connection.generateRandomString(50);
        this.to = connection.generateRandomString(50);
        this.content = connection.generateRandomString(255);
        return this;
    }

    public Message parse(ResultSet rs) throws SQLException {

        Message m = new Message();
        m.id = rs.getString(1);
        m.createdDateTime = rs.getString(2);
        m.sendDateTime = rs.getString(3);
        m.from = rs.getString(4);
        m.to = rs.getString(5);
        m.content = rs.getString(6);
        m.isRead = rs.getInt(7);
        return m;
    }


    public String getSQLInsert(Message[] details) {

        if(null == details) {
            details = new Message[]{this};
        }
        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " (ID, CREATED_DATE_TIME, SEND_DATE_TIME, MESSAGE_FROM, MESSAGE_TO, CONTENT, IS_READ)" +
                " VALUES ";

        for (int i = 0; i < details.length - 1; i++) {
            sql = sql + getValueString(details[i]) + ", ";
        }
        sql = sql + getValueString(details[details.length - 1]);

        return sql;

    }

    public String getSQLSelect(String[] arr) {
        String sql = " SELECT * FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " WHERE ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }

    public String getSQLSelectAll() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE + ";";
    }

    public String getSQLSelectUnread() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " WHERE IS_READ = 0 ORDER BY RAND() LIMIT 1;";
    }

    public String getSQLSelectRead() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " WHERE IS_READ = 1 ORDER BY RAND() LIMIT 1;";
    }

    public String getSQLUpdate(String id) {

        return " UPDATE " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " SET " +
                " IS_READ = 1 " +
                " WHERE " +
                " ID = '" + id + "'";
    }

    public String getSQLDelete(String id) {

        return " DELETE FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " WHERE " +
                " ID = '" + id + "'";
    }

    public String getSQLDeleteMultiple(String[] arr) {
        String sql = " DELETE FROM " + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE +
                " WHERE ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }

    private String getValueString(Message detail) {
        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.sendDateTime + "', " +
                "'" + detail.from + "', " +
                "'" + detail.to + "', " +
                "'" + detail.content + "', " +
                "'" + detail.isRead + "'" +
                ")";
    }


}
