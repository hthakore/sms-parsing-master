package com.joaquimley.smsparsing.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BankList {
    private SQLiteDatabase database;
    private MySQliteHelper dbHelper;


    public static final String TableName = "banklist";
    public static final String BANKNAME = "bankname";

    public BankList(Context context)
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

    public boolean isBankExist(String bankName){
        Cursor cur = database.query(TableName,null,BANKNAME +"= '"+bankName+"'",null,null,null,null);
        cur.moveToFirst();
        if (cur != null && cur.getCount()>0){
            return true;
        }
        return false;
    }

}
