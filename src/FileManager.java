import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileManager {

    public static Text read(File path){
        Text temp_text = new Text("");
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

                return new Text(text.toString());
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
        return temp_text;
    }

    public static String readText(File path){
        Text temp_text = new Text("");
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
    public static void writeNewFile(List<String> list , File file){
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            for (String s : list)
                out.write(s + "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
    public static void writeFile(List<String> list , String path) {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            for (String s : list)
                out.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
