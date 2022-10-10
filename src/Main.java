
import java.awt.im.spi.InputMethod;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {


        //String ss ="Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
        //String ss = "apelle figlio di apollo fece una palla di pelle di pollo tutti i pesci vennero a galla per guardare la palla di pelle di pollo fata da apelle figlio di apollo";
        //  String ss ="Caratterizzato da un universo narrativo inedito, in cui Peter Parker è Spider-Man già da otto anni e combatte il crimine fra le strade (e i tetti!) di Manhattan, il gioco reinventa alcuni personaggi raccontando una storia appassionante, a maggior ragione per i fan dell'eroe Marvel.";
        String ss = "banana";


        SuffixArray sa = SuffixArray.buildSuffixArray(ss);

        System.out.println(sa.getSa()[6]);
        System.out.println(sa.getIsa()[2]);




    }
}