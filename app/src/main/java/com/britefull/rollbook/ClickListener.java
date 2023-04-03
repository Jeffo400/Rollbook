package com.britefull.rollbook;

import android.view.View;

public interface ClickListener {

    void onClass1Clicked(View view, int position);
    void onClass2Clicked(View view, int position);
    void onCheckOutClicked(View view, int position);
    void onLongClicked(int position);
    void onNameClicked(int position);
}
