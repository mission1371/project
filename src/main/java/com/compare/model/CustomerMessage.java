package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class CustomerMessage {


    private String id;
    private String createdDateTime;
    private String customerId;
    private String messageId;

    public CustomerMessage generate(DatabaseManager connection, String customerId, String messageId) {
        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.customerId = customerId;
        this.messageId = messageId;
        return this;
    }

    public CustomerMessage parse(ResultSet rs) throws SQLException {
        CustomerMessage message = new CustomerMessage();
        message.id = rs.getString(1);
        message.createdDateTime = rs.getString(2);
        message.customerId = rs.getString(3);
        message.messageId = rs.getString(4);
        return message;
    }

    public String getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSQLInsert(CustomerMessage[] details) {

        if (null == details) {
            details = new CustomerMessage[]{this};
        }

        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.CUSTOMER_MESSAGE_TABLE +
                " (ID, CREATED_DATE_TIME, CUSTOMER_ID, MESSAGE_ID)" +
                " VALUES ";

        for (int i = 0; i < details.length - 1; i++) {
            sql = sql + getValueString(details[i]) + ", ";
        }
        sql = sql + getValueString(details[details.length - 1]);

        return sql;
    }

    public String getSQLSelect(String customerId) {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_MESSAGE_TABLE +
                " WHERE CUSTOMER_ID = '" + customerId + "';";
    }

    public String getSQLSelectMessages(String customerId) {

        return " SELECT * FROM "
                + Constants.SCHEMA + "." + Constants.MESSAGE_TABLE + " m, "
                + Constants.SCHEMA + "." + Constants.CUSTOMER_MESSAGE_TABLE + " cm "
                + " WHERE cm.CUSTOMER_ID = '" + customerId + "'"
                + " AND m.ID = cm.MESSAGE_ID;";
    }

    public String getSQLDeleteMultiple(String[] arr) {
        String sql = " DELETE FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_MESSAGE_TABLE +
                " WHERE ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }

    private String getValueString(CustomerMessage detail) {
        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.customerId + "', " +
                "'" + detail.messageId + "'" +
                ")";
    }


}
