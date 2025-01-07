import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Arrays;
import java.util.HashMap; // https://www.w3schools.com/java/java_hashmap.asp
import java.util.Map;
import java.util.Scanner;


public class Alch{
    // Taking in the game values
    static Map<String, int[]> ingredients = new HashMap<>();
    static Map<String, int[]> aspects = new HashMap<>();

    // Aggregatszustände 
    // States Nrm, Slc, Pwd, dis, des, Cri == Normal, Sliced, Powddered, Dissolved, Destilled, Cristalline
    final static String[] states = {"Nrm", "Slc ", "Pwd ", "Dis ", "Des ", "Cri "}; // 
    int[] aspectValues;
    int capacity;


    public static void main(String[] args) {
        startUp();
        Scanner scanner = new Scanner(System.in);
        menu(scanner);
    }
    // Called before  the game actually does stuff  
    public static void startUp(){
        FileToHash("ingredients.csv", ingredients, 5);
        FileToHash("aspects.csv", aspects, 25);
    }
    
    // Main Menu that gets called everytime the type of action changes
    public static void menu(Scanner scanner){
        System.out.println("--------------------------------------------------------------------");
        System.out.println("View Inventory: 0, Convert Material: 1, Visit City: 2, Brew: 3, Nothing: 4, Exit: 5");
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
            case "3"->{
                Brew(scanner);
            }
            case "4"->{
                City(scanner);
            }
            case "5"->{
                break;
            }
            default -> {
                System.out.println("Wrong Input");
                menu(scanner);
            }

        }
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

    // Write Data to file to save it for the next session
    public static void Saving(){
    }

    // Print out ALL the info 
    // Fixed with Copilot
    public static void OUT_ALL() {
        clearScreen();
    
        // Define column widths
        int ingredientColumnWidth = 15; // Width for the ingredient name
        int stateColumnWidth = 8;      // Width for each state
    
        // Print header
        System.out.printf("%-" + ingredientColumnWidth + "s", "Ingredient");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%-" + stateColumnWidth + "s", states[i]);
        }
        System.out.println(); // New line
        System.out.println("-".repeat(ingredientColumnWidth + stateColumnWidth * 5)); // Separator line
    
