package com.joaquimley.smsparsing.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQliteHelper extends SQLiteOpenHelper {

    // Constants
    public static final String DatabaseName = "ExpenceDB.db";
    private static final int DATABASEVERSION = 1;

    Context _context ;
    public static final String ID = "_id";

    // Create DB to store To Do Tasks
    public MySQliteHelper(Context context) {
        super(context, DatabaseName, null, DATABASEVERSION);
        _context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE1_BANKDETAIL = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "( %s INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        " %s TEXT , %s TEXT ," +
                        " %s TEXT , %s TEXT ," +
                        " %s TEXT, %s TEXT )",
                BankDetail.TableName,ID,
                BankDetail.BANKID,
                BankDetail.DATE,BankDetail.ISDEBIT,
                BankDetail.AMOUNT,BankDetail.CURRENTBAL,
                BankDetail.EXPENCENAME);

        String CREATE1_BANKLIST = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "( %s INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        " %s TEXT)",
                BankList.TableName,ID,
                BankList.BANKNAME);

        db.execSQL(CREATE1_BANKDETAIL);
        db.execSQL(CREATE1_BANKLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
