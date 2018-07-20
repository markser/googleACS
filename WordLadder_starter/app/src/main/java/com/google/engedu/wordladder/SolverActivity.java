package com.google.engedu.wordladder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.engedu.worldladder.R;

import java.util.ArrayList;

public class SolverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);

        Intent intent = getIntent();
        final String[] words = intent.getStringArrayExtra("wordPath");

        //Set up the buttons and text view
        TextView startTextView = new TextView(this);
        TextView endTextView = new TextView(this);
        Button solverButton = new Button(this);
        final Button resetButton = new Button(this);


        //sets up layout for scrollview so that you can dynamically add to it
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_solver);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //sets up button parameters
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //add starting word to the starting text view
        startTextView.setText(words[0]);
        layout.addView(startTextView);

        //adds the spaces for the words that go between the start and end words
        for (int i = 0; i < words.length - 2; i++) {
            EditText editText = new EditText(this);
            editText.setId(i + 1);
            editText.setLayoutParams(params);
            layout.addView(editText);
        }
        //set the end word in the layout
        endTextView.setText(words[words.length - 1]);
        layout.addView(endTextView);


        //sets up the solving button
        solverButton.setText("Solve");
        solverButton.setLayoutParams(buttonParams);
        solverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < words.length - 2; i++) {
                    EditText editText = (EditText) findViewById(i + 1);
                    editText.setText(words[i + 1]);

                }
            }
        });
        layout.addView(solverButton);

//        not necessary because android has a back button, brain fart
//        //restart button that brings the user back to the main screen
//        resetButton.setText("Back");
//        resetButton.setLayoutParams(buttonParams);
//        resetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        layout.addView(resetButton);
//    }
    }
}