        // Print table rows
        for (Map.Entry<String, int[]> entry : ingredients.entrySet()) {
            String ingredient = entry.getKey();
            int[] counts = entry.getValue();
    
            // Print the ingredient name with padding
            System.out.printf("%-" + ingredientColumnWidth + "s", ingredient);
    
            // Print counts with consistent column width
            for (int count : counts) {
                System.out.printf("%-" + stateColumnWidth + "s", "x" + count);
            }
            System.out.println(); 
        }
    }
    

    // print out a data about a single ingredient
    public static void OUT_SINGLE(String ingredient){
        clearScreen();
        int[] counts = ingredients.get(ingredient);
        System.out.println("Ingredient: " + ingredient);
        for (int i = 0; i < 4; i++) {
            System.out.println(states[i] + ingredient +" x" + counts[i]);
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
        clearScreen();
        System.out.println("Welcome to the city, there is nothing yet here.");
        System.out.println(GetTextblock2());
        menu(scanner);
    }

    public static void Brew(Scanner scanner){
        clearScreen();
        OUT_ALL();
        System.out.println("--------------------------------------------------------------------");
        System.out.println(GetTextblock());
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Enter the ingredient and the amount to sacrefice to the pot");
        String putIN = " ";
        Alch mainPot = new Alch(50);// Standart Pot and the only one in the current version
        while (true) {
            getPotData(mainPot);
            if (putIN.equals("exit")) {
                break;
            }
            putIN = scanner.nextLine();
            if (!ingredients.containsKey(putIN)) {
                System.out.println("Ingredient does not exist!");
                continue;
            }
            //
            // Need some better way to call the correct Ingredient
            // Currently only uses the starting resoure
            //
            ingredients.get(putIN)[0]--;
            mainPot.changeIngredient(putIN, 1);

        }
        menu(scanner);
    }

    public static void getPotData(Alch currentPot){
        System.out.println("You have those things in your pot:");
        System.out.println("Air, Fire, Earth, Water, Order, Entropy, Chaos, Flux\n" + Arrays.toString(currentPot.aspectValues));
    }
    public static boolean hasSpace(Alch currentPot){
        int occupied = 0;
        for (int i = 0; i < currentPot.aspectValues.length; i++) {
            occupied += currentPot.aspectValues[i];
        }
        return occupied < currentPot.capacity;

    }

    // Creates an Object of the brewing pot, maily doing it this way to train for university
    public Alch(int Capacity){
        //Air, Fire, Earth, Water, Order, Entropy, Chaos, Flux
        capacity = Capacity;
        aspectValues = new int[5];
    }

    // Checks if the brewing pot contains enough of the wanted aspects
    public boolean isEnough(Alch currentPot){
        for (int i = 0; i < currentPot.aspectValues.length; i++) {
            if (this.aspectValues[i] < currentPot.aspectValues[i]) {
                return false;
            }
        }
        return true;
    }

    // Material is from the aspects and ingredients hash
    // action is -1 for subtraction, 1 for addition, > |1| for multiplieng
    public void changeIngredient(String material, int action){
        ingredients.get(material)[0]--;
        for (int i = 0; i < this.aspectValues.length; i++) {
            if (!hasSpace(this)) {
                System.out.println("Cauldren is full");
                break;
            }
            this.aspectValues[i]+= action * aspects.get(material)[i];

        }
    }

    // The Text Block Stuff is fun and usefull but blocks to much space
    private static String GetTextblock(){
        return """
                Enter Up to 999 different ingredients to the pot.
                Leftovers can only be voidet at the moment, unlock newer tech/ wait for a newer game version to change that.
                Enter the Name and Amount to add them to the pot. Type "Brew" to finish the mixture.
                """;
    }

    private  static String GetTextblock2(){
        return """
                                _____                    _____            _____                    _____                   _______                   _____          
                    /\\    \\                  /\\    \\          /\\    \\                  /\\    \\                 /::\\    \\                 /\\    \\         
                    /::\\    \\                /::\\____\\        /::\\    \\                /::\\____\\               /::::\\    \\               /::\\    \\        
                /::::\\    \\              /:::/    /       /::::\\    \\              /:::/    /              /::::::\\    \\             /::::\\    \\       
                /::::::\\    \\            /:::/    /       /::::::\\    \\            /:::/    /              /::::::::\\    \\           /::::::\\    \\      
                /:::/\\:::\\    \\          /:::/    /       /:::/\\:::\\    \\          /:::/    /              /:::/~~\\:::\\    \\         /:::/\\:::\\    \\     
                /:::/__\\:::\\    \\        /:::/    /       /:::/  \\:::\\    \\        /:::/____/              /:::/    \\:::\\    \\       /:::/__\\:::\\    \\    
            /::::\\   \\:::\\    \\      /:::/    /       /:::/    \\:::\\    \\      /::::\\    \\             /:::/    / \\:::\\    \\      \\:::\\   \\:::\\    \\   
            /::::::\\   \\:::\\    \\    /:::/    /       /:::/    / \\:::\\    \\    /::::::\\    \\   _____   /:::/____/   \\:::\\____\\   ___\\:::\\   \\:::\\    \\  
            /:::/\\:::\\   \\:::\\    \\  /:::/    /       /:::/    /   \\:::\\    \\  /:::/\\:::\\    \\ /\\    \\ |:::|    |     |:::|    | /\\   \\:::\\   \\:::\\    \\ 
            /:::/  \\:::\\   \\:::\\____\\/:::/____/       /:::/____/     \\:::\\____\\/:::/  \\:::\\    /::\\____\\|:::|____|     |:::|    |/::\\   \\:::\\   \\:::\\____\\
            \\::/    \\:::\\  /:::/    /\\:::\\    \\       \\:::\\    \\      \\::/    /\\::/    \\:::\\  /:::/    / \\:::\\    \\   /:::/    / \\:::\\   \\:::\\   \\::/    /
            \\/____/ \\:::\\/:::/    /  \\:::\\    \\       \\:::\\    \\      \\/____/  \\/____/ \\:::\\/:::/    /   \\:::\\    \\ /:::/    /   \\:::\\   \\:::\\   \\/____/ 
                    \\::::::/    /    \\:::\\    \\       \\:::\\    \\                       \\::::::/    /     \\:::\\    /:::/    /     \\:::\\   \\:::\\    \\     
                    \\::::/    /      \\:::\\    \\       \\:::\\    \\                       \\::::/    /       \\:::\\__/:::/    /       \\:::\\   \\:::\\____\\    
                    /:::/    /        \\:::\\    \\       \\:::\\    \\                      /:::/    /         \\::::::::/    /         \\:::\\  /:::/    /    
                    /:::/    /          \\:::\\    \\       \\:::\\    \\                    /:::/    /           \\::::::/    /           \\:::\\/:::/    /     
                    /:::/    /            \\:::\\    \\       \\:::\\    \\                  /:::/    /             \\::::/    /             \\::::::/    /      
                    /:::/    /              \\:::\\____\\       \\:::\\____\\                /:::/    /               \\::/____/               \\::::/    /       
                    \\::/    /                \\::/    /        \\::/    /                \\::/    /                 ~~                      \\::/    /        
                    \\/____/                  \\/____/          \\/____/                  \\/____/                                           \\/____/         

                                                                                                 
                """;
    }

    // Clears the screen completly, does not work on windows (propably)
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}