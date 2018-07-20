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

package com.google.engedu.blackhole;

import android.nfc.Tag;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.content.ContentValues.TAG;

/* Class that represent the state of the game.
 * Note that the buttons on screen are not updated by this class.
 */
public class BlackHoleBoard {
    // The number of turns each player will take.
    public final static int NUM_TURNS = 10;
    // Size of the game board. Each player needs to take 10 turns and leave one empty tile.
    public final static int BOARD_SIZE = NUM_TURNS * 2 + 1;
    // Relative position of the neighbors of each tile. This is a little tricky because of the
    // triangular shape of the board.
    public final static int[][] NEIGHBORS = {{-1, -1}, {0, -1}, {-1, 0}, {1, 0}, {0, 1}, {1, 1}};
    // When we get to the Monte Carlo method, this will be the number of games to simulate.
    private static final int NUM_GAMES_TO_SIMULATE = 2000;
    // The tiles for this board.
    public BlackHoleTile[] tiles;
    // The number of the current player. 0 for user, 1 for computer.
    private int currentPlayer;
    // The value to assign to the next move of each player.
    private int[] nextMove = {1, 1};
    // A single random object that we'll reuse for all our random number needs.
    private static final Random random = new Random();

    // Constructor. Nothing to see here.
    BlackHoleBoard() {
        tiles = new BlackHoleTile[BOARD_SIZE];
        reset();
    }

    // Copy board state from another board. Usually you would use a copy constructor instead but
    // object allocation is expensive on Android so we'll reuse a board instead.
    public void copyBoardState(BlackHoleBoard other) {
        this.tiles = other.tiles.clone();
        this.currentPlayer = other.currentPlayer;
        this.nextMove = other.nextMove.clone();
    }

