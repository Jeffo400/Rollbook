/*
This java code is an implementation of Recycler View
Used to handle the list of students in the main window
Methods are used to handle the database of students
*/

package com.britefull.rollbook;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private ClickListener listener;
    private List<Student> studentList;
    private Context context;

    StudentsAdapter(List<Student> list, Context context, ClickListener listener){
        this.studentList = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_block, parent, false);
        return new StudentViewHolder(v, listener);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int position) {
        // Use the provided ViewHolder from the onCreateViewHolder method
        // to populate the current row on the RecyclerView
        // "position" is used to locate the correct student from the Main Activity array
        studentViewHolder.studentName.setText(
                studentList.get(position).getFullName());

        if(MainActivity.classroom.get(position).getAllergies() == null ||
                MainActivity.classroom.get(position).getAllergies().isEmpty()){
            studentViewHolder.studentName.setTextColor(Color.rgb(66,66,66));
        } else {
            studentViewHolder.studentName.setTextColor(Color.rgb(173, 20, 87));
        }

        // Activates long clicks for studentName view when ViewHolder is bound
        studentViewHolder.studentName.setLongClickable(true);

        // Retrieves correct button color when adapter is updated
        checkInColorUpdate1(studentViewHolder.buttonClass1, MainActivity.classroom.get(position));
        checkInColorUpdate2(studentViewHolder.buttonClass2, MainActivity.classroom.get(position));
        checkOutColorUpdate(studentViewHolder.buttonCheckOut, MainActivity.classroom.get(position));
    }

    // Retrieves correct color for Class 1 Check In button
    public static void checkInColorUpdate1(ImageButton imageButton, Student student){
        int myColor;
        if(imageButton.getId() == R.id.buttonClass1){
            if(student.getClass1()){
                myColor = Color.rgb(0, 151, 167);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                // Gray out button
                myColor = Color.rgb(224, 224, 224);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    // Retrieves correct color for Class 2 Check In button
    public static void checkInColorUpdate2(ImageButton imageButton, Student student){
        int myColor;
        if(imageButton.getId() == R.id.buttonClass2){
            if(student.getClass2()){
                myColor = Color.rgb(0, 151, 167);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                // Gray out button
                myColor = Color.rgb(224, 224, 224);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    // Retrieves correct color for Check Out button
    public static void checkOutColorUpdate(ImageButton imageButton, Student student){
        int myColor;
        if(imageButton.getId() == R.id.buttonCheckOut){
            if(student.getCheckOut()){
                myColor = Color.rgb(173, 20, 87);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                // Gray out button
                myColor = Color.rgb(224, 224, 224);
                imageButton.setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
