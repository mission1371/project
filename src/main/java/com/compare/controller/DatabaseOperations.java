package com.compare.controller;

import com.compare.service.AverageOperations;
import com.compare.service.ComplexOperations;
import com.compare.service.SimpleOperations;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
@RestController
@RequestMapping(value = "/v1/service/database/", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
public class DatabaseOperations {

    @Autowired
    private SimpleOperations simple;

    @Autowired
    private AverageOperations average;

    @Autowired
    private ComplexOperations complex;


    /**
     * ------------------- Simple Operations -------------------
     **/

    @ResponseBody
    @RequestMapping("simple/createOperation")
    public String simpleCreateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        simple.createMessage();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("simple/readOperation")
    public String simpleReadOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        return generateReturn(snap, simple.readMessages());

    }

    @ResponseBody
    @RequestMapping("simple/updateOperation")
    public String simpleUpdateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        simple.updateMessage();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("simple/deleteOperation")
    public String simpleDeleteOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        simple.deleteOperation();

        return generateReturn(snap);

    }


    /**
     * ------------------- Average Operations -------------------
     **/


    @ResponseBody
    @RequestMapping("average/createOperation")
    public String averageCreateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        average.createInvoice();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("average/readOperation")
    public String averageReadOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        return generateReturn(snap, average.readInvoice());

    }

    @ResponseBody
    @RequestMapping("average/updateOperation")
    public String averageUpdateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        average.updateInvoice();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("average/deleteOperation")
    public String averageDeleteOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        average.deleteInvoice();

        return generateReturn(snap);

    }


    /**
     * ------------------- Complex Operations -------------------
     **/


    @ResponseBody
    @RequestMapping("complex/createOperation")
    public String complexCreateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        complex.createRecords();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("complex/readOperation")
    public String complexReadOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        return generateReturn(snap, complex.readRecords());

    }

    @ResponseBody
    @RequestMapping("complex/updateOperation")
    public String complexUpdateOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        complex.updateRecords();

        return generateReturn(snap);

    }

    @ResponseBody
    @RequestMapping("complex/deleteOperation")
    public String complexDeleteOperation() throws SQLException {

        SnapshotSystem snap = new SnapshotSystem();
        complex.deleteRecords();

        return generateReturn(snap);

    }


//
//
//
//
//    @ResponseBody
//    @RequestMapping("readOperation")
//    public String readOperation() {
//
//
//        SnapshotSystem snap = new SnapshotSystem();
////        System.out.println("init free memory --> " + (snap.freeMemory / (1024 * 1024)));
//        JsonArray records = service.readRecords();
//
//        return generateReturn(snap, records);
//    }
//
//    @ResponseBody
//    @RequestMapping("updateOperation")
//    public String updateOperation() {
//
//        SnapshotSystem snap = new SnapshotSystem();
//        service.updateRecords();
//
//        return generateReturn(snap);
//    }
//
//
//    @ResponseBody
//    @RequestMapping("deleteOperation")
//    public String deleteOperation() {
//
//        SnapshotSystem snap = new SnapshotSystem();
//        service.deleteRecords();
//
//        return generateReturn(snap);
//    }

    private String generateReturn(SnapshotSystem preSnap) {
        return generateReturn(preSnap, null);
    }

    private String generateReturn(SnapshotSystem preSnap, JsonElement records) {
        int mb = 1024 * 1024;

        SnapshotSystem postSnap = new SnapshotSystem();
//        System.out.println("final free memory --> " + (postSnap.freeMemory / mb));


        long duration = postSnap.time.getMillis() - preSnap.time.getMillis();
        long used = (preSnap.freeMemory - postSnap.freeMemory) / mb;

        System.out.println(duration);

        JsonObject ret = new JsonObject();
        ret.addProperty("success", "true");
        ret.add("data", records);

        ret.addProperty("duration", duration);
        ret.addProperty("used", used);
        // TODO memory usage
        // TODO cpu usage, cycle

        return ret.toString();


//        NumberFormat format = NumberFormat.getInstance();
//
//        StringBuilder sb = new StringBuilder();
//        long maxMemory = runtime.maxMemory();
//        long allocatedMemory = runtime.totalMemory();
//        long freeMemory = runtime.freeMemory();

//        sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
//        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
//        sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
//        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");


    }

    private class SnapshotSystem {

        DateTime time;
        long maxMemory;
        long allocatedMemory;
        long freeMemory;

        public SnapshotSystem() {
            Runtime runtime = Runtime.getRuntime();
            this.time = new DateTime();
            this.maxMemory = runtime.maxMemory();
            this.allocatedMemory = runtime.totalMemory();
            this.freeMemory = runtime.freeMemory();
        }

    }
}
