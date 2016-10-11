import java.io.Serializable;

/**
 * Created by Kelly on 10/1/2016.
 * Pattern contains a string, as well as its own hashCode and equals
 * method, both of which are overridden
 * Serializable so the last guess can be saved into computer
 */
public class Pattern implements Serializable {
    /**
     * String that stores the pattern of guesses
     */
    private String pattern;

    /**
     * Constructor for pattern
     * @param p string containing guesses
     */
    Pattern(String p){
        pattern = p;
    }

    /**
     * Returns the string pattern of this Pattern object
     * @return this Pattern's string of guesses
     */
    public String getPattern(){
        return this.pattern;
    }

    /**
     * Creates a custom hash code for the Pattern object
     * @return int of custom created hash code
     */
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + pattern.hashCode();
        return result;
    }

    /**
     * Overridden equals method that compares the string of
     * guesses to check equality of patterns
     * @param p true if strings are equals, else false
     * @return
     */
    @Override
    public boolean equals(Object p){
        if(p instanceof  Pattern){
            Pattern patt = (Pattern) p;
            return pattern.equals( patt.getPattern());
        }
        return false;
    }

}
