/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */
package commandhandling;

/**
 * This Enum class represents the types that a result of a command can be.
 * 
 * @author Programmieren-Team
 */
public enum CommandResultType {
    
    /**
     * The command was executed successfully.
     */
    SUCCESS,
    
    /**
     * An error occured during processing the command.
     */
    FAILURE;
}
