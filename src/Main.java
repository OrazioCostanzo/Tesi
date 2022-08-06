import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {

        String ss = "«Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum$»";
        //String ss = "banana";
        /*
        StringBuilder ss = new StringBuilder();
        long lenn = 5000000;
        char[]gen = {'A','B','C','D'};
        for(int i = 0; i < lenn; i++)
            ss.append(gen[(int) (Math.random() * 4)]);
         */
        SuffixArray s = SuffixArray.buildSuffixArray(ss.toLowerCase());


        long n = s.getLength();
        try(PrintWriter writer = new PrintWriter("PROVA.txt")){
            for(int i=0 ; i<n ; i++) writer.println( "LCP ARRAY:" + s.getLcp()[i] + " SUFFISSO:" +  s.getText().substring(s.getSaIndex()[i]));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean numberElementSATest(SuffixArray sa){
        return sa.getLength() == sa.getText().length();
    }
}