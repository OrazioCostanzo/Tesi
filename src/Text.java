import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Text {
    private int text_length;
    private String text;
    private String[] text_array;
    private String[] no_char_array;


    public Text (String text){
        this.text = text;
        this.text_length = text.length();
        this.text_array = this.text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        this.no_char_array = this.text.replaceAll("\\w|\\d"," ").trim().split("\\s+");
    }
    public Text (File path){
        Text text  = FileManager.read(path);
        this.text = text.getText();
        this.text_length = text.getText_length();
        this.text_array = text.getText_array();
        this.no_char_array = text.getNo_char_array();
    }

    public String[] getNo_char_array(){
        return this.no_char_array;
    }
    public String[] getText_array() {
        return this.text_array;
    }


    public boolean isSubstring(String string){
        SuffixArray sa = SuffixArray.buildSuffixArray(this.text);
        return  sa.isSubstring(string) >= 0;
    }

    public int getText_length() {
        return text_length;
    }

    public String getText() {
        return text;
    }

    public Map<String,Long> getWordFre(){
        return Arrays.stream(this.text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String,Long> getWordFreLc(){
        return Arrays.stream(Arrays.stream(this.text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    public Map<String,Long> getWordFreUc(){;
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
    public Map<String,Long> getNoWordChar(){
        return Arrays.stream(this.no_char_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
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



    public static String readFile(File path){
        return FileManager.readText(path);
    }
    public static void writeNewFile(Map<String,Long> map , String path, String header) {
       FileManager.writeNewFile(map, FileManager.createFile(path), header);
    }
    public static void writeNewFile(Map<String,Long> map , String path) {
        FileManager.writeNewFile(map, FileManager.createFile(path));
    }

    public static void writeNewFile(List<String> list , String path, String header) {
        FileManager.writeNewFile(list, FileManager.createFile(path), header);
    }
    public static void writeNewFile(List<String> list , String path) {
        FileManager.writeNewFile(list, FileManager.createFile(path));
    }
    public static void writeFile(List<String> list , String path, String header) {
        FileManager.writeFile(list, path, header);
    }
    public static void writeFile(List<String> list , String path) {
        FileManager.writeFile(list, path);
    }
    public static void writeFile(Map<String,Long> map , String path, String header) {
        FileManager.writeFile(map, path, header);
    }
    public static void writeFile(Map<String,Long> map , String path) {
        FileManager.writeFile(map, path);
    }


    public static List<String> getStrFre(String str, String search){
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        Arrays.stream(sa.getSa()).forEach(System.out::println);
        return  IntStream.of(sa.getStrFre(search)).boxed().map(String::valueOf).toList();
    }

    public static List<String> getAlphabetText(String str){
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        return Arrays.stream(SuffixArray.getAlphabet(sa)).map(String::valueOf).toList();
    }

    public static List<String> getLCS(int k, String... strings){
        return SuffixArray.buildSuffixArray(strings).getLCSList(k);
    }

    public static List<String> getMaw(String str){
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        return MinimalAbsentWords.getMawAsList(MinimalAbsentWords.buildMAW(sa));
    }
    public static List<String> getLRS(String str){
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        Map <Integer,String> map = sa.getLRSMap(sa.getLRS());
        Set<Integer> keys = map.keySet();
        return keys.stream().map(map::get).toList();
    }

}
