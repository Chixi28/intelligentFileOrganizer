package objectsforsorting;
import commandhandling.DocumentHandling;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the aspects of a document.
 * @author ushug
 */
public class Document extends Files {
    /**
     * If the doc is printed, it puts true otherwise false.
     */
    public boolean docPrinted = false;
    /**
     * The number of times each document was accessed.
     */
    public int accessNumber;
    /**
     * All the tags present in a document.
     */
    public List<Tags> tags = new ArrayList<>();
    /**
     * The directory of the document.
     */
    private String directory;
    /**
     * The type of document.
     */
    private TypesOfDocs docType;

    /**
     * Public constructor to create a Document.
     * @param directory Directory of the document
     * @param docType Type of the document.
     * @param accessNumber The number of times the document was accessed
     * @param inputtedTags All the tags present in the document
     */
    public Document(String directory, TypesOfDocs docType, String accessNumber, ArrayList<ArrayList<String>> inputtedTags) {
        try {
            if (docType != null && tagsValueCheck(inputtedTags) && (Integer.parseInt(accessNumber) > 0)
                    && !directorySpaceCheck(directory)) {
                this.directory = directory;
                this.accessNumber = Integer.parseInt(accessNumber);
                this.docType = docType;
                for (ArrayList<String> tag : inputtedTags) {
                    if (DocumentHandling.docToBeAdded) {
                        if (tag.size() == 1) {
                            tagsCreation(tag.get(0));
                        } else {
                            tagsCreation(tag.get(0), tag.get(1));
                        }
                    }
                }
                if (!ifExecutablePresent() && docType.equals(TypesOfDocs.PROGRAM)) {
                    programTagAdd();
                }
            } else {
                errorHandlingDocument(inputtedTags, accessNumber);
            }
        } catch (NumberFormatException e) {
            exceptionHandlingDocument();
        }

    }
    private static void errorHandlingDocument(ArrayList<ArrayList<String>> inputtedTags, String accessNumber) {
        if (DocumentHandling.errorStatement.isEmpty()) {
            DocumentHandling.errorStatement = StringsFinalRequired.ERRORSTART + (!tagsValueCheck(inputtedTags)
                    ? StringsFinalRequired.ERROR_SAME_TAG_DIFFERENT_TYPE
                    : !(Integer.parseInt(accessNumber) > 0) ? StringsFinalRequired.ERROR_ACCESS_NUMBER_LESS
                    : StringsFinalRequired.ERROR_DIRECTORY_HAS_SPACES);
        }
        DocumentHandling.docToBeAdded = false;
    }
    private static void exceptionHandlingDocument() {
        DocumentHandling.docToBeAdded = false;
        if (DocumentHandling.errorStatement.isEmpty()) {
            DocumentHandling.errorStatement = StringsFinalRequired.ERROR_ACCESS_NOT_A_NUM;
        }
    }
    /**
     * Gives the directory of the particular document.
     * @return String which contains the directory of the document.
     */
    public String getDirectory() {
        return directory;
    }
    private static boolean tagsValueCheck(ArrayList<ArrayList<String>> inputtedTags) {
        if (DocumentsInfo.tagsPresentInDoc.isEmpty()) {
            for (ArrayList<String> tag : inputtedTags) {
                DocumentsInfo.tagsPresentInDoc.add(tag);
            }
            return true;
        } else {
            for (int i = 0; i < inputtedTags.size(); i++) {
                for (int j = 0; j < DocumentsInfo.tagsPresentInDoc.size(); j++) {
                    if (inputtedTags.get(i).size() != DocumentsInfo.tagsPresentInDoc.get(j).size() && inputtedTags.get(i).get(0).
                            equalsIgnoreCase(DocumentsInfo.tagsPresentInDoc.get(j).get(0))) {
                        return false;
                    } else if (inputtedTags.get(i).size() == DocumentsInfo.tagsPresentInDoc.get(j).size() && DocumentsInfo.
                            tagsPresentInDoc.get(j).size() == 2 && inputtedTags.get(i).get(0).equalsIgnoreCase(DocumentsInfo.
                            tagsPresentInDoc.get(j).get(0)) && differentMultiValueCheck(inputtedTags.get(i), DocumentsInfo.
                            tagsPresentInDoc.get(j))) {
                        return false;
                    }
                }
                DocumentsInfo.tagsPresentInDoc.add(inputtedTags.get(i));
            }
        }
        return true;
    }
    private static boolean differentMultiValueCheck(ArrayList<String> tag, ArrayList<String> tagToCompare) {
        if ((tag.get(1).charAt(0) >= StringsFinalRequired.ZERO && tag.get(1).charAt(0) <= StringsFinalRequired.NINE)
                && (tagToCompare.get(1).toLowerCase().charAt(0) >= StringsFinalRequired.ALPHABET_A && tagToCompare.get(1).
                toLowerCase().charAt(0) <= StringsFinalRequired.ALPHABET_Z) || (tagToCompare.get(1).charAt(0) >= StringsFinalRequired.ZERO
                && tagToCompare.get(1).charAt(0) <= StringsFinalRequired.NINE) && (tag.get(1).toLowerCase().charAt(0)
                >= StringsFinalRequired.ALPHABET_A && tag.get(1).toLowerCase().charAt(0) <= StringsFinalRequired.ALPHABET_Z)) {
            return true;
        }
        return false;
    }
    private void docTypeTag(MultivalueTags tag) {
        if (tag.isNumeric && docType.equals(TypesOfDocs.IMAGE) && tag.getName().equalsIgnoreCase(StringsFinalRequired.SIZE)) {
            tag.setName(StringsFinalRequired.IMAGE_SIZE);
            if (tag.valueNumericTag < StringsFinalRequired.IMAGE_SIZE_ICON) {
                tag.setValue(StringsFinalRequired.ICON);
            } else if (tag.valueNumericTag < StringsFinalRequired.IMAGE_SIZE_SMALL) {
                tag.setValue(StringsFinalRequired.SMALL);
            } else if (tag.valueNumericTag < StringsFinalRequired.IMAGE_SIZE_MEDIUM) {
                tag.setValue(StringsFinalRequired.MEDIUM);
            } else {
                tag.setValue(StringsFinalRequired.LARGE);
            }
        } else if (tag.isNumeric && docType.equals(TypesOfDocs.AUDIO) && tag.getName().equalsIgnoreCase(StringsFinalRequired.LENGTH)) {
            tag.setName(StringsFinalRequired.AUDIO_LENGTH);
            if (tag.valueNumericTag < StringsFinalRequired.AUDIO_LENGTH_SAMPLE) {
                tag.setValue(StringsFinalRequired.SAMPLE);
            } else if (tag.valueNumericTag < StringsFinalRequired.AUDIO_LENGTH_SHORT) {
                tag.setValue(StringsFinalRequired.SHORT);
            } else if (tag.valueNumericTag < StringsFinalRequired.AUDIO_LENGTH_NORMAL) {
                tag.setValue(StringsFinalRequired.NORMAL);
            } else {
                tag.setValue(StringsFinalRequired.LONG);
            }
        } else if (tag.isNumeric && docType.equals(TypesOfDocs.VIDEO) && tag.getName().equalsIgnoreCase(StringsFinalRequired.LENGTH)) {
            tag.setName(StringsFinalRequired.VIDEO_LENGTH);
            if (tag.valueNumericTag < StringsFinalRequired.VIDEO_LENGTH_CLIP) {
                tag.setValue(StringsFinalRequired.CLIP);
            } else if (tag.valueNumericTag < StringsFinalRequired.VIDEO_LENGTH_SHORT) {
                tag.setValue(StringsFinalRequired.SHORT);
            } else if (tag.valueNumericTag < StringsFinalRequired.VIDEO_LENGTH_MOVIE) {
                tag.setValue(StringsFinalRequired.MOVIE);
            } else {
                tag.setValue(StringsFinalRequired.LONG);
            }
        } else if (tag.isNumeric && docType.equals(TypesOfDocs.TEXT) && tag.getName().equalsIgnoreCase(StringsFinalRequired.WORD)) {
            tag.setName(StringsFinalRequired.TEXT_LENGTH);
            if (tag.valueNumericTag < StringsFinalRequired.TEXT_LENGTH_SHORT) {
                tag.setValue(StringsFinalRequired.SHORT);
            } else if (tag.valueNumericTag < StringsFinalRequired.TEXT_LENGTH_MEDIUM) {
                tag.setValue(StringsFinalRequired.MEDIUM);
            } else {
                tag.setValue(StringsFinalRequired.LONG);
            }
        }
    }
    private void genreTagChange(MultivalueTags tag) {
        if (docType.equals(TypesOfDocs.AUDIO)) {
            tag.setName(StringsFinalRequired.AUDIO_GENRE);
        } else if (docType.equals(TypesOfDocs.VIDEO)) {
            tag.setName(StringsFinalRequired.VIDEOGENRE);
        } else if (docType.equals(TypesOfDocs.TEXT)) {
            tag.setName(StringsFinalRequired.TEXTGENRE);
        }
    }
    private void tagsCreation(String name, String value) {
        MultivalueTags tagAdd = new MultivalueTags(name, value);
        if (DocumentHandling.docToBeAdded) {
            if (tagAdd.isNumeric) {
                docTypeTag(tagAdd);
                tagAdd.isNumeric = false;
            } else if (tagAdd.getName().equalsIgnoreCase(StringsFinalRequired.GENRE)) {
                genreTagChange(tagAdd);
            }
            tags.add(tagAdd);
        }
    }
    private void tagsCreation(String name) {
        Labels tagAdd = new Labels(name);
        tags.add(tagAdd);
    }
    private void programTagAdd() {
        if (docType.equals(TypesOfDocs.PROGRAM)) {
            Labels tagAdd = new Labels(StringsFinalRequired.EXECUTABLE);
            tags.add(tagAdd);
        }
    }
    private boolean ifExecutablePresent() {
        if (DocumentHandling.docToBeAdded) {
            for (Tags tags : tags) {
                if (tags.nameOfTag.equalsIgnoreCase(StringsFinalRequired.EXECUTABLE)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Checks if a tags is repeated more than 1 time in a document.
     * @param inputtedTags List of the tags present in the document.
     * @return True if the tag occurs more than once or otherwise false.
     */
    public static boolean tagCopyPresent(ArrayList<ArrayList<String>> inputtedTags) {
        List<String> tagsName = new ArrayList<>();
        int occurences = 0;
        for (ArrayList<String> tag : inputtedTags) {
            tagsName.add(tag.get(0));
        }
        for (int i = 0; i < tagsName.size(); i++) {
            for (int j = 0; j < tagsName.size(); j++) {
                if (tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.SIZE)
                        || tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.IMAGE_SIZE)) {
                    if (tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.SIZE)
                            || tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.IMAGE_SIZE)) {
                        occurences++;
                    }
                } else if (tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.LENGTH) || tagsName.get(i).equalsIgnoreCase(
                        StringsFinalRequired.AUDIO_LENGTH) || tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.VIDEO_LENGTH)
                        || tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.TEXT_LENGTH)) {
                    if (tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.LENGTH) || tagsName.get(j).equalsIgnoreCase(
                            StringsFinalRequired.AUDIO_LENGTH) || tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.VIDEO_LENGTH)
                            || tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.TEXT_LENGTH)) {
                        occurences++;
                    }
                } else if (tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.GENRE) || tagsName.get(i).equalsIgnoreCase(
                        StringsFinalRequired.AUDIO_GENRE) || tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.VIDEOGENRE)
                        || tagsName.get(i).equalsIgnoreCase(StringsFinalRequired.TEXTGENRE)) {
                    if (tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.GENRE) || tagsName.get(j).equalsIgnoreCase(
                            StringsFinalRequired.AUDIO_GENRE) || tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.VIDEOGENRE)
                            || tagsName.get(j).equalsIgnoreCase(StringsFinalRequired.TEXTGENRE)) {
                        occurences++;
                    }
                } else {
                    if (tagsName.get(i).equalsIgnoreCase(tagsName.get(j))) {
                        occurences++;
                    }
                }
            }
            if (occurences > 1) {
                return true;
            }
            occurences = 0;
        }
        return false;
    }
    private static boolean directorySpaceCheck(String directory) {
        if (directory.contains(StringsFinalRequired.SPACE)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the tags given are valid.
     * @param inputtedTags List of tags present in the document
     * @return True if the tags are valid otherwise false
     */
    public static boolean validTagName(ArrayList<ArrayList<String>> inputtedTags) {
        List<String> tagsName = new ArrayList<>();
        for (ArrayList<String> tag : inputtedTags) {
            tagsName.add(tag.get(0));
        }
        for (int i = 0; i < tagsName.size(); i++) {
            char ifAlphabet = tagsName.get(i).charAt(0);
            if ((ifAlphabet >= StringsFinalRequired.ALPHABET_A && ifAlphabet <= StringsFinalRequired.ALPHABET_Z)
                    || (ifAlphabet >= StringsFinalRequired.ALPHABET_CAP_A && ifAlphabet <= StringsFinalRequired.ALPHABET_CAP_Z)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks the validity of the tags.
     * @param inputtedTags List of tags present in the document
     * @return Error statement for the particular error.
     */
    public static String tagsValidity(ArrayList<ArrayList<String>> inputtedTags) {
        if (!validTagName(inputtedTags)) {
            DocumentHandling.docToBeAdded = false;
            return StringsFinalRequired.ERROR_DOC_TAG_INVALID;
        } else if (tagCopyPresent(inputtedTags)) {
            DocumentHandling.docToBeAdded = false;
            return StringsFinalRequired.ERROR_DOC_SAME_TAG_MORE_TIME;
        }
        return null;
    }

    /**
     * Checks if the name of the documents given unique.
     * @param inputtedDocs All the documents present in the text file
     * @return True if the names are unique otherwise false
     */
    public static boolean docsDirectoryCheck(ArrayList<Document> inputtedDocs) {
        List<String> nameOfTheDocs = new ArrayList<>();
        int occurence = 0;
        for (int i = 0; i < inputtedDocs.size(); i++) {
            nameOfTheDocs.add(inputtedDocs.get(i).directory);
        }
        for (int i = 0; i < nameOfTheDocs.size(); i++) {
            for (int j  = 0; j < nameOfTheDocs.size(); j++) {
                if (nameOfTheDocs.get(j).equals(nameOfTheDocs.get(i))) {
                    occurence++;
                }
            }
            if (occurence > 1) {
                return false;
            }
            occurence = 0;
        }
        return true;
    }

    /**
     * Resets the field docPrinted of all the documents to false.
     * @param documents List of all documents in the text file.
     */
    public static void documentPrintReset(ArrayList<Document> documents) {
        for (Document document : documents) {
            document.docPrinted = false;
        }
    }
}
