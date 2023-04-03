package com.britefull.rollbook.RollLog;

public class RollEntry {

    // Instance variables
    private String date;
    private String rollData;

    // Getters
    public String getDate() {
        return date;
    }
    public String getRollData() {
        return rollData;
    }

    // SQLite variables
    static final String TABLE_NAME = "rollentries";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_DATA = "rollentry";
    // SQlite Creator
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_DATE + " TEXT, "
            + COLUMN_DATA + " TEXT"
            + ")";


    // PRIMARY CONSTRUCTOR
    public RollEntry(String date, int[] rollData) {
        this.date = date;

        StringBuilder stringBuilder = new StringBuilder("");

        // Appends the attendance data into a string
        for(int i=0; i<rollData.length; i++ ){
            stringBuilder.append(rollData[i]);
            if(i != rollData.length - 1){
                stringBuilder.append(",");
            }
        }

        // Saves input as a string
        this.rollData = stringBuilder.toString();
    }

    // SECONDARY CONSTRUCTOR
    RollEntry(String date, String rollData) {
        this.date = date;
        this.rollData = rollData;
    }
}
