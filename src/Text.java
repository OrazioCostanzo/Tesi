import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class that uses methods for "pattern matching" via suffix arrays and/or streams.
 */
public class Text {

    /**
     * Checks if a string is a substring of a text.
     * @param string substring to search.
     * @param text Text on which to search for substring.
     * @return true if exists, false otherwise.
     */
    public static boolean isSubstring(String string, String text){
        SuffixArray sa = SuffixArray.buildSuffixArray(text);
        return  sa.isSubstring(string) >= 0;
    }

    /**
     * Returns a map where for each word is associated the number of times it is repeated in the text.
     * @param text
     * @return map with the search result.
     */
    public static Map<String,Long> getWordFre(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    /**
     * Returns a map where for each word is associated the number of times it is repeated in the text (lowercase).
     * @param text
     * @return map with the search result.
     */
    public static Map<String,Long> getWordFreLc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Returns a map where for each word is associated the number of times it is repeated in the text (uppercase).
     * @param text
     * @return map with the search result.
     */
    public static Map<String,Long> getWordFreUc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Returns a list with the least frequent words in the text.
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMinWordFre(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    /**
     * Returns a list with the least frequent words in the text(lowercase).
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMinWordFreLc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(Arrays.stream(text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    /**
     * Returns a list with the least frequent words in the text(uppercase).
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMinWordFreUc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().min(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    /**
     * Returns a list with the most frequent words in the text.
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMaxWordFre(String  text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    /**
     * Returns a list with the most frequent words in the text(lowercase).
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMaxWordFreLc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(Arrays.stream(text_array).map(String::toLowerCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    /**
     * Returns a list with the most frequent words in the text(uppercase).
     * @param text
     * @return list with the search result.
     */
    public static List<String> getMaxWordFreUc(String text){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        Map<String,Long>  map = Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicLong m = new AtomicLong(map.values().stream().max(Comparator.naturalOrder()).get());
        return map.keySet().stream().map(key -> map.get(key).compareTo(m.get()) == 0 ? key + ":" + map.get(key) : "").filter(element -> !element.equals("")).toList();
    }
    public static Map<String,Long> getNoWord(String text){
        String[] text_array = text.replaceAll("\\w|\\d"," ").trim().split("\\s+");
        return Arrays.stream(text_array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    /**
     * Returns a list with words starting with "sub".
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordStartWith(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(text_array).filter(str -> str.startsWith(sub)).distinct().toList();
    }
    /**
     * Returns a list with words starting with "sub" (uppercase).
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordStartWithUc(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.startsWith(sub)).toList();
    }
    /**
     * Returns a list with words starting with "sub" (lowercase).
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordStartWithLc(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toLowerCase).toArray(String[]::new)).filter(str -> str.startsWith(sub)).toList();
    }
    /**
     * Returns a list with words ending with "sub".
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordEndWith(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(text_array).filter(str -> str.endsWith(sub)).toList();
    }
    /**
     * Returns a list with words ending with "sub" (lowercase).
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordEndWithLc(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.endsWith(sub)).toList();
    }
    /**
     * Returns a list with words ending with "sub" (uppercase).
     * @param text
     * @param sub words that are searched for in the text begin with "sub".
     * @return list with the search result.
     */
    public static List<String> getWordEndWithUc(String text, String sub){
        String[] text_array = text.replaceAll("\\s+|\\W"," ").trim().split("\\s+");
        return Arrays.stream(Arrays.stream(text_array).map(String::toUpperCase).toArray(String[]::new)).filter(str -> str.endsWith(sub)).toList();
    }

    /**
     * Reads text in a file.
     * @param path the path to the file to read.
     * @return returns the text read in the file.
     */
    public static String readFile(File path){
        return FileManager.readText(path);
    }

    /**
     * Writes the header followed by the map content passed as parameter to a new file.
     * @param map
     * @param path
     * @param header
     */
    public static void writeNewFile(Map<String,Long> map , String path, String header) {
       FileManager.writeNewFile(map, FileManager.createFile(path), header);
    }
    /**
     * Writes the contents of the map passed as parameter to a new file.
     * @param map
     * @param path
     */
    public static void writeNewFile(Map<String,Long> map , String path) {
        FileManager.writeNewFile(map, FileManager.createFile(path));
    }
    /**
     * Writes the header followed by the list content passed as parameter to a new file.
     * @param list
     * @param path
     * @param header
     */
    public static void writeNewFile(List<String> list , String path, String header) {
        FileManager.writeNewFile(list, FileManager.createFile(path), header);
    }
    /**
     * Writes the contents of the list passed as parameter to a new file.
     * @param list
     * @param path
     */
    public static void writeNewFile(List<String> list , String path) {
        FileManager.writeNewFile(list, FileManager.createFile(path));
    }
    /**
     * Writes the header followed by the contents of the list passed as a parameter to a file.
     * @param list
     * @param path
     * @param header
     */
    public static void writeFile(List<String> list , String path, String header) {
        FileManager.writeFile(list, path, header);
    }
    /**
     * Writes the contents of the list passed as parameter to a file.
     * @param list
     * @param path
     */
    public static void writeFile(List<String> list , String path) {
        FileManager.writeFile(list, path);
    }
    /**
     * Writes the header followed by the contents of the map passed as a parameter to a file.
     * @param map
     * @param path
     * @param header
     */
    public static void writeFile(Map<String,Long> map , String path, String header) {
        FileManager.writeFile(map, path, header);
    }
    /**
     * Writes the contents of the map passed as parameter to a file.
     * @param map
     * @param path
     */
    public static void writeFile(Map<String,Long> map , String path) {
        FileManager.writeFile(map, path);
    }

    /**
     * If it exists, it returns the frequency of a string in a text.
     * @param str text on which to build the suffix array and where the search will be done.string to search for.
     * @param search string to search for.
     * @return returns the frequency of the string.
     */
    public static long getStrFre(String str, String search){
        SuffixArray.setFilter("");
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        return  sa.getStrFre(search)[0];
    }

    /**
     * Returns the alphabet of the text passed as a parameter, that is, all the different letters that belong to the text.
     * @param str
     * @return list with the letters belonging to the text.
     */
    public static List<String> getAlphabetText(String str){
        SuffixArray.setFilter("");
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        return Arrays.stream(SuffixArray.getAlphabet(sa)).map(String::valueOf).toList();
    }

    /**
     * Constructs a list containing the longest common substrings among k strings.
     * @param k the number of strings we want to find LCS for.
     * @param strings Strings on which to build the suffix array.
     * @return The list with the LCS ( if exist).
     */
    public static List<String> getLCS(int k, String... strings){
        SuffixArray.setFilter("");
        return SuffixArray.buildSuffixArray(strings).getLCSList(k);
    }

    /**
     * Builds and returns a list with the MAWs of the string passed to the parameter.
     * @param str
     * @return Returns a list with the MAW of the string passed to the parameter.
     */
    public static List<String> getMaw(String str){
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        return MinimalAbsentWords.getMawAsList(MinimalAbsentWords.buildMAW(sa));
    }

    /**
     * Get the longest repeated substring.
     * @param str
     * @return returns a list that contains all the LCS.
     */
    public static List<String> getLRS(String str){
        SuffixArray.setFilter("");
        SuffixArray sa = SuffixArray.buildSuffixArray(str);
        Map <Integer,String> map = sa.getLRSMap(sa.getLRS());
        Set<Integer> keys = map.keySet();
        return keys.stream().map(map::get).toList();
    }

}
