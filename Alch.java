import java.io.File;
import java.io.FileNotFoundException; // https://www.w3schools.com/java/java_hashmap.asp
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Alch{
    static Map<String, int[]> ingredients = new HashMap<>();
    final static String[] states = {"", "Sliced ", "powder", "solution", "destillat", "Crystalline "}; // 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Setup();
        menu(scanner);
    }
    public static void menu(Scanner scanner){
        System.out.println("View Inventory: 0, Convert Material: 1, Visit City: 2, Get Help");
        String dec = scanner.nextLine();
        switch (dec) {
            case "0"-> {
                OUT_ALL();
                menu(scanner);

            }
            case "1"->{
                System.out.println("Choose material");
                String material = scanner.nextLine();
                System.out.println("Choose action\n Actions: slice, grind, dissolve, destill, crystelize");
                String action = scanner.nextLine();
                Convert(action, material);
                menu(scanner);

            }
            case "2"->{
                City(scanner);
            }
            default -> {
                System.out.println("Wrong Input");
                menu(scanner);
            }

        }
    }

    // Read data from file and store it in hasmap for easier working
    // csv needs to be whitespace free
    public static void Setup(){
        File ing = new File("ingredients.csv");
        try (Scanner fileScanner = new Scanner(ing)) {
            while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            String ingredient = (String) parts[0];
            int[] counts = new int[5];
            for (int i = 1; i < parts.length; i++) {
                counts[i - 1] = Integer.parseInt(parts[i]);
            }
            ingredients.put(ingredient, counts);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // Write Data to file to save it for the next session
    public static void Saving(){

    }

    // Print out ALL the info 
    public static void OUT_ALL(){
        for (Map.Entry<String, int[]> entry : ingredients.entrySet()) {
            String ingredient = entry.getKey();
            int[] counts = entry.getValue();
            System.out.println("Ingredient: " + ingredient);
            for (int i = 0; i < 4; i++) {
                System.out.println("State " + states[i] + " count: " + counts[i]);
            }
            System.out.println();
        }
    }

    public static void OUT_SINGLE(String ingridiant){
        int[] counts = ingredients.get(ingridiant);
        System.out.println("Ingredient: " + ingridiant);
        for (int i = 0; i < 4; i++) {
            System.out.println("State " + states[i] + " count: " + counts[i]);
        }
        System.out.println();
    }

    // Convert states for ingrediants
    // Takes String Input for conversion
    public static void Convert(String action, String material){
        action = action.trim().toLowerCase(); // Removing accidental whitespaces and making everythin even
        material = material.trim(); // Can not be set to all lowercase since I store them like this: Material
        if (!ingredients.containsKey(material)) {
            System.out.println("Material not found: " + material);
            return;
        }
        switch (action) {
            case "slice" -> {
                // Slice
                ingredients.get(material)[0] -= 2;
                ingredients.get(material)[1] += 1;
            }
            case "grind" -> {
                // Grind
                ingredients.get(material)[1] -= 2;
                ingredients.get(material)[2] += 1;
            }
            case "dissolve" -> {
                // Dissolve
                ingredients.get(material)[2] -= 2;
                ingredients.get(material)[3] += 1;
            }
            case "destill" -> {
                // Destill
                ingredients.get(material)[3] -= 2;
                ingredients.get(material)[4] += 1;
            }
            case "crystelize" -> {
                // Crystallize
                ingredients.get(material)[4] -= 2;
                ingredients.get(material)[5] += 1;
            }
            default -> System.out.println("Wrong Action!!!");
        }
        OUT_SINGLE(material);
    }

    public static void City(Scanner scanner){
        System.out.println("Welcome to the city, there is nothing yet here.");
        menu(scanner);
    }

}