package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class Invoice {

    private String id;
    private String createdDateTime;
    private String customerId;
    private int invoiceNumber;
    private String invoiceDate;
    private String dueDate;
    private float total = 0;
    private float discount = 0;

    public Invoice generate(DatabaseManager connection) {
        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.customerId = connection.generateOid();
        this.invoiceNumber = connection.generateNumber(0);
        this.invoiceDate = connection.getDate();
        this.dueDate = connection.getDate();
        this.discount = (float) (Math.random() * 10);
        return this;
    }

    public Invoice parse(ResultSet rs) throws SQLException {

        Invoice invoice = new Invoice();
        invoice.id = rs.getString(1);
        invoice.createdDateTime = rs.getString(2);
        invoice.customerId = rs.getString(3);
        invoice.invoiceNumber = rs.getInt(4);
        invoice.invoiceDate = rs.getString(5);
        invoice.dueDate = rs.getString(6);
        invoice.discount = rs.getFloat(7);

        return invoice;
    }

    public String getId() {
        return id;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSQLInsert(Invoice[] details) {

        if (null == details) {
            details = new Invoice[]{this};
        }
        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE +
                " (ID, CREATED_DATE_TIME, CUSTOMER_ID, INVOICE_NUMBER, INVOICE_DATE, DUE_DATE, TOTAL, DISCOUNT)" +
                " VALUES ";

        for (int i = 0; i < details.length - 1; i++) {
            sql = sql + getValueString(details[i]) + ", ";
        }
        sql = sql + getValueString(details[details.length - 1]);

        return sql;

    }

    public String getSQLSelectAll() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE + ";";
    }

    public String getSQLSelectOne() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE +
                " ORDER BY RAND() LIMIT 1;";
    }

    public String getSQLUpdate(String id, float total) {

        return " UPDATE " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE +
                " SET " +
                " TOTAL = " + total +
                " WHERE " +
                " ID = '" + id + "'";
    }

    public String getSQLDelete(String id) {

        return " DELETE FROM " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE +
                " WHERE ID = '" + id + "'";
    }

    public String getSQLDeleteMultiple(String[] arr) {
        String sql = " DELETE FROM " + Constants.SCHEMA + "." + Constants.INVOICE_TABLE +
                " WHERE ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }

    private String getValueString(Invoice detail) {

        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.customerId + "', " +
                "'" + detail.invoiceNumber + "', " +
                "'" + detail.invoiceDate + "', " +
                "'" + detail.dueDate + "', " +
                "'" + detail.total + "', " +
                "'" + detail.discount + "'" +
                ")";
    }


}
