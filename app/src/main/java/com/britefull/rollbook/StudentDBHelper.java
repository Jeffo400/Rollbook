/*
This code extends SQLiteOpenHelper
This code is used to handle the SQLite Database for storing student data
This code uses Student.java which is the Student object to handle student information coming
    in and out of the table
 */

package com.britefull.rollbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class StudentDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sonlight.db";
    private static final int DATABASE_VERSION = 2;

    // Sets the database name and version
    StudentDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create classroom table
        db.execSQL(Student.CREATE_TABLE);
        db.close();
    }

    public void createTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Student.CREATE_TABLE);
        db.close();
    }

    public void deleteAllStudents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Student.DELETE_TABLE); // Deletes the SQLite table storing students
        db.execSQL(Student.CREATE_TABLE); // Recreates a fresh table to store students
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        try{
            db.execSQL("ALTER TABLE " + Student.TABLE_NAME
                    + " ADD COLUMN " + Student.COLUMN_CLASS1 + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + Student.TABLE_NAME
                    + " ADD COLUMN " + Student.COLUMN_CLASS2 + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + Student.TABLE_NAME
                    + " ADD COLUMN " + Student.COLUMN_CHECKOUT + " INTEGER DEFAULT 0");
        } catch (Exception e){
        }
    }

    public long insertStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_FNAME, student.getFirstName());
        values.put(Student.COLUMN_LNAME, student.getLastName());
        values.put(Student.COLUMN_GRADE, student.getGrade());
        values.put(Student.COLUMN_GENDER, student.getGender());
        values.put(Student.COLUMN_PARENT1, student.getParent1());
        values.put(Student.COLUMN_PARENT2, student.getParent2());
        values.put(Student.COLUMN_ALLERGIES, student.getAllergies());
        values.put(Student.COLUMN_NOTES, student.getNotes());

        long id = db.insert(Student.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /*
    public Student getStudent(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ID, Student.COLUMN_FNAME, Student.COLUMN_LNAME,
                    Student.COLUMN_GRADE, Student.COLUMN_GENDER, Student.COLUMN_PARENT1,
                    Student.COLUMN_PARENT2, Student.COLUMN_ALLERGIES, Student.COLUMN_NOTES},
                Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Student mStudent = new Student(cursor.getString(cursor.getColumnIndex(Student.COLUMN_FNAME)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_LNAME)),
                cursor.getInt(cursor.getColumnIndex(Student.COLUMN_GRADE)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT1)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT2)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_ALLERGIES)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_NOTES)));

        cursor.close();
        db.close();

        return mStudent;
    }
    */

    /**
     * Loops through the db table "classroom" to find all students
     * @return Returns an ArrayList of type Student from db, containing all students
     */
    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> studentList = new ArrayList<>();

        // Select All Query
        // Orders by last name then first name
        String selectAllQuery = "SELECT * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_LNAME + ", " +
                Student.COLUMN_FNAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        // Loops through all db rows and adds each student in db to studentList ArrayList
        if(cursor.moveToFirst()){
            do{
                Student mStudent = new Student(
                        cursor.getLong(cursor.getColumnIndex(Student.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_FNAME)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_LNAME)),
                        cursor.getInt(cursor.getColumnIndex(Student.COLUMN_GRADE)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT1)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT2)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_ALLERGIES)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_NOTES)),
                        getBoolean(cursor, Student.COLUMN_CLASS1),
                        getBoolean(cursor, Student.COLUMN_CLASS2),
                        getBoolean(cursor, Student.COLUMN_CHECKOUT)
                );

                studentList.add(mStudent);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return studentList;
    }

    /**
     * Uses Cursor to select all students in "classroom" table
     * @return Returns total number of students through an integer
     */
    /*
    public int getStudentCount(){
        String countQuery = "SELECT * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }
    */

    public void updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_FNAME, student.getFirstName());
        values.put(Student.COLUMN_LNAME, student.getLastName());
        values.put(Student.COLUMN_GRADE, student.getGrade());
        values.put(Student.COLUMN_GENDER, student.getGender());
        values.put(Student.COLUMN_PARENT1, student.getParent1());
        values.put(Student.COLUMN_PARENT2, student.getParent2());
        values.put(Student.COLUMN_ALLERGIES, student.getAllergies());
        values.put(Student.COLUMN_NOTES, student.getNotes());

        db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?", new String[]{String.valueOf(student.getID())});
        db.close();
    }

    public void updateStatus(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(student.getClass1()){
            values.put(Student.COLUMN_CLASS1, 1);
        } else {
            values.put(Student.COLUMN_CLASS1, 0);
        }
        if(student.getClass2()){
            values.put(Student.COLUMN_CLASS2, 1);
        } else {
            values.put(Student.COLUMN_CLASS2, 0);
        }
        if(student.getCheckOut()){
            values.put(Student.COLUMN_CHECKOUT, 1);
        } else {
            values.put(Student.COLUMN_CHECKOUT, 0);
        }

        db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?", new String[]{String.valueOf(student.getID())});
        db.close();
    }

    public void resetStatus(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_CLASS1, 0);
        values.put(Student.COLUMN_CLASS2, 0);
        values.put(Student.COLUMN_CHECKOUT, 0);

        db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?", new String[]{String.valueOf(student.getID())});
        db.close();
    }

    /**
     * Deletes student from database "classroom" table using Student object
     * that is passed in.
     * @param student Accepts parameter of type Student
     */
    public void deleteStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Student.TABLE_NAME, Student.COLUMN_ID + " = ?", new String[]{String.valueOf(student.getID())});
        db.close();
    }

    /**
     * Converts SQLite integers to boolean
     * @param cursor Takes in Cursor object
     * @param column Takes a Student DB column name in the form of a String
     * @return Returns a boolean
     */
    private boolean getBoolean(Cursor cursor, String column){
        boolean result;
        result = cursor.getInt(cursor.getColumnIndex(column)) != 0;

        return result;
    }
}
