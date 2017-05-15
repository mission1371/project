package com.compare.model;

import com.compare.conf.Constants;
import com.compare.conf.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class Customer {

    private String id;
    private String createdDateTime;
    private String name;
    private String lastName;
    private String dateOfBirth;


    public Customer generate(DatabaseManager connection) {
        this.id = connection.generateOid();
        this.createdDateTime = connection.generateTimestamp();
        this.name = connection.generateRandomString(100);
        this.lastName = connection.generateRandomString(100);
        this.dateOfBirth = connection.getDate();
        return this;
    }

    public Customer parse(ResultSet rs) throws SQLException {

        Customer c = new Customer();
        c.id = rs.getString(1);
        c.createdDateTime = rs.getString(2);
        c.name = rs.getString(3);
        c.lastName = rs.getString(4);
        c.dateOfBirth = rs.getString(5);
        return c;
    }

    public String getSQLInsert() {
        return " INSERT INTO " + Constants.SCHEMA + "." + Constants.CUSTOMER_TABLE +
                " (ID, CREATED_DATE_TIME, NAME, LAST_NAME, DATE_OF_BIRTH)" +
                " VALUES ( " +
                "'" + this.id + "', " +
                "'" + this.createdDateTime + "', " +
                "'" + this.name + "', " +
                "'" + this.lastName + "', " +
                "'" + this.dateOfBirth + "'" +
                ");";
    }

    public String getId() {
        return id;
    }

    public String getSQLSelectOne() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_TABLE +
                " ORDER BY RAND() LIMIT 1;";
    }

    public String getSQLSelectAll() {
        return " SELECT * FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_TABLE + ";";
    }

    public String getSQLDelete(String id) {
        return " DELETE FROM " + Constants.SCHEMA + "." + Constants.CUSTOMER_TABLE +
                " WHERE ID = '" + id + "'";
    }

}
