import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // https://www.w3schools.com/java/java_hashmap.asp


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
        Util.FileToHash("ingredients.csv", ingredients, 5);
        Util.FileToHash("aspects.csv", aspects, 25);
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
                OUT_ALL();
                System.out.println("Choose material");
                String material = scanner.nextLine();
                System.out.println("Choose action\n Actions: slice, grind, dissolve, destill, crystelize");
                String action = scanner.nextLine();
                System.out.println("How much?");
                int amount = scanner.nextInt();
                Convert(action, material, amount);
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

    // Print out ALL the info 
    // Fixed with Copilot
    public static void OUT_ALL() {    
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
        Util.clearScreen();
        int[] counts = ingredients.get(ingredient);
        System.out.println("Ingredient: " + ingredient);
        for (int i = 0; i < 4; i++) {
            System.out.println(states[i] + ingredient +" x" + counts[i]);
        }
        System.out.println();
    }

    // Convert states for ingrediants
    // Takes String Input for conversion
    public static void Convert(String action, String material, int amount){
        action = action.trim().toLowerCase(); // Removing accidental whitespaces and making everythin even
        material = material.trim(); // Can not be set to all lowercase since I store them like this: Material
        if (!ingredients.containsKey(material)) {
            System.out.println("Material not found: " + material);
            return;
        }
        int minus = -2 * amount;
        int plus = 1 * amount;

        switch (action){
            case "slice" -> {
                // Slice
                if(ingredients.get(material)[0] + minus < 0) break;
                ingredients.get(material)[0] += minus;
                ingredients.get(material)[1] += plus;
            }
            case "grind" -> {
                // Grind
                if(ingredients.get(material)[1] + minus < 0) break;
                ingredients.get(material)[1] += minus;
                ingredients.get(material)[2] += plus;
            }
            case "dissolve" -> {
                // Dissolve
                if(ingredients.get(material)[2] + minus < 0) break;
                ingredients.get(material)[2] += minus;
                ingredients.get(material)[3] += plus;
            }
            case "destill" -> {
                // Destill
                if(ingredients.get(material)[3] + minus < 0) break;
                ingredients.get(material)[3] += minus;
                ingredients.get(material)[4] += plus;
            }
            case "crystelize" -> {
                // Crystallize
                if(ingredients.get(material)[4] + minus < 0) break;
                ingredients.get(material)[4] += minus;
                ingredients.get(material)[5] += plus;
            }
            default -> System.out.println("Wrong Action!!!");
        }
        OUT_SINGLE(material);
    }

    public static void City(Scanner scanner){
        Util.clearScreen();
        System.out.println("Welcome to the city, there is nothing yet here.");
        menu(scanner);
    }

    public static void Brew(Scanner scanner){
        Util.clearScreen();
        System.out.println("--------------------------------------------------------------------");
        System.out.println(Textblocks.GiveText("Pot"));
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Enter the ingredient and the amount to sacrefice to the pot");
        String putIN = " ";
        int amount;
        Alch mainPot = new Alch(50);// Standart Pot and the only one in the current version
        while (true) {
            OUT_ALL();
            getPotData(mainPot);
            if (putIN.equals("exit")) {
                break;
            }
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            putIN = parts[0];
            // Try Catch to set the amount to one if no amount is given
            try {
                amount = Integer.parseInt(parts[1]);
                amount = Math.abs(amount); // Just ignoring the minus and making it positive
                if (amount == 0) amount = 1; // Player is stupid, setting amount to 1
            } catch (Exception e) {
                amount = 1;
            }
            if (!ingredients.containsKey(putIN)) {
                System.out.println("Ingredient does not exist!");
                continue;
            }
            
            //
            // Need some better way to call the correct Ingredient
            // Currently only uses the starting resoure
            //
            if (ingredients.get(putIN)[0] - amount >= 0) {
                ingredients.get(putIN)[0] -= amount;
            }else{
                int missing = Math.abs(ingredients.get(putIN)[0] - amount);
                amount -= missing;
                System.out.println("You dont have that much: Putting in:" + amount);
            }
            mainPot.changeIngredient(putIN, 1, amount);

        }
        menu(scanner);
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

    public static void getPotData(Alch currentPot){
        System.out.println("You have those things in your pot:");
        System.out.println("Air, Fire, Earth, Water, Order, Entropy, Chaos, Flux\n" + Arrays.toString(currentPot.aspectValues));
    }
    public static boolean hasSpace(Alch currentPot){
        // Currently views all aspects as one mashed together value
        int occupied = 0;
        for (int i = 0; i < currentPot.aspectValues.length; i++) {
            occupied += currentPot.aspectValues[i];
        }
        return occupied < currentPot.capacity;

    }
    // Material is from the aspects and ingredients hash
    // action is -1 for subtraction, 1 for addition, > |1| for multiplieng
    public void changeIngredient(String material, int action, int amount){
        for (int i = 0; i < this.aspectValues.length; i++) {
            if (!hasSpace(this)) {
                System.out.println("Cauldren is full");
                break;
            }
            this.aspectValues[i]+= action * aspects.get(material)[i] * amount;
            // Can Create a small overflorw since it checks if there is space before an potentialy limitless amount of stuff is added
            // Solution is just calling the hasSpace Function again and removing overshoot
            if (!hasSpace(this)) {
                System.out.println("Cauldren would be overflorwing"); // Not visible since the clearScreen clears it immidiatly
                this.aspectValues[i]-= action * aspects.get(material)[i] * amount;
                break;
            }
        }
    }
}