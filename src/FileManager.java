import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class will be used to write, read or overwrite files.
 */
public class FileManager {

    /**
     * If path exists, it reads text from a file.
     * @param path file path..
     * @return returns a string containing the text in the file.
     */
    public static String readText(File path){
        if(path.exists()){
            try(BufferedReader in = new BufferedReader(new FileReader(path))){

                String line = "";
                StringBuilder text = new StringBuilder();

                do{
                    line = in.readLine();
                    if(line != null)
                        text.append(line);
                }
                while(line != null);

                return text.toString();
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            System.out.println("file " + path + " not found");
        }
        return "";
    }

    /**
     * creates a file in the path passed as a parameter.
     * @param path file path.
     * @return returns the file.
     */
    public static File createFile(String path) {
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

    /**
     * Takes a list and writes it to a new file with header.
     * @param list list to write to file.
     * @param file
     * @param header header to be written at the beginning of the file.
     */
    public static void writeNewFile(List<String> list , File file, String header){
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            out.write(header + "\n" + "\n");
            for (String s : list)
                out.write(s + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a list and writes it to a new file.
     * @param list list to write to file.
     * @param file
     */
    public static void writeNewFile(List<String> list , File file){
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            for (String s : list)
                out.write(s + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes a map and writes it to a new file.
     * @param map map to write to file.
     * @param file
     */
    public static void writeNewFile(Map<String,Long> map , File file){
        List<String> list = map.keySet().stream().sorted().collect(Collectors.mapping(key -> key + ":" + map.get(key), Collectors.toList()));
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            for (String s : list)
                out.write(s + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a list and writes it to a new file with header.
     * @param map map to write to file.
     * @param file
     * @param header header to be written at the beginning of the file.
     */
    public static void writeNewFile(Map<String,Long> map , File file, String header) {
        List<String> list = map.keySet().stream().sorted().collect(Collectors.mapping(key -> key + ":" + map.get(key), Collectors.toList()));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(header + "\n" + "\n");
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a list and writes it to a file (overwriting) with header.
     * @param list list to write to file.
     * @param path
     * @param header header to be written at the beginning of the file.
     */
    public static void writeFile(List<String> list , String path, String header) {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(header + "\n" + "\n");
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a list and writes it to a file (overwriting).
     * @param list list to write to file.
     * @param path
     */
    public static void writeFile(List<String> list , String path) {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a map and writes it to a file (overwriting) with header.
     * @param map map to write to file.
     * @param path
     * @param header header to be written at the beginning of the file.
     */
    public static void writeFile(Map<String,Long> map , String path, String header) {
        File file = new File(path);
        List<String> list = map.keySet().stream().sorted().collect(Collectors.mapping(key -> key + ":" + map.get(key), Collectors.toList()));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(header + "\n" + "\n");
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Takes a list and writes it to a file (overwriting).
     * @param map map to write to file.
     * @param path
     */
    public static void writeFile(Map<String,Long> map , String path) {
        File file = new File(path);
        List<String> list = map.keySet().stream().sorted().collect(Collectors.mapping(key -> key + ":" + map.get(key), Collectors.toList()));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
