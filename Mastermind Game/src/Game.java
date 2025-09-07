
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
 * @author 41874
 */
public class Game {

    String[][] board;
    String[] secret;
    String[] guess;
    String[] guessedCheck;
    String[] colors = {"R", "G", "B", "Y", "C", "P"};

    int rows = 10; //Attempts
    int cols; //pegs

    int exact = 0; //exact
    int sameColor = 0; //same colour

    String name = "";

    String r = "\u001B[31m";
    String g = "\u001B[32m";
    String b = "\u001B[34m";
    String y = "\u001B[33m";
    String p = "\u001B[35m";
    String cy = "\u001B[36m";
    String reset = "\u001B[0m";
    //blue= B, purple=p; yellow= Y; green =g; red=r; c: cyan

    public Game() {
        init();
        board = new String[rows][cols];
        displayGrid();
        play();
    }

    public void displayGrid() {

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

               if (board[r][c] != null) {
                System.out.print(b+board[r][c] + "   "+reset);
            } else {
                System.out.print("0   ");
            }
            }
            System.out.println();
        }
    }

    public void init() {
        System.out.println(r + "WELCOME TO MASTERMIND" + reset);
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your Name: ");
        name = s.next();
        getPegs();
        secret = randomCode();
        howToPlay();
        System.out.println(Arrays.toString(secret));

    }

    public void getPegs() {
        Scanner s = new Scanner(System.in);
        do {
            System.out.print("How many pegs do you want to play with (4,5 or 6): ");
            cols = s.nextInt();
        } while (cols < 4 || cols > 6);

    }

    public void howToPlay() {
        System.out.println("HOW TO PLAY....");
        System.out.println("First it will display a grid with 0's ");
        System.out.println("Then Enter a sequence of " + cols + " letters representing your guess for the secret code but you cant repeat any letters and do not put spaces between them.");
        System.out.println("The letters are representing colors:-");
        System.out.println(r + "Red - " + "r");
        System.out.println(b + "Blue - " + "b");
        System.out.println(p + "Purple- " + "p");
        System.out.println(y + "Yellow- " + "y");
        System.out.println(g + "Green- " + "g");
        System.out.println(cy + "Cyan- " + "c" + reset);
        System.out.println("Enter the guess - for e.g- rbpy");
        System.out.println("****************************************************************");
        System.out.println("After entering your guess, the program will display the game board, showing your guess, the number of "
                + "\ncorrect color, correct position " + r + "Red" + reset
                + "\ncorrect color " + y + "Yellow" + reset
                + "\nif nothing matches then Black.");
        System.out.println("Repeat until you get the right guess before you run out of attempts");
        System.out.println("***************************************************************");
        System.out.println();
    }

    public void play() {
        Scanner s = new Scanner(System.in);

        for (int k = rows - 1; k >= 0; k--) {
            int attempt = rows - k;

           guess = null;

            boolean isValidGuess = false;
            while (!isValidGuess) {

                System.out.print(p + "\nEnter your guess- e.g- rbpy: " + reset);
                String guessStr = s.nextLine().toUpperCase();
                guessStr=guessStr.replaceAll("\\W","");
                if (isValidColor(guessStr) && guessStr.length()==cols) {
                    guess = guess(guessStr);
                    isValidGuess = true;
                } else {
                    System.out.println(r+ "Invalid input!" + reset);
                }
            }

            for (int i = 0; i < guess.length; i++) {
                
                board[k][i] = guess[i];
            }

            displayGrid();
 
           
            guessedCheck = new String[cols];

            for (int i = 0; i < guess.length; i++) {

                if (guess[i].equals(secret[i])) {
                    exact++;

                } else {
                    for (int j = 0; j < guess.length; j++) {
                        if (guess[i].equals(secret[j])) {
                            sameColor++;

                        }
                    }
                }
            }

            if (exact == cols) {
                System.out.println(g + "Congratulations! You guessed the code! " 
                        + name + " in the " + attempt + "th attempt." + reset);
                writeWinnerToFile(name,attempt);
                break;
            } else {
                System.out.println(r + "Incorrect...Try Again!" + reset);
            }

            for (int i = 0; i < guessedCheck.length; i++) {

                if (exact > 0) {
                    guessedCheck[i] = r + "RED" + reset;
                    exact--;
                } else if (sameColor > 0) {
                    guessedCheck[i] = y + "YELLOW" + reset;
                    sameColor--;
                } else {
                    guessedCheck[i] = "BLACK";
                }

            }

            System.out.println(Arrays.toString(guessedCheck));

        }

        if (exact < cols) {
            System.out.println(r + "You Lose! " + name + reset);
            System.out.println(b + "This is the answer" + Arrays.toString(secret));
        }

    }

    private String[] guess(String guessStr) {
        String[] guess = new String[cols];
        for (int i = 0; i < guess.length; i++) {
            guess[i] = guessStr.substring(i, i + 1);

        }
        return guess;
    }
    //r=83 , b=98, p=80,y=122,g=71,c=99
    private boolean isValidColor(String str) {
        
        
        for (int i = 0; i < str.length(); i++) {
            boolean valid=false;
            for (String s:colors) {
                if (s.equals(str.substring(i, i + 1))) {
                    valid=true;
                }
            }
            
            if(!valid){
             return false;
            }
        }
        return true;
    }
//    private boolean containsDuplicates(String guessStr) {
//        for (int i = 0; i < guessStr.length(); i++) {
//            char c = guessStr.charAt(i);
//            for (int j = i + 1; j < guessStr.length(); j++) {
//                if (c == guessStr.charAt(j)) {
//                    return true; 
//                }
//            }
//        }
//        return false; 
//    }
    private String[] randomCode() {
        Random random = new Random();
        String[] c = new String[cols];
        boolean[] d = new boolean[6];
        for (int i = 0; i < c.length; i++) {
            int digit;
            do {
                digit = random.nextInt(6) + 1;
            } while (d[digit - 1]); // Repeat if the digit is already used
            
            if (digit == 1) {
                c[i] = "R";
            } else if (digit == 2) {
                c[i] = "B";
            } else if (digit == 3) {
                c[i] = "P";
            } else if (digit == 4) {
                c[i] = "Y";
            } else if (digit == 5) {
                c[i] = "G";
            } else if (digit == 6) {
                c[i] = "C";
            }
            d[digit - 1] = true;
        }
        return c;

    }

    private void writeWinnerToFile(String winnerName, int attempts) {
        try {
            FileWriter fileWriter = new FileWriter("winners.txt", true); // Append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Winner: " + winnerName + ", Attempts: " + attempts);
            bufferedWriter.newLine();
            bufferedWriter.close();
     

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        openWinnersFile();
    }

    public void openWinnersFile() {
        try {
            File file = new File("winners.txt");
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("File not found.");
            }
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
}
