public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        char[] gen = {'A','B','C','D'};
        int loop = 100000;
        for(int i = 0; i < loop; i++){
            sb.append (gen[(int) (Math.random() * gen.length)]) ;
        }
        SuffixArray s = SuffixArrayBuilder.buildSuffixArray(sb.toString());

        System.out.println(numberElementSATest(s));



    }
    public static boolean numberElementSATest(SuffixArray sa){
        if(sa.sa().length == sa.text().length() ) return true;
        return false;
    }
}