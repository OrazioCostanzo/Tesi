import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The purpose of this class is to search for MAWs in a text.
 */
public class MinimalAbsentWords {

    /**
     * Returns a map containing the minimal absent words grouped by the first character.
     * @param sa the suffix array on which to build the map.
     * @return returns a map with
     * key: first character
     * value: minimal absent words.
     */
    public static Map<Character,List<String>>  buildMAW(SuffixArray sa){
        Map<Character, Integer> alphabet = getAlphabetMap(SuffixArray.getAlphabet(sa));
        boolean[][] b1 = new boolean[alphabet.size()][(sa.getText().length() - 1) << 1];
        boolean[][] b2 = new boolean[alphabet.size()][(sa.getText().length() - 1) << 1];
        Arrays.stream(b1).forEach(row -> Arrays.fill(row, false));
        Arrays.stream(b2).forEach(row -> Arrays.fill(row, false));
        Integer[] sa_index = Arrays.copyOfRange(sa.getSa(), 1, sa.getSa().length);
        Integer[] lcp = Arrays.copyOfRange(sa.getLcp(), 1, sa.getLcp().length);
        String text = sa.getText().substring(0, sa.getText().length() - 1);


        topDown(text, sa_index, lcp, b1, b2, alphabet, sa.getMax_lcp());
        bottomUp(text, sa_index, lcp, b1, b2, alphabet, sa.getMax_lcp());


        return   getMAW(text,sa_index,lcp,b1,b2, alphabet);


    }

    private static Map<Character,List<String>> getMAW(String text, Integer[] sa_index, Integer[] lcp, boolean[][] b1, boolean[][] b2, Map<Character,Integer> alphabet) {
        ArrayList<String> maw = new ArrayList<>();
        Character[] key_set = alphabet.keySet().toArray(Character[]::new);

        Stream.iterate(0, index -> ++index)
              .limit(sa_index.length)
              .forEach(index -> {
                  // for each char in b1
                  Stream.iterate(0, alphabet_index -> ++alphabet_index)
                          .limit(alphabet.size())
                          .forEach(row -> {
                              //System.out.println("CICLO:" + index +"alfabet index:" + row + " alphabet map:" + key_set[row]);
                              int b_index = (index << 1);

                              if(b2[row][b_index] != b1[row][b_index]){
                                  maw.add(key_set[row] + text.substring(sa_index[index], sa_index[index] + lcp[index] + 1));
                              }
                              if(b2[row][b_index + 1] != b1[row][b_index + 1] && ((sa_index[index] + lcp[index + 1] + 1)  <  text.length())){
                                  maw.add(key_set[row] + text.substring(sa_index[index], sa_index[index] + lcp[index + 1] + 1));
                              }
                          });
              });

       return maw.stream()
               .distinct()
               .collect(Collectors.groupingBy(element -> element.charAt(0)));


    }


