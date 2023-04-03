package com.britefull.rollbook.RollLog;

/*
This code builds a database history of the attendance data and saves it locally
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RollDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "history.db";
    private static final int DATABASE_VERSION = 1;

    public RollDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create roll history table
        db.execSQL(RollEntry.CREATE_TABLE);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + RollEntry.TABLE_NAME);
        db.close();
        onCreate(db);
    }

    public long insertEntry(RollEntry rollEntry){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RollEntry.COLUMN_DATE, rollEntry.getDate());
        values.put(RollEntry.COLUMN_DATA, rollEntry.getRollData());

        long id = db.insert(RollEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<RollEntry> getAllEntries(){
        ArrayList<RollEntry> entryList = new ArrayList<>();

        // Select All Query
        String selectAllQuery = "SELECT * FROM " + RollEntry.TABLE_NAME + " ORDER BY "
                + RollEntry.COLUMN_DATE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        // Loops through all db rows and adds each entry in db to entryList ArrayList
        if(cursor.moveToFirst()){
            do{
                RollEntry mEntry = new RollEntry(
                        cursor.getString(cursor.getColumnIndex(RollEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(RollEntry.COLUMN_DATA)));

                entryList.add(mEntry);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entryList;
    }

    public void deleteAllEntries(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + RollEntry.TABLE_NAME);
        db.close();
    }


}
