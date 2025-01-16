import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Util {
    // Clears the screen completly, does not work on windows (propably)
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
    public static void clearLines(int lineAmount){
        while (lineAmount > 0) {
            lineAmount--;
            System.out.print("\033[F");  
        }
    }
    // Write Data to file to save it for the next session
    public static void Saving(){

    }
        // Read data from file and store it in hasmap for easier working
    // csv needs to be whitespace free
    public static void FileToHash(String file, Map<String, int[]> map, int valueAmount){
        File ing = new File(file);
        try (Scanner fileScanner = new Scanner(ing)) {
            fileScanner.nextLine(); // Skip the first line since it has comments in it
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String ingredient = (String) parts[0];
                int[] counts = new int[valueAmount];
                for (int i = 1; i < parts.length; i++) {
                    counts[i - 1] = Integer.parseInt(parts[i]);
                }
                map.put(ingredient, counts);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
