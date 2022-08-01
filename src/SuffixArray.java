import java.util.Arrays;

/**
 * This class manages an array of integers (suffix array) and the text on which the suffix array has been built.
 * @version 1.0 (8/1/2022)
 * @author Orazio Costanzo
 * @param sa An integer array that contains the indices of the text suffixes
 * @param text The text on which to create the suffix array
 */
public record SuffixArray (Integer[] sa, String text){
    /**
     * Print all suffix into the suffix array.
     */
    public void printSA(){
        Arrays.stream(sa)
                .map(text::substring)
                .forEach(System.out::println);
    }
    /**
     * Prints the first n suffixes.
     * @param n number of elements you want to print.
     */
    public void printSA(final long n){
        final long sa_size = sa.length;
        if(n > sa_size || n < 0) {
            System.out.println("The size of the suffix array is: " + sa_size);
            return;
        }
        Arrays.stream(sa)
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
        final long sa_size = sa.length;
        if((m < 0 || m > sa_size) || (n < 0)) {
            System.out.println("The size of the suffix array is: " + sa_size);
            return;
        }
        Arrays.stream(sa)
                .skip(m)
                .limit(n)
                .map(text::substring)
                .forEach(System.out::println);
    }
    @Override
    public String toString(){
        return Arrays.stream(sa)
                .map(index -> "Index:[" + index + "] " + "Suffix:\"" + text.substring(index) + "\"\n")
                .reduce("", String::concat);
    }
}
