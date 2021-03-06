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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //Testing private functions are a bad idea
    //so i just copied the function over
    @Test
    public void testisPalindrome() throws Exception {
        char[] testarino = {'t', 'a', 'c', 'o', 'c', 'a', 't'};
        int start = 0;
        int end = testarino.length ;
        assertEquals(true, isPalindrome(testarino, start, end));

        String input = "Was it a cat I saw";
        input = input.toLowerCase().replaceAll("\\s+", "");
        System.out.println(input);
        char[] testarino2 = input.toCharArray();
        start = 0;
        end = testarino2.length ;
        assertEquals(true, isPalindrome(testarino2, start, end));

        input = "No lemon, no melon";
        input = input.toLowerCase().replaceAll("\\s+", "");
        System.out.println(input);
        testarino2 = input.toCharArray();
        start = 0;
        end = testarino2.length ;
        assertEquals(true, isPalindrome(testarino2, start, end));

        input = "Steponnopets";
        input = input.toLowerCase().replaceAll("\\s+", "");
        System.out.println(input);
        testarino2 = input.toCharArray();
        start = 0;
        end = testarino2.length ;
        System.out.println(end);
        assertEquals(true, isPalindrome(testarino2, start, end));

    }

    private boolean isPalindrome(char[] text, int start, int end) {
        end--;// discarding excluding one
        while(start<=end)
        {
            if(!(text[start]==text[end]))
            {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

}