import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MinimalAbsentWords {
    private Integer[][] B1;
    private Integer[][] B2;
    public Map<Character, Integer> alphabet_map;
    private int alphabet_size;

    private MinimalAbsentWords(SuffixArray sa, Character[] alphabet, int alphabet_size){

        AtomicInteger index = new AtomicInteger(0); // index for the mapping

        this.alphabet_map = Arrays.stream(alphabet)
                                  .collect( Collectors.toMap(key -> key, value -> index.getAndIncrement())); // mapping each char with index, key = char , value = index.

        this.alphabet_size = alphabet.length;

        long text_len =(long) (sa.getText().length() - 1)  << 1; // we do not consider the first element of the array because it is the virtual character
        this.B1  = new Integer[(int) text_len][alphabet_size];
        this.B2  = new Integer[(int) text_len][alphabet_size];



    }

    public static MinimalAbsentWords getMAW(SuffixArray sa, Character[] alphabet){
        MinimalAbsentWords maw = initialize(sa, alphabet);
        System.out.println(maw.alphabet_map);
        topDown(sa, maw);
        bottomUp(sa, maw);
        return maw;
    }

    public Integer[][] getB1() {
        return B1;
    }

    public Integer[][] getB2() {
        return B2;
    }

    public Map<Character, Integer> getAlphabet_map() {
        return alphabet_map;
    }

    public void printB1(){
        Arrays.stream(this.B1)
                .forEach(row -> Arrays.stream(row)
                        .forEach(column -> System.out.println("row: " + row + " column: " + column)));
    }

    public void printB2(){
        Arrays.stream(this.B2)
                .forEach(row -> Arrays.stream(row)
                        .forEach(column -> System.out.println("row: " + row + " column: " + column)));

    }

    private static MinimalAbsentWords initialize(SuffixArray sa, Character[] alphabet) {
        Arrays.sort(alphabet); // sort array char.
        MinimalAbsentWords maw = new MinimalAbsentWords(sa, alphabet, alphabet.length);
        return maw;
    }

    private static void topDown(SuffixArray sa, MinimalAbsentWords maw) {

        Integer[][] interval = new Integer[sa.getMax_lcp() + 1][maw.alphabet_size]; // assign matrix with rows = max element in lcp + 1 array and column = size of alphabet
        Arrays.stream(interval).forEach(row -> Arrays.fill(row,0)); // fill all matrix with 0

        Deque<Integer> lcp_stack = new ArrayDeque<>(); // stack with more features
        lcp_stack.push(0); // initialize stack with 0 , first element have lcp = 0

        String text = sa.getText().substring(0, sa.getText().length() - 1); // text without VIRTUAL CHAR
        long text_len = text.length(); // text size without VIRTUAL_CHAR

        int rows = interval.length; // number of interval rows
        int columns = interval[0].length; // number of interval columns ==> alphabet size


        Integer[] sa_index = Arrays.copyOfRange(sa.getSa(), 1, sa.getSa().length); //without VIRTUAL_CHAR, first element of sa_index.
        Integer[] lcp = Arrays.copyOfRange(sa.getLcp(), 1, sa.getLcp().length);  // without VIRTUAL_CHAR position, first element of lcp array.

        Integer pop_stack_value = lcp_stack.peek();

        for(int i = 0; i < sa_index.length; i++){ // for all element into sa_index

            if(i > 0 && (lcp[i] < lcp[i - 1]) ){
                while(lcp_stack.peek() > lcp[i] && lcp_stack.peek() != null){
                    pop_stack_value = lcp_stack.pop();
                    for(int j = 0; j < columns; j++){
                        interval[pop_stack_value][j] = 0;
                    }
                }
                if(lcp_stack.peek() < lcp[i])
                    interval[lcp[i]] = interval[pop_stack_value];

                maw.B1[2*i - 1] = interval[pop_stack_value];
            }

            if(sa_index[i] > 0 ){

                int char_index = maw.alphabet_map.get(text.charAt(sa_index[i] - 1)); // index into interval array of previous char
                Integer peek_stack_value = lcp_stack.peek();

                Iterator<Integer> stack_iterator = lcp_stack.iterator();
                stack_iterator.next();

                while(interval[peek_stack_value][char_index] == 0){
                    interval[peek_stack_value][char_index] = 1;
                    if(stack_iterator.hasNext()) {
                        peek_stack_value = stack_iterator.next();
                    }
                    else break;
                }

                interval[lcp[i]][char_index] = 1;
                maw.B1[2*i][char_index] = 1;
                maw.B1[2*i + 1][char_index] = 1;
                maw.B2[2*i][char_index] = 1;
                maw.B1[2*i + 1][char_index] = 1;
            }

            if(i > 0 && lcp[i] > 0 && sa_index[i - 1] > 0 ){
                Integer prev_char_index = maw.alphabet_map.get(text.charAt(sa_index[i - 1] - 1)); // index of previous char of previous suffix
                interval[lcp[i]][prev_char_index] = 1;
            }

            maw.B2[2*i] = interval[lcp[i]];

            if(lcp_stack.peek() != lcp[i]) {
                lcp_stack.push(lcp[i]);
            }
        }
    }

    private static void bottomUp(SuffixArray sa, MinimalAbsentWords maw) {
    }


}
