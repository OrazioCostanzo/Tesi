import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Text {

    private SuffixArray sa;
    private int text_length;
    private String text;
    String[] text_array;


    public Text (String text){
        this.text = text;
        SuffixArray.setFilter("");
        this.text_length = text.length();
        this.sa = SuffixArray.buildSuffixArray(text);
        this.text_array = this.text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
    }

    public Text (File path){
        Text text  = FileManager.read(path);
        this.sa = text.getSa();
        this.text = text.getText();
        this.text_length = text.getText_length();
        this.text_array = text.getText_array();
    }

    public String[] getText_array() {
        return text_array;
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

    public Map<String,Long> getWordFre(){
        //String[] text_array = this.text.replaceAll("\\s+|\\W"," ").trim().toLowerCase().split("\\s+");
        return Arrays.stream(this.text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String,Long> getWordFreLc(){
        //String[] text_array = this.text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    public Map<String,Long> getWordFreUc(){
        //String[] text_array = this.text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public List<String> getMinWordFre(){
        Map<String,Long>  map = Arrays.stream(this.text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    public List<String> getMinMaxWordFreLc(){
        Map<String,Long>  map = Arrays.stream(Arrays.stream(this.text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }

    public List<String> getMinWordFreUc(){
        Map<String,Long>  map = Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }

    public List<String> getMaxWordFre(){
        Map<String,Long>  map = Arrays.stream(this.text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }

    public List<String> getMaxWordFreLc(){
        Map<String,Long>  map = Arrays.stream(Arrays.stream(this.text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }

    public List<String> getMaxWordFreUc(){
        Map<String,Long>  map = Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }

    public List<String> getWordStartWith(String sub){
        return Arrays.stream(this.text_array).filter(str -> str.startsWith(sub)).toList();
    }
    public List<String> getWordStartWithUc(String sub){
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.startsWith(sub)).toList();
    }
    public List<String> getWordStartWithLc(String sub){
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toLowerCase).toArray(String[]::new)).filter(str -> str.startsWith(sub)).toList();
    }
    public List<String> getWordEndWith(String sub){
        return Arrays.stream(this.text_array).filter(str -> str.endsWith(sub)).toList();
    }
    public List<String> getWordEndWithLc(String sub){
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.endsWith(sub)).toList();
    }
    public List<String> getWordEndWithUc(String sub){
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.endsWith(sub)).toList();
    }


    public static void writeFile(Map<String,Long> map , String path, String header) {
       FileManager.write(map, FileManager.createFile(path), header);
    }
    public static void writeFile(Map<String,Long> map , String path) {
        FileManager.write(map, FileManager.createFile(path));
    }


    public static void writeFile(List<String> list , String path, String header) {
        FileManager.write(list, FileManager.createFile(path), header);
    }
    public static void writeFile(List<String> list , String path) {
        FileManager.write(list, FileManager.createFile(path));
    }

    public List<String> getLRS(){
        Map <Integer,String> map = this.sa.getLRSMap(this.sa.getLRS());
        Set<Integer> keys = map.keySet();
        return keys.stream().map(map::get).toList(); // key -> map.get(key)
    }

    public List<String> getStrFre(String str){
        return  IntStream.of(this.sa.getStrFre(str)).boxed().map(String::valueOf).toList();
    }
}
