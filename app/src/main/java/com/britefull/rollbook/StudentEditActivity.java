package com.britefull.rollbook;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import static android.widget.ArrayAdapter.createFromResource;

public class StudentEditActivity extends AppCompatActivity {

    // Variable for storing a Student object to be handled in this Activity
    Student student;
    long id;

    // Variable for storing intent to go back to MainActivity
    Intent intent;

    // Variable for storing expected intent result from MainActivity
    int expectedResult;

    // Variable for storing the student's position in Recyclerview.Adapter
    int position;
    Spinner grade_spinner;
    Spinner gender_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("New Activity", "Success");

        // Prevents keyboard from auto-opening
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_student_edit);


        // Get the Student object passed from MainActivity
        Bundle bundle = getIntent().getExtras();
        student = bundle.getParcelable("com.briteful.rollbook_alpha01.Student");
        expectedResult = bundle.getInt("EXPECTED_RESULT");
        if(expectedResult == MainActivity.STUDENT_EXIST_SAVED){
            position = bundle.getInt("POSITION");
            id = bundle.getLong("DB_ID");
            Log.i("Debug", "Position received from main: " + String.valueOf(position));
        }

        Log.i("Debug", "Grade: " + String.valueOf(student.getGrade()));

        // Fills the form with the Student object that is passed in
        fillEditor(student);

        // Gets the spinner drop-down selector for student grades
        grade_spinner = findViewById(R.id.grade_spinner);

        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> gradeAdapter = createFromResource(this,
                R.array.grade_options_list, android.R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(gradeAdapter);
        grade_spinner.setOnItemSelectedListener(new GradeSpinnerActivity());
        grade_spinner.setSelection(student.getGrade());


        // Gets the spinner drop-down selector for gender
        gender_spinner = findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options_list, android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(genderAdapter);
        gender_spinner.setOnItemSelectedListener(new GenderSpinnerActivity());
        for(int i=0; i<genderAdapter.getCount(); i++){
            if(genderAdapter.getItem(i).toString().equals(student.getGender())){
                gender_spinner.setSelection(i);
                break;
            }
        }

    }

    // This method populates the editor with an Student object that is passed in
    public void fillEditor(Student inputStudent){

        EditText editFirstName = findViewById(R.id.editFirstName);
        editFirstName.setText(inputStudent.getFirstName());

        EditText editLastName = findViewById(R.id.editLastName);
        editLastName.setText(inputStudent.getLastName());

        EditText editParent1 = findViewById(R.id.editParent1);
        editParent1.setText(inputStudent.getParent1());

        EditText editParent2 = findViewById(R.id.editParent2);
        editParent2.setText(inputStudent.getParent2());

        EditText editAllergies = findViewById(R.id.editAllergies);
        editAllergies.setText(inputStudent.getAllergies());

        EditText editNotes = findViewById(R.id.editNotes);
        editNotes.setText(inputStudent.getNotes());
    }

    // This method saves the info and exits back to MainActivity
    public void saveAndExit(View view){
        EditText editFirstName = findViewById(R.id.editFirstName);
        student.setFirstName(editFirstName.getText().toString());

        EditText editLastName = findViewById(R.id.editLastName);
        student.setLastName(editLastName.getText().toString());

        EditText editParent1 = findViewById(R.id.editParent1);
        student.setParent1(editParent1.getText().toString());

        EditText editParent2 = findViewById(R.id.editParent2);
        student.setParent2(editParent2.getText().toString());

        EditText editAllergies = findViewById(R.id.editAllergies);
        student.setAllergies(editAllergies.getText().toString());

        EditText editNotes = findViewById(R.id.editNotes);
        student.setNotes(editNotes.getText().toString());

        // Grades and gender are handled by the spinner activities

        intent = new Intent();
        if (expectedResult == MainActivity.STUDENT_NEW_SAVED){
            intent.putExtra("com.briteful.rollbook_alpha01.Student", student);
            setResult(expectedResult, intent);
        } else if(expectedResult == MainActivity.STUDENT_EXIST_SAVED){
            Bundle bundle = new Bundle();
            bundle.putParcelable("com.briteful.rollbook_alpha01.Student", student);
            bundle.putInt("POSITION", position);
            bundle.putLong("DB_ID", id);
            Log.i("Debug", "Position sent to main: " + position);
            Log.i("Debug", "Result code: " + expectedResult);
            intent.putExtras(bundle);
            setResult(expectedResult, intent);

        }
        finish();
    }

    // Handles the cancel button, closes current activity
    public void cancelFunc(View view){
        // Sets the activity result to a cancelled state, returns empty intent
        intent = new Intent();
        setResult(MainActivity.STUDENT_EDIT_CANCELLED, intent);
        finish();
    }

    // Handles user selection of the grade spinner
    public class GradeSpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            student.setGrade(parent.getSelectedItemPosition());
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }

    public class GenderSpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            student.setGender(parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    }
}