import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class manages an array of integers (suffix array) and the text on which the suffix array has been built.
 * @version 1.0 (8/1/2022)
 * @author Orazio Costanzo
 */
public class  SuffixArray {
    //Fields
    //------------------------------------------------------------------------------------------------------------------
    private Integer[] sa_index; // suffix array index
    private Integer[] isa_index; //text array index
    private Integer[] lcp; //lcp array
    private int max_lcp;
    private String text; //text
    private List<String> strings;
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
        this.isa_index = new Integer[sa_length];
        this.lcp = new Integer[sa_length] ;

        for(int i=0 ; i < sa_length; i++)
            this.isa_index[sa_index[i]] = i;
    }

    public SuffixArray() {
         this(new Integer[]{0},"");
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
    public static SuffixArray buildSuffixArray(String text){
        return SuffixArrayBuilder.buildSuffixArray(text);
    }

    public static SuffixArray buildSuffixArray(String... strings){
        return SuffixArrayBuilder.buildSuffixArray(strings);
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
//if existed, return position index
    public long isSubstring(String string){
        return binarySearch(this.sa_index, string);
    }
//binarysearch?
    private long binarySearch(Integer[] sa_array, String string){
        long left = 0;
        int str_len = string.length();
        long right = sa_array.length - 1;
        int result = 0;
        String text = this.getText();
        long text_len  = this.getText().length();
        long middle = 0;

        while(left <= right){
            middle =  left + (right - left) / 2;
            long end = str_len + sa_array[(int) middle];

            if(end < text_len)
                result = string.compareTo(text.substring(sa_array[(int) middle], (int) end));
            else
                result = string.compareTo(text.substring(sa_array[(int) middle]));

            if(result == 0)
                return middle;
            if(result < 0 )
                right = middle - 1;
            if(result > 0)
                left = middle + 1;
        }
        return -1;
    }


    //------------------------------------------------------------------------------------------------------------------


    //Getter
    //------------------------------------------------------------------------------------------------------------------

    public List<String> getStrings(){
        return this.strings;
    }
    /**
     * Builds an Character[] by taking the (distinct) characters in the text.
     * @param sa Suffix Array.
     * @return Character[] array with al distinct characters in the text.
     */
    public static Character[] getAlphabet(SuffixArray sa){
        String text = sa.getText().substring(0, sa.getText().length() - 1);// text without VIRTUAL_CHAR

        return text.chars()
                   .distinct()
                   .mapToObj(element -> (char) element)
                   .toArray(Character[]::new);
    }


    //get string frequency
    //log2 (n) binary search + k with k = number of times the substring repeats, O(max(log2 (n), k))?

    /**
     * Search a str in the suffix array, if exists return number of repetitions else return new int[]{0,0,0}
     * @param str Substring of which we want to count the repetitions.
     * @return int[]{n,start,end} : n = number of repetitions, range(start= first_substring_index, end= last_substring_index).
     */
    public int[] getStrFre(String str){
        int first_index = (int) isSubstring(str);
        int index = first_index + 1;
        int str_len = str.length();
        String text = getText();
        int text_len = text.length();
        int[] result = new int[]{0,0,0}; // repetitions number, first index, last index


        if(first_index >= 0 && first_index < sa_index.length){
            // ricerca altre stringhe uguali negli indici sotto
            while(index < sa_index.length && getLcpElement(index) >= str_len ){
                //if((getSaElement(index) + str.length() < text_len) && text.substring(getSaElement(index), getSaElement(index) + str.length()).equals(str))
                result[0]++;
                index++;
            }
            result[2] = index - 1;
            //altrimenti la prima stringa nel primo indice trovato con il binarysearch lo conta due volte
            index = first_index;
            //up
            while(index > 0 && getLcpElement(index) >= str_len ){
                //if((getSaElement(index) + str.length() < text_len) && text.substring(getSaElement(index), getSaElement(index) + str.length()).equals(str))
                result[0]++;
                index--;
            }
            result[1] = index;
        // controllo l'ultimo indice sopra
            if((getSaElement(index) + str.length() < text_len) && (text.substring(getSaElement(index), getSaElement(index) + str.length()).equals(str)) )
                result[0]++;

            return result;
        }
        return result;
    }

    public int getMax_lcp(){
        return this.max_lcp;
    }
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
    public Integer[] getIsa() {
        return isa_index;
    }
    public  int getIsaElement(int index){
        return isa_index[index];
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

    //long common bubstring
    public List<String> getLCSList(int k){
        List<String> list_lcp = new ArrayList();
        if(this.strings == null){
            return  new ArrayList<>();
        }
        Integer[][] bound = new Integer[this.strings.size()][2];

        if(k > 0 && k <= strings.size() && k < Sentinel.SIZE_SENTINEL) {

            int lower_bound = 0;
            int upper_bound;
            int index = 0;

            int[] counter = new int[this.strings.size()];
            Arrays.fill(counter, 0);
            int first = (int) Arrays.stream(this.lcp).filter(element -> element == -1).count();
            int max_lcp = 0;
            Integer[] window = new Integer[]{first,first};


            for (String str : this.strings) {
                upper_bound = (str.length() - 1) + (lower_bound);
                bound[index][0] = lower_bound;
                bound[index++][1] = upper_bound;
                lower_bound = upper_bound + 2;
            }




            for (int i = first; i < this.lcp.length  ; i++) {
                for(int el = 0; el < bound.length; el++){
                    if(this.getSaElement(window[1]) >= bound[el][0] && this.getSaElement(window[1]) <= bound[el][1]){
                        counter[el]++;
                        break;
                    }
                }

                long num_el = Arrays.stream(counter).filter(el -> el > 0).count();
                //controllo se la finistra ha k suffissi consecutivi di k stringhe
                while(num_el == k){
                    //prendo il massimo delle ripetizioni consecutive di suffissi appartenenti alla stessa stringa
                    int n_element = Arrays.stream(counter).max().getAsInt();
                    // se ci sono più suffissi della stessa stringa consecutivi diniminuisco di uno la finestra dal top
                    // sono i primi consecutivi o nel mezz
                    if(n_element > 1){
                        // tolgo il primo elmento e diminuisco la finestra
                        for(int el = 0; el < bound.length; el++){
                            if(this.getSaElement(window[0]) >= bound[el][0] && this.getSaElement(window[0]) <= bound[el][1]){
                                counter[el]--;
                                break;
                            }
                        }
                        window[0]++;
                        num_el = Arrays.stream(counter).filter(el -> el > 0).count();
                    }
                    if(n_element  == 1){

                        //prendo lcp minimo tra i suffissi consecutivi
                        int  min_lcp_temp = Integer.MAX_VALUE;
                        int  min_index = this.getLcpElement(window[0] + 1);

                        for(int h = window[0] + 1; h <= window[1]; h++){
                            if(min_lcp_temp > this.getLcpElement(h)){
                                min_lcp_temp = this.getLcpElement(h);
                                min_index = this.getSaElement(h);
                            }
                        }

                        if(min_lcp_temp > max_lcp){
                            max_lcp = min_lcp_temp;
                            list_lcp = new ArrayList<>();
                            list_lcp.add(this.getText().substring(min_index, min_index + min_lcp_temp));
                        }
                        if(min_lcp_temp == max_lcp){
                            list_lcp.add(this.getText().substring(min_index, min_index + min_lcp_temp));
                        }
                        // tolgo il primo elmento e diminuisco la finestra
                        for(int el = 0; el < bound.length; el++){
                            if(this.getSaElement(window[0]) >= bound[el][0] && this.getSaElement(window[0]) <= bound[el][1]){
                                counter[el]--;
                                break;
                            }
                        }
                        window[0]++;
                        num_el = Arrays.stream(counter).filter(el -> el > 0).count();
                    }
                }
                window[1]++;
            }
        }

        return list_lcp.stream().distinct().toList();
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
                     .collect(Collectors.groupingBy(element -> getLcpElement(getIsaElement(element)))); // get map with K = number of LRS  and V = List of index in the text
    }

    public Map<Integer,String> getLRSMap(Map<Integer,List<Integer>> map ){
          return   map.values()
                       .stream()
                       .toList()
                       .get(0)
                       .stream()
                       .collect(Collectors.toMap(this::getIsaElement,
                                                 value -> getText()
                                                         .substring(value,
                                                                    value + getLcpElement(getIsaElement(value)))));
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
    private void setLcpSentinel(int index){
        this.lcp[index] = SuffixArrayBuilder.FIRST_LCP_POSITION;
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
        private static int FIRST_LCP_POSITION = -1;
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

        private static SuffixArray buildSuffixArray(String text) {
            text = SuffixArrayBuilder.textCleaner(text) + Sentinel.first.getSentinel();
            return  buildLcpArray(Suffix.buildSuffixArray(text));
        }

        private static SuffixArray buildSuffixArray(String... strings){
            if(strings.length <= Sentinel.SIZE_SENTINEL && strings.length >= 2) {
                List<String> temp_list = Arrays.stream(strings).toList();

                AtomicInteger index = new AtomicInteger(0);
                int sentinel_size = strings.length;
                Sentinel[] sentinels = Sentinel.values();
                SuffixArray s = buildLcpArray(Suffix.buildSuffixArray(Arrays.stream(strings)
                        .limit(Sentinel.SIZE_SENTINEL)
                        .map(string -> SuffixArrayBuilder.textCleaner(string))
                        .map(string -> string + sentinels[index.getAndIncrement()].getSentinel())
                        .reduce("", (s1, s2) -> s1 + s2)));

                Stream.iterate(0, lcp_index -> ++lcp_index).limit(sentinel_size).forEach(lcp_index -> s.setLcpSentinel(lcp_index));
                s.strings = temp_list;
                return s;
            }
            return new SuffixArray();
        }



        private static SuffixArray buildLcpArray(SuffixArray sa_array) {
            int max_lcp_element=0;
            int len = 0;  // how many characters remain
            long sa_position;  // the position of the suffix in the suffix array
            long sa_array_len = sa_array.getLength();  // sa_array.getLength() == sa_array.sa_index.length;
            long prev; //index of previous suffix
            Integer[] sa_index = sa_array.getSa();
            String text = sa_array.getText();

                for (int i = 0; i < sa_array_len; i++) {
                    sa_position = sa_array.getIsaElement(i);
                    if (sa_position > 0) {    // text_position == 0 when i = sa_array.length - 1 (end sa_array)
                        prev = sa_index[(int) sa_position - 1];  // Take a previous index
                        while (text.charAt(i + len) == text.charAt((int) (prev + len))) {
                            len = len + 1;
                        }     //while same char
                        sa_array.setLcp(sa_position, len);
                        max_lcp_element = Math.max(max_lcp_element, len);
                        len = Math.max((len - 1), 0);  //decrease number of equal characters
                    } else {
                        sa_array.setLcp(sa_position, FIRST_LCP_POSITION);
                    }
                }

            sa_array.max_lcp = max_lcp_element;
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
    //-----------------------------------------------------------------------------------------------------------------

    protected enum Sentinel{
        first('!'),
        second('\"'),
        third('#'),
        fourth('$'),
        fifth('%'),
        sixth('&');

        private final static int SIZE_SENTINEL = Sentinel.values().length;
        char sentinel;
        Sentinel(char c){
            sentinel = c;
        }

        public char getSentinel(){
            return sentinel;
        }
    }
}
