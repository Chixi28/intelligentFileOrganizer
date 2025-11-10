package objectsforsorting;

import commandhandling.DocumentHandling;

/**
 * This class deals with all the aspects of a label.
 * @author ushug
 */
public class Labels extends Tags {
    /**
     * This public constructor creates a label.
     * @param name name of the tag
     */
    public Labels(String name) {
        if (labelTagsWithSymbolsCheck(name) && DocumentHandling.docToBeAdded && !ifLength(name) && !ifGenre(name)) {
            nameOfTag = name;
            isLabel = true;
        } else {
            if (DocumentHandling.errorStatement.isEmpty()) {
                DocumentHandling.errorStatement = StringsFinalRequired.ERRORSTART + (!labelTagsWithSymbolsCheck(name)
                        ? StringsFinalRequired.LABEL_HAS_SYMBOLS : ifLength(name) ? StringsFinalRequired.NAME_OF_THE_LENGTH
                        : StringsFinalRequired.NAME_IS_GENRE);
            }
            DocumentHandling.docToBeAdded = false;
        }
    }
    private static boolean labelTagsWithSymbolsCheck(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (name.toLowerCase().charAt(i) < StringsFinalRequired.ALPHABET_A || name.toLowerCase().charAt(i)
                    > StringsFinalRequired.ALPHABET_Z) {
                return false;
            }
        }
        return true;
    }
    private static boolean ifLength(String name) {
        if (name.equalsIgnoreCase(StringsFinalRequired.LENGTH) || name.equalsIgnoreCase(StringsFinalRequired.TEXT_LENGTH) || name.
                equalsIgnoreCase(StringsFinalRequired.AUDIO_LENGTH) || name.equalsIgnoreCase(StringsFinalRequired.VIDEO_LENGTH)
                || name.equalsIgnoreCase(StringsFinalRequired.SIZE) || name.equalsIgnoreCase(StringsFinalRequired.IMAGE_SIZE)) {
            return true;
        }
        return false;
    }
    private static boolean ifGenre(String name) {
        if (name.equalsIgnoreCase(StringsFinalRequired.GENRE) || name.equalsIgnoreCase(StringsFinalRequired.AUDIO_GENRE)
                || name.equalsIgnoreCase(StringsFinalRequired.TEXTGENRE) || name.equalsIgnoreCase(StringsFinalRequired.VIDEOGENRE)) {
            return true;
        }
        return false;
    }
}
