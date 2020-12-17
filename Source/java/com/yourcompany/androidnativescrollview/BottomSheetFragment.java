package com.yourcompany.androidnativescrollview;

import android.app.ActionBar;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetFragment";

    public static BottomSheetFragment newInstance(ArrayList<String> stringArray) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setStringArray(stringArray);
        return bottomSheetFragment;
    }

    public void setStringArray(ArrayList<String> newStringArray) {
        stringArray = newStringArray;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Create params for views---------------
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Create a layout---------------
        LinearLayout linearLayout = new LinearLayout(this.getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //----Create a TextView------
        TextView textView = new TextView(this.getActivity());

        textView.setText("This TextView is dynamically created");
        textView.setLayoutParams(params);


        //--Create A EditText------------------
        EditText editText = new EditText(this.getActivity());
        editText.setLayoutParams(params);

        //----Create a CheckBox-------------
        CheckBox checkBox = new CheckBox(this.getActivity());
        checkBox.setLayoutParams(params);

        //--- Create a RadioGroup---------------
        RadioGroup radioGroup = new RadioGroup(this.getActivity());
        radioGroup.setLayoutParams(params);

        //--------Create a RadioButton----------
        RadioButton radioButton = new RadioButton(this.getActivity());
        radioButton.setLayoutParams(params);

        //-----Create a Button--------
        Button button = new Button(this.getActivity());
        button.setText("This Button is dynamically created");
        button.setLayoutParams(params);


        //---Add all elements to the layout
        linearLayout.addView(textView);
        linearLayout.addView(checkBox);
        linearLayout.addView(editText);
        linearLayout.addView(radioGroup);
        linearLayout.addView(radioButton);

        linearLayout.addView(button);

        for (String s : stringArray) {

            TextView newTextView = new TextView(this.getActivity());
            newTextView.setText(s);
            newTextView.setLayoutParams(params);
            linearLayout.addView(newTextView);
        }


        //---Create a layout param for the layout-----------------
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // ScrollView

        ScrollView scroll = new ScrollView(this.getContext());
        scroll.setLayoutParams(layoutParams);

        scroll.addView(linearLayout);

        //Set the custom view

        dialog.setContentView(scroll);
    }

    private ArrayList<String> stringArray;
}
