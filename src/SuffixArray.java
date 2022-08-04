import java.util.Arrays;

/**
 * This class manages an array of integers (suffix array) and the text on which the suffix array has been built.
 * @version 1.0 (8/1/2022)
 * @author Orazio Costanzo
 */
public class  SuffixArray {

    private Integer[] sa_index; // suffix array index
    private Integer[] ta_index; //text array index
    private Integer[] lcp; //lcp array
    private String text; //text

    public SuffixArray(Integer[] sa_index, String text) {
        this.sa_index = sa_index;
        this.text = text;
        int sa_length = sa_index.length;
        this.ta_index = new Integer[sa_length];
        this.lcp = new Integer[sa_length];

        for(int i=0 ; i < sa_index.length; i++)
            this.ta_index[this.sa_index[i]] = i;
    }


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
    @Override
    public String toString(){
        return Arrays.stream(sa_index)
                .map(index -> "Index:[" + index + "] " + "Suffix:\"" + text.substring(index) + "\"\n")
                .reduce("", String::concat);
    }

    /**
     * sa_index getter
     * @return Integer[]
     */
    public Integer[] getSaIndex() {
        return sa_index;
    }

    /**
     * ta_index getter
     * @return Integer[]
     */
    public Integer[] getTaIndex() {
        return ta_index;
    }
    public long getTaCurrentIndex(int index){
        return this.ta_index[index];
    }

    /**
     * lcp getter
     * @return Integer[]
     */
    public Integer[] getLcp() {
        return lcp;
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

    public void setLcp(long text_position, int len) {
        this.lcp[(int) text_position] = len;
    }
}
