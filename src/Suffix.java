import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class creates a suffix array by comparing all suffixes of the text.
 * @version 1.0 (8/1/2022).
 * @author Orazio Costanzo.
 */
public class Suffix implements Comparable<Suffix>{
    private static char PUPPET_CHAR = '$';
    private int index;
    private int rank;
    private int next_rank;
    /**
     * Creates and initializes a suffix object.
     * @param index The original index of the suffix (position in the text).
     * @param rank The rank of first char.(text.charAt(i) - PUPPET_CHAR) , (PUPPET_CHAR = '$').
     * @param next_rank Initialize next_rank = 0.
     */
    public Suffix(int index, int rank, int next_rank){
        this.index = index;
        this.rank = rank;
        this.next_rank = next_rank;
    }
    /**
     * Constructor which accepts a Suffix object.
     * @param s a Suffix object.
     */
    public Suffix(Suffix s){
        this.index = s.index;
        this.rank = s.rank;
        this.next_rank = s.next_rank;
    }
    /**
     * This method creates an integer array that contains the indexes of the sorted suffixes.
     * Sorting algorithm. @see Arrays.
     * @param sa Suffix array to be processed.
     * @return Returns an array of integers with sorted suffixes.
     */
    private static Integer[] build(Suffix[] sa){
        int sa_size = sa.length;
        AtomicInteger inc = new AtomicInteger(0);
        //inserisco il successivo rank
        Arrays.stream(sa)
              .forEach(suffix -> {
                  suffix.next_rank = (inc.get() + 1 < sa_size ? sa[inc.get() + 1].rank : -1);
                  inc.getAndIncrement();
              });
        /*for(int i=0 ; i<sa_size ; i++) sa[i].next_rank = (i + 1 < sa_size ? sa[i + 1].rank : -1);*/
        Arrays.sort(sa);

        int[] index = new int[sa_size];
        //ciclo per ogni 2^i caratteri da controllare quindi parto da 4 dato che i primi due li abibamo fatti e poi shifto 4 a destra cosi diventa 8 ,16... fino a 2*n
        for(int length = 4 ; length < 2*sa_size ; length <<=1){

            int rank = 0, previous = sa[0].rank;
            sa[0].rank = rank;
            index[sa[0].index] = 0;

            for(int i = 1 ; i < sa_size ; i++){
                if(sa[i].rank == previous && sa[i].next_rank == sa[i - 1].next_rank){
                    previous = sa[i].rank;
                    sa[i].rank = rank;
                }
                else{
                    previous = sa[i].rank;
                    sa[i].rank = ++rank ;
                }
                index[sa[i].index] = i;
            }
            int finalLength = length;
            Arrays.stream(sa)
                  .forEach(suffix -> {
                      int next_position = suffix.index + finalLength / 2;
                      suffix.next_rank = (next_position < sa_size ? sa[index[next_position]].rank : -1);
                   });
            /*for (Suffix suffix : sa) {
                // parte dall i esima poszione dell suffisso corrente e vede se ha un carattere length/2 caratteri dopo
                int next_position = suffix.index + length / 2;
                //assegna il next_rank
                suffix.next_rank = (next_position < sa_size ? sa[index[next_position]].rank : -1);
            }*/
            Arrays.sort(sa);
        }


        Integer[] suffix_array = new Integer[sa_size];
        for(int i = 0 ; i < sa_size ; i++){
            suffix_array[i] = sa[i].index;
        }

        return suffix_array;
        //ricalcolo i rank , il primo elemento ha rank 1  e calcolo il previuos
    }
    private static Suffix[] initialize(String text){
        int text_length = text.length();
        AtomicInteger index = new AtomicInteger(-1);
        Suffix[] sa = new Suffix[text_length];

        Object[] temp_sa = Arrays.stream(sa)
                                 .map(suffix -> {
                                     index.getAndIncrement();
                                     return new Suffix(index.get(), text.charAt(index.get()) - Suffix.PUPPET_CHAR, 0);
                                 })
                                 .toArray();
        for(int i = 0; i < temp_sa.length; i++) {
            if(temp_sa[i] instanceof Suffix)
                sa[i] = (Suffix) temp_sa[i];
        }

       /*for(int i=0 ; i<text_length ; i++)
            //inserisco il rank, cioè la differenza del carattere corrente con $ (quanto dista il carattere corrente con $)
            sa[i] = new Suffix(i, text.charAt(i) - Suffix.PUPPET_CHAR,0);
            */

        return sa;
    }
    /**
     * if(this.rank != suffix.rank) return Integer.compare(this.rank, suffix.rank);<br>
     * return Integer.compare(this.next_rank, suffix.next_rank);.<br>
     * @param suffix the object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable
     */
    @Override
    public int compareTo(Suffix suffix){
        if(this.rank != suffix.rank) return Integer.compare(this.rank, suffix.rank);
        return Integer.compare(this.next_rank, suffix.next_rank);
    }
    private static SuffixArray getSA(Integer[] sa, String text){
        return new SuffixArray(sa, text);
    }
    /**
     * Build a suffix array , on the text.
     * @param text Raw text (without being processed).Text on which to create the suffix array.
     * @return Suffix Array object.
     */
    public static SuffixArray buildSuffixArray(String text){
        return getSA(build(initialize(text)),text);
    }

}