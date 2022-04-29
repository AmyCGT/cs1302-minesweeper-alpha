package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The Driver class for MinesweeperGame. This class
 * contains the one and only main method in the package.
 */
public class MinesweeperDriver {

     /**
     * Main method. This method contains the one and only standard
     * input @code Scanner object. It also handles the singular
     * command line argument and calls the @code play() method from
     * @code MinesweeperGame.java.
     *
     * @param args the shell argument provided for the program
     */
    public static void main(String[] args) {

        Scanner stdIn = new Scanner(System.in); //the command for each round
        MinesweeperGame game = null;

        String seedPath = args[0];
        File file = new File(seedPath);
        if (args.length != 1) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } else {
            game = new MinesweeperGame(stdIn, seedPath);
            game.readSeed(file);
        }
        game.play();

    } // main
} // MinesweeperDriver
