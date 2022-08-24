
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {


       // String ss = "«Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum$»";
       // String ss = "Apelle figlio di apollo fece una palla di pelle di pollo tutti i i pesci vennero a galla per guardare la palla di pelle di pollo fata da apelle figlio di apollùlo";
        //String ss ="Caratterizzato da un universo narrativo inedito, in cui Peter Parker è Spider-Man già da otto anni e combatte il crimine fra le strade (e i tetti!) di Manhattan, il gioco reinventa alcuni personaggi raccontando una storia appassionante, a maggior ragione per i fan dell'eroe Marvel.";
      //String ss = "banana";

 StringBuilder ss = new StringBuilder();
        long lenn = 10;
        char[]gen = {'A','B','C','D','T','F'};
        for(int i = 0; i < lenn; i++)
            ss.append(gen[(int) (Math.random() * 6)]);








        long start = System.currentTimeMillis();
        SuffixArray s = SuffixArray.buildSuffixArray(ss.toString());
        long end = System.currentTimeMillis();
        System.out.println("CREAZIONE SUFFIX ARRAY " + (end - start));



      int n = s.getText().length();
      try(PrintWriter writer = new PrintWriter("PROVA.txt")){
            for(int i=0 ; i<n ; i++) writer.println("LCP INDEX: " + s.getLcpElement(i) +" SA INDEX:"  + s.getSaElement(i) + " TA INDEX: " + s.getIsaElement(i) + " SUFFISSO:" +  s.getText().substring(s.getSaElement(i)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }






/*

        start = System.currentTimeMillis();
        Map<Integer, List<Integer>> map = s.getLRS();
        System.out.println(s.getLRSMap(map));
        end = System.currentTimeMillis();
        System.out.println("CREAZIONE MAPPA:" + (end - start));


        start = System.currentTimeMillis();
        int[]frequenza = s.getStrFre("ABA");
        end = System.currentTimeMillis();
        Arrays.stream(frequenza).forEach(System.out::println);
        System.out.println("Frequenza " + (end - start));


 */

        start = System.currentTimeMillis();
        Map<Character,List<String>> maw = MinimalAbsentWords.buildMAW(s);
        end = System.currentTimeMillis();
        List<String> listaa = MinimalAbsentWords.getMawAsList(maw);

        System.out.println(listaa);
    }
}