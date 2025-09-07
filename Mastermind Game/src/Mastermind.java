
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nitya
 */
public class Mastermind {

    private final String[] COLOUR = {"R", "G", "B", "Y", "C", "P"};
    private String[][] board; //board array
    private String[][] guessedCheck;// hint array
    private String[] secret;// secret code
    private String[] guess;//user guess 

    private int rows; //Attempts
    private int cols; //pegs

    private int exact = 0; //exact
    private int justColor = 0; //same colour

    //player name 
    String name = "";

    //colours
    String r = "\u001B[31m";
    String g = "\u001B[32m";
    String b = "\u001B[34m";
    String y = "\u001B[33m";
    String p = "\u001B[35m";
    String cy = "\u001B[36m";
    String reset = "\u001B[0m";
    //blue= B, purple=p; yellow= Y; green =g; red=r; c: cyan

    Scanner s = new Scanner(System.in);

    public Mastermind() {
        init();
        board = new String[rows][cols];
        guessedCheck = new String[rows][cols];
        guess = new String[cols];
        displayGrid();
        play();
    }

    private void init() {
        System.out.println(r + "WELCOME TO MASTERMIND" + reset);
        System.out.print("Enter your Name: ");
        name = s.next(); //saves the name
        getPegs(); //gets the pegs
        secret = randomCode(); //generates the random code
        howToPlay(); // description
    }