    private static void topDown(String text, Integer[] sa_index, Integer[] lcp, boolean[][] b1, boolean[][] b2, Map<Character, Integer> alphabet, int max_lcp) {

        boolean[][] interval = new boolean[alphabet.size()][max_lcp + 2];
        int pre_char;
        ArrayDeque<Integer> lifo_lcp = new ArrayDeque<>();
        ArrayDeque<Integer> lifo_mem = new ArrayDeque<>();
        Arrays.stream(interval).forEach(row -> Arrays.fill(row,false));
        Integer top_stack = 0;
        lifo_lcp.push(top_stack);
        Integer next_lcp;

// for all element in suffix array
        for(int i = 0; i < text.length(); i++){

            // se non è il primo elemento del suffix array e se non hanno lo stesso prefisso comune più lungo
            if(i > 0 && lcp[i] < lcp[i - 1]){
                top_stack = lifo_lcp.pop();

                //per tutti gli elmenti che sono presenti nello stack
                while(!lifo_lcp.isEmpty() && top_stack > lcp[i]){
                    next_lcp = lifo_lcp.pop();

                    if(next_lcp <= lcp[i]){
                        // per tutti gli lcp piu piccoli e uguali
                        for(int j = 0; j < alphabet.size(); j++){

                            if(next_lcp != lcp[i]){
                                interval[j][lcp[i]] = interval[j][top_stack];
                            }

                            b1[j][(i << 1) -1] = interval[j][top_stack];
                            b2[j][(i << 1) -1] = interval[j][top_stack];

                            if(next_lcp == lcp[i]){
                                b2[j][(i << 1) - 1] = interval[j][next_lcp];
                            }
                        }
                    }
                    // si azzerano tutti gli elementi dello stack maggiori del lcp[i] corrente, questo perchè il prefisso piu lungo comune cambia
                    for(int j = 0; j < alphabet.size(); j++){
                        interval[j][top_stack] = false;
                    }
                    //ultimo elemento uscito dallo stack
                    top_stack = next_lcp;
                }
                // rimetto nello stack il prefisso comune <= lcp[i]
                lifo_lcp.push(top_stack);
            }

            if(sa_index[i] > 0) {
                pre_char = alphabet.get(text.charAt(sa_index[i] - 1));
                while(!lifo_lcp.isEmpty()){
                    top_stack = lifo_lcp.pop();
                    lifo_mem.push(top_stack);
                    if(interval[pre_char][top_stack] == true) break;
                    interval[pre_char][top_stack] = true;
                }
                while (!lifo_mem.isEmpty()){
                    top_stack = lifo_mem.pop();
                    lifo_lcp.push(top_stack);
                }
                interval[pre_char][lcp[i]] = true;
            }
            else {
                pre_char = -1;
            }


            if(i > 0 && lcp[i] > 0 && sa_index[i - 1] > 0){
                interval[alphabet.get(text.charAt(sa_index[i - 1] - 1))][lcp[i]] = true;
            }

            for(int j = 0; j < alphabet.size(); j++){
                b2[j][i << 1] = interval[j][lcp[i]];
            }

            if(pre_char != -1){
                b1[pre_char][(i << 1) + 1] = true;
                b1[pre_char][i << 1] = true;
                b2[pre_char][(i << 1) + 1] = true;
                b2[pre_char][i << 1] = true;
            }

            top_stack = lifo_lcp.pop();
            if(top_stack != lcp[i]){
                lifo_lcp.push(top_stack);
                top_stack = lcp[i];
            }
            lifo_lcp.push(top_stack);

        }
    }




    private static void bottomUp(String text, Integer[] sa_index, Integer[] lcp, boolean[][] b1, boolean[][] b2, Map<Character, Integer> alphabet, int max_lcp){
        boolean[][] interval = new boolean[alphabet.size()][max_lcp + 2];
        int pre_char;
        ArrayDeque<Integer> lifo_lcp = new ArrayDeque<>();
        ArrayDeque<Integer> lifo_rem = new ArrayDeque<>();
        ArrayDeque<Integer> lifo_mem = new ArrayDeque<>();
        Arrays.stream(interval).forEach(row -> Arrays.fill(row,false));
        Integer top_stack;
        Integer next_lcp;
        Integer next_next_lcp = 0;
        int n = text.length();
        int next_stack;
        Iterator<Integer> stack_it;


        while(!lifo_lcp.isEmpty()){
            top_stack = lifo_lcp.pop();
            for(int j = 0; j < alphabet.size(); j++){
                interval[j][top_stack] = false;
            }
        }



        top_stack = 0;
        lifo_lcp.push(top_stack);

        for(int i = (n - 1); i >= 0; i--){

            top_stack = lifo_lcp.pop();
            next_lcp = lcp[i] + 1;

            // fino a quando il suffisso corrente ha un prefisso comune più corto
            while((!lifo_lcp.isEmpty()) && top_stack > lcp[i]){
                lifo_rem.push(top_stack);
                next_stack = lifo_lcp.pop();

                if(next_stack < lcp[i]){
                    for(int j = 0; j < alphabet.size(); j++){
                        interval[j][lcp[i]] = interval[j][top_stack];
                    }
                    next_lcp = top_stack;
                }
                if(next_stack == lcp[i]){
                    next_lcp = top_stack;
                }
                top_stack = next_stack;
            }
            lifo_lcp.push(top_stack);

            for(int k = 0; k < alphabet.size(); k++){
                if(b1[k][i << 1] == true){
                    while((!lifo_lcp.isEmpty())){
                        top_stack = lifo_lcp.pop();
                        lifo_mem.push(top_stack);
                        if(interval[k][top_stack] == true) break;
                        interval[k][top_stack] = true;
                    }
                    while (!lifo_mem.isEmpty()){
                        top_stack = lifo_mem.pop();
                        lifo_lcp.push(top_stack);
                    }
                    interval[k][lcp[i]] = true;
                }
            }

            for(int j = 0; j < alphabet.size(); j++){
                b2[j][i << 1] = (b2[j][i << 1] || interval[j][lcp[i]]); // 1 if (1,0 or 0,1 or 11)  or 0 otherwise
                // non è il primo ciclo
                if(i < (n - 1)){
                    b1[j][(i << 1) + 1] = (b1[j][(i << 1)+1] || interval[j][next_next_lcp]);
                    b2[j][(i << 1) + 1] = (interval[j][lcp[i+1]] || b2[j][(i << 1 )+1]);
                }
            }

            next_next_lcp = next_lcp;

            if(i < (n - 1) && lcp[i + 1] > lcp[i]){
                top_stack  = lifo_rem.pop();

                for(int j = 0; j < alphabet.size(); j++){
                    b1[j][i << 1] = b1[j][i << 1] || interval[j][top_stack];
                    interval[j][top_stack] = false;
                }

                while(!lifo_rem.isEmpty()){
                    top_stack = lifo_rem.pop();
                    for(int j = 0; j < alphabet.size(); j++){
                        interval[j][top_stack] = false;
                    }
                }
            }
            top_stack = lifo_lcp.pop();

            if(top_stack != lcp[i]){
                lifo_lcp.push(top_stack);
                top_stack = lcp[i];
            }
            lifo_lcp.push(top_stack);
        }
    }


