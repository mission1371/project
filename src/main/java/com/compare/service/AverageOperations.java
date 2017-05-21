package com.compare.service;

import com.compare.conf.DatabaseManager;
import com.compare.model.Invoice;
import com.compare.model.InvoiceDetail;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by umut.taherzadeh on 2017-05-14.
 */
public class AverageOperations extends DatabaseManager {


    public void createInvoice() throws SQLException {

        Invoice invoice = new Invoice().generate(this);
        InvoiceDetail invoiceDetail = new InvoiceDetail();

        float total = 0;
        int lineNumber = generateNumber(10);
        InvoiceDetail[] details = new InvoiceDetail[lineNumber];
        for (int i = 0; i < lineNumber; i++) {

            InvoiceDetail line = new InvoiceDetail().generate(this, invoice.getId());
            total = total + line.getLineTotal();
            details[i] = line;
        }

        invoice.setTotal(total);

        getConnection().createStatement().executeUpdate(invoice.getSQLInsert(null));
        getConnection().createStatement().executeUpdate(invoiceDetail.getSQLInsert(details));

    }


    public JsonArray readInvoice() throws SQLException {

        Gson gson = new Gson();

        Invoice invoice = new Invoice().generate(this);
        InvoiceDetail invoiceDetail = new InvoiceDetail();

        JsonArray results = new JsonArray();

        ResultSet rs_invoices = getConnection().createStatement().executeQuery(invoice.getSQLSelectAll());

        while (rs_invoices.next()) {
            Invoice i = invoice.parse(rs_invoices);

            JsonArray lines = new JsonArray();
            ResultSet rs_lines = getConnection().createStatement().executeQuery(invoiceDetail.getSQLSelect(i.getId()));
            while (rs_lines.next()) {
                lines.add(gson.toJsonTree(invoiceDetail.parse(rs_lines)));
            }

            JsonObject obj = new JsonObject();
            obj.add("master", gson.toJsonTree(i));
            obj.add("detail", lines);

            results.add(obj);
        }


        return results;


    }

    public void updateInvoice() throws SQLException {

        Invoice invoice = new Invoice();
        InvoiceDetail invoiceDetail = new InvoiceDetail();

        ResultSet rs_master = getConnection().createStatement().executeQuery(invoice.getSQLSelectOne());

        while (rs_master.next()) {
            Invoice selected = invoice.parse(rs_master);
            float total = 0;

            ResultSet rs_detail = getConnection().createStatement().executeQuery(invoiceDetail.getSQLSelect(selected.getId()));
            List<InvoiceDetail> lines = new ArrayList<InvoiceDetail>();

            while (rs_detail.next()) {
                InvoiceDetail line = invoiceDetail.parse(rs_detail);
                line.setUnitPrice(generateDecimal());
                line.setQuantity(generateNumber(10));
                line.setLineTotal(line.getUnitPrice() * line.getQuantity());
                total = total + line.getLineTotal();
                lines.add(line);
            }

            getConnection().createStatement().executeUpdate(selected.getSQLUpdate(selected.getId(), total));
            if(lines.isEmpty()) {
                continue;
            }
            getConnection().createStatement().executeUpdate(invoiceDetail.getSQLUpdate(lines.toArray(new InvoiceDetail[lines.size()])));

        }


    }

    public void deleteInvoice() throws SQLException {


        Invoice invoice = new Invoice().generate(this);
        InvoiceDetail invoiceDetail = new InvoiceDetail();


        ResultSet rs_master = getConnection().createStatement().executeQuery(invoice.getSQLSelectOne());

        while (rs_master.next()) {
            Invoice selected = invoice.parse(rs_master);
            getConnection().createStatement().executeUpdate(invoice.getSQLDelete(selected.getId()));
            getConnection().createStatement().executeUpdate(invoiceDetail.getSQLDelete(selected.getId()));


        }
    }
}
