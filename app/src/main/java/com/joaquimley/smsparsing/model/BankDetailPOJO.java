package com.joaquimley.smsparsing.model;

public class BankDetailPOJO {

    String id,bankid,date,isdebit,amount,currentbal,expencename;

    public BankDetailPOJO(String id, String bankid, String date, String isdebit, String amount, String currentbal, String expencename) {
        this.id = id;
        this.bankid = bankid;
        this.date = date;
        this.isdebit = isdebit;
        this.amount = amount;
        this.currentbal = currentbal;
        this.expencename = expencename;
    }

    public String getId() {
        return id;
    }

    public String getBankid() {
        return bankid;
    }

    public String getDate() {
        return date;
    }

    public String getIsdebit() {
        return isdebit;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrentbal() {
        return currentbal;
    }

    public String getExpencename() {
        return expencename;
    }
}
