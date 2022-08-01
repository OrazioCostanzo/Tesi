public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        char[] gen = {'A','B','C','D'};
        int loop = 100000;
        for(int i = 0; i < loop; i++){
            sb.append (gen[(int) (Math.random() * gen.length)]) ;
        }
        SuffixArray s = SuffixArrayBuilder.buildSuffixArray("bnanaaanana");
        s.printSA();



    }
}