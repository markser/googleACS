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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//Still don't really understand what is actually happening, but it works apparently.


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random rand = new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String input) {
        HashMap<Character, TrieNode> temp = children;
        for (int x = 0; x < input.length(); x++) {
            if (!temp.containsKey(input.charAt(x))) {
                temp.put(input.charAt(x), new TrieNode());
            }
            if (x == input.length() - 1) {
                temp.get(input.charAt(x)).isWord = true;
            }
            temp = temp.get(input.charAt(x)).children;
        }
    }

    public boolean isWord(String input) {
        TrieNode temp = searchNode(input);
        return temp != null && temp.isWord;
    }

    private TrieNode searchNode(String input) {
        TrieNode temp = this;
        for (int x = 0; x < input.length(); x++) {
            if (!temp.children.containsKey(input.charAt(x))) {
                return null;
            }
            temp = temp.children.get(input.charAt(x));
        }
        return temp;
    }

    public String getAnyWordStartingWith(String input) {
        TrieNode temp = searchNode(input);
        if (temp == null) {
            return null;
        }
        while (!temp.isWord) {
            for (Character c : temp.children.keySet()) {
                temp = temp.children.get(c);
                input += c;
                break;
            }
        }
        return input;
    }

    public String getGoodWordStartingWith(String input) {
        TrieNode temp = searchNode(input);
        if (temp == null) {
            return null;
        }
        // get a random word
        ArrayList<Character> notWord = new ArrayList<>();
        ArrayList<Character> word = new ArrayList<>();

        Character c;

        while (true) {
            notWord.clear();
            word.clear();
            for (Character foo : temp.children.keySet()) {
                if (temp.children.get(foo).isWord) {
                    word.add(foo);
                } else {
                    notWord.add(foo);
                }
            }
            if (notWord.size() == 0) {
                input += word.get(rand.nextInt(word.size()));
                break;
            } else {
                c = notWord.get(rand.nextInt(notWord.size()));
                input += c;
                temp = temp.children.get(c);
            }
        }
        return input;
    }
}