package com.compare.service;

import com.compare.conf.DatabaseManager;
import com.compare.model.Message;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class SimpleOperations extends DatabaseManager {


    public void createMessage() throws SQLException {

        Message message = new Message().generate(this);
        getConnection().createStatement().executeUpdate(message.getSQLInsert(null));

    }


    public JsonArray readMessages() throws SQLException {

        JsonArray arr = new JsonArray();
        Message message = new Message();
        Gson gson = new Gson();

        ResultSet rs = getConnection().createStatement().executeQuery(message.getSQLSelectAll());

        while (rs.next()) {
            arr.add(gson.toJsonTree(message.parse(rs)).getAsJsonObject());
        }

        return arr;
    }

    public void updateMessage() throws SQLException {

        Message message = new Message();
        ResultSet rs = getConnection().createStatement().executeQuery(message.getSQLSelectUnread());

        while (rs.next()) {
            Message selected = message.parse(rs);
            getConnection().createStatement().executeUpdate(selected.getSQLUpdate(selected.getId()));
        }

    }

    public void deleteOperation() throws SQLException {

        Message message = new Message();
        ResultSet rs = getConnection().createStatement().executeQuery(message.getSQLSelectRead());

        while (rs.next()) {
            Message selected = message.parse(rs);
            getConnection().createStatement().executeUpdate(selected.getSQLDelete(selected.getId()));
        }
    }
}
