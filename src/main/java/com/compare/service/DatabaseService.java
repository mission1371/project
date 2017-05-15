package com.compare.service;

import com.compare.conf.DatabaseManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
public class DatabaseService extends DatabaseManager implements IDatabaseService {

    @Override
    public void createRecords() {

        Statement stmt = null;

        try {
            stmt = getConnection().createStatement();

            for (int i = 0; i < 5; i++) {
                String sql = new StringBuilder()
                        .append(" INSERT INTO deneme.simple_table ")
                        .append("(")
                        .append("OID, CREATED, COLUMN1, COLUMN2, COLUMN3")
                        .append(")")
                        .append(" VALUES ")
                        .append("(")
                        .append("'" + generateOid() + "', ")
                        .append("'" + new DateTime().toString("yyyyMMddHHmmss") + "', ")
                        .append("'col 1 value', ")
                        .append("'col 2 value', ")
                        .append("'col 3 value'")
                        .append(")")
                        .append(";")
                        .toString();
                stmt.executeUpdate(sql);
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonArray readRecords() {
        ResultSet rs;
        Statement stmt;
        JsonArray returnArr = new JsonArray();
        try {
            stmt = getConnection().createStatement();

            String select = new StringBuilder()
                    .append(" SELECT ")
                    .append(" * ")
                    .append(" FROM ")
                    .append(" deneme.simple_table ")
                    .toString();

            rs = stmt.executeQuery(select);

            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("oid", rs.getString(1));
                obj.addProperty("created", rs.getString(2));
                obj.addProperty("column1", rs.getString(3));
                obj.addProperty("column2", rs.getString(4));
                obj.addProperty("column3", rs.getString(5));
                returnArr.add(obj);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnArr;
    }

    @Override
    public void updateRecords() {

        ResultSet rs;
        Statement stmt;
        try {
            stmt = getConnection().createStatement();

            String select = new StringBuilder()
                    .append(" SELECT ")
                    .append(" * ")
                    .append(" FROM ")
                    .append(" deneme.simple_table ")
                    .toString();

            rs = stmt.executeQuery(select);

            while (rs.next()) {

                String update = new StringBuilder()
                        .append(" UPDATE ")
                        .append(" deneme.simple_table ")
                        .append(" SET ")
                        .append(" COLUMN1 = '" + String.valueOf(Math.random() * 5000) + "', ")
                        .append(" COLUMN2 = '" + String.valueOf(Math.random() * 5000) + "', ")
                        .append(" COLUMN3 = '" + String.valueOf(Math.random() * 5000) + "'  ")
                        .append(" WHERE ")
                        .append(" OID = '" + rs.getString(1) + "'")
                        .toString();
                Statement updateStmt = getConnection().createStatement();
                updateStmt.executeUpdate(update);
                updateStmt.close();
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteRecords() {

        ResultSet rs;
        Statement stmt;
        try {
            stmt = getConnection().createStatement();

            String select = new StringBuilder()
                    .append(" SELECT ")
                    .append(" * ")
                    .append(" FROM ")
                    .append(" deneme.simple_table ")
                    .toString();

            rs = stmt.executeQuery(select);

            while (rs.next()) {

                String delete = new StringBuilder()
                        .append(" DELETE FROM ")
                        .append(" deneme.simple_table ")
                        .append(" WHERE ")
                        .append(" OID = '" + rs.getString(1) + "'")
                        .toString();
                Statement updateStmt = getConnection().createStatement();
                updateStmt.executeUpdate(delete);
                updateStmt.close();
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
