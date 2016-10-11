import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Created by Kelly on 10/1/2016.
 * Where the gameplay takes place
 */
public class Driver {

    /**
     * Loops four times while the computer guesses are random, then makes
     * 'smart' guesses based upon the player's past guesses
     * @param args standard input to main
     */
    public static void main(String[] args){

        //some variables to start us off right
        Computer comp = new Computer();
        Scanner scan = new Scanner(System.in);
        String guesses = "";
        int userWinCount = 0;
        int cpuWinCount = 0;
        int userWin;
        int chooseType;
        int compGuess;
        int userPlay;

        //File management
        //-------------------------------------------------------
        /////////////////////////////////////////////////////////
        File f = new File("computer.dat");
        boolean reload = false;

        if(f.exists()){
            System.out.println("It seems you have saved data\nWould you like to continue or start a new game?");
            System.out.println("1. Continue (Harder computer)\n2. New Game (Easier computer)");

            int response = checkInput(1, 2, scan.nextInt());
            if(response == 1){
                reload = true;
            } else { reload = false; }
        }
        if (f.exists() && reload){
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                comp = (Computer)in.readObject();
                in.close();
            } catch (IOException e){
                System.out.println("Error processing file");
            } catch (ClassNotFoundException e){
                System.out.println("Could not find class");
            }
        }
        /////////////////////////////////////////////////////////
        //-------------------------------------------------------

        //guesses will be random
        for (int i = 0; i < 4; i++){
            System.out.println("Would you like to play?");
            System.out.println("1. Yes\n2. No");
            userPlay = checkInput(1, 2, scan.nextInt());
            if(userPlay == 1) {
                //do nothing
            } else if (userPlay == 2){
                break;
            }
            System.out.println("Choose a pokemon");
            System.out.println("1. Fire\n2. Water\n3. Grass");
            chooseType = checkInput(1, 3, scan.nextInt());
            compGuess = comp.makeRandomPrediction();

            userWin = winner(chooseType, compGuess);

            //gives credit to winner
            if(userWin == 1){
                userWinCount++;
            } else if (userWin == 0) {
                //do nothing
            }else {
                cpuWinCount++;
            }
            displayGuesses(chooseType, compGuess);
            displayWinner(userWinCount, cpuWinCount);
            guesses += guessIntString(chooseType);
        }
        //stores the first round of guesses
        comp.storePattern(guesses);

        boolean userContinue = true;
        //------------------------------------------------------------------------------------
        //guesses will not be random
        while(userContinue) {
            System.out.println("Would you like to play?");
            System.out.println("1. Yes\n2. No");
            userPlay = checkInput(1, 2, scan.nextInt());
            if(userPlay == 1) {
                //do nothing
            } else if (userPlay == 2){
                break;
            }
            System.out.println("Choose a pokemon");
            System.out.println("1. Fire\n2. Water\n3. Grass");
            chooseType = checkInput(1, 3, scan.nextInt());

            //even though this is after the guess, the comp shouldn't know.
            //it is still 'guessing'
            compGuess = comp.makePrediction(guesses);

            userWin = winner(chooseType, compGuess);
            if(userWin == 1){
                userWinCount++;
            } else if (userWin == 0) {
                //do nothing
            }else {
                cpuWinCount++;
            }
            displayGuesses(chooseType, compGuess);
            displayWinner(userWinCount, cpuWinCount);

            //updates the guesses
            //takes old guess and adds and replaces with up to date info
            guesses = addChar(guesses, guessIntString(chooseType));

            //stores new pattern into hash map
            comp.storePattern(guesses);
        }

        System.out.println("Would you like to save the computer to a file?");
        System.out.println("1. Yes\n2. No");
        int saveFile = checkInput(1, 2, scan.nextInt());
        if (saveFile == 1){
            comp.saveLastPattern(guesses);

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
                out.writeObject(comp);
                out.close();
            } catch (IOException e) {
                System.out.println("Error processing file");
            }

            System.out.println("Have a nice day! -file saved");
        } else if (saveFile == 2 ){
            System.out.println("Have a nice day! -file not saved");
        }

        //------------------------------------------------------------------------------------
    }

    /**
     * Displays the user's and computer's guesses
     * @param a user guess
     * @param b computer guess
     */
    public static void displayGuesses(int a, int b){
        String uGuess = guessIntString(a);
        String cGuess = guessIntString(b);
        System.out.println("\nYou guessed: " + uGuess + "\nThe computer guessed: " + cGuess + "\n");
    }

    /**
     * Gets rid of first character in string, and appends
     * the specified character to the end of the string,
     * creating a 'new' pattern
     * @param old old pattern
     * @param newString String to be appended to old
     * @return the resulting string from the previous description
     */
    public static String addChar(String old, String newString){
        return old.substring(1,old.length()) + newString;
    }

    /**
     * Displays the winner based upon the 'battle table', ie what beats what
     * @param a user guess
     * @param b computer guess
     * @return 1 if user wins, 0 if tie, -1 if computer wins
     */
    public static int winner(int a, int b){
        //returns true if user wins, false if comp wins
        // F1 -> G3
        // G3 -> W2
        // W2 -> F1

        int winner;
        // F = 1, W = 2, G = 3
        if( (a == 1 && b == 3) || (a == 3 && b == 2) || (a == 2 && b == 1)){
            winner = 1;
            System.out.println("You have won the round!\n");
        } else if( a == b){
            winner = 0;
            System.out.println("You have tied!\n");
        }  else {
            winner = -1;
            System.out.println("The computer has won the round!\n");
        }

        if ( (a == 1 && b == 3) || (a == 3 && b == 1)){
            System.out.println("Fire beats Grass");
        } else if ( (a == 3 && b == 2) || (a == 2 && b == 3)){
            System.out.println("Grass beats Water");
        } else if ( (a == 2 && b == 1) || ( a == 1 && b == 2)){
            System.out.println("Water beats Fire");
        }
        return winner;
    }

    /**
     * Displays the win percentage of the computer and also the current
     * amount of wins on each side
     * @param userWins amount of times user has won
     * @param cpuWins amount of times computer has won
     */
    public static void displayWinner( int userWins, int cpuWins){
        System.out.println("\nUser score: " + userWins + "\nComputer score: " + cpuWins + "\n");
        float percentage = 0;
        try {
            percentage = ((float)cpuWins/((float)(userWins + cpuWins))) * 100;
        } catch (ArithmeticException ex){
            System.out.println("Division by zero, folks. Nothing to see here");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Computer has won " + df.format(percentage) + "% of the games" );
    }

    /**
     * Converts the integer associated with each type into a string
     * that can be stored into a patter
     * @param choice int representation of either fire, water, or grass
     * @return String of F for fire, W for water, and G for grass
     */
    public static String guessIntString(int choice){
        String guess = "";
        switch (choice){
            case 1:
                guess = "F";
                break;
            case 2:
                guess = "W";
                break;
            case 3:
                guess = "G";
                break;
            default:
                System.out.println("Gottamn");
        }
        return guess;
    }

    /**
     * Checks user input for validity
     * @param low lower bound
     * @param high upper bound
     * @param check int to be checked between bounds
     * @return the correctly bound integer (if the user ever does enter one)
     */
    public static int checkInput(int low, int high, int check){
        int getItRight = check;

        Scanner scan = new Scanner(System.in);
        while(true){
            if(getItRight < low || getItRight > high){
                System.out.println("Please enter a valid integer "+low+"-"+high);
                getItRight = scan.nextInt();
            } else if (getItRight >= low && getItRight <= high){
                return getItRight;
            }
        }
    }
}
