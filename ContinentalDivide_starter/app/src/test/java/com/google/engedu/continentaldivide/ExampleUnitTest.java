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

package com.google.engedu.continentaldivide;

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
    @Test
    public void helperTester() throws  Exception{
        int temp = 0;
        for(int i = 0; i < 5;i++){
            for ( int k = 0;k < 5;k ++){
                assertArrayEquals(new int[]{k,i},healper(temp));
                temp++;
            }
        }
    }
    public int[] healper(int input){
        int[] output = new int[2];
        int x=input%5;
        int y=input/5;
        output[0] = x;
        output[1] = y;
        return output;
    }
}