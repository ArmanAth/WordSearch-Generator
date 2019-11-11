/**
 * WordSearch class
 * Takes a list of words from a text file from user, and generates a random word search with those words, outputted into a text and HTML file
 * @author Arman Atharinejad
 * @version October 2018
 */
package WordSearch;

import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WordSearch {
    public static void main (String[] args) throws IOException{
        //Creating scanner and file
        Scanner scanner = new Scanner(System.in);

        //Declaring variables
        final char[] CHARACTERS = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        final String TEXT_FILE = "wordsearch.txt";
        final String HTML_FILE = "wordsearch.html";
        String[] words;
        String fileName;
        int writeDirection, sideLength, lengthLongest;
        boolean goodSide, fileExists;

        //Asking user for file name
        System.out.println("Please enter the file name of the word list (don't include file extension)");
        //Checking if file exists
        do{
            fileName = scanner.nextLine();
            File file = new File(fileName + ".txt");
            if(!file.exists()){
                System.out.println("This file does not exist. Please create a file or enter the correct name (don't include file extension)");
                fileExists = false;
            }
            else{
                fileExists = true;
            }
        }while(!fileExists);

        //Calling method to read in all words for word search, find length of the longest word
        words = readWords(fileName);
        lengthLongest = longestWord(words);

        //Asking user for side length and making sure they picked a reasonable length
        System.out.println("Please enter the side length of the word search");
        do {
            sideLength = scanner.nextInt();
            //Asking user to pick a larger number if the length is less than number of words or length of longest word
            if (sideLength < readNumWords(fileName) || sideLength <= lengthLongest){
                System.out.println("Please choose a length longer than the number of words and the length of the longest word");
                goodSide = false;
            }
            else{
                goodSide = true;
            }

        }while(!goodSide);

        //Declaring 2d array for grid
        char[][] grid = new char[sideLength][sideLength];

        //Filling word search with empty spaces to later fill with random characters
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = ' ';
            }
        }

        //Beginning of loop to fill grid with words
        for(int i = 0; i < words.length; i++){
            //Determining how to put current word into grid (1= horizontal, 2= vertical, 3 diagonal,)
            writeDirection = (int)((Math.random() * 3 + 1));

            //Calling specific method to type out word onto grid, depending on the value of writeDirection
            if(writeDirection == 1){
                grid = writeHortizontal(grid, words[i], sideLength);
            }
            else if(writeDirection == 2){
                grid = writeVertical(grid, words[i], sideLength);
            }
            else if(writeDirection == 3){
                grid = writeDiagonal(grid, words[i], sideLength);
            }
        }

        //Loop to fill remainder of word search with characters
        for(int i = 0; i < sideLength; i++){
            for(int j = 0; j < sideLength; j++){
                //checking if spot in the grid is an empty space
                if(grid[i][j] == ' '){
                    //picking a random character from array of characters
                    grid[i][j] = CHARACTERS[(int)(Math.random() * 26)];
                }
            }
        }

        //Calling methods to create and write the word search into text and html files
        writeHtml(grid, HTML_FILE, words);
        writeText(grid, TEXT_FILE, words);

        //Closing scanner
        scanner.close();
        //Telling user word search has been generated
        System.out.println("Please check your folder for the word search");
    }
    //----------------------------------------------
    /**
     * readNumWords
     * Reads a file containing words, counts the number of words
     * method returns an int with number of words
     * @param fileName name of text file containing list of words
     * @return int, with number of words from "words.txt"
     */
    public static int readNumWords(String fileName) throws IOException{
        //Creating file and file scanner
        File file = new File(fileName + ".txt");
        Scanner fileScanner = new Scanner(file);

        //Declaring variables
        int numWords = 0;

        //Finding how many words are in the file
        while(fileScanner.hasNext()){
            fileScanner.next();
            numWords++;
        }
        //Closing scanner
        fileScanner.close();
        return numWords;
    }
    //-----------------------------------------------------------------
    /**
     * readWords
     * Reads words from a text file into an array
     * method returns an array of strings
     * @param fileName name of file containing list of words
     * @return string array, with words read in from "words.txt"
     */
    public static String[] readWords(String fileName) throws IOException{
        //Creating file and file scanner
        File file = new File(fileName + ".txt");
        Scanner fileScanner = new Scanner(file);

        //Declaring array, calling readNumWords method to determine the length of the array
        String[] words = new String[readNumWords(fileName)];

        //Reading in words
        for(int i = 0; i < words.length; i++){
            words[i] = fileScanner.nextLine();
        }
        //Closing scanner
        fileScanner.close();
        return words;
    }
    //-----------------------------------------------------------------
    /**
     * longestWord
     * Reads words from an array and finds length of longest word
     * method returns an int
     * @param words array of words
     * @return int, with length of longest word
     */
    public static int longestWord(String[] words){
        //Declare variables
        int currentLength;
        int max = 0;
        for(int i = 0; i < words.length; i++){
            currentLength = words[i].length();
            if(currentLength > max){
                max = currentLength;
            }
        }
        return max;
    }
    //-----------------------------------------------------------------
    /**
     * writeHorizontal
     * Takes current grid and word, puts word into grid in a horizontal direction
     * method returns a 2d char array
     * @param grid current word search grid
     * @param word current word to be put into the grid
     * @param size length of grid
     * @return updated grid with word printed out horizontally
     */
    public static char[][] writeHortizontal(char[][] grid, String word, int size){
        //Finding length of word
        int length = word.length();
        //initializing x and y
        int x;
        int y;
        //Initializing crossover check condition elements
        boolean validPlacement;
        int yDirection = 0;
        int xDirection;
        int direction = 1;

        do {
            //Determining random y coordinate
            y = (int)(Math.random() * (size));
            //Randomizing 1 or -1 to make word go up or down
            xDirection = (int) ((Math.random() * 2) + 1);
            if (xDirection == 1) {
                xDirection = 1;
                //Making x coordinate at least "length" units from the right to ensure no out of bounds exceptions
                x = (int) (Math.random() * (size - (length)));
            } else {
                xDirection = -1;
                //Making y coordinate at least "length" units from the top to ensure no out of bounds exceptions
                x = (int) ((Math.random() * (size - (length))) + length);
            }
            //Checking if the word, with current given coordinates, can be placed (either overlaps or doesn't hit another word)
            validPlacement = checkCross(grid, word, x, y, xDirection, yDirection, direction);
        }while (!validPlacement);

        //Putting word, char by char, into grid array
        for(int i = 0; i < word.length(); i++){
            grid[x][y] = word.charAt(i);
            //Adding one to X so next char will be printed 1 space right
            x = x + xDirection;
        }
        return grid;
    }

    //-----------------------------------------------------------------
    /**
     * writeVertical
     * Takes current grid and word, puts word into grid in a vertical direction
     * method returns a 2d char array
     * @param grid current word search grid
     * @param word current word to be put into the grid
     * @param size length of grid
     * @return updated grid with word printed out vertically
     */
    public static char[][] writeVertical(char[][] grid, String word, int size){
        //Finding length of word
        int length = word.length();
        //initializing x and y
        int y;
        int x;
        //Initializing crossover check condition elements
        boolean validPlacement;
        int xDirection = 0;
        int yDirection;
        int direction = 2;

        do {
            //Randomizing x coordinate
            x = (int) (Math.random() * (size));
            //Randomizing 1 or -1 to make word go up or down
            yDirection = (int) ((Math.random() * 2) + 1);
            if (yDirection == 1) {
                yDirection = 1;
                //Making y coordinate at least "length" units from the bottom to ensure no out of bounds exceptions
                y = (int) (Math.random() * (size - (length)));
            }
            else {
                yDirection = -1;
                //Making y coordinate at least "length" units from the top to ensure no out of bounds exceptions
                y = (int) ((Math.random() * (size - (length))) + length);
            }
            //Checking if the word, with current given coordinates, can be placed (either overlaps or doesnt hit another word)
            validPlacement = checkCross(grid, word, x, y, xDirection, yDirection, direction);
        }while(!validPlacement);

        //Putting word, char by char, into grid array
        for(int i = 0; i < word.length(); i++){
            grid[x][y] = word.charAt(i);
            //Adding one to y so next char will be printed 1 space down
            y = y + yDirection;
        }
        return grid;
    }
    //-----------------------------------------------------------------
    /**
     * writeDiagonal
     * Takes current grid and word, puts word into grid in a diagonal direction
     * method returns a 2d char array
     * @param grid current word search grid
     * @param word current word to be put into the grid
     * @param size length of grid
     * @return updated grid with word printed out diagonally upwards
     */
    public static char[][] writeDiagonal(char[][] grid, String word, int size){
        //Finding length of word
        int length = word.length();
        //Initializing x and y
        int y = 0;
        int x = 0;
        int yDirection = 0;
        int xDirection = 0;
        int direction;
        //Initializing crossover check condition elements
        boolean validPlacement;
        int checkDirection = 3;

        do{
            //Rand number from 1-4 to determine which directions the word will be written out
            direction = (int) ((Math.random() * 4) + 1);
            if(direction == 1){
                //X moves right, Y moves down
                yDirection = 1;
                xDirection = 1;
                //Making x and y coordinates randomized, but certain distance from borders to prevent out of bounds
                y = (int)(Math.random() * (size - (length)));
                x = (int)(Math.random() * (size - (length)));
            }
            else if(direction == 2){
                //X moves right, y moves up
                yDirection = -1;
                xDirection = 1;
                //Making x and y coordinates randomized, but certain distance from borders to prevent out of bounds
                y = (int)((Math.random() * (size - (length))) + length);
                x = (int)(Math.random() * (size - (length)));
            }
            else if(direction == 3){
                //X moves left, y moves down
                yDirection = 1;
                xDirection = -1;
                //Making x and y coordinates randomized, but certain distance from borders to prevent out of bounds
                y = (int)(Math.random() * (size - (length)));
                x = (int)((Math.random() * (size - (length))) + length);
            }
            else if(direction == 4){
                //X moves right, y moves up
                yDirection = -1;
                xDirection = -1;
                //Making x and y coordinates randomized, but certain distance from borders to prevent out of bounds
                y = (int)((Math.random() * (size - (length))) + length);
                x = (int)((Math.random() * (size - (length))) + length);
            }
            //Checking if the word, with current given coordinates, can be placed (either overlaps or doesnt hit another word)
            validPlacement = checkCross(grid, word, x, y, xDirection, yDirection, checkDirection);
        }while(!validPlacement);

        //Putting word, char by char, into grid array
        for(int i = 0; i < word.length(); i++){
            grid[x][y] = word.charAt(i);
            //Adding one to y so next char will be printed 1 space down
            y = y + yDirection;
            x = x + xDirection;
        }
        return grid;
    }
    //-----------------------------------------------------------------
    /**
     * checkCross
     * Takes current grid and word, generated x and y coordinates and directions, determines if word can be printed
     * method returns a boolean to determine if the words coordinates have to be generated again
     * @param grid current grid
     * @param word current word
     * @param x starting x coordinate of word
     * @param y starting y coordinate of word
     * @param yDirection vertical direction the word will be printed in the grid
     * @param xDirection horizontal direction the word will be printed in the grid
     * @param direction direction the word will be printed (horizontal, vertical, diagonal)
     * @return boolean either true or false to determine if coordinates have to be regenerated
     */
    public static boolean checkCross(char[][] grid, String word, int x, int y, int xDirection, int yDirection, int direction) {
        //Declaring condition as true, to be changed to false if word cannot crossover
        boolean valid = true;

        //If-else if structure  to take each case (horizontal, vertical, diagonal)
        if (direction == 1) {
            //Horizontal case
            for (int i = 0; i < word.length(); i++) {
                //Checking if there is a character in the specified grid space
                if (grid[x][y] != ' ') {
                    //Checking if the character at the specified index of the grid matches character at index of word
                    if (grid[x][y] != word.charAt(i)) {
                        valid = false;
                    }
                }
                //Advancing x coordinate to check next space
                x = x + xDirection;
            }
        } else if (direction == 2) {
            //Vertical case
            for (int i = 0; i < word.length(); i++) {
                //Checking if there is a character in the specified grid space
                if (grid[x][y] != ' ') {
                    //Checking if the character at the specified index of the grid matches character at index of word
                    if (grid[x][y] != word.charAt(i)) {
                        valid = false;
                    }
                }
                //Advancing y coordinate to check next space
                y = y + yDirection;
            }
        } else if (direction == 3) {
            //Diagonal case
            for (int i = 0; i < word.length(); i++) {
                //Checking if there is a character in the specified grid space
                if (grid[x][y] != ' ') {
                    //Checking if the character at the specified index of the grid matches character at index of word
                    if (grid[x][y] != word.charAt(i)) {
                        valid = false;
                    }
                }
                //Advancing x and y coordinates to check next space
                y = y + yDirection;
                x = x + xDirection;
            }
        }
        return valid;
    }
    //-----------------------------------------------------------------
    /**
     * writeHtml
     * Takes completed grid, name of html file and all words, creates html file and writes in the wordsearch
     * @param grid completed word search grid
     * @param fileName name of html file to create
     * @param words list of all words
     */
    public static void writeHtml(char[][] grid, String fileName, String[] words) throws  IOException{
        //Creating file and bufferedwriter objects
        File file = new File(fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        //Basic tags and table properties
        bw.write("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<h1><font size = +5;> Word Search</font></h1>" +
                "<style>" +
                "table { float: left; border-collapse: collapse; table-layout: fixed; }" +
                "th, td { border: 1px solid black; text-align: center; width: 50px; }" +
                "tab { padding: 15px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<font size = +3>");
        //Opening table
        bw.write("<table>");
        //Putting grid array into a table
        for(int i = 0; i < grid.length; i++){
            //New row
            bw.write("<tr>");
            for(int j = 0; j < grid.length; j++){
                //Putting characters into grid spaces
                bw.write("<td>" + grid[i][j] + "</td>");
            }
            //Close tag for row
            bw.write("</tr>");
        }
        //Closing table
        bw.write("</table>");
        //Adding padding to reduce space between words
        bw.write("<tab>Words<div>");
        //Printing out words beside the table
        for(int i = 0; i < words.length; i++){
            bw.write("<div><tab><font size = +2>" + words[i] + "</font>");
        }
        //closing tags
        bw.write("</font>" +
                "</body>" +
                "</html>");
        //close bufferedwriter
        bw.close();
    }
    //-----------------------------------------------------------------
    /**
     * writeText
     * Takes completed grid, name of text file and all words, creates text file and writes in the wordsearch
     * @param grid completed word search grid
     * @param fileName name of file to be created
     * @param words list of all words
     */
    public static void writeText(char[][] grid, String fileName, String[] words) throws  IOException{
        //Creating file and filewriter objects
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file);

        //Printing all the words
        for(String word: words){
            pw.println(word);
        }

        //Printing grid
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                pw.print(grid[i][j] + " ");
            }
            //new row
            pw.println("");
        }
        //closing printwriter
        pw.close();
    }
}

