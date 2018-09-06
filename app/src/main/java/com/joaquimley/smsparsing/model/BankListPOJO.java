package com.joaquimley.smsparsing.model;

public class BankListPOJO {

    String id,bankname;

    public BankListPOJO(String id, String bankname) {
        this.id = id;
        this.bankname = bankname;
    }

    public String getId() {
        return id;
    }

    public String getBankname() {
        return bankname;
    }
}
