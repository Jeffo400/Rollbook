/**
This is an object used to handle each student's individual data
 */

package com.britefull.rollbook;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private long id;
    private String firstName;
    private String lastName;
    private int grade;
    private String gender;
    private String allergies;
    private String parent1;
    private String parent2;
    private String notes;

    /**
     * Boolean variables for tracking attendance status, default = false
     */
    private boolean class1 = false;
    private boolean class2 = false;
    private boolean checkOut = false;
    // full name variable used for sorting and display
    private String fullName;

    // SQLite variables
    public static final String TABLE_NAME = "classroom";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_LNAME = "lname";
    public static final String COLUMN_GRADE = "grade";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PARENT1 = "parent1";
    public static final String COLUMN_PARENT2 = "parent2";
    public static final String COLUMN_ALLERGIES = "allergies";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_CLASS1 = "class1";
    public static final String COLUMN_CLASS2 = "class2";
    public static final String COLUMN_CHECKOUT = "checkout";

    // Command string for creating a SQLite table
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FNAME + " TEXT, "
            + COLUMN_LNAME + " TEXT, "
            + COLUMN_GRADE + " INTEGER, "
            + COLUMN_GENDER + " TEXT, "
            + COLUMN_PARENT1 + " TEXT, "
            + COLUMN_PARENT2 + " TEXT, "
            + COLUMN_ALLERGIES + " TEXT, "
            + COLUMN_NOTES + " TEXT, "
            + COLUMN_CLASS1 + " INTEGER DEFAULT 0, "
            + COLUMN_CLASS2 + " INTEGER DEFAULT 0, "
            + COLUMN_CHECKOUT + " INTEGER DEFAULT 0"
            + ")";


    // Command string deleting the SQLite table
    public static final String DELETE_TABLE =
            "DROP TABLE " + TABLE_NAME;

    // SETTERS
    public void setFullName(){
        fullName = lastName + ", " + firstName;
    }
    public void setID(long id){
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setFullName();
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        setFullName();
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    public void setParent1(String parent1) {
        this.parent1 = parent1;
    }
    public void setParent2(String parent2) {
        this.parent2 = parent2;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    // Flips the attendance for class attendance to toggle the attendance buttons
    public void flipClass1() {
        class1 = !class1;
    }
    public void flipClass2() {
        class2 = !class2;
    }
    public void flipCheckOut() {
        checkOut = !checkOut;
    }

    // GETTERS
    public long getID(){
        return this.id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public int getGrade(){
        return grade;
    }
    public String getGender(){
        return gender;
    }
    public String getAllergies(){
        return allergies;
    }
    public String getParent1(){
        return parent1;
    }
    public String getParent2(){
        return parent2;
    }
    public String getNotes(){
        return notes;
    }
    public boolean getClass1(){
        return class1;
    } // Gets the attendance status for class 1
    public boolean getClass2(){
        return class2;
    } // Gets the attendance status for class 2
    public boolean getCheckOut(){
        return checkOut;
    }
    public String getFullName(){
        return this.fullName;
    }



    // PRIMARY CONSTRUCTOR, no ID
    Student(String firstName, String lastName, int grade, String gender,
            String parent1, String parent2, String allergies, String notes){
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade; // Kindergarten "K" = 0
        this.gender = gender;
        this.allergies = allergies;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.notes = notes;
        setFullName();
    }

    /*
    // SECONDARY CONSTRUCTOR, yes ID
    Student(long id, String firstName, String lastName, int grade, String gender,
            String parent1, String parent2, String allergies, String notes){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade; // Kindergarten "K" = 0
        this.gender = gender;
        this.allergies = allergies;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.notes = notes;
        setFullName();
    }
    */

    // FULL CONSTRUCTOR, yes ID
    Student(long id, String firstName, String lastName, int grade, String gender,
            String parent1, String parent2, String allergies, String notes,
            boolean class1, boolean class2, boolean checkOut){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade; // Kindergarten "K" = 0
        this.gender = gender;
        this.allergies = allergies;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.notes = notes;
        this.class1 = class1;
        this.class2 = class2;
        this.checkOut = checkOut;
        setFullName();
    }

    // PARCEL INTERPRETER
    // Creates a student object from a parcel
    private Student(Parcel in){
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.grade = in.readInt();
        this.gender = in.readString();
        this.parent1 = in.readString();
        this.parent2 = in.readString();
        this.allergies = in.readString();
        this.notes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // PARCEL WRITER
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeInt(grade);
        parcel.writeString(gender);
        parcel.writeString(parent1);
        parcel.writeString(parent2);
        parcel.writeString(allergies);
        parcel.writeString(notes);
    }

    public static final Creator<Student> CREATOR = new Creator<Student>(){

        @Override
        public Student createFromParcel(Parcel parcel) {
            return new Student(parcel);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}

