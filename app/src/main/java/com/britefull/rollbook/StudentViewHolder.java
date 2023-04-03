package com.britefull.rollbook;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView studentName;
    ImageButton buttonClass1;
    ImageButton buttonClass2;
    ImageButton buttonCheckOut;
    private ClickListener listenerRef;

    StudentViewHolder(View view, ClickListener clickListener) {
        super(view);

        studentName = view.findViewById(R.id.studentName);
        buttonClass1 = view.findViewById(R.id.buttonClass1);
        buttonClass2 = view.findViewById(R.id.buttonClass2);
        buttonCheckOut = view.findViewById(R.id.buttonCheckOut);

        // Apply the passed in clickListener object to each view
        listenerRef = clickListener;
        studentName.setOnClickListener(this);
        studentName.setOnLongClickListener(this);
        buttonClass1.setOnClickListener(this);
        buttonClass2.setOnClickListener(this);
        buttonCheckOut.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        // finds the button or view ID and determines appropriate method to call
        switch (v.getId()){
            case R.id.buttonClass1:
                listenerRef.onClass1Clicked(v, getLayoutPosition());
                break;
            case R.id.buttonClass2:
                listenerRef.onClass2Clicked(v, getLayoutPosition());
                break;
            case R.id.studentName:
                listenerRef.onNameClicked(getLayoutPosition());
                break;
            case R.id.buttonCheckOut:
                listenerRef.onCheckOutClicked(v, getLayoutPosition());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {

        listenerRef.onLongClicked(getLayoutPosition());
        return true;
        // return false;
    }


}


