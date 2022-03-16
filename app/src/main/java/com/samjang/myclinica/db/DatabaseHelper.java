package com.samjang.myclinica.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="myClinica";
    public static final String TABLE_NAME="accounts";
    public static final String COL_1="id";
    public static final String COL_2="email";
    public static final String COL_3="password";
    public static final String COL_4="firstName";
    public static final String COL_5="middleName";
    public static final String COL_6="lastName";
    public static final String COL_7="phoneNum";
    public static final String COL_8="birthDay";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,email VARCHAR,password VARCHAR,firstName VARCHAR, middleName VARCHAR, lastName VARCHAR, phoneNum VARCHAR, birthday DATE)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }
}
