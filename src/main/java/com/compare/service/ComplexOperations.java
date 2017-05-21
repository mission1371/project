package com.compare.service;

import com.compare.conf.DatabaseManager;
import com.compare.model.*;
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
public class ComplexOperations extends DatabaseManager {


    public void createRecords() throws SQLException {


        Customer customer = new Customer().generate(this);
        Message message = new Message().generate(this);
        CustomerMessage customerMessage = new CustomerMessage().generate(this, customer.getId(), message.getId());
        Invoice invoice = new Invoice().generate(this);

        CustomerAddress customerAddresses = new CustomerAddress();
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        CustomerPayment customerPayment = new CustomerPayment();

        int numOfAddress = generateNumber(5);
        CustomerAddress[] addresses = new CustomerAddress[numOfAddress];
        for (int i = 0; i < numOfAddress; i++) {
            addresses[i] = new CustomerAddress().generate(this, customer.getId());
        }

        float total = 0;
        int numOfInvoiceLine = generateNumber(10);
        InvoiceDetail[] details = new InvoiceDetail[numOfInvoiceLine];
        for (int k = 0; k < numOfInvoiceLine; k++) {
            details[k] = new InvoiceDetail().generate(this, invoice.getId());
            total = total + details[k].getLineTotal();
        }
        invoice.setTotal(total);
        invoice.setCustomerId(customer.getId());

        int numOfPayment = generateNumber(10);
        CustomerPayment[] payments = new CustomerPayment[numOfPayment];
        float remaining = total;
        for (int j = 0; j < numOfPayment; j++) {
            CustomerPayment payment = new CustomerPayment();
            payment.id = generateOid();
            payment.createdDateTime = generateTimestamp();
            payment.customerId = customer.getId();
            payment.invoiceId = invoice.getId();
            payment.total = total;
            payment.amount = total / numOfPayment;
            remaining = remaining - payment.amount;
            payment.remaining = remaining;
            payments[j] = payment;
        }

        getConnection().createStatement().executeUpdate(customer.getSQLInsert());
        getConnection().createStatement().executeUpdate(message.getSQLInsert(null));

        getConnection().createStatement().executeUpdate(customerMessage.getSQLInsert(null));
        getConnection().createStatement().executeUpdate(customerAddresses.getSQLInsert(addresses));
        getConnection().createStatement().executeUpdate(invoice.getSQLInsert(null));
        getConnection().createStatement().executeUpdate(invoiceDetail.getSQLInsert(details));
        getConnection().createStatement().executeUpdate(customerPayment.getSQLInsert(payments));

    }


    public JsonArray readRecords() throws SQLException {


        JsonArray result = new JsonArray();
        Customer customer = new Customer();

        ResultSet rs = getConnection().createStatement().executeQuery(customer.getSQLSelectAll());

        while (rs.next()) {
            JsonObject obj = fetchRecord(customer.parse(rs));
            result.add(obj);

        }

        return result;
    }


    public void updateRecords() throws SQLException {

        Customer customer = new Customer();
        Message message = new Message();
        CustomerMessage customerMessage = new CustomerMessage();
        Invoice invoice = new Invoice();
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        CustomerPayment customerPayment = new CustomerPayment();

        List<Message> messages = new ArrayList<Message>();
        List<CustomerMessage> messageRelations = new ArrayList<CustomerMessage>();
        List<Invoice> invoices = new ArrayList<Invoice>();
        List<InvoiceDetail> invoiceDetails = new ArrayList<InvoiceDetail>();
        List<CustomerPayment> invoiceRelations = new ArrayList<CustomerPayment>();

        ResultSet rs_master = getConnection().createStatement().executeQuery(customer.getSQLSelectOne());

        while (rs_master.next()) {
            Customer selected = customer.parse(rs_master);

            int num = generateNumber(9);
            for(int i = 0 ; i < num ; i++) {
                messages.add(new Message().generate(this));
                messageRelations.add(new CustomerMessage().generate(this, selected.getId(), messages.get(i).getId()));
            }

            for(int j = 0 ; j < num ; j++) {
                invoices.add(new Invoice().generate(this));
                float total = 0;
                int numOfInvoiceLine = generateNumber(10);
                for (int k = 0; k < numOfInvoiceLine; k++) {
                    InvoiceDetail line = new InvoiceDetail().generate(this, invoices.get(j).getId());
                    total = total + line.getLineTotal();
                    invoiceDetails.add(line);
                }
                invoices.get(j).setTotal(total);
                invoices.get(j).setCustomerId(selected.getId());

                int numOfPayment = generateNumber(10);
                float remaining = total;
                for (int l = 0; l < numOfPayment; l++) {
                    CustomerPayment payment = new CustomerPayment();
                    payment.id = generateOid();
                    payment.createdDateTime = generateTimestamp();
                    payment.customerId = customer.getId();
                    payment.invoiceId = invoices.get(j).getId();
                    payment.total = total;
                    payment.amount = total / numOfPayment;
                    remaining = remaining - payment.amount;
                    payment.remaining = remaining;
                    invoiceRelations.add(payment);
                }
            }


            getConnection().createStatement().executeUpdate(message.getSQLInsert(messages.toArray(new Message[messages.size()])));
            getConnection().createStatement().executeUpdate(customerMessage.getSQLInsert(messageRelations.toArray(new CustomerMessage[messageRelations.size()])));
            getConnection().createStatement().executeUpdate(invoice.getSQLInsert(invoices.toArray(new Invoice[invoices.size()])));
            getConnection().createStatement().executeUpdate(invoiceDetail.getSQLInsert(invoiceDetails.toArray(new InvoiceDetail[invoiceDetails.size()])));
            getConnection().createStatement().executeUpdate(customerPayment.getSQLInsert(invoiceRelations.toArray(new CustomerPayment[invoiceRelations.size()])));
        }
    }

