import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class constructs and return  a suffix array.
 * @version 1.0 (8/1/2022).
 * @author Orazio Costanzo.
 */
public class SuffixArrayBuilder{
    private static String FILTER = "\\s+|\\W";
    private static String VIRTUAL_CHAR = "$"; //virtual character that does not belong to the alphabet of the text
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
        text = text + VIRTUAL_CHAR;
        SuffixArray temp =  Suffix.buildSuffixArray(text);
        temp =  buildLcpArray(temp);
        return temp;
    }

    private static SuffixArray buildLcpArray(SuffixArray sa_array) {
        int len = 0;  // how many characters remain
        long text_position;  // the position of the suffix in the text as if sa_index had not been sorted
        long sa_array_len = sa_array.getLength();
        long prev = 0;
        Integer[] sa_index = sa_array.getSaIndex();
        String text = sa_array.getText();

        for(int i = 0; i < sa_array_len; i++){
            text_position = sa_array.getTaCurrentIndex(i);
            if(text_position > 0) {                            // text_position == 0 when i = sa_array.length - 1 (end sa_array)
                prev = sa_index[(int) text_position - 1]; // Take a previous index
                while(text.charAt(i + len) == text.charAt((int) (prev + len))){
                    len = len + 1;
                }     //while same char
                sa_array.setLcp(text_position,len);
                len = Math.max((len - 1), 0);  //decrease number of equal characters
            }else{
                sa_array.setLcp(text_position, -1);
            }
        }
        return sa_array;
    }


    /**
     * Return Virtual Char
     * @return String
     */
    public static String getVirtualChar() {
        return VIRTUAL_CHAR;
    }
    public static void setVirtualChar(Character new_virtual_char){
        VIRTUAL_CHAR =  String.valueOf(new_virtual_char);
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
