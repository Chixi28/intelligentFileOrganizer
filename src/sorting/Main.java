package sorting;
import commandhandling.CommandHandler;
import objectsforsorting.StringsFinalRequired;
/**
 * Main class which runs the program.
 * @author ushug
 */
public final class Main {
    private Main() {
    }
    /**
     * For the initialisation of the program.
     * @param args java and name of the program.
     */
    public static void main(String[] args) {
        System.out.println(StringsFinalRequired.INTRODUCTION);
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.handleUserInput();
    }
}
