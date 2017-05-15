package com.compare.model;

import com.compare.conf.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class CustomerPayment {


    public String id;
    public String createdDateTime;
    public String customerId;
    public String invoiceId;
    public float total = 0;
    public float amount = 0;
    public float remaining = 0;

    public CustomerPayment parse(ResultSet rs) throws SQLException {

        CustomerPayment payment = new CustomerPayment();
        payment.id = rs.getString(1);
        payment.createdDateTime = rs.getString(2);
        payment.customerId = rs.getString(3);
        payment.invoiceId = rs.getString(4);
        payment.total  = rs.getFloat(5);
        payment.amount  = rs.getFloat(6);
        payment.remaining  = rs.getFloat(7);
        return payment;
    }



    public String getSQLInsert(CustomerPayment[] payments) {

        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.CUSTOMER_PAYMENT_TABLE +
                " (ID, CREATED_DATE_TIME, CUSTOMER_ID, INVOICE_ID, TOTAL, AMOUNT, REMAINING)" +
                " VALUES ";

        for (int i = 0; i < payments.length - 1; i++) {
            sql = sql + getValueString(payments[i]) + ", ";
        }
        sql = sql + getValueString(payments[payments.length - 1]);

        return sql;
    }

    public String getSQLSelect(String customerId) {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_PAYMENT_TABLE +
                " WHERE CUSTOMER_ID = '" + customerId + "';";
    }

    public String getSQLSelectInvoices(String customerId) {

        return " SELECT * FROM "
                + Constants.SCHEMA + "." + Constants.INVOICE_TABLE + " i, "
                + Constants.SCHEMA + "." + Constants.CUSTOMER_PAYMENT_TABLE + " cp "
                + " WHERE cp.CUSTOMER_ID = '" + customerId + "'"
                + " AND i.ID = cp.INVOICE_ID;";

    }

    ;

    public String getSQLDeleteMultiple(String[] arr) {
        String sql = " DELETE FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_PAYMENT_TABLE +
                " WHERE ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }


    private String getValueString(CustomerPayment detail) {

        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.customerId + "', " +
                "'" + detail.invoiceId + "', " +
                "'" + detail.total + "', " +
                "'" + detail.amount + "', " +
                "'" + detail.remaining + "'" +
                ")";
    }


}