    private static Map<Character, Integer> getAlphabetMap(Character[] alphabet){
        Arrays.sort(alphabet);
        AtomicInteger index = new AtomicInteger(0);
        return Arrays.stream(alphabet)
                     .collect(Collectors.toMap(key -> key, value -> index.getAndIncrement()));
    }

    /**
     * Print all the MAW found.
     * @param maw map containing all MAWs.
     */
    public static void printMaw(Map<Character,List<String>> maw){
        Character[] alphabet = maw.keySet().toArray(Character[]::new);
        Arrays.stream(alphabet)
                .forEach(c -> maw.get(c).stream().forEach(System.out::println));
    }
//START WITH

    /**
     * Print all the MAW found start with "c".
     * @param maw map containing all MAWs.
     * @param c the first character to search on.
     */
    public static void printMawSW(Map<Character,List<String>> maw, char c){
        if(maw.get(c) != null)
            maw.get(c).stream().forEach(System.out::println);
        else
            System.out.println("empty list");
    }

    /**
     * Returns all MAWs in a list.
     * @param maw map containing all MAWs.
     * @return the list with the MAW.
     */
    public static List<String> getMawAsList(Map<Character,List<String>> maw){
        Character[] alphabet = maw.keySet().toArray(Character[]::new);
        ArrayList<String> maw_list = new ArrayList<>();

        Arrays.stream(alphabet)
                .forEach(c -> maw.get(c).stream().forEach(element -> maw_list.add(element)));
         return maw_list;
    }
// time log2(strings.length) * 3 ==> O(log2(string.length)?

    /**
     * Checks if a string passed as a parameter is a MAW in a suffix array built on a list of strings.
     * @param maw string to check.
     * @param strings list of strings on which to build the suffix array.
     * @return returns true if "maw" is a MAW or false otherwise.
     */
    public static boolean isMaw(String maw, String... strings){
        SuffixArray sa = SuffixArray.buildSuffixArray(strings);
        String left = maw.substring(0,maw.length() - 1);
        String right = maw.substring(1,maw.length());

        if(sa.isSubstring(left) >= 0  && sa.isSubstring(right) >= 0 && sa.isSubstring(maw) == -1){
            return  true;
        }
        return false;

    }
    /**
     * Returns all MAWs in a list starting with "c".
     * @param maw map containing all MAWs.
     * @param c the first character to search on.
     */
    public static List<String> getMawAsListSW(Map<Character,List<String>> maw, char c){
        if(maw.get(c) != null) {
            ArrayList<String> maw_list = new ArrayList<>();
            return maw_list = new ArrayList<>(maw.get(c).stream().toList());
        }
        return null;
    }

}
