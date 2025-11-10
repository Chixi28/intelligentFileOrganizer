package commandhandling;
import objectsforsorting.DocumentsInfo;
import objectsforsorting.StringsFinalRequired;
import objectsforsorting.TypesOfDocs;
import objectsforsorting.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * This class handles all the documents required for sorting.
 * @author ushug
 */
public class DocumentHandling implements Command {
    /**
     * A boolean which shows true when the documents are loaded.
     */
    public static boolean toLoadDocs = false;
    /**
     * A boolean which shows true when the document is supposed to be loaded.
     */
    public static boolean docToBeAdded = true;
    /**
     * Error statement for all the possible errors.
     */
    public static String errorStatement = StringsFinalRequired.EMPTYSTRING;
    private static TypesOfDocs stringToEnum(String typeOfDocString) {
        if (!missingDocType(typeOfDocString)) {
            if (typeOfDocString.equalsIgnoreCase(StringsFinalRequired.EMPTYSTRING + TypesOfDocs.AUDIO)) {
                return TypesOfDocs.AUDIO;
            } else if (typeOfDocString.equalsIgnoreCase(StringsFinalRequired.EMPTYSTRING + TypesOfDocs.IMAGE)) {
                return TypesOfDocs.IMAGE;
            } else if (typeOfDocString.equalsIgnoreCase(StringsFinalRequired.EMPTYSTRING + TypesOfDocs.PROGRAM)) {
                return TypesOfDocs.PROGRAM;
            } else if (typeOfDocString.equalsIgnoreCase(StringsFinalRequired.EMPTYSTRING + TypesOfDocs.TEXT)) {
                return TypesOfDocs.TEXT;
            } else {
                return TypesOfDocs.VIDEO;
            }
        }
        return null;
    }
    private static ArrayList<ArrayList<String>> tagsSorting(String[] dataSplit) {
        ArrayList<ArrayList<String>> tagsAddedToDoc = new ArrayList<>();
        for (int i = StringsFinalRequired.POSITION_OF_TAGS_INPUT; i < dataSplit.length; i++) {
            if (dataSplit[i].contains(StringsFinalRequired.EQUAL_SIGN)) {
                String[] tagAndValue = dataSplit[i].split(StringsFinalRequired.EQUAL_SIGN);
                List<String> tag = new ArrayList<>(List.of(tagAndValue));
                tagsAddedToDoc.add((ArrayList<String>) tag);
            } else {
                List<String> tag = new ArrayList<>();
                tag.add(dataSplit[i]);
                tagsAddedToDoc.add((ArrayList<String>) tag);
            }
        }
        return tagsAddedToDoc;
    }
    private static Document documentProceed(String data) {
        String[] dataSplit = data.trim().split(StringsFinalRequired.COMMA_SIGN);
        ArrayList<ArrayList<String>> tagsAddedToDoc = new ArrayList<>(tagsSorting(dataSplit));
        if (dataSplit.length >= StringsFinalRequired.POSITION_OF_TAGS_INPUT) {
            Document docToAdd = new Document(dataSplit[0], stringToEnum(dataSplit[1]), dataSplit[2],
                    tagsAddedToDoc);
            if (docToBeAdded) {
                return docToAdd;
            }
        } else {
            errorStatement = StringsFinalRequired.ERROR_ARG;
        }
        docToBeAdded = false;
        return null;
    }
    private static void loadFunction(String[] commandArguments) {
        try {
            Path path = Paths.get(commandArguments[1]);
            String outputData = StringsFinalRequired.EMPTYSTRING;
            File file = new File(String.valueOf(path));
            Scanner scanner = new Scanner(file);
            while (DocumentsInfo.fileToId.containsValue(DocumentsInfo.id)) {
                DocumentsInfo.id++;
            }
            List<Document> documentsInLoop = null;
            if (scanner.hasNextLine()) {
                while (scanner.hasNextLine() && docToBeAdded) {
                    String data = scanner.nextLine();
                    String[] dataSplit = data.trim().split(StringsFinalRequired.COMMA_SIGN);
                    ArrayList<ArrayList<String>> tagsAddedToDoc = new ArrayList<>(tagsSorting(dataSplit));
                    Document docToAdd = documentProceed(data);
                    if (!Document.tagCopyPresent(tagsAddedToDoc) && Document.validTagName(tagsAddedToDoc)
                            && dataSplit.length >= StringsFinalRequired.POSITION_OF_TAGS_INPUT
                            && docToBeAdded) {
                        DocumentsInfo.listOfFiles.add(docToAdd);
                        List<Document> documentPutInList = new ArrayList<>(DocumentsInfo.listOfFiles);
                        documentsInLoop = new ArrayList<>(documentPutInList);
                        if (scanner.hasNextLine()) {
                            outputData += data + StringsFinalRequired.NEXT_LINE;
                        } else {
                            outputData += data;
                        }
                    } else if (Document.tagCopyPresent(tagsAddedToDoc) || !Document.validTagName(tagsAddedToDoc)) {
                        errorStatement = Document.tagsValidity(tagsAddedToDoc);
                        docToBeAdded = false;
                    } else if (!docToBeAdded || dataSplit.length < StringsFinalRequired.POSITION_OF_TAGS_INPUT) {
                        docToBeAdded = false;
                    }
                }
                if (docToBeAdded && Document.docsDirectoryCheck((ArrayList<Document>) DocumentsInfo.listOfFiles)) {
                    DocumentsInfo.fileToId.put(commandArguments[1], DocumentsInfo.id);
                    int idOfDoc = DocumentsInfo.fileToId.get(commandArguments[1]);
                    DocumentsInfo.idToDoc.put(idOfDoc, (ArrayList<Document>) documentsInLoop);
                    DocumentsInfo.listOfFiles.clear();
                    System.out.println(StringsFinalRequired.LOADED + commandArguments[1] + StringsFinalRequired.WITH_ID + DocumentsInfo.id);
                    System.out.println(outputData);
                    toLoadDocs = true;
                    DocumentsInfo.id++;
                } else if (errorStatement.isEmpty() && !Document.docsDirectoryCheck((ArrayList<Document>) DocumentsInfo.listOfFiles)) {
                    errorStatement = StringsFinalRequired.ERROR_MORE_DOC_SAME_DIRECTORY;
                    DocumentsInfo.listOfFiles.clear();
                }
                if (!errorStatement.isEmpty()) {
                    System.out.println(errorStatement);
                }
            } else {
                System.out.println(StringsFinalRequired.ERRORSTART + StringsFinalRequired.FILE
                        + commandArguments[1] + StringsFinalRequired.IS_EMPTY);
            }
            docToBeAdded = true;
        } catch (FileNotFoundException e) {
            System.out.println(StringsFinalRequired.ERRORSTART + StringsFinalRequired.FILE
                    + commandArguments[1] + StringsFinalRequired.NOT_FOUND);
        }
    }
    private static boolean missingDocType(String docType) {
        if (docToBeAdded) {
            List<TypesOfDocs> docTypeValue = new ArrayList<>(List.of(TypesOfDocs.values()));
            for (int i = 0; i < docTypeValue.size(); i++) {
                if (docTypeValue.get(i).name().equalsIgnoreCase(docType)) {
                    return false;
                }
            }
            if (errorStatement.isEmpty()) {
                errorStatement = StringsFinalRequired.ERROR_DOC_NOT_PRESENT;
            }
            DocumentHandling.docToBeAdded = false;
            return true;
        }
        return false;
    }
    private static boolean documentPresentInTheFile(String fileId, String nameOfTheDirectory) {
        List<Document> listOfDocs = DocumentsInfo.idToDoc.get(Integer.parseInt(fileId));
        for (int i = 0; i < listOfDocs.size(); i++) {
            if (listOfDocs.get(i).getDirectory().equalsIgnoreCase(nameOfTheDirectory)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public CommandResult execute(String[] commandArguments) {
        switch (commandArguments[0]) {
            case StringsFinalRequired.LOAD_COMMAND:
                if (commandArguments.length == 2) {
                    loadFunction(commandArguments);
                    DocumentsInfo.tagsPresentInDoc.clear();
                } else {
                    System.out.println(StringsFinalRequired.ERROR_PATH_NOT_FOUND);
                }
                break;
            case StringsFinalRequired.CHANGE_COMMAND:
                try {
                    if (commandArguments.length == StringsFinalRequired.MINIMUM_LENGTH_OF_CHANGE_COMMAND) {
                        List<Document> listOfDocs = DocumentsInfo.idToDoc.get(Integer.parseInt(commandArguments[1]));
                        int previousAccessNumber = 0;
                        String directoryOfTheDoc = StringsFinalRequired.EMPTYSTRING;
                        if (listOfDocs != null && documentPresentInTheFile(commandArguments[1],
                                commandArguments[2]) && Integer.parseInt(commandArguments
                                [StringsFinalRequired.POSITION_OF_TAGS_INPUT]) >= 0) {
                            for (int i = 0; i < listOfDocs.size(); i++) {
                                if (listOfDocs.get(i).getDirectory().equalsIgnoreCase(commandArguments[2])) {
                                    directoryOfTheDoc = listOfDocs.get(i).getDirectory();
                                    previousAccessNumber = listOfDocs.get(i).accessNumber;
                                    listOfDocs.get(i).accessNumber = Integer.parseInt(commandArguments
                                            [StringsFinalRequired.POSITION_OF_TAGS_INPUT]);
                                }
                            }
                            System.out.println(StringsFinalRequired.CHANGE + previousAccessNumber
                                    + StringsFinalRequired.TO + commandArguments[StringsFinalRequired.POSITION_OF_TAGS_INPUT]
                                    + StringsFinalRequired.FOR + directoryOfTheDoc);
                        } else {
                            System.out.println(StringsFinalRequired.ERRORSTART + (listOfDocs == null
                                    ? StringsFinalRequired.ERROR_NO_DOC_LOADED
                                    : !documentPresentInTheFile(commandArguments[1], commandArguments[2])
                                    ? StringsFinalRequired.ERROR_NO_DOC_IN_FILE : StringsFinalRequired.ERROR_ACCESS_NUMBER_LESS));
                        }
                    } else {
                        System.out.println(StringsFinalRequired.ERROR_ARG_CHANGE_COMMAND);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(StringsFinalRequired.ERROR_ACCESS_NOT_A_NUM);
                }
                break;
            default:
        }
        return new CommandResult(CommandResultType.SUCCESS, null);
    }
}
