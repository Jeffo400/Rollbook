package com.britefull.rollbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.britefull.rollbook.RollLog.HistoryActivity;
import com.britefull.rollbook.RollLog.RollDBHelper;
import com.britefull.rollbook.RollLog.RollEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    // Objects for handling RecyclerView
    static ArrayList<Student> classroom;
    StudentsAdapter adapter;

    // Database pointers
    private StudentDBHelper db;
    private RollDBHelper rollDB;

    // Integers used for storing the desired intent result to go to StudentEditActivity
    public static final int STUDENT_NEW_SAVED = 1;
    public static final int STUDENT_EXIST_SAVED = 2;
    public static final int STUDENT_EDIT_CANCELLED = 3;
    public static final int HISTORY_DELETED = 400;

    // Used for storing attendance count
    int class1Size = 0;
    int class2Size = 0;
    int classBothSize = 0;
    TextView textViewClass1;
    TextView textViewClass2;
    TextView textViewClassBoth;

    // Used for handling dateView status
    TextView textViewUploaded;
    TextView dateView;
    boolean uploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("com.britefull.rollbook", Context.MODE_PRIVATE);

        // Sets the necessary TextViews
        textViewClass1 = findViewById(R.id.textViewClass1);
        textViewClass2 = findViewById(R.id.textViewClass2);
        textViewClassBoth = findViewById(R.id.textViewClassBoth);
        textViewClass1.setText(String.valueOf(class1Size));
        textViewClass2.setText(String.valueOf(class2Size));
        textViewClassBoth.setText(String.valueOf(classBothSize));
        dateView = findViewById(R.id.dateView);
        textViewUploaded = findViewById(R.id.textViewUploaded);

        // Gets the last saved date from SharedPreferences, if empty then uses "Date" string as default
        dateView.setText(sharedPreferences.getString("date", "Date"));
        // Gets the uploaded status from SharedPreferences, if empty then uses "false" as default
        setUploadStatus(sharedPreferences.getBoolean("uploaded", false));


        // getCurrentDate(dateView);
        if("Date".equals(dateView.getText().toString())){
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_error_black_24dp)
                    .setTitle("Start new day")
                    .setMessage("There's no date set. Start a new day?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getCurrentDate(dateView);
                            /*
                            Insert with reset view code
                             */
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        // Sets the classroom ArrayList
        classroom = new ArrayList<>();

        // Sets the database helpers
        db = new StudentDBHelper(this);
        db.createTable();   // Creates a classroom database table if it doesn't exist
        classroom.addAll(db.getAllStudents());
        rollDB = new RollDBHelper(this);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new StudentsAdapter(classroom, getApplication(), new ClickListener() {
            @Override
            public void onClass1Clicked(View view, int position) {
                class1ClickHandler(view, position);
            }
            @Override
            public void onClass2Clicked(View view, int position) {
                class2ClickHandler(view, position);
            }
            @Override
            public void onCheckOutClicked(View view, int position) {
                checkOutClickHandler(view, position);
            }
            @Override
            public void onLongClicked(int position) {
                longClickHandler(position);
            }
            @Override
            public void onNameClicked(int position) {
                switchToEditor(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /**
         * Set up for the button to add a new student
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                switchToBlankEditor();
            }
        });


        countClass1();
        countClass2();
        countClassBoth();
    }


    // Handles Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    Handles the Main Toolbar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.reset_database: //Resets the student database to be empty
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_error_black_24dp)
                        .setTitle("Delete All Students")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteAllStudents();
                                classroom = new ArrayList<>();
                                classroom.addAll(db.getAllStudents()); // Rebuilds the classroom arraylist
                                // adapter.notifyDataSetChanged();
                                resetViews();
                                setUploadStatus(false);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            case R.id.send_data: // Sends data to Google drive
                int[] mAttendance;
                mAttendance = countAttendance();

                // Fills in date if not filled out already
                if("Date".equals(dateView.getText())){
                    getCurrentDate(dateView);
                }


                // Saves attendance to local db history
                dateView = findViewById(R.id.dateView);
                RollEntry rollEntry = new RollEntry(dateView.getText().toString(), mAttendance);
                rollDB.insertEntry(rollEntry);

                // Builds upload package as a string
                String[] uploadPackage = new String[26];
                for(int i=0; i<=24; i++){
                    uploadPackage[i] = String.valueOf(mAttendance[i]);
                }
                uploadPackage[25] = dateView.getText().toString();

                // Saves attendance to cloud
                PostData postData = new PostData(getApplicationContext());
                postData.execute(uploadPackage[0], uploadPackage[1], uploadPackage[2], uploadPackage[3], uploadPackage[4],
                        uploadPackage[5], uploadPackage[6], uploadPackage[7], uploadPackage[8], uploadPackage[9],
                        uploadPackage[10], uploadPackage[11], uploadPackage[12], uploadPackage[13], uploadPackage[14],
                        uploadPackage[15], uploadPackage[16], uploadPackage[17], uploadPackage[18], uploadPackage[19],
                        uploadPackage[20], uploadPackage[21], uploadPackage[22], uploadPackage[23], uploadPackage[24],
                        uploadPackage[25]);

                // Sets uploaded text indicator
                setUploadStatus(true);
                return true;

            case R.id.sort_alphabet:
                sortAtoZ();
                return true;

            case R.id.view_raw_data:
                switchToHistory();
                return true;

            case R.id.start_new_day:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_error_black_24dp)
                        .setTitle("Start new day")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(Student student:classroom){
                                    db.resetStatus(student);
                                }
                                resetViews();
                                setUploadStatus(false);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            default:
                return false;
        }
    }

    /**
     *  Method for taking an existing Student object and passing it to StudentEditActivity
     *      for editing of attributes.
     *  Uses startActivityforResult and passes an expected result code of int = 2 corresponding
     *      to STUDENT_EXIST_SAVED, meaning an existing student is successfully saved.
     * @param position Takes the position in ArrayList "classroom" where the desired
     *                 student is located.
     */
    public void switchToEditor(int position){
        Student student = classroom.get(position);
        Intent intent = new Intent(this, StudentEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.briteful.rollbook_alpha01.Student", student);
        bundle.putInt("EXPECTED_RESULT", STUDENT_EXIST_SAVED);
        bundle.putInt("POSITION", position);
        bundle.putLong("DB_ID", student.getID());
        intent.putExtras(bundle);

        startActivityForResult(intent, STUDENT_EXIST_SAVED);
    }

    /**
     * Method for switching to StudentEditActivity to create a new student.
     * Creates a temporary empty Student object to pass to StudentEditActivity.
     * Uses startActivityforResult and passes an expected result code of int = 1 corresponding
     *      to STUDENT_NEW_SAVED, meaning a new student is successfully saved.
     */
    public void switchToBlankEditor(){
        Student mStudent = new Student(null, null, 0, null, null, null, null, null);
        Intent intent = new Intent(this, StudentEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.briteful.rollbook_alpha01.Student", mStudent);
        bundle.putInt("EXPECTED_RESULT", STUDENT_NEW_SAVED);
        intent.putExtras(bundle);
        startActivityForResult(intent, STUDENT_NEW_SAVED);
        // startActivity(intent);
    }

    public void switchToHistory(){
        ArrayList<RollEntry> historyList = rollDB.getAllEntries();

        String textHistory;
        StringBuilder builder = new StringBuilder("");

        for(RollEntry entry : historyList){
            builder.append(entry.getDate());
            builder.append("\n");
            builder.append(entry.getRollData());
            builder.append("\n");
        }
        textHistory = builder.toString();

        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("HISTORY", textHistory);
        startActivityForResult(intent, HISTORY_DELETED);
    }

    /**
     * Handles the Student object passed back from StudentEditActivity and startActivityForResult
     * @param requestCode handles the startActivityForResult request code
     *                    requestCode = 1; New student saved
     *                    requestCode = 2; Existing student saved
     * @param resultCode handles the result code returned by StudentEditActivity
     *                   resultCode = 3; A cancel was performed in StudentEditActivity
     * @param intent handles the Intent object returned from StudentEditActivity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode, intent);
        // If requestCode == resultCode == 1 meaning Student object is saved
        if(resultCode == STUDENT_NEW_SAVED){
            // Gets the student info returned from StudentEditActivity
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            Student mStudent = bundle.getParcelable("com.briteful.rollbook_alpha01.Student");

            // Sets the full name of the student
            mStudent.setFullName();

            // Saves student to database, gets database ID, and applies to temporary student object
            long mID = db.insertStudent(mStudent);
            mStudent.setID(mID);

            // Saves the student to classroom arraylist and notifies Recyclerview.Adapter
            classroom.add(mStudent);
            int newPosition = classroom.size()-1;
            adapter.notifyItemInserted(newPosition);

        }
        if(resultCode == STUDENT_EXIST_SAVED){
            // Gets the student info returned from StudentEditActivity
            Bundle bundle = intent.getExtras();
            if (bundle == null) throw new AssertionError();
            Student mStudent = bundle.getParcelable("com.briteful.rollbook_alpha01.Student");
            mStudent.setFullName();
            int position = bundle.getInt("POSITION");
            mStudent.setID(bundle.getLong("DB_ID"));

            // Saves the info from StudentEditActivity to the correct student
            classroom.get(position).setFirstName(mStudent.getFirstName());
            classroom.get(position).setLastName(mStudent.getLastName());
            classroom.get(position).setGrade(mStudent.getGrade());
            classroom.get(position).setGender(mStudent.getGender());
            classroom.get(position).setParent1(mStudent.getParent1());
            classroom.get(position).setParent2(mStudent.getParent2());
            classroom.get(position).setAllergies(mStudent.getAllergies());
            classroom.get(position).setNotes(mStudent.getNotes());

            // Saves student to database
            db.updateStudent(mStudent);

            // Notifies the Recyclerview.Adapter
            adapter.notifyItemChanged(position);

        }
        // Else a cancel was performed in StudentEditActivity and no further action needed

        if(resultCode == HISTORY_DELETED){
            rollDB.deleteAllEntries();
        }
    }

    public void class1ClickHandler(View view, int position){
        ImageButton imageButton = (ImageButton) view;

        // Flips the attendance boolean and applies result to button tint
        classroom.get(position).flipClass1();
        StudentsAdapter.checkInColorUpdate1(imageButton, classroom.get(position));

        // Saves status to DB
        db.updateStatus(classroom.get(position));

        // Updates attendance counter
        countClass1();
        countClassBoth();
    }

    public void class2ClickHandler(View view, int position){
        ImageButton imageButton = (ImageButton) view;

        // Flips the attendance boolean and applies result to button tint
        classroom.get(position).flipClass2();
        StudentsAdapter.checkInColorUpdate2(imageButton, classroom.get(position));

        // Saves status to DB
        db.updateStatus(classroom.get(position));

        // Flips the attendance boolean and applies result to button tint
        countClass2();
        countClassBoth();
    }


    // Method to flip the check out state of each student
    public void checkOutClickHandler(View view, int position){
        ImageButton imageButton = (ImageButton) view;

        // Updates button tint based on check out status
        classroom.get(position).flipCheckOut();
        StudentsAdapter.checkOutColorUpdate(imageButton, classroom.get(position));

        // Saves status to DB
        db.updateStatus(classroom.get(position));
    }


    /*
     * Method to allow deleting students
     * @param position Takes the position passed from classroom ArrayList
     */
    public void longClickHandler(int position){
        final int myPosition = position;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_error_black_24dp)
                .setTitle("Delete student")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Deletes student from database
                        db.deleteStudent(classroom.get(myPosition));

                        // Deletes student from classroom arraylist and updates UI
                        classroom.remove(myPosition);
                        adapter.notifyItemRemoved(myPosition);
                        countClass1();
                        countClass2();
                        countClassBoth();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /*
     * Method to sort the classroom ArrayList alphabetically
     */
    public void sortAtoZ() {
        Collections.sort(classroom, new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                String s1 = student.getFullName();
                String s2 = t1.getFullName();
                return s1.compareToIgnoreCase(s2);
            }
        });

        adapter.notifyDataSetChanged();
    }

    public void getCurrentDate(TextView textView){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String stringDate = mDateFormat.format(calendar.getTime());
        textView.setText(stringDate);

        // Saves date to SharedPreferences
        sharedPreferences.edit().putString("date", stringDate).apply();
    }

    // Method to count class 1 attendance and update UI
    public void countClass1(){
        int counter = 0;
        for(int i =0; i<classroom.size(); i++){
            if(classroom.get(i).getClass1()){
                counter++;
            }
        }
        class1Size = counter;
        textViewClass1.setText(String.valueOf(class1Size));
    }

    // Method to count class 2 attendance and update UI
    public void countClass2(){
        int counter = 0;
        for(int i =0; i<classroom.size(); i++){
            if(classroom.get(i).getClass2()){
                counter++;
            }
        }
        class2Size = counter;
        textViewClass2.setText(String.valueOf(class2Size));
    }

    // Method to count students in both class 1 and 2
    public void countClassBoth(){
        int counter = 0;
        for(int i=0; i<classroom.size(); i++){
            if(classroom.get(i).getClass1() && classroom.get(i).getClass2()){
                counter++;
            }
        }
        classBothSize = counter;
        textViewClassBoth.setText(String.valueOf(classBothSize));
    }


    /**Method to count full attendance for record keeping
     * @return Returns an int[] of size 25
     */
    public int[] countAttendance(){
        int[] attendance = new int[25];
        attendance[24] = classBothSize;

        for(Student tempStudent : classroom){
            if(tempStudent.getGender().equals("Female")){
                if(tempStudent.getGrade()==0){
                    if(tempStudent.getClass1()){
                        attendance[0]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[12]++;
                    }
                }
                if(tempStudent.getGrade()==1){
                    if(tempStudent.getClass1()){
                        attendance[1]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[13]++;
                    }
                }
                if(tempStudent.getGrade()==2){
                    if(tempStudent.getClass1()){
                        attendance[2]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[14]++;
                    }
                }
                if(tempStudent.getGrade()==3){
                    if(tempStudent.getClass1()){
                        attendance[3]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[15]++;
                    }
                }
                if(tempStudent.getGrade()==4){
                    if(tempStudent.getClass1()){
                        attendance[4]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[16]++;
                    }
                }
                if(tempStudent.getGrade()==5){
                    if(tempStudent.getClass1()){
                        attendance[5]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[17]++;
                    }
                }
            } else if(tempStudent.getGender().equals("Male")){
                if(tempStudent.getGrade()==0){
                    if(tempStudent.getClass1()){
                        attendance[6]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[18]++;
                    }
                }
                if(tempStudent.getGrade()==1){
                    if(tempStudent.getClass1()){
                        attendance[7]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[19]++;
                    }
                }
                if(tempStudent.getGrade()==2){
                    if(tempStudent.getClass1()){
                        attendance[8]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[20]++;
                    }
                }
                if(tempStudent.getGrade()==3){
                    if(tempStudent.getClass1()){
                        attendance[9]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[21]++;
                    }
                }
                if(tempStudent.getGrade()==4){
                    if(tempStudent.getClass1()){
                        attendance[10]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[22]++;
                    }
                }
                if(tempStudent.getGrade()==5){
                    if(tempStudent.getClass1()){
                        attendance[11]++;
                    }
                    if(tempStudent.getClass2()){
                        attendance[23]++;
                    }
                }
            }
        }

        return attendance;
    }


    /**
     * Method to reset the views in MainActivity when starting a new day
     */
    public void resetViews(){
        for(Student tempStudent : classroom){
            if(tempStudent.getClass1()){
                tempStudent.flipClass1();
            }
            if(tempStudent.getClass2()){
                tempStudent.flipClass2();
            }
            if(tempStudent.getCheckOut()){
                tempStudent.flipCheckOut();
            }
        }
        adapter.notifyDataSetChanged();

        dateView = findViewById(R.id.dateView);
        getCurrentDate(dateView);

        class1Size = 0;
        class2Size = 0;
        classBothSize = 0;
        textViewClass1 = findViewById(R.id.textViewClass1);
        textViewClass2 = findViewById(R.id.textViewClass2);
        textViewClassBoth = findViewById(R.id.textViewClassBoth);
        textViewClass1.setText(String.valueOf(class1Size));
        textViewClass2.setText(String.valueOf(class2Size));
        textViewClassBoth.setText(String.valueOf(classBothSize));
    }

    void setUploadStatus(boolean status){
        if(status){
            uploaded = true;
        } else {
            uploaded = false;
        }

        if(uploaded){
            textViewUploaded.setVisibility(View.VISIBLE);
            sharedPreferences.edit().putBoolean("uploaded", true).apply();
        } else {
            textViewUploaded.setVisibility(View.INVISIBLE);
            sharedPreferences.edit().putBoolean("uploaded", false).apply();
        }
    }

}


