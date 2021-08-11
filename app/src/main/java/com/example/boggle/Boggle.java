package com.example.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Boggle {

    //Global variables referenced by private functions.
    private List<String> legalWords = new ArrayList<String>(); //Dictionary of words
    private List<String> wordsFound = new ArrayList<String>(); //Found words to return
    private int xMax, yMax, boardWidth, boardHeight; //Board dimensions
    char[][] board; //Represents board grid

    public Boggle() {
    }

    // prior to solving any board, configure the legal words
    public void setLegalWords(List<String> legalWords)
    // alphabetically-sorted array of legal words
    {
        this.legalWords.addAll(legalWords);
    }

    // Solves the given boggle board
    // boardWidth: width of the board
    // boardHeight: height of the board
    // boardLetters: boardWidth*boardHeight characters - row major
    public List<String> solveBoard(int boardWidth, int boardHeight, String boardLetters) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.xMax = boardWidth - 1;
        this.yMax = boardHeight - 1;
        this.board = new char[boardHeight][boardWidth];
        String currWord = "";

        // Place letters into grid
        board = setBoard(boardLetters);
        // Remove some ineligible words
        optimizeLegalWordList(boardLetters);

        // For each board letter try and find a path for words recursively.
        // creates a parallel 2d array of the board to track used letters.
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                boolean[][] boardLetterUsed = new boolean[boardHeight][boardWidth];
                setBoardLetterUsedFalse(boardLetterUsed);
                currWord = "";
                traverseBoard(currWord, x, y, boardLetterUsed);
            }
        }

        Collections.sort(wordsFound);
        System.out.println(wordsFound);

        return wordsFound;
    }

    // Initializes board on a 2d array.
    private char[][] setBoard(String boardLetters) {
        char[][] board = new char[boardWidth][boardHeight];
        int charIndex = 0;
        boardLetters = boardLetters.toLowerCase();

        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                board[y][x] = boardLetters.charAt(charIndex);
                charIndex++;
            }
        }
        printBoard(board);
        return board;
    }

    // Recursively traverses every path of letters on the board. When a valid word is found
    // it is added to the list.
    // currWord: Current string of letters being traversed.
    // x: Current x coordinate on the board 2d array.
    // y: Current y coordinate on the board 2d array.
    // boardLetterUsed: Parallel 2d array that tracks if a letter has been used in the current
    //                  recursion call.
    private void traverseBoard(String currWord, int x, int y, boolean[][] boardLetterUsed) {
        // Adds current letter to the string
        currWord = currWord + board[y][x];
        // Flags position(letter) as used
        boardLetterUsed[y][x] = true;
        // Add current string if its a legal word.
        containsString(currWord);

        // Recursive traverse calls ***********************************************************
        // Traverse Up if able
        if (y > 0 && !boardLetterUsed[y - 1][x]) {
            traverseBoard(currWord, x, y - 1, boardLetterUsed);
            boardLetterUsed[y - 1][x] = false;
        }

        //Traverse Up-Right if able
        if (x < xMax && y > 0 && !boardLetterUsed[y - 1][x + 1]) {
            traverseBoard(currWord, x + 1, y - 1, boardLetterUsed);
            boardLetterUsed[y - 1][x + 1] = false;
        }

        //Traverse Right if able
        if (x < xMax && !boardLetterUsed[y][x + 1]) {
            traverseBoard(currWord, x + 1, y, boardLetterUsed);
            boardLetterUsed[y][x + 1] = false;
        }

        //Traverse Down-Right if able
        if (x < xMax && y < yMax && !boardLetterUsed[y + 1][x + 1]) {
            traverseBoard(currWord, x + 1, y + 1, boardLetterUsed);
            boardLetterUsed[y + 1][x + 1] = false;
        }

        //Traverse Down if able
        if (y < yMax && !boardLetterUsed[y + 1][x]) {
            traverseBoard(currWord, x, y + 1, boardLetterUsed);
            boardLetterUsed[y + 1][x] = false;
        }

        //Traverse Down-Left if able
        if (x > 0 && y < yMax && !boardLetterUsed[y + 1][x - 1]) {
            traverseBoard(currWord, x - 1, y + 1, boardLetterUsed);
            boardLetterUsed[y + 1][x - 1] = false;
        }

        //Traverse Left if able
        if (x > 0 && !boardLetterUsed[y][x - 1]) {
            traverseBoard(currWord, x - 1, y, boardLetterUsed);
            boardLetterUsed[y][x - 1] = false;
        }

        //Traverse Left-Up if able
        if (x > 0 && y > 0 && !boardLetterUsed[y - 1][x - 1]) {
            traverseBoard(currWord, x - 1, y - 1, boardLetterUsed);
            boardLetterUsed[y - 1][x - 1] = false;
        }
        // End of recursive traverse calls ****************************************************
    }

    //Check if word is legal and add it list to be returned
    private void containsString(String currWord) {
        int result = Collections.binarySearch(legalWords, currWord);
        if (result >= 0 && !wordsFound.contains(currWord)) {
            wordsFound.add(currWord);
        }
    }


    // Prints the board to the system output.
    // Used for testing.
    public String printBoard(char[][] board) {
        String output = new String();
        for (int y = 0; y < board.length; y++) {
            output = output + "\n";
            for (int x = 0; x < board[0].length; x++) {
                output = output + "[" + board[y][x] + "]";
            }
        }
        System.out.println(output);
        return output;
    }

    //Set letter used checks to false
    private void setBoardLetterUsedFalse(boolean[][] boardLetterUsed) {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                boardLetterUsed[y][x] = false;
            }
        }

    }

    //Remove words that don't start with a letter on the board
    //and are less than 3 in length to optimize list traversal.
    private void optimizeLegalWordList(String boardLetters) {
        boolean hasFirstLetter = false;
        for (int i = legalWords.size() - 1; i >= 0; i--) {
            if (legalWords.get(i).length() < 3)
                legalWords.remove(i);
        }
        for (int i = legalWords.size() - 1; i >= 0; i--) {
            for (int j = 0; j < boardLetters.length(); j++) {
                if (legalWords.get(i).startsWith(String.valueOf(boardLetters.charAt(j)))) {
                    hasFirstLetter = true;
                }
            }
            if (!hasFirstLetter) {
                legalWords.remove(i);
            }
            hasFirstLetter = false;
        }
    }
}

