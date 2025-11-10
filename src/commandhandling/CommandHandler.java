package commandhandling;
/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class handles the user input and executes the commands.
 * 
 * @author Programmieren-Team
 */
public final class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = " +";
    private static final String ERROR_PREFIX = "ERROR: ";
    private static final String COMMAND_NOT_FOUND_FORMAT = ERROR_PREFIX  + "Command '%s' not found%n";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final String LOAD_COMMAND = "load";
    private static final String ADD_DOCS = "add";
    private static final String CHANGE_COMMAND = "change";
    private static final String RUN_COMMAND = "run";
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler.
     *
     */
    public CommandHandler() {
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Starts the interaction with the user.
     * @throws RuntimeException when the file is not found
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Quits the interaction with the user.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) throws FileNotFoundException {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);
        executeCommand(commandName, commandArguments);
    }

    private void executeCommand(String commandName, String[] commandArguments) throws FileNotFoundException {
        String[] commandAndArguments;
        commandAndArguments = new String[commandArguments.length + 1];
        commandAndArguments[0] = commandName;
        for (int i = 0; i < commandArguments.length; i++) {
            commandAndArguments[i + 1] = commandArguments[i];
        }
        if (!commands.containsKey(commandName)) {
            System.err.print(COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
        } else {
            CommandResult result = commands.get(commandName).execute(commandAndArguments);
            switch (result.getType()) {
                case FAILURE:
                case SUCCESS:
                default:
            }
        }
    }
    private void initCommands() {
        this.addCommand(QUIT_COMMAND_NAME, new QuitCommand(this));
        this.addCommand(LOAD_COMMAND, new DocumentHandling());
        this.addCommand(CHANGE_COMMAND, new DocumentHandling());
        this.addCommand(ADD_DOCS, new DocumentHandling());
        this.addCommand(RUN_COMMAND, new RunCommand());
    }
    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }
}
