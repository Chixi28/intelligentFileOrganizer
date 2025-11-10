/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package commandhandling;

/**
 * This command quits a {@link CommandHandler command handler}.
 *
 * @author Programmieren-Team
 */
final class QuitCommand implements Command {

    private static final String QUIT_WITH_ARGUMENTS_ERROR = "quit does not allow args!";
    
    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     * 
     * @param commandHandler the command handler to be quitted
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(String[] commandArguments) {
        if (commandArguments.length != 1) {
            return new CommandResult(CommandResultType.FAILURE, QUIT_WITH_ARGUMENTS_ERROR);
        }
        
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
    }
}
