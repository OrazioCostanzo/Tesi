import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * This class manages an array of integers (suffix array) and the text on which the suffix array has been built.
 * @version 1.0 (8/1/2022)
 * @author Orazio Costanzo
 */
public class  SuffixArray {
    //Fields
    //------------------------------------------------------------------------------------------------------------------
    private Integer[] sa_index; // suffix array index
    private Integer[] ta_index; //text array index
    private Integer[] lcp; //lcp array
    private Character[] left_char_array;
    private String text; //text
    //------------------------------------------------------------------------------------------------------------------


    //Constructors
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Initialize sa_index, ta_index, lcp array
     * @param sa_index Suffix array after creation
     * @param text Text after filtering
     */
    public SuffixArray(Integer[] sa_index, String text) {
        int sa_length = sa_index.length;

        this.sa_index = sa_index;
        this.text = text;
        this.ta_index = new Integer[sa_length];
        this.left_char_array = new Character[sa_length];
        this.lcp = new Integer[sa_length] ;

        for(int i=0 ; i < sa_length; i++)
            this.ta_index[sa_index[i]] = i;
    }
    //------------------------------------------------------------------------------------------------------------------


    //Method
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Print all suffix into the suffix array.
     */
    public void printSA(){
        Arrays.stream(sa_index)
                .map(text::substring)
                .forEach(System.out::println);
    }

    /**
     * Prints the first n suffixes.
     * @param n number of elements you want to print.
     */
    public void printSA(final long n){
        final long sa_size = sa_index.length;

        if(n > sa_size || n < 0) {
            System.out.println("The size of the suffix array is: " + sa_size);
            return;
        }
        Arrays.stream(sa_index)
                .limit(n)
                .map(text::substring)
                .forEach(System.out::println);
    }

    /**
     * Skip the first m suffixes and print the next n suffixes.
     * @param m number of suffixes you want to skip.
     * @param n number of elements you want to print.
     */
    public void printSA(final long m, final long n){
        final long sa_size = sa_index.length;

        if((m < 0 || m > sa_size) || (n < 0)) {
            System.out.println("The size of the suffix array is: " + sa_size);
            return;
        }
        Arrays.stream(sa_index)
                .skip(m)
                .limit(n)
                .map(text::substring)
                .forEach(System.out::println);
    }

    /**
     * Call Suffix.buildSuffixArray(text) to build a suffix array.
     * Suffix.buildSuffixArray(text) ==> build(initialize(text).
     * This method creates an integer array that contains the indexes of the sorted suffixes.
     * Sorting algorithm from Arrays class.
     * @param text The text on which to create the suffix array.
     * @return return a SuffixArray object.
     * @see java.util.Arrays
     */
    public static SuffixArray buildSuffixArray(String... text){
        String txt = Arrays.stream(text)
                .reduce("",(left_txt, right_txt) -> left_txt + right_txt);
        return SuffixArrayBuilder.buildSuffixArray(txt);
    }

    /**
     * Clean the text with specific filter, before processing it.
     * @param text Raw text (without being processed)
     * @return Returns the filtered text
     */
    public static String textCleaner(String text){
        return  SuffixArrayBuilder.textCleaner(text);
    }

    /**
     * Returns a String object representing this Suffix Array.
     * @return Returns the suffix array in the format: index:[i-th] Suffix:substring(index)
     */
    @Override
    public String toString(){
        return Arrays.stream(sa_index)
                .map(index -> "Index:[" + index + "] " + "Suffix:\"" + text.substring(index) + "\"\n")
                .reduce("", String::concat);
    }
    //------------------------------------------------------------------------------------------------------------------


    //Getter
    //------------------------------------------------------------------------------------------------------------------
    /**
     * sa_index getter
     * @return return sa_index
     */
    public Integer[] getSa() {
        return sa_index;
    }
    public int getSaElement(int index){
        return sa_index[index];
    }

    /**
     * ta_index getter
     * @return return ta_index
     */
    public Integer[] getTa() {
        return ta_index;
    }
    public  int getTaElement(int index){
        return ta_index[index];
    }

    /**
     * lcp getter
     * @return return lcp array
     */
    public Integer[] getLcp() {
        return lcp;
    }
    public int getLcpElement(int index){
        return this.lcp[index];
    }
    public Character[] getLCA(){
        return left_char_array;
    }
    public Character getLCAElement(int index) {
        return left_char_array[index];
    }


    /**
     * Get the longest repeated substring.
     * @return List with LRS.
     */
//longest repeating subsequence
    public Map<Integer,List<Integer>> getLRS() {
        AtomicInteger index = new AtomicInteger(-1);

        Optional<Integer> max = Arrays.stream(lcp)
                .max(Comparator.naturalOrder());

        return Arrays.stream(sa_index)
                     .filter(element -> {
                         index.getAndIncrement();
                         return getLcpElement(index.get()) == max.get();
                     })
                     .collect(Collectors.groupingBy(element -> getLcpElement(getTaElement(element)))); // get map with K = number of LRS  and V = List of index in the text
    }


