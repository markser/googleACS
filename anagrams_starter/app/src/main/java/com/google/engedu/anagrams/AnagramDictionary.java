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

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private int wordLength = DEFAULT_WORD_LENGTH;

    //required data structures for this app to work properly
    private ArrayList<String> wordList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();

            //add to the arrayList data structure
            wordList.add(word);

            //add the same word to a hashSet and hashMap
            wordSet.add(word);

            String sortedWord = sortLetters(word);

            //if sortedWord is already a key, we add it to the same key
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            }
            //else we create a new arrayList and add that as the value to that key in the hm
            else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                lettersToWord.put(sortedWord, temp);
            }
            //check word length and store in sizeToWords
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                sizeToWords.put(word.length(), temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //if valid, then hashSet has that word in it && the word should not be a subString of the baseWord; i.e prefix or postfix
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        //sort the target word
        String sortedTargetWord = sortLetters(targetWord);

        //first step is to iterate through all words and find the anagrams
        for (String word : wordList) {
            //sort the word
            String sortedWord = sortLetters(word);

            //if it matches to sortedTargetWord, then it's an anagram of it
            if (sortedTargetWord.equals(sortedWord)) {
                //add the original word
                result.add(word);
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordKey;
        for (char i = 'a'; i <= 'z'; i++) {
            wordKey = sortLetters(word.concat("" + i));
            if(lettersToWord.containsKey(wordKey)) {
                result.addAll(lettersToWord.get(wordKey));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> t = sizeToWords.get(wordLength);
        if(wordLength < MAX_WORD_LENGTH) wordLength++;
        while (true) {
            String wordTemp = t.get(random.nextInt(t.size()));
            if(getAnagramsWithOneMoreLetter(wordTemp).size() >= MIN_NUM_ANAGRAMS)
                return wordTemp;
        }
    }


    private String sortLetters(String word) {
        char[] words = word.toCharArray();
        Arrays.sort(words);
        return new String(words);
    }
}