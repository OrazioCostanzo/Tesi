import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        //String ss = "«Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum$»";
        //String ss = "Apelle figlio di apollo fece una palla di pelle di pollo tutti i i pesci vennero a galla per guardare la palla di pelle di pollo fata da apelle figlio di apollùlo";
        //String ss ="Caratterizzato da un universo narrativo inedito, in cui Peter Parker è Spider-Man già da otto anni e combatte il crimine fra le strade (e i tetti!) di Manhattan, il gioco reinventa alcuni personaggi raccontando una storia appassionante, a maggior ragione per i fan dell'eroe Marvel.";
        String ss = "AABABABB";

/*
        StringBuilder ss = new StringBuilder();
        long lenn = 1000;
        char[]gen = {'A','B','C','D'};
        for(int i = 0; i < lenn; i++)
            ss.append(gen[(int) (Math.random() * 4)]);

 */





        System.out.println(SuffixArray.getVirtualChar());
        long start = System.currentTimeMillis();


        SuffixArray s = SuffixArray.buildSuffixArray(ss.toString());
        long end = System.currentTimeMillis();
        System.out.println("CREAZIONE SUFFIX ARRAY " + (end - start));



        long n = s.getLength();
       try(PrintWriter writer = new PrintWriter("PROVA.txt")){
            for(int i=0 ; i<n ; i++) writer.println("LCP INDEX: " + s.getLcpElement(i) +" SA INDEX:"  + s.getSaElement(i) + " TA INDEX: " + s.getIsaElement(i) + " SUFFISSO:" +  s.getText().substring(s.getSaElement(i)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }








        start = System.currentTimeMillis();
        Map<Integer, List<Integer>> map = s.getLRS();
        System.out.println(s.getLRSMap(map));
        end = System.currentTimeMillis();
        System.out.println("CREAZIONE MAPPA:" + (end - start));

        s.getSWNC();

        start = System.currentTimeMillis();


        end = System.currentTimeMillis();
        System.out.println("BINARYSEARCH " + (end - start));



        MinimalAbsentWords maw = MinimalAbsentWords.getMAW(s,new Character[]{'A','B'});
         maw.printB1();
        System.out.println();

    }
}