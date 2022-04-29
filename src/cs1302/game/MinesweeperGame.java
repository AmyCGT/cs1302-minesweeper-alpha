package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class represents the Minesweeper Game.
 *
 */
public class MinesweeperGame {

    // variable declarations
    private final Scanner stdIn;
    private int rows;
    private int cols;
    private int numMines;
    private boolean[][] hasMineArray;
    private String[][] mArray;
    private String[][] noFogArray;
    private int rounds;
    private boolean rmFog = false;
    private int gRow;
    private int gCol;
    private int mRow;
    private int mCol;
    private int rRow;
    private int rCol;

    /**
     * {@code} MinesweeperGame() constructor initializes instance variables
     * and contributes to the set up of the game.
     * This constructor is called from {@code} Driver class where the initial
     * values for {@code} stdIn and {@code} seedPath are.
     * Called by the {@code} Driver class, it creates an object instance.
     *
     * @param stdIn the scanner for user input for each playable round
     * @param seedPath the String that represents the {@code} .txt file for in line constructor
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.stdIn = stdIn;
        rows = 0;
        cols = 0;
        numMines = 0;
        rounds = 0;
    }

    /**
     * {@code} readSeed() method reads the seed file and contains multiple
     * arrays that assign where mines are and the original 2D array for
     * the minefield plot, fog and no fog.
     *
     * @param seed the seed file used to lay out the parameters for the game
     */
    public void readSeed(File seed) {
        Scanner reader = null;
        try {
            reader = new Scanner(seed);
        } catch (FileNotFoundException e) {
            System.err.println();
            System.err.println("Seed File Not Found Error: " + e.getMessage());
            System.exit(2);
        } // try


        if (reader.hasNextInt()) { // scans through seed fine for numbers
            rows = reader.nextInt();
            if (reader.hasNextInt()) {
                cols = reader.nextInt();
                if (reader.hasNextInt()) {
                    this.numMines = reader.nextInt();
                } // if
            }
        } else {
            System.err.println();
            System.err.println("Seed File Malformed Error: there was an unexpected token");
            System.exit(3);
        } // if

        if (rows < 5 || rows > 10 || cols < 5 || cols > 10) { // constraints to board size
            System.out.println();
            System.err.print("Seed File Malformed Error: Cannot create a mine field ");
            System.err.print(" with that many rows and/or columns!");
            System.err.println();
            System.exit(3);
        } // if

        if (numMines < 1 || numMines > ((rows * cols) - 1)) {
            System.err.println();
            System.err.print("Seed File Malformed Error: Cannot create a mine field ");
            System.err.print(" with that many rows and/or columns!");
            System.err.println();
            System.exit(3);
        }

        hasMineArray = new boolean [rows][cols]; // new boolean array for mines
        mArray = new String [rows][cols]; // new String array for minefield
        noFogArray = new String [rows][cols]; // String array for noFog minefield
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                hasMineArray[i][j] = false;
                mArray[i][j] = "   "; // initialize mArray, aka, the mine array
                noFogArray[i][j] = "   ";  // initialize fogArray
            }
        } // for loop; initialize arrays

        int count = 0;
        for (int i = 0; i < this.numMines; i++) { // assigns the mines in mine array
            int x = 0, y = 0;
            if (reader.hasNextInt()) {
                x = reader.nextInt();
                count++;
                if (reader.hasNextInt()) {
                    y = reader.nextInt();
                    count++;
                    if ((x >= 0 && x < rows) && (y >= 0 && y < cols)) {
                        hasMineArray[x][y] = true;
                        count++;
                    } else {
                        System.err.println();
                        System.err.println("Seed File Malformed Error: Not in bound");
                        System.exit(3);
                    } // if else
                } // if
            } // if
        } // if
        if (count != 3 * numMines) {
            System.err.println();
            System.err.println("Seed File Malformed Error: Not enough mine locations.");
            System.exit(3);
        } // if

    } // readSeed

    /**
     * Prints out a welcome message at the beginning of the game.
     */
    public void printWelcome() {
        File welcome = new File("welcome.txt");
        Scanner fileScan = null;
        try {
            fileScan = new Scanner(welcome);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            System.out.println(line);
        } // while
    } // printWelcome

    /**
     * Returns the number of mines adjacent to the specified
     * square in the grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return the number of adjacent mines
     */
    private int getNumAdjMines(int row, int col) {
        int numAdjMines = 0;
        for (int i = row - 1; i <= row + 1; i++) { // looks at blocks adjacent to square
            if (!(i >= 0 && i < mArray.length)) { // if there is a block
                continue;
            }
            for (int j = col - 1; j <= col + 1; j++) { // looks at blocks below
                if ((i == row && j == col) || (!(j >= 0 && j < mArray[0].length))) {
                    continue;
                } else {
                    if (hasMineArray[i][j]) { // if there is a mine
                        numAdjMines++;
                    } // if
                } // if
            } // for



        } // for loop
        return numAdjMines;
    }

    /**
     * Prints the current contents of the mine field to standard
     * output.
     */
    public void printMineField() {
        for (int i = 0; i < mArray.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < mArray[i].length; j++) {
                System.out.print(mArray[i][j]);
                if (j < mArray[i].length - 1) {
                    System.out.print("|");
                } // if
            } // for loop
            System.out.println("|");
        } // for loop
        System.out.print("    ");
        for (int i = 0; i < mArray[0].length; i++) {
            System.out.print(i + "   ");
        } // for loop
        System.out.println();
    } //printMineField()

    /**
     * Prints number of past rounds completed.
     */
    public void printPastRounds() {
        System.out.println("Rounds Completed: " + rounds);
        System.out.println();
    } // printPastRounds()

    /**
     * Prints the minesweeper-alpha: bash into standard output.
     */
    public void printBash() {
        System.out.println();
        System.out.print("minesweeper-alpha: ");
    }

    /**
     * Prints the game prompt to standard output and interprets user
     * input from standard input. It also calls other methods based on
     * commands.
     */
    public void promptUser() {
        printPastRounds();
        if (rmFog) {
            printNoFogField();
            rmFog = false;
        } else {
            printMineField();
        }
        printBash();
        getUserInput();
    }

    /**
     * Determines the next move based on user input.
     */
    public void getUserInput() {
        String uIn = this.stdIn.nextLine().trim();
        Scanner reader = new Scanner(uIn);
        String tweaked = reader.next().trim();

        try {
            if (tweaked.equals("help") || tweaked.equals("h")) {
                help();
            } else if (tweaked.equals("guess") || tweaked.equals("g")) {
                if (!guess(reader)) {
                    printInv();
                } else {
                    guess(reader);
                }
            } else if (tweaked.equals("mark") || tweaked.equals("m")) {
                if (!mark(reader)) {
                    printInv();
                } else {
                    mark(reader);
                }
            } else if (tweaked.equals("reveal") || tweaked.equals("r")) {
                if (!reveal(reader)) {
                    printInv();
                } else {
                    reveal(reader);
                }
            } else if (tweaked.equals("nofog")) {
                noFog();
            } else if (tweaked.equals("quit") || tweaked.equals("q")) {
                quit();
            } else {
                this.rounds++;
                printInv();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println();
            System.err.println("Invalid Command: " + e.getMessage());
        }
    }

    /**
     * Prints invalid command error statement.
     */
    public void printInv() {
        System.err.println();
        System.err.println("Invalid Command: Your command is invalid.");
    }

    /**
     * Shows the help menu.
     */
    public void help() {
        rounds++;
        System.out.println();
        System.out.println("Commands Available... ");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" -   Mark: m/mark   row col");
        System.out.println(" -  Guess: g/guess  row col");
        System.out.println(" -   Help: h/help");
        System.out.println(" -   Quit: q/quit");
        System.out.println();
    }

    /**
     * Quits the game and displays the goodbye message. The prgram exits
     * gracefully.
     */
    public void quit() {
        System.out.println();
        System.out.println("Quitting the game...");
        System.out.println();
        System.out.println("Bye!");
        System.exit(0);
    }

    /**
     * Reveals a square, showing either a mine or a number representing
     * the adjacent mines.
     * @param reader the user's input
     * @return false if coordinates were typed incorrectly
     */
    public boolean reveal(Scanner reader) {
        boolean revealed = false;
        rRow = 0; // revealed row
        rCol = 0; // revealed columm
        try {
            if (reader.hasNextInt()) { // if there is an int
                rRow = reader.nextInt(); // assigns int to rRow
                if (reader.hasNextInt()) { // if there is another int
                    rCol = reader.nextInt(); // assigns int to rCol
                    if (!reader.hasNextInt()) { // no more ints
                        rounds++;
                        revealed = true; // block is revealed
                        if (!hasMineArray[rRow][rCol]) { // if there is not a mine
                            mArray[rRow][rCol] = " " + getNumAdjMines(rRow, rCol) + " ";
                            System.out.println();
                        } else {
                            printLoss();
                        } // if
                    } // if
                } // if
            } // if
        } catch (ArrayIndexOutOfBoundsException e) {
            rounds--;
            System.err.println();
            System.err.println("Invalid Command: " + e.getMessage());
        } // try
        return revealed;
    } // reveal

     /**
     * Marks the square as definitely containing a mine.
     *
     * @param reader the user's input
     * @return false if coordinates were typed incorrectly
     */
    public boolean mark(Scanner reader) {
        boolean marked = false;
        mRow = 0;
        mCol = 0;
        try {
            if (reader.hasNextInt()) {
                mRow = reader.nextInt();
                if (reader.hasNextInt()) {
                    mCol = reader.nextInt();
                    if (!reader.hasNextInt()) {
                        marked = true;
                        rounds++;
                        mArray[mRow][mCol] = " F ";
                        System.out.println();
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            rounds--;
            System.err.println();
            System.err.println("Invalid Command: " + e.getMessage());
        } // try
        return marked;

    } // marked

     /**
     * Marks the area with a {@code} ? as a guess to where
     * the mine may be.
     *
     * @param reader the user's input
     * @return false if coordinates were typed incorrectly
     */
    public boolean guess(Scanner reader) {
        boolean guessed = false;
        gRow = 0;
        gCol = 0;

        try {
            if (reader.hasNextInt()) {
                gRow = reader.nextInt();
                if (reader.hasNextInt()) {
                    gCol = reader.nextInt();
                    if (!reader.hasNextInt()) {
                        guessed = true;
                        rounds++;
                        mArray[gRow][gCol] = " ? ";
                        System.out.println();
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            rounds--;
            System.err.println();
            System.err.println("Invalid Command: " + e.getMessage());
        } // try
        return guessed;

    } // guess

    /**
     * Removes the fog of war for the next round only. All squares containing
     * mines are displayed to user with less than and greater than symbols.
     */
    private void noFog() {
        rounds++;
        rmFog = true;
        System.out.println();
    }

    /**
     * Prints noFogArray instead of mArray.
     */
    public void printNoFogField() {
        for (int i = 0; i < noFogArray.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < noFogArray[i].length; j++) {
                if (hasMineArray[i][j]) {
                    System.out.print("<" + mArray[i][j].substring(1, 2) + ">");
                } else {
                    System.out.print(mArray[i][j]);
                }
                if (j < noFogArray[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.println("   ");
        for (int i = 0; i < mArray[0].length; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();

    } //printNoFogField

    /**
     * Prints true if all conditions for winning have been met.
     *
     * @return false if conditions for winning are not met.
     */
    public boolean isWon() {
        boolean allMinesMarked = true;
        boolean allSquaresRevealed = true;
        for (int i = 0; i < mArray.length; i++) { // looks through array row
            for (int j = 0; j < mArray[0].length; j++) { // looks through array col
                if (hasMineArray[i][j]) { // if there is mine
                    if (mArray[i][j] != " F ") { // if mine is not marked definitely
                        allMinesMarked = false;
                    }
                } else { // if there is no mine
                    if (mArray[i][j] == " F " || mArray[i][j] == " ? " || mArray[i][j] == "   ") {
                        allSquaresRevealed = false;
                    } // if square is marked
                }
            }
        }
        if (allMinesMarked == true && allSquaresRevealed == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates and returns the final score.
     *
     * @return the calculated final score as a double value
     */
    private double score() {
        double score = Math.round(100.0 * rows * cols / rounds);
        return score;
    } // score

    /**
     * Prints the contents of the {@code} gamewon.txt file
     * and the final {@code} score().
     */
    public void printWin() {
        File win = new File("gamewon.txt");
        Scanner fileScan = null;
        try {
            fileScan = new Scanner(win);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } // try
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            System.out.print(line);
            if (fileScan.hasNextLine()) { // if there is another line after the previous
                System.out.println(); // go to the next line
            } // if
        } // while
        System.out.print(" " + score());
        System.out.println();
        System.exit(0);
    } // printWin

    /**
     * Prints the contents of the {@code} gameover.txt file
     * and the final {@code} score(). Exits the game.
     */
    public void printLoss() {
        File loss = new File("gameover.txt");
        Scanner fileScan = null;
        try {
            fileScan = new Scanner(loss);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } // try
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            System.out.println(line);
        }
        System.out.println();
        System.exit(0);
    } // printLoss

    /**
     * Starts the game and plays through it until exit status is reached.
     */
    public void play() {
        printWelcome();
        while (true) {
            promptUser();
            if (isWon()) {
                break;
            }
        }
        printWin();
    } // play

}
