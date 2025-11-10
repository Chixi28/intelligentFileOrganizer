package objectsforsorting;

import commandhandling.DocumentHandling;

/**
 * This class deals with all the aspects of the multi-value tags.
 * @author ushug
 */
public class MultivalueTags extends Tags {
    /**
     * Value of the numeric Tag.
     */
    public int valueNumericTag;
    /**
     * Value of the multi-value Tag.
     */
    protected String value;
    private String name;
    /**
     * Public constructor to create a multi-value Tag.
     * @param name name of the tag.
     * @param value value of the tag.
     */
    public MultivalueTags(String name, String value) {
        if (Character.isAlphabetic(name.charAt(0)) && multiValueTagsWithSymbolsCheck(name)
                && !ifExecutableWithValue(name, value)) {
            this.name = name.toLowerCase();
            nameOfTag = name.toLowerCase();
            valueAssignment(value);
        } else {
            if (DocumentHandling.errorStatement.isEmpty()) {
                DocumentHandling.errorStatement = StringsFinalRequired.ERRORSTART + (!Character.isAlphabetic(name.charAt(0))
                        ? StringsFinalRequired.ERROR_FIRST_CHARACTER : !multiValueTagsWithSymbolsCheck(name) ? StringsFinalRequired.
                        ERROR_TAG_HAS_SYMBOLS : StringsFinalRequired.EXECUTABLE_HAS_VALUE);
            }
            DocumentHandling.docToBeAdded = false;
        }
    }
    private void valueAssignment(String value) {
        this.value = value;
        valuePresent = value;
        isMultiValue = true;
        try {
            if ((Integer) Integer.parseInt(value) instanceof Integer) {
                isNumeric = true;
                valueNumericTag = Integer.parseInt(value);
                numericVal = Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            isNumeric = false;
        }
    }
    /**
     * Setter to set the name of the tag.
     * @param name name of the tag.
     */
    protected void setName(String name) {
        this.name = name;
        nameOfTag = name;
    }
    /**
     * Setter to set the value of the tag.
     * @param value value of the tag.
     */

    protected void setValue(String value) {
        this.value = value;
        valuePresent = value;
    }
    /**
     * Getter to get the name of the tag.
     * @return name of the tag.
     */
    public String getName() {
        return name;
    }
    private static boolean multiValueTagsWithSymbolsCheck(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (name.toLowerCase().charAt(i) < StringsFinalRequired.ALPHABET_A || name.toLowerCase().charAt(i)
                    > StringsFinalRequired.ALPHABET_Z) {
                return false;
            }
        }
        return true;
    }
    private static boolean ifExecutableWithValue(String name, String value) {
        if (name.equalsIgnoreCase(StringsFinalRequired.EXECUTABLE) && value != null) {
            return true;
        }
        return false;
    }
}
