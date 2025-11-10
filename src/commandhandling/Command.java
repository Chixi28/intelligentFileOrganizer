package commandhandling;
/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

import java.io.FileNotFoundException;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 */
public interface Command {
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     * @throws java.io.FileNotFoundException when the file is not found
     */
    CommandResult execute(String[] commandArguments) throws FileNotFoundException;
}
