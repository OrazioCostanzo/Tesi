import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Text {

    private SuffixArray sa;
    private int text_length;
    private String text;


    public Text (String text){
        this.text = text;
        this.text_length = text.length();
        this.sa = SuffixArray.buildSuffixArray(text);
    }

    public Text (File path){

        if(path.exists()){
            try(BufferedReader in = new BufferedReader(new FileReader(path))){
                String line = "";
                StringBuilder text = new StringBuilder();
                do{
                    line = in.readLine();
                    if(line != null)
                        text.append(line);
                }while(line != null);

                this.text = text.toString();
                System.out.println("ECCOMI CON LA PROVA :" + this.text);
                this.text_length = text.length();
                this.sa = SuffixArray.buildSuffixArray(this.text);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            System.out.println("file " + path + " not found");
        }

    }


    public void printLRS(){
        System.out.println(this.sa.getLRSMap(this.sa.getLRS()));
    }

    public List<String> getLCSList(int n){
        return this.sa.getLCSList(n);
    }

    public long isSubstring(String string){
        return  this.sa.isSubstring(string);
    }
    public SuffixArray getSa() {
        return sa;
    }

    public List<String> getMaw(){
        return MinimalAbsentWords.getMawAsList(MinimalAbsentWords.buildMAW(this.sa));
    }
    public int getText_length() {
        return text_length;
    }

    public String getText() {
        return text;
    }

    public Map<String,Long> getWordFreLc(){
        String[] text_array = this.text.replaceAll("\\s+|\\W"," ").trim().toLowerCase().split("\\s+");
        return Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String,Long> getWordFre(){
        String[] text_array = this.text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


    public static void writeFile(Map<String,Long> map , String path) {
        File file = createFile(path);
        List<String> list = map.keySet().stream().sorted().collect(Collectors.mapping(key -> key + ":" + map.get(key), Collectors.toList()));

        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
             for(String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeFile(List<String> list , String path) {
        File file = createFile(path);

        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            for(String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getLRS(){
        Map <Integer,String> map = this.sa.getLRSMap(this.sa.getLRS());
        Set<Integer> keys = map.keySet();
        return keys.stream().map(map::get).toList(); // key -> map.get(key)
    }

    private static File createFile(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                System.out.println("file " + path + " already exists");
            }else if ( file.createNewFile()){
                System.out.println("file created");
            }else {
                System.out.println("file name exist");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }
}