    public Map<Integer,String> getLRSMap(Map<Integer,List<Integer>> map ){
          return   map.values()
                       .stream()
                       .toList()
                       .get(0)
                       .stream()
                       .collect(Collectors.toMap(this::getTaElement,
                                                 value -> getText()
                                                         .substring(value,
                                                                    value + getLcpElement(getTaElement(value)))));
    }

    private long getTaCurrentIndex(int index){
        return this.ta_index[index];
    }

    /**
     * Return the original text (after textCleaner)
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Return  sa_index size (number of suffixes)
     * @return long
     */
    public long getLength(){
        return this.sa_index.length;
    }

    /**
     * Get VIRTUAL_CHAR (char that does not belong to the text's alphabet).
     * @return Return virtual char
     */
    public static String getVirtualChar() {
        return SuffixArrayBuilder.getVirtualChar();
    }
    //------------------------------------------------------------------------------------------------------------------


    //Setter
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Inserts in the appropriate index the number of equal occurrences
     * between "text.charAt(i + len)"  and  "text.charAt(prev + len)"
     * where "prev = sa_index[(int) text_position - 1];".
     * @param text_position It is used to mark the suffixes of the text following the order in which they appear in the text,
     * "text_position = sa_array.getTaCurrentIndex(i);".
     * @param len Number of equal occurrences.
     */
    private void setLcp(long text_position, int len) {
        this.lcp[(int) text_position] = len;
    }

    /**
     * Set the new VIRTUAL_CHAR
     * @param new_virtual_char VIRTUAL_CHAR = new_virtual_char.
     */
    public static void setVirtualChar(Character new_virtual_char){
        SuffixArrayBuilder.setVirtualChar(new_virtual_char);
    }

    /**
     * Change the filter to be applied to the text before it is processed.
     * Default filter is: \\s+|\\W.
     * If there is an PatternSyntaxException , catch it and not change the filter.
     * @param regex Regex that sets the new filter.ù
     */
    public static void setFilter(String regex){
        SuffixArrayBuilder.setFilter(regex);
    }

    /**
     * This method replaces the filtered characters (via the regex) with a string.
     * The default replacement string is "",(empty string),otherwise enter the new string to replace.
     * @param new_replace New string to replace.
     */
    public static void setReplace(String new_replace){
        SuffixArrayBuilder.setReplace(new_replace);
    }


    //------------------------------------------------------------------------------------------------------------------


    //Inner class
    //------------------------------------------------------------------------------------------------------------------
    /**
     * This class constructs and return  a suffix array.
     * @version 1.0 (8/1/2022).
     * @author Orazio Costanzo.
     */
    public static class SuffixArrayBuilder{
        //Fields
        //------------------------------------------------------------------------------------------------------------------
        private static String FILTER = "\\s+|\\W";
        private static String VIRTUAL_CHAR = "$"; //virtual character that does not belong to the alphabet of the text
        private static String REPLACE = "";
        //------------------------------------------------------------------------------------------------------------------


        //Method
        //------------------------------------------------------------------------------------------------------------------
        private static String textCleaner(String text){
            return text.trim()
                    .replaceAll(FILTER,REPLACE);
        }

        private static SuffixArray buildSuffixArray(String text){
            text = SuffixArrayBuilder.textCleaner(text) + VIRTUAL_CHAR;
            return  buildLCA( buildLcpArray( Suffix.buildSuffixArray(text)));
        }
        private static SuffixArray buildLCA(SuffixArray sa_array) {
          sa_array.left_char_array = Arrays.stream(sa_array.getSa())
                    .map(index -> index != 0 ? sa_array.getText().charAt(index - 1) : null)
                    .toArray(Character[]::new);
          return sa_array;
        }

        private static SuffixArray buildLcpArray(SuffixArray sa_array) {
            int len = 0;  // how many characters remain
            long text_position;  // the position of the suffix in the text as if sa_index had not been sorted
            long sa_array_len = sa_array.getLength();
            long prev; //index of previous suffix
            Integer[] sa_index = sa_array.getSa();
            String text = sa_array.getText();

            for(int i = 0; i < sa_array_len; i++){
                text_position = sa_array.getTaCurrentIndex(i);
                if(text_position > 0) {                            // text_position == 0 when i = sa_array.length - 1 (end sa_array)
                    prev = sa_index[(int) text_position - 1];  // Take a previous index
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
        //------------------------------------------------------------------------------------------------------------------


        //Getter
        //------------------------------------------------------------------------------------------------------------------
        private static String getVirtualChar() {
            return VIRTUAL_CHAR;
        }
        //------------------------------------------------------------------------------------------------------------------


        //Setter
        //------------------------------------------------------------------------------------------------------------------
        private static void setVirtualChar(Character new_virtual_char){
            VIRTUAL_CHAR =  String.valueOf(new_virtual_char);
        }

        private static void setFilter(String regex){
            try{
                FILTER = Pattern.compile(regex)
                        .toString();
            }catch(PatternSyntaxException ex){
                System.out.println("Error Pattern Syntax, filter is: " + FILTER);
            }
        }

        private static void setReplace(String new_replace){
            REPLACE = new_replace;
        }
        //------------------------------------------------------------------------------------------------------------------
    }
    //------------------------------------------------------------------------------------------------------------------
}