    public void deleteRecords() throws SQLException {

        Customer customer = new Customer();
        CustomerMessage customerMessage = new CustomerMessage();
        CustomerAddress customerAddresses = new CustomerAddress();
        CustomerPayment customerPayment = new CustomerPayment();

        Message message = new Message();
        Invoice invoice = new Invoice();
        InvoiceDetail invoiceDetail = new InvoiceDetail();

        ResultSet rs_master = getConnection().createStatement().executeQuery(customer.getSQLSelectOne());

        while (rs_master.next()) {

            Customer selected = customer.parse(rs_master);
            /** get message relations **/
            ResultSet rs_message_relations = getConnection().createStatement().executeQuery(customerMessage.getSQLSelect(selected.getId()));

            List<String> arrMessage = new ArrayList<String>();
            List<String> arrRelation = new ArrayList<String>();
            while (rs_message_relations.next()) {
                CustomerMessage rel = customerMessage.parse(rs_message_relations);
                arrMessage.add(rel.getMessageId());
                arrRelation.add(rel.getId());
            }

            getConnection().createStatement().executeUpdate(message.getSQLDeleteMultiple(arrMessage.toArray(new String[arrMessage.size()])));
            getConnection().createStatement().executeUpdate(customerMessage.getSQLDeleteMultiple(arrRelation.toArray(new String[arrRelation.size()])));


            /** delete address **/
            getConnection().createStatement().executeUpdate(customerAddresses.getSQLDelete(selected.getId()));


            /** get invoice relations **/
            ResultSet rs_invoice_relations = getConnection().createStatement().executeQuery(customerPayment.getSQLSelect(selected.getId()));

            List<String> arrInvoice = new ArrayList<String>();
            List<String> arrInvoiceRelation = new ArrayList<String>();
            while (rs_invoice_relations.next()) {
                CustomerPayment rel = customerPayment.parse(rs_invoice_relations);
                arrInvoiceRelation.add(rel.id);
                arrInvoice.add(rel.invoiceId);
            }

            getConnection().createStatement().executeUpdate(invoiceDetail.getSQLDeleteMultiple(arrInvoice.toArray(new String[arrInvoice.size()])));
            getConnection().createStatement().executeUpdate(invoice.getSQLDeleteMultiple(arrInvoice.toArray(new String[arrInvoice.size()])));
            getConnection().createStatement().executeUpdate(customerPayment.getSQLDeleteMultiple(arrInvoiceRelation.toArray(new String[arrInvoiceRelation.size()])));

            /** ---> delete customer **/
            getConnection().createStatement().executeUpdate(customer.getSQLDelete(selected.getId()));

        }
    }


    private JsonObject fetchRecord(Customer customer) throws SQLException {

        Gson gson = new Gson();
        JsonObject customerJson = new JsonObject();
        customerJson.add("customer", gson.toJsonTree(customer));

        Message message = new Message();
        Invoice invoice = new Invoice();
        CustomerMessage customerMessage = new CustomerMessage();
        CustomerAddress customerAddress = new CustomerAddress();
        CustomerPayment customerPayment = new CustomerPayment();


        /** fetch messages **/
        ResultSet rs_message = getConnection().createStatement().executeQuery(customerMessage.getSQLSelectMessages(customer.getId()));
        JsonArray messages = new JsonArray();
        while (rs_message.next()) {
            messages.add(gson.toJsonTree(message.parse(rs_message)));
        }

        /** fetch addresses **/
        ResultSet rs_address = getConnection().createStatement().executeQuery(customerAddress.getSQLSelect(customer.getId()));
        JsonArray addresses = new JsonArray();
        while (rs_address.next()) {
            addresses.add(gson.toJsonTree(customerAddress.parse(rs_address)));
        }

        /** fetch payments **/
        ResultSet rs_payment = getConnection().createStatement().executeQuery(customerPayment.getSQLSelect(customer.getId()));
        JsonArray payments = new JsonArray();
        while (rs_payment.next()) {
            payments.add(gson.toJsonTree(customerPayment.parse(rs_payment)));
        }

        /** fetch invoices **/
        ResultSet rs_invoice = getConnection().createStatement().executeQuery(customerPayment.getSQLSelectInvoices(customer.getId()));
        JsonArray invoices = new JsonArray();
        while (rs_invoice.next()) {
            invoices.add(gson.toJsonTree(invoice.parse(rs_invoice)));
        }


        customerJson.add("messages", messages);
        customerJson.add("addresses", addresses);
        customerJson.add("payments", payments);
        customerJson.add("invoices", invoices);

        return customerJson;
    }
}
