package networkAPI;
/**
 * This class represents the delimiters which is a pair of characters used to
 * separate value pairs and their elements in a string.
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */
public class Delimiters {
	
	// Character used to separate pairs
    private char pairSeparator;
    
    // Character used to separate keys from values inside of a pair
    private char keyValueSeparator;

    /**
     * Creates a Delimiter object with the separators defined above
     * 
     * @param pairSeparator
     * @param keyValueSeparator
     */
    public Delimiters(char pairSeparator, char keyValueSeparator) {
        this.pairSeparator = pairSeparator;
        this.keyValueSeparator = keyValueSeparator;
    }

    /**
     * Creates a Delimiter object with default separators (comma, colon)
     * 
     * @return a Delimiter using default characters
     */
    public static Delimiters createDefault() { 
        return new Delimiters(',', ':'); 
    }

    /**
     * Returns the character used to separate pairs
     * 
     * @return the pair separator character
     */
    public char getPairSeparator() {
        return pairSeparator;
    }

    /**
     * Returns the character used to separate keys from values
     * 
     * @return the key value separator character
     */
    public char getKeyValueSeparator() {
        return keyValueSeparator;
    }
}