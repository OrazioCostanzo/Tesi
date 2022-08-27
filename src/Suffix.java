import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * This class creates a suffix array by comparing all suffixes of the text.
 * @version 1.0 (8/1/2022).
 * @author Orazio Costanzo.
 */
public class Suffix implements Comparable<Suffix>{

    //Fields
    //------------------------------------------------------------------------------------------------------------------
    private static char PUPPET_CHAR = SuffixArray.Sentinel.first.getSentinel();
    private final int index;
    private int rank;
    private int next_rank;

    //------------------------------------------------------------------------------------------------------------------


    //Constructors
    //------------------------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------


    //Methods
    //------------------------------------------------------------------------------------------------------------------


    /**
     * This method creates an integer array that contains the indexes of the sorted suffixes.
     * Sorting algorithm. @see Arrays.
     * @param sa Suffix array to be processed.
     * @return Returns an array of integers with sorted suffixes.
     */
    private static Integer[] build(Suffix[] sa){
        int sa_size = sa.length;
        AtomicInteger index = new AtomicInteger(0);

        //Inserisco il successivo rank
        Arrays.stream(sa)
                .forEach(suffix -> {
                    suffix.next_rank = (index.get() + 1 < sa_size ? sa[index.get() + 1].rank : -1);
                    index.getAndIncrement();
                });

        //ordino in base ai primi due caratteri
        Arrays.sort(sa);

        index.set(0);

        int[] array_index = new int[sa_size];

        //ciclo per ogni 2^i caratteri , quindi parto da 4 dato che i primi due li abbiamo fatti e poi shifto 4 a destra cosi diventa 8 ,16... fino a 2*n
        for(int length = 4 ; length < 2*sa_size ; length <<=1){
            int rank = 0;
            int previous = sa[0].rank;

            //first rank
            sa[0].rank = rank; //sa[0] = rank = 0
            array_index[sa[0].index] = 0;


            for(int i = 1 ; i < sa_size ; i++){
                if(sa[i].rank == previous && sa[i].next_rank == sa[i - 1].next_rank){
                    previous = sa[i].rank;
                    sa[i].rank = rank;
                }
                else{
                    previous = sa[i].rank;
                    sa[i].rank = ++rank ;
                }
                array_index[sa[i].index] = i;
            }

            int finalLength = length;

            Arrays.stream(sa)
                  .forEach(suffix -> {
                      int next_position = suffix.index + finalLength / 2;
                      suffix.next_rank = (next_position < sa_size ? sa[array_index[next_position]].rank : -1);
                   });

            Arrays.sort(sa);
        }

        index.set(0);

        return Arrays.stream(sa)
                .map(suffix -> suffix.index)
                .toArray(Integer[]::new);
    }

    private static Suffix[] initialize(String text){
        int text_length = text.length();
        AtomicInteger index = new AtomicInteger(-1);

        return Stream.generate(() -> {
            index.getAndIncrement();
            return new Suffix(index.get(), text.charAt(index.get()) - Suffix.PUPPET_CHAR, 0);
        })
        .limit(text_length)
        .toArray(Suffix[]::new);
    }

    /**
     * Compare either this rank with other suffix rank or this next_rank with other suffix next_rank.
     * @param suffix the object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable
     */
    @Override
    public int compareTo(Suffix suffix){
        if(this.rank != suffix.rank) return Integer.compare(this.rank, suffix.rank);
        return Integer.compare(this.next_rank, suffix.next_rank);
    }

    /**
     * Build a suffix array , on the text.
     * @param text Raw text (without being processed).Text on which to create the suffix array.
     * @return Suffix Array object.
     */
    public static SuffixArray buildSuffixArray(String text){
        return getSA(build(initialize(text)),text);
    }
    //------------------------------------------------------------------------------------------------------------------


    //Getter
    //------------------------------------------------------------------------------------------------------------------
    private static SuffixArray getSA(Integer[] sa, String text){
        return new SuffixArray(sa, text);
    }
    //------------------------------------------------------------------------------------------------------------------

}