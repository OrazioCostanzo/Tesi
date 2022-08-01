import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class constructs and return  a suffix array.
 * @version 1.0 (8/1/2022).
 * @author Orazio Costanzo.
 */
public class SuffixArrayBuilder{
    private static String FILTER = "\\s+|\\W";
    private static String REPLACE = "";
    /**
     * Call Suffix.buildSuffixArray(text) to build a suffix array.
     * Suffix.buildSuffixArray(text) ==> build(initialize(text).
     * This method creates an integer array that contains the indexes of the sorted suffixes.
     * Sorting algorithm from Arrays class.
     * @param text The text on which to create the suffix array.
     * @return return a SuffixArray object.
     * @see java.util.Arrays
     */
    public static SuffixArray buildSuffixArray(String text){
        text = SuffixArrayBuilder.textCleaner(text);
        return Suffix.buildSuffixArray(text);
    }
    /**
     * Change the filter to be applied to the text before it is processed.
     * Default filter is: \\s+|\\W.
     * If there is an PatternSyntaxException , catch it and not change the filter.
     * @param regex Regex that sets the new filter.ù
     */
    public static void setFilter(String regex){
        try{
            FILTER = Pattern.compile(regex)
                    .toString();
        }catch(PatternSyntaxException ex){
            System.out.println("Error Pattern Syntax, filter is: " + FILTER);
        }
    }
    /**
     * Clean the text with specific filter, before processing it.
     * @param text Raw text (without being processed)
     * @return Returns the filtered text
     */
    public static String textCleaner(String text){
        return text.trim()
                   .replaceAll(FILTER,REPLACE);
    }
    /**
     * This method replaces the filtered characters (via the regex) with a string.
     * The default replacement string is "",(empty string),otherwise enter the new string to replace.
     * @param new_replace New string to replace.
     */
    public static void setReplace(String new_replace){
        REPLACE = new_replace;
    }
}
