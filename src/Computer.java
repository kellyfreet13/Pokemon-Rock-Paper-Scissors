import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kelly on 10/1/2016.
 * Computer method that generates guesses, and adds
 * guesses to a hash map of type <Pattern, Integer>
 *
 *     Serializable so its state can be saved upon user request
 */
public class Computer implements Serializable{
    /**
     * Hash map that will store the pattern of guesses
     */
    private HashMap<Pattern, Integer> hm;

    /**
     * Pattern to be saved as the last pattern when the user saves the
     * state of the computer
     */
    private Pattern lastPattern;

    /**
     * Constructor of computer object,
     * initializes the hash map  a new hash map and
     * the last pattern to null
     */
    public Computer(){
        hm =  new HashMap<Pattern, Integer>();
        lastPattern = null;
    }

    /**
     * Stores the pattern into the computer's hash map,
     * checks if that key already exists, if so, it increments
     * the value 1 to signify that an addition has been made to
     * the amount of times that guess has been seen
     *
     * else, it adds the pattern with an occurrence of one
     * (first occurrence)
     * @param p string to be stored into the pattern
     *          and then into the computer's hash map
     */
    public void storePattern(String p){
        Pattern pattern = new Pattern(p);

        //value is the amount of times the user has made the last choice
        //or in other words, how many times the user has chosen that pattern
        boolean mapKey = hm.containsKey(pattern);
        int value;
        if(mapKey != false){
            value = hm.get(pattern);
            value++;
            hm.put(pattern, value);

        }  else {
            hm.put(pattern, 1);
        }
    }


    /**
     * returns the value of the key specified,
     * in this case the string passed in
     * @param p string pattern to be found in hash map
     * @return the value stored in that key's location
     */
    public int getValue(String p){
        Pattern pattern = new Pattern(p);
        String key = pattern.getPattern();
        Integer mapKey = hm.get(key);

        if(mapKey != null){
            return hm.get(key);
        }
        else {
            return 0;
        }
    }

    /**
     * Makes a prediction of the user's next guess based
     * upon the highest value of the possible next keys
     * @param p user's last guess pattern
     * @return int representing either
     * fire, water or grass
     */
    public int makePrediction(String p){

        String added1 = addChar(p, "F");
        String added2 = addChar(p, "W");
        String added3 = addChar(p, "G");

        int prob1 = getValue(added1);
        int prob2 = getValue(added2);
        int prob3 = getValue(added3);

        int[] probArr = new int[3];

        //checks to see if it has even occurred
        boolean isInHashMap1 = hm.containsKey(new Pattern(added1));
        boolean isInHashMap2 = hm.containsKey(new Pattern(added2));
        boolean isInHashMap3 = hm.containsKey(new Pattern(added3));

        int numFire = 0;
        int numGrass = 0;
        int numWater = 0;
        if(isInHashMap1){
            numFire = hm.get(new Pattern(added1));
        } else if (isInHashMap2) {
            numWater = hm.get(new Pattern(added2));
        } else if (isInHashMap3){
            numGrass = hm.get(new Pattern(added3));
        }

        probArr[0] = numFire;
        probArr[1] = numWater;
        probArr[2] = numGrass;

        if ( (isInHashMap1 == false) && (isInHashMap2 == false) && (isInHashMap3 == false)){
            return makeRandomPrediction();
        }


        int max = probArr[0];
        for (int i = 0; i < probArr.length; i++){
            if (probArr[i] > max){
                max = probArr[i];
            }
        }


        int occ = 0;
        if(max == prob1){
            occ = battleTable(1); //FIRE
        } else if (max == prob2){
            occ =  battleTable(2); //WATER
        } else if (max == prob3){
            occ = battleTable(3); //GRASS
        }

        return occ;
    }

    /**
     * Makes a random prediction while the first
     * guess string is being created
     * @return int representing either
     * fire, water or grass
     */
    public int makeRandomPrediction(){
        return (int)(Math.random()*3)+1;
    }

    /**
     * Gets rid of first character in string, and appends
     * the specified character to the end of the string,
     * creating a 'new' pattern
     * @param old old pattern
     * @param newString String to be appended to old
     * @return the resulting string from the previous description
     */
    public String addChar(String old, String newString){
        return old.substring(1,old.length()) + newString;
    }

    /**
     * Calculates what the computer should guess
     * based on the 'battle table' of what beats what
     * (for example fire beats grass) <- represented as ints
     * @param a integer representing a value of fire, grass, or water
     * @return integer representing a value of fire, grass, or water
     *  that the computer should guess
     */
    private int battleTable(int a){
        int winningBet = 0;

        if ( a == 1 ){
            winningBet = 2;
        } else if ( a == 2){
            winningBet = 3;
        } else if ( a == 3){
            winningBet = 1;
        }

        return winningBet;
    }

    /**
     * Saves the last pattern the user guessed when the computer
     * object is saved
     * @param p
     */
    public void saveLastPattern(String p){
        lastPattern = new Pattern(p);
    }
}
