package com.compare.service;

import com.google.gson.JsonArray;

/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
public interface IDatabaseService {


    void createRecords();

    JsonArray readRecords();

    void updateRecords();

    void deleteRecords();
}
