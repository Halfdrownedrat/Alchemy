// Seperate Class that is used to return strings for art or large textblocks

public class Textblocks {
    public static String GiveText(String what){
        switch (what) {
            case "Charles"-> {
                return """
                    Hi, Im Charles
                """;
            }
            case "Pot"->{
                return """
                Enter Up to 50 different ingredients to the pot.
                Leftovers can only be voidet at the moment, unlock newer tech/ wait for a newer game version to change that.
                Enter the Name and Amount to add them to the pot. Type "Brew" to finish the mixture.
                """;
            }
            default -> System.out.println("No String for that");
        }
        return null;

    }
}