    // Reset this board to its default state.
    public void reset() {
        currentPlayer = 0;
        nextMove[0] = 1;
        nextMove[1] = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles[i] = null;
        }
    }

    // Translates column and row coordinates to a location in the array that we use to store the
    // board.
    protected int coordsToIndex(int col, int row) {
        return col + row * (row + 1) / 2;
    }

    // This is the inverse of the method above.
    protected Coordinates indexToCoords(int i) {
        Coordinates result = new Coordinates();
        // TODO: Compute the column and row number for the ith location in the array.
        // The row number is the triangular root of i as explained in wikipedia:
        // https://en.wikipedia.org/wiki/Triangular_number#Triangular_roots_and_tests_for_triangular_numbers
        // The column number is i - (the number of tiles in all the previous rows).
        // This is tricky to compute correctly so use the unit test in BlackHoleBoardTest to get it
        // right.
        result.y = yHelper(i);
        result.x = xHelper(i);
        return result;
    }

    private static int xHelper(int input) {
        int temp = yHelper(input);
        return (int) (input - ((Math.pow(temp, 2)) + temp) / 2);
    }

    private static int yHelper(int input) {
        return (int) (Math.sqrt((8 * input) + 1) - 1) / 2;
    }

    // Getter for the number of the player's next move.
    public int getCurrentPlayerValue() {
        return nextMove[currentPlayer];
    }

    // Getter for the number of the current player.
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    // Check whether the current game is over (only one blank tile).
    public boolean gameOver() {
        int empty = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {
                if (empty == -1) {
                    empty = i;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // Pick a random valid move on the board. Returns the array index of the position to play.
    public int pickRandomMove() {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {
                possibleMoves.add(i);
            }
        }
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }


    //I HATE MONTE CARLO
    //IT'S NOW ALMOST IMPOSSIBLE TO WIN
    //I LOVE MONTE CARLO

    //need to implement minmax, but now it's fine the way it is
    //bug? it's not picking random moves, it has it's default start moves and picks randomly after it goes pass 5ish

    public int pickMove() {
//        // TODO: Implement this method have the computer make a move.
//        // At first, we'll just invoke pickRandomMove (above) but later, you'll need to replace
//        // it with an algorithm that uses the Monte Carlo method to pick a good move.

        HashMap<Integer, ArrayList<Integer>> possibleMoves = new HashMap<>();
        ArrayList<Integer> invalidTiles = new ArrayList<>();

        //populates the Hashmap with ArrayLists
        for (int i = 0; i < BOARD_SIZE; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            possibleMoves.put(i, temp);
        }

        for (int k = 0; k < NUM_GAMES_TO_SIMULATE; k++) {
            //creates a new board and copies the current board state
            BlackHoleBoard copy = new BlackHoleBoard();
            copy.copyBoardState(this);
            //if any of the tiles are not clicked, then add placement to nulltiles
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (copy.tiles[i] == null) {
                    invalidTiles.add(i);

                }
            }
            int simKey = 0;
            int simPlayer = 1;
            //this is getting the values of the tiles, the circle that appear on the android device screen
            int uMoves = nextMove[0];
            int cMoves = nextMove[1];

            for (int i = 0; i < invalidTiles.size(); i++) {
                //preparing for getting random placement
                int randInt = random.nextInt(invalidTiles.size());
                int randTile = invalidTiles.get(randInt);
                copy.tiles[randTile] = new BlackHoleTile(simPlayer, simMoves(simPlayer, uMoves, cMoves));
                //handler to update simPlayer into either the user or the computer
                updateSim(simPlayer, uMoves, cMoves);
                invalidTiles.remove(randInt);
                simKey = randTile;
            }
            int simScore = copy.getScore();
            //add the simulated finished game with it's score to the Hashmap
            ArrayList<Integer> simGame = possibleMoves.get(simKey);
            simGame.add(simScore);
            possibleMoves.put(simKey, simGame);
        }
        //time to calculate the best move to make by using the score of the simulated game
        int avg = Integer.MAX_VALUE, bestPossibleMove = 0;
        for (int x = 0; x < BOARD_SIZE; x++) {
            ArrayList<Integer> temp = possibleMoves.get(x);
            //checking if the selected possible move is not selected in the actual game display on the android device
            // and if possibleMoves at x is not empty
            if (this.tiles[x] == null && !temp.isEmpty()) {
                int sum = 0, tempAvg;
                //getting the avg that in possibleMove[x]
                for (int y = 0; y < temp.size(); y++) {
                    sum += temp.get(y);
                }
                //compare that avg with the best avg that has been found
                tempAvg = sum / temp.size();
                //updates avg if the tempAvg is larger and stores the possible
                //tile location in bestPossibleMove
                if (tempAvg < avg) {
                    avg = tempAvg;
                    bestPossibleMove = x;
                }
            }
        }
        return bestPossibleMove;
    }

    private int simMoves(int simPlayer, int uMoves, int cMoves) {
        if (simPlayer == 0) {
            return uMoves;
        } else {
            return cMoves;
        }
    }

    private void updateSim(int simPlayer, int uMoves, int cMoves) {
        if (simPlayer == 0) {
            uMoves++;
            simPlayer = 1;
        } else {
            cMoves++;
            simPlayer = 0;
        }
    }


    // Makes the next move on the board at position i. Automatically updates the current player.
    public void setValue(int i) {
        tiles[i] = new BlackHoleTile(currentPlayer, nextMove[currentPlayer]);
        nextMove[currentPlayer]++;
        currentPlayer++;
        currentPlayer %= 2;
    }

    /* If the game is over, computes the score for the current board by adding up the values of
     * all the tiles that surround the empty tile.
     * Otherwise, returns 0.
     */
    public int getScore() {
        int score = 0;
        // TODO: Implement this method to compute the final score for a given board.
        // Find the empty tile left on the board then add/substract the values of all the
        // surrounding tiles depending on who the tile belongs to.
        Coordinates temp = new Coordinates();
        ArrayList<BlackHoleTile> foo = new ArrayList<>();
        if (gameOver()) {
            for (int x = 0; x < tiles.length; x++) {
                if (tiles[x] == null) {
                    temp = indexToCoords(x);
                }
            }
            foo = getNeighbors(temp);
            for (BlackHoleTile bar : foo) {
                //checks to see if the tile is user
                if (bar.player == 0) {
                    score += bar.value;
                } else {
                    //subtract score if tile is computer's
                    score -= bar.value;
                }
            }
        }
        return score;
    }

    // Helper for getScore that finds all the tiles around the given coordinates.
    public ArrayList<BlackHoleTile> getNeighbors(Coordinates coords) {
        ArrayList<BlackHoleTile> result = new ArrayList<>();
        for (int[] pair : NEIGHBORS) {
            BlackHoleTile n = safeGetTile(coords.x + pair[0], coords.y + pair[1]);
            if (n != null) {
                result.add(n);
            }
        }
        return result;
    }

    public ArrayList<Integer> getNeighboursCoordinates(Coordinates coords) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int[] pair : NEIGHBORS) {
            BlackHoleTile n = safeGetTile(coords.x + pair[0], coords.y + pair[1]);
            int tempX = coords.x + pair[0];
            int tempY = coords.y + pair[1];
            BlackHoleBoard temp = new BlackHoleBoard();
            if (n != null) {
                result.add(temp.coordsToIndex(tempX, tempY));
            }
        }
        return result;
    }

    // Helper for getNeighbors that gets a tile at the given column and row but protects against
    // array over/underflow.
    private BlackHoleTile safeGetTile(int col, int row) {
        if (row < 0 || col < 0 || col > row) {
            return null;
        }
        int index = coordsToIndex(col, row);
        if (index >= BOARD_SIZE) {
            return null;
        }
        return tiles[index];
    }
}
