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

package com.google.engedu.palindromes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<Range, PalindromeGroup> findings = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This function will get the user's input and replace all non-alphanumeric
     * characters with emptySpace. It will then check if the userInput is already a palindrome
     * IF - it's a palindrome, it will notify the user with a TextView message
     * ELSE - it's not a palindrome, it will call breakIntoPalindromes on userInput
     * The findings.clear() is a to clear broken up palindromes that was stored for the past game.
     *
     * @return true
     */

    public boolean onFindPalindromes(View view) {
        findings.clear();

        EditText editText = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textView);
        String userInput = editText.getText().toString();
        char[] textAsChars = userInput.toCharArray();

        userInput = userInput.toLowerCase().replaceAll("[^A-Za-z0-9]", "");
        if (isPalindrome(textAsChars, 0, userInput.length())) {
            String temp = " is already a palindrome!";
            userInput += temp;
            textView.setText(userInput);
        } else {
            PalindromeGroup palindromes = breakIntoPalindromes(userInput.toCharArray(), 0, userInput.length());
            textView.setText(palindromes.toString());
        }
        return true;
    }

    /**
     * isPalindrome is a simple method that determines if the text is a palindrome or not
     * it first checks if the text has something in it or not and then has essentially two
     * pointers that go from the outside in.
     *
     * @param text  the userInput as character array
     * @param start the start of the array, usually will be 0
     * @param end   the end of the array.length, that's why the if statement has -1 for end var
     * @return true if it's a palindrome
     * false if it's NOT a palindrome
     */
    private boolean isPalindrome(char[] text, int start, int end) {
        end--;// currently, array.length | needs to be array.length-1
        while (start <= end) {
            if (!(text[start] == text[end])) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    /**
     * This breaks up the userInput into palindromes, going from the largest to smallest palindromes that it can find.
     * This is not the best method, but is the easiest method to implement/understand imo.
     *
     * @param text  the userInput as character array
     * @param start the int value of the start of the array, usually will be 0
     * @param end   the int value of the length of the array
     * @return PalindromeGroup, to be added to findings
     */
    private PalindromeGroup greedyBreakIntoPalindromes(char[] text, int start, int end) {
        int tempEnd = start + 1;
        PalindromeGroup bestGroup = null;
        while (tempEnd <= text.length && isPalindrome(text, start, tempEnd)) {
            bestGroup = new PalindromeGroup(text, start, tempEnd);
            tempEnd++;
        }
        tempEnd--;
        if (tempEnd < end) {
            bestGroup.append(breakIntoPalindromes(text, tempEnd, end));
        }
        return bestGroup;
    }

    /**
     * This recursively looks for all prefixes of the string that are palindromes find the smallest amount
     * of total palindromes
     * Uses isPalindrome method and the PalindromeGroup class to make life easier.
     *
     * @param text  the userInput as character array
     * @param start the int value of the start of the array, usually will be 0
     * @param end   the int value of the length of the array
     * @return PalindromeGroup, to be added to findings
     */

    private PalindromeGroup recursiveBreakIntoPalindromes(char[] text, int start, int end) {
        PalindromeGroup bestGroup = null;
        int newEnd = start + 1;
        while (newEnd <= text.length) {
            if (isPalindrome(text, start, newEnd)) {
                PalindromeGroup temp = new PalindromeGroup(text, start, newEnd);
                if (newEnd < end) {
                    temp.append(recursiveBreakIntoPalindromes(text, newEnd, end));
                }
                if (bestGroup == null || bestGroup.length() > temp.length()) {
                    bestGroup = temp;
                }
            }
            newEnd++;
        }
        return bestGroup;
    }

    /**
     * This is the fanciest helper function that is used. Dynamic programming :O
     * This will use hashmaps and dynamic programming to achieve the same thing as the function above,
     * but with the added benefit of being faster at more edge cases like "aaaaaaaaaaaaaaaaaaaaaababad"
     *
     * @param text  the userInput as character array
     * @param start the int value of the start of the array, usually will be 0
     * @param end   the int value of the length of the array
     * @return PalindromeGroup, to be added to findings
     */
    private PalindromeGroup dynamicProgrammingBreakIntoPalindromes(char[] text, int start, int end) {
        findings = new HashMap<>();
        PalindromeGroup bestGroup = null;
        Range range = new Range(start, end);

        if (findings.containsKey(range)) {
            bestGroup = findings.get(range);
        } else {
            int newEnd = start + 1;
            while (newEnd <= text.length) {
                if (isPalindrome(text, start, newEnd)) {
                    PalindromeGroup temp = new PalindromeGroup(text, start, newEnd);
                    if (newEnd < end) {
                        temp.append(dynamicProgrammingBreakIntoPalindromes(text, newEnd, end));
                    }
                    if (bestGroup == null || bestGroup.length() > temp.length()) {
                        bestGroup = temp;
                    }
                }
                newEnd++;
            }
            findings.put(range, bestGroup);
        }

        return bestGroup;
    }

    /**
     * This is a way to try the different ways of breaking up the userInput by only changing one
     * line of code instead of commenting out the other functions
     *
     * @param text  the userInput as a character array
     * @param start the int value of the start of the array, usually will be 0
     * @param end   the int value of the length of the array
     * @return PalindromeGroup the final output of palindromes
     */
    private PalindromeGroup breakIntoPalindromes(char[] text, int start, int end) {
        PalindromeGroup result = dynamicProgrammingBreakIntoPalindromes(text, start, end);
        return result;
    }
}