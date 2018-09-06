package com.joaquimley.smsparsing.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BankDetail {
    private SQLiteDatabase database;
    private MySQliteHelper dbHelper;


    public static final String TableName = "bankdetail";
    public static final String BANKID = "bankid";
    public static final String DATE = "date";
    public static final String ISDEBIT = "isdebit";
    public static final String AMOUNT = "amount";
    public static final String CURRENTBAL = "currentbal";
    public static final String EXPENCENAME = "expencename";


    public BankDetail(Context context)
    {
        dbHelper = new MySQliteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public boolean insert(ContentValues values)
    {
        long results = database.insert(TableName , null, values);
        if (results == -1)
            return false;
        else
            return true;
    }
}
