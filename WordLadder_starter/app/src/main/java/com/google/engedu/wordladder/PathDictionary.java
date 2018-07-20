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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

public class PathDictionary {
//    private static long startTime = System.currentTimeMillis();
    private static final int MAX_WORD_LENGTH = 4;
    private static HashSet<String> words = new HashSet<>();
    private HashMap<String,ArrayList<String>> ingest = new HashMap<>();

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");

        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);

        }
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private static ArrayList<String> neighbours(String word) {
        ArrayList<String> output = new ArrayList<>();
        //change userinput to become a valid input for computer
        String userInput = word.toLowerCase();
        for(String arrayInput : words){
            //make sure that the length of the word is the same and the neighbouring words
            if(arrayInput.length() == userInput.length()){
                for(int x = 0; x < userInput.length(); x++){
                    //temp string to hold the inputed word with removed character
                    String temp = userInput.substring(0, x) + userInput.substring(x+1);
                    //if a neighbouring word is valid, then it's added to the ArrayList
                    if(arrayInput.contains(temp)){
                        output.add(arrayInput);
                    }
                }
            }
        }
        return output;
    }

    //*********************************************************************************************************************
    //needs to implement something to keep the depth of BFS in check
    //*********************************************************************************************************************

    //^^^^^ has been implemented ^^^^^
    public static String[] findPath(String start, String end) {
        //create the necessary data structures
        //hodor, HOLD THE DOOR
        ArrayDeque<ArrayList<String>> hodor = new ArrayDeque<>();
        ArrayList<String> checked = new ArrayList<>();
        ArrayList<String> firstElement = new ArrayList<>();
        //populate data structures with param start
        checked.add(start);
        firstElement.add(start);
        hodor.add(firstElement);

        while (!hodor.isEmpty()) {
            //get's the first thing in the hodor and sets it as currentPath
            ArrayList<String> curr = hodor.poll();
            //Get's the lastword in currenPath
            String lastWord = curr.get(curr.size() - 1);
            //get's the neighbours for the last word
            for (String temp : neighbours(lastWord)) {
                if (curr.size() > 7) {
                    break;
                }

                //checks if the start and end are neighbours
                if (temp.equals(end)) {
                    curr.add(end);
                    return curr.toArray(new String[curr.size()]);
                } else {
                    // visiting the rest of the neighbours and locating the next possible neighbour
                    // make sure to not look through the neighbours path of already parsed through
                    if (!checked.contains(temp)) {
                        checked.add(temp);
                        ArrayList<String> theRest = new ArrayList<>(curr);
                        //add's the neighbouring words to theRest lists
                        theRest.add(temp);
                        //add's to the running list (hodor) to check the rest of the neighbouring words
                        hodor.add(theRest);
                    }
                }
            }

        }
        return null;
    }
}