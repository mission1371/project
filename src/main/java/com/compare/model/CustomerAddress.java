package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class CustomerAddress {

    private String id;
    private String createdDateTime;
    private String customerId;
    private String country;
    private String state;
    private String city;
    private String street;
    private String zipCode;
    private String buildingNumber;
    private String doorNumber;

    public CustomerAddress generate(DatabaseManager connection, String customerId) {
        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.customerId = customerId;
        this.country = connection.generateRandomString(50);
        this.state = connection.generateRandomString(50);
        this.city = connection.generateRandomString(50);
        this.street = connection.generateRandomString(255);
        this.zipCode = connection.generateRandomString(10);
        this.buildingNumber = connection.generateRandomString(10);
        this.doorNumber = connection.generateRandomString(10);
        return this;
    }

    public CustomerAddress parse(ResultSet rs) throws SQLException {

        CustomerAddress address = new CustomerAddress();
        address.id = rs.getString(1);
        address.createdDateTime = rs.getString(2);
        address.customerId = rs.getString(3);
        address.country = rs.getString(4);
        address.state = rs.getString(5);
        address.city = rs.getString(6);
        address.street = rs.getString(7);
        address.zipCode = rs.getString(8);
        address.buildingNumber = rs.getString(9);
        address.doorNumber = rs.getString(10);
        return address;
    }


    public String getSQLInsert(CustomerAddress[] details) {
        String sql = " INSERT INTO " + Constants.SCHEMA + "." + Constants.CUSTOMER_ADDRESS_TABLE +
                " (ID, CREATED_DATE_TIME, CUSTOMER_ID, COUNTRY, STATE, CITY, STREET, ZIP_CODE, BUILDING_NUMBER, DOOR_NUMBER)" +
                " VALUES ";

        for (int i = 0; i < details.length - 1; i++) {
            sql = sql + getValueString(details[i]) + ", ";
        }
        sql = sql + getValueString(details[details.length - 1]);

        return sql;
    }

    public String getSQLSelect(String customerId) {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_ADDRESS_TABLE +
                " WHERE CUSTOMER_ID = '" + customerId + "';";
    }

    public String getSQLDelete(String customerId) {

        return " DELETE FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_ADDRESS_TABLE +
                " WHERE CUSTOMER_ID = '" + customerId + "'";
    }


    private String getValueString(CustomerAddress detail) {

        return "( " +
                "'" + detail.id + "', " +
                "'" + detail.createdDateTime + "', " +
                "'" + detail.customerId + "', " +
                "'" + detail.country + "', " +
                "'" + detail.state + "', " +
                "'" + detail.city + "', " +
                "'" + detail.street + "', " +
                "'" + detail.zipCode + "', " +
                "'" + detail.buildingNumber + "', " +
                "'" + detail.doorNumber + "'" +
                ")";
    }

}
