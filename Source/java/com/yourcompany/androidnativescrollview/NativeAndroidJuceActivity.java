package com.yourcompany.androidnativescrollview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class NativeAndroidJuceActivity extends AppCompatActivity
{
    private static final String LOG_TAG =
            NativeAndroidJuceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // call the native C++ class constructor
        constructNativeClass();


        //Create params for views---------------
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Create a layout---------------
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

//        linearLayout.addView(contentView);

        setContentView(linearLayout, params);

        addJuceMainComponent(linearLayout);
    }

    public void showBottomSheet(ArrayList<String> stringArray) {
        BottomSheetFragment bottomSheetFragment =
                BottomSheetFragment.newInstance(stringArray);
        bottomSheetFragment.show(getSupportFragmentManager(),
                BottomSheetFragment.TAG);
    }

    @Override
    protected void onDestroy()
    {
        // call the native C++ class destructor
        destroyNativeClass();
        super.onDestroy();
    }

    // called by NativeAndroidJuceActivity.cpp
//    public void addToLog(String message)
//    {
//        EditText editText = (EditText) findViewById(R.id.juceOutput);
//
//        editText.setText(message + "\n" + editText.getText());
//    }
//    public void addJuceComponentButtonClicked(View sender)
//    {
//        addRemoveJuceComponent(findViewById(R.id.juceStage));
//    }

    //==============================================================================
    private native void constructNativeClass();
    private native void destroyNativeClass();
    private native void addJuceMainComponent(View container);

    private long cppCounterpartInstance;

}