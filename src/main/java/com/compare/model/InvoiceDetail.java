package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class InvoiceDetail {

    private String id;
    private String createdDateTime;
    private String invoiceId;
    private String itemName;
    private String itemDescription;
    private float unitPrice = 0;
    private float quantity = 0;
    private float lineTotal = 0;

    public InvoiceDetail generate(DatabaseManager connection, String invoiceId) {

        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.invoiceId = invoiceId;
        this.itemName = connection.generateRandomString(100);
        this.itemDescription = connection.generateRandomString(1000);
        this.unitPrice = connection.generateDecimal();
        this.quantity = connection.generateNumber(10);
        this.lineTotal = this.unitPrice * this.quantity;

        return this;
    }

    public InvoiceDetail parse(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();

        detail.id = rs.getString(1);
        detail.createdDateTime = rs.getString(2);
        detail.invoiceId = rs.getString(3);
        detail.itemName = rs.getString(4);
        detail.itemDescription = rs.getString(5);
        detail.unitPrice = rs.getFloat(6);
        detail.quantity = rs.getFloat(7);
        detail.lineTotal = rs.getFloat(8);

        return detail;
    }

    public float getLineTotal() {
        return lineTotal;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setLineTotal(float lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getSQLInsert(InvoiceDetail[] details) {

        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.INVOICE_DETAIL_TABLE +
                " (ID, CREATED_DATE_TIME, INVOICE_ID, ITEM_NAME, ITEM_DESCRIPTION, UNIT_PRICE, QUANTITY, LINE_TOTAL)" +
                " VALUES ";

        for (int i = 0; i < details.length - 1; i++) {
            sql = sql + getValueString(details[i]) + ", ";
        }
        sql = sql + getValueString(details[details.length - 1]);

        return sql;
    }

    public String getSQLSelect(String invoiceId) {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.INVOICE_DETAIL_TABLE +
                " WHERE INVOICE_ID = '" + invoiceId + "';";
    }

    public String getSQLUpdate(InvoiceDetail[] details) {

        String sql = " UPDATE " + Constants.SCHEMA + "." + Constants.INVOICE_DETAIL_TABLE +
                " SET ";

        String sqlUnitPrice = " UNIT_PRICE = ( case ";
        String sqlQuantity = " QUANTITY = ( case ";
        String sqlLineTotal = " LINE_TOTAL = ( case ";
        String ids = "";
        for (int i = 0; i < details.length; i++) {
            sqlUnitPrice = sqlUnitPrice + " when ID = '" + details[i].id + "' then '" + details[i].unitPrice + "'";
            sqlQuantity = sqlQuantity + " when ID = '" + details[i].id + "' then '" + details[i].quantity + "'";
            sqlLineTotal = sqlLineTotal + " when ID = '" + details[i].id + "' then '" + details[i].lineTotal + "'";
            ids = ids + ", '" + details[i].id + "'";
        }

        sqlUnitPrice = sqlUnitPrice + " end )";
        sqlQuantity = sqlQuantity + " end )";
        sqlLineTotal = sqlLineTotal + " end )";

        sql = sql + sqlUnitPrice + ", " + sqlQuantity + ", " + sqlLineTotal + " WHERE ID in ( " + ids.substring(1, ids.length()) + " )";
        return sql;
    }

    public String getSQLDelete(String id) {

        return " DELETE FROM " + Constants.SCHEMA + "." + Constants.INVOICE_DETAIL_TABLE +
                " WHERE " +
                " INVOICE_ID = '" + id + "'";
    }

    public String getSQLDeleteMultiple(String[] arr) {
        String sql = " DELETE FROM " + Constants.SCHEMA + "." + Constants.INVOICE_DETAIL_TABLE +
                " WHERE INVOICE_ID IN ( ";

        for (int i = 0; i < arr.length - 1; i++) {
            sql += "'" + arr[i] + "', ";
        }
        return sql + "'" + arr[arr.length - 1] + "' );";
    }

    private String getValueString(InvoiceDetail detail) {

        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.invoiceId + "', " +
                "'" + detail.itemName + "', " +
                "'" + detail.itemDescription + "', " +
                "'" + detail.unitPrice + "', " +
                "'" + detail.quantity + "', " +
                "'" + detail.lineTotal + "'" +
                ")";
    }

}
