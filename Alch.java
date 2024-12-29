import java.util.HashMap; // https://www.w3schools.com/java/java_hashmap.asp
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
                OUT();
                menu(scanner);

            }
            case "1"->{
                System.out.println("Choose material");
                String material = scanner.nextLine();
                System.out.println("Choose action");
                String action = scanner.nextLine();
                Convert(material, action);
                menu(scanner);

            }
            case "2"->{
                City();
            }
            default -> {
                System.out.println("Wrong Input");
                menu(scanner);
            }

        }
    }

    // Read data from file and store it in hasmap for easier working
    public static void Setup(){
        ingredients.put("TEST", new int[5]);// Ininitalize stuff, setting value to 0

    }

    // Write Data to file to save it for the next session
    public static void Saving(){

    }

    // Print out ALL the info 
    public static void OUT(){
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

    // Convert states for ingrediants
    // Takes String Input for conversion
    public static void Convert(String action, String material){
        switch (action) {
            case "slice" -> {
                // Slice
                ingredients.get("TEST")[0] -= 2;
                ingredients.get("TEST")[1] += 1;
            }
            case "grind" -> {
                // Grind
                ingredients.get("TEST")[1] -= 2;
                ingredients.get("TEST")[2] += 1;
            }
            case "dis" -> {
                // Dissolve
                ingredients.get("TEST")[2] -= 2;
                ingredients.get("TEST")[3] += 1;
            }
            case "dest" -> {
                // Destill
                ingredients.get("TEST")[3] -= 2;
                ingredients.get("TEST")[4] += 1;
            }
            case "cryst" -> {
                // Crystallize
                ingredients.get("TEST")[4] -= 2;
                ingredients.get("TEST")[5] += 1;
            }
            default -> System.out.println("Wrong Action!!!");
        }
    }

    public static void City(){
        System.out.println("Welcome to the city, there is nothing yet here.");
    }

}