/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostText, ghostStatus;
    private static final String win = "You Win!";
    private static final String lose = "You Lose!";
    private String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            input = savedInstanceState.getString(ghostText.toString());
        }

        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();

        //assigning anything that is on the layout.xml
        ghostText = (TextView) findViewById(R.id.ghostText);
        ghostStatus = (TextView) findViewById(R.id.gameStatus);

        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            Log.d("ERROR", e.toString());
        }

        onStart(null);

    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        ghostText.setText(input);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        input = ghostText.getText().toString();
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Activity Life cycle", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Activity Life cycle", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences settings = getApplicationContext().getSharedPreferences("MyPref", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("ghostText", input);
//
//        // Commit the edits!
//        editor.apply();
//
//        Log.e("Activity Life cycle", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Activity Life cycle", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Activity Life cycle", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Activity Life cycle", "onDestroy");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
    private void computerTurn() {
        //get current word fragment
        String word = ghostText.getText().toString();

        //if it's a valid word
        if (dictionary.isWord(word) && word.length() >= 4) {
            ghostStatus.setText(win);
        }
        //else get the word fragment and check for its prefix
        else {
            String longerWord = dictionary.getGoodWordStartingWith(word);
           // add next char to word fragment if a word is found
            if (longerWord != null) {
                char nextChar = longerWord.charAt(word.length());
                word += nextChar;
                ghostText.setText(word);
                input = word;
                ghostStatus.setText(USER_TURN);
            } else {
                //if no word exists with the word fragment as a prefix then user lose
                ghostStatus.setText(lose);
            }
        }
        userTurn = true;
        // label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode <= KeyEvent.KEYCODE_Z && keyCode >= KeyEvent.KEYCODE_A) {
            char pressKey = (char) event.getUnicodeChar();
            ghostText.append(String.valueOf(pressKey));
            input = ghostText.getText().toString();
            userTurn = false;
            computerTurn();
        }

        return super.onKeyUp(keyCode, event);
    }

    public void challenge(View view) {
        //get the existing word
        String curr = ghostText.getText().toString();
        //if the word is a valid word then the challenge is successful
        if (curr.length() >= 4 && dictionary.isWord(curr)) {
            ghostStatus.setText(win);
        } else {
            // if a word can be formed with the fragment as prefix, declare victory for the computer and display a possible word
            String anotherWord = dictionary.getGoodWordStartingWith(curr);
            if (anotherWord != null) {
                ghostStatus.setText(lose);
                ghostText.setText(anotherWord);
                input = ghostText.getText().toString();
            }
            //If a word cannot be formed with the fragment, declare victory for the user
            else {
                ghostStatus.setText(win);
            }
        }
    }
}