    private void displayGrid() {
        
        //fills the array and outputs 2D array
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != null) {
                    System.out.print(b + " " + board[r][c] + " " + reset);
                } else {
                    System.out.print(" 0 ");
                }
            }
            System.out.print(" -->  ");
            for (int c = 0; c < cols; c++) {
                if (guessedCheck[r][c] != null) {
                    System.out.print(guessedCheck[r][c] + " " + reset);
                } else {
                    System.out.print(" - ");
                }
            }
            System.out.println();
        }
    }

    private void getPegs() {
        //gets the peg- (cols) and sets rows according to that
        do {
            System.out.print("How many pegs do you want to play with (4,5 or 6): ");
            cols = s.nextInt();
            
        } while (cols < 4 || cols > 6);

        switch (cols) {
            case 5:
                rows = 12;
                break;
            case 6:
                rows = 14;
                break;
            case 4:
                rows = 10;
                break;
            default:
                break;
        }

    }

    private void howToPlay() {
        System.out.println("HOW TO PLAY....");
        System.out.println("First it will display a grid with 0's ");
        System.out.println("Then Enter a sequence of " + cols + " letters representing your guess for the secret code.");
        System.out.println("The letters are representing colors:-");
        System.out.println(r + "Red - " + "r");
        System.out.println(b + "Blue - " + "b");
        System.out.println(p + "Purple- " + "p");
        System.out.println(y + "Yellow- " + "y");
        System.out.println(g + "Green- " + "g");
        System.out.println(cy + "Cyan- " + "c" + reset);
        System.out.println("Enter the guess - for e.g- rbpy");
        System.out.println("*****************************************************************************************************");
        System.out.println("After entering your guess, the program will display the game board, showing your guess, the number of "
                + "\ncorrect color, correct position- " + r + "Red" + reset
                + "\ncorrect color- " + y + "Yellow" + reset
                + "\nif nothing matches- Black.");
        System.out.println("Repeat until you get the right guess before you run out of attempts");
        System.out.println("******************************************************************************************************");
        System.out.println();
    }

    private void colourCheckHint(String[] g, String[] s) {
        //checks the colour
        for (int i = 0; i < g.length; i++) {
            if (g[i].equals(s[i])) {
                exact++;
            } else {
                for (int j = 0; j < g.length; j++) {
                    if (g[i].equals(s[j])) {
                        justColor++;
                    }
                }
            }
        }
    }

    private void fillGuessedCheckArray(int k) {
        //fills the colour check array
        for (int i = 0; i < guessedCheck[k].length; i++) {
            if (exact > 0) {
                guessedCheck[k][i] = r + "RED" + reset;
                exact--;
            } else if (justColor > 0) {
                guessedCheck[k][i] = y + "YELLOW" + reset;
                justColor--;
            } else {
                guessedCheck[k][i] = "BLACK";
            }
        }
    }

    private void fillGuessArray(int k) {
        //fills the board array with guess
        for (int i = 0; i < guess.length; i++) {
            board[k][i] = guess[i];
        }
    }

    private void play() {
        Scanner sc = new Scanner(System.in);
        for (int k = rows - 1; k >= 0; k--) { //rows 
            int attempts = rows - k;
            boolean isValidGuess = false;
            //checks for valid input
            while (!isValidGuess) {
                System.out.print(p + "\nAttempt:- "+attempts+" Colours - {r, c, b, p, y, g} Enter your guess: " + reset);
                String guessStr = sc.nextLine().toUpperCase();
                guessStr = guessStr.replaceAll("\\W", "");
                if (isValidColor(guessStr) && guessStr.length() == cols) {
                    guess = guessArray(guessStr);
                    isValidGuess = true;
                } else {
                    System.out.println(r + "Invalid input!" + reset);
                }
            }
            //fills the arrays 
            fillGuessArray(k);
            colourCheckHint(guess, secret);
            //checks for win
            if (exact == cols) {
                System.out.println(g + "Congratulations! You guessed the code! "
                        + name + " in the " + attempts + "th attempt." + reset);
                writeWinnerToFile(name, attempts);
                break;
            }
            fillGuessedCheckArray(k);
            displayGrid();
        }
        
        // if attempts are over then lose
        if (exact < cols) {
            System.out.println(r + "You Lose! " + name + reset);
            System.out.println(b + "This is the answer" + Arrays.toString(secret));
        }

    }

    private String[] guessArray(String guessStr) {
        //takes the guess string and converts it into guesssrray
        String[] g = new String[cols];
        for (int i = 0; i < g.length; i++) {
            g[i] = guessStr.substring(i, i + 1);
        }
        return g;
    }

    private boolean isValidColor(String str) {
      //checks for valid color input
        for (int i = 0; i < str.length(); i++) {
            boolean valid = false;
            for (String s : COLOUR) {
                if (str.equals("Q") || str.equals("q")) {
                    System.out.println(r + "You Quit! " + name + reset);
                    System.exit(0);
                }
                if (s.equals(str.substring(i, i + 1))) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    private String[] randomCode() {
        //random secret code
        Random random = new Random();
        String[] c = new String[cols];
        boolean[] d = new boolean[6];
        for (int i = 0; i < c.length; i++) {
            int digit;
            do {
                digit = random.nextInt(6) + 1;
            } while (d[digit - 1]);
            switch (digit) {
                case 1:
                    c[i] = COLOUR[digit - 1];
                    break;
                case 2:
                    c[i] = COLOUR[digit - 1];
                    break;
                case 3:
                    c[i] = COLOUR[digit - 1];
                    break;
                case 4:
                    c[i] = COLOUR[digit - 1];
                    break;
                case 5:
                    c[i] = COLOUR[digit - 1];
                    break;
                case 6:
                    c[i] = COLOUR[digit - 1];
                    break;
                default:
                    break;
            }
            d[digit - 1] = true;
        }
        return c;
    }

    private void writeWinnerToFile(String n, int attempts) {
        //puts the winner in txt file
        try {
            FileWriter f = new FileWriter("win.txt", true); // Append mode
            BufferedWriter b = new BufferedWriter(f);
            b.write("Winner: " + n + ", Attempts: " + attempts);
            b.newLine();
            b.close();

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        openWinnersFile();
    }

    private void openWinnersFile() {
        //opens the file 
        try {
            File f = new File("win.txt");
            if (f.exists()) {
                Desktop.getDesktop().open(f);
            } else {
                System.out.println("File not found.");
            }
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
}
