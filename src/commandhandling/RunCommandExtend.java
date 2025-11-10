package commandhandling;
import objectsforsorting.StringsFinalRequired;
import objectsforsorting.Tags;
import objectsforsorting.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
/**
 * This class contains all the methods required for sorting.
 * @author ushug
 */
public final class RunCommandExtend {
    protected static String print = StringsFinalRequired.EMPTYSTRING;
    private RunCommandExtend() {
    }
    /**
     * Gives the log with base 2.
     * @param x The number to find the log off.
     * @param base Base of the log.
     * @return Log of some number with base 2.
     */
    protected static double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }
    /**
     * Gives tag from information gain calculated.
     * @param toSortMap HashMap containing Tags to Info gain relation
     * @param infoGain Info gain of the tag.
     * @return tag
     */
    protected static Tags keyReturn(HashMap<Tags, Double> toSortMap, Double infoGain) {
        List<Tags> entryKeys = new ArrayList<>(toSortMap.keySet());
        for (int i = 0; i < entryKeys.size(); i++) {
            if (Objects.equals(toSortMap.get(entryKeys.get(i)), infoGain)) {
                return entryKeys.get(i);
            }
        }
        return null;
    }
    /**
     * Prints the information gain of all the tags.
     * @param sortedMap HashMap containing Tags to Information gain relation.
     */
    protected static void infoPrint(HashMap<Tags, Double> sortedMap) {
        List<Tags> entryTags = new ArrayList<>(sortedMap.keySet());
        for (int i = 0; i < entryTags.size(); i++) {
            double input = sortedMap.get(entryTags.get(i)).floatValue();
            if (input != 0) {
                Locale.setDefault(Locale.US);
                print += String.format(StringsFinalRequired.SLASH_SIGN + entryTags.get(i).nameOfTag.toLowerCase()
                        + StringsFinalRequired.ROUNDING_UP_NUMBER, input);
            }
        }
    }
    /**
     * Prints the information gain of all the tags based on the address.
     * @param sortedMap HashMap containing Tags to Information gain relation.
     * @param address Address of the document
     */
    protected static void infoPrint(HashMap<Tags, Double> sortedMap, String address) {
        List<Tags> entryTags = new ArrayList<>(sortedMap.keySet());
        for (int i = 0; i < entryTags.size(); i++) {
            float input = sortedMap.get(entryTags.get(i)).floatValue();
            if (input != 0) {
                print += String.format(address + StringsFinalRequired.SLASH_SIGN + entryTags.get(i).nameOfTag.toLowerCase()
                        + StringsFinalRequired.ROUNDING_UP_NUMBER.replace(StringsFinalRequired.COMMA_SIGN.charAt(0),
                        StringsFinalRequired.POINT_SIGN.charAt(0)), input);
            }
        }
    }
    /**
     * Checks if the information gain of all the lists created is less than 10^-3.
     * @param sortedmap HashMap containing Tags to Information gain relation.
     * @return gives true if all the lists have less than 10^-3 info gain otherwise false.
     */
    protected static boolean infoLimit(HashMap<Tags, Double> sortedmap) {
        int limit = 0;
        List<Tags> keys = new ArrayList<>(sortedmap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            if (sortedmap.get(keys.get(i)) < Math.pow(StringsFinalRequired.INFO_GAIN_LIMIT, StringsFinalRequired.INFO_GAIN_POWER)) {
                limit++;
                if (limit == keys.size()) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    /**
     * Gives a list of all the tags present in the document.
     * @param documents All the documents present in the text file.
     * @return Tags present in the document.
     */
    protected static ArrayList<Tags> tagsPresent(ArrayList<Document> documents) {
        List<Tags> tagsPresent = new ArrayList<>();
        boolean toAdd = true;
        for (Document document : documents) {
            for (int i = 0; i < document.tags.size(); i++) {
                if (!tagsPresent.contains(document.tags.get(i))) {
                    if (!tagsPresent.isEmpty()) {
                        for (int j = 0; j < tagsPresent.size(); j++) {
                            if (tagsPresent.get(j).nameOfTag.equals(document.tags.get(i).nameOfTag)) {
                                toAdd = false;
                                break;
                            }
                        }
                    }
                    if (toAdd) {
                        tagsPresent.add(document.tags.get(i));
                    }
                    toAdd = true;
                }
            }
        }
        return (ArrayList<Tags>) tagsPresent;
    }
    /**
     * Returns the Directory of the specified document which have the specified tags inputted.
     * @param address Directory of the document.
     * @param documents documents present in the txt file.
     * @return The name of the document.
     */
    protected static String getDocNameThroughTags(String address, ArrayList<Document> documents) {
        List<String> tags = new ArrayList<>(List.of(address.trim().split(StringsFinalRequired.SLASH_SIGN)));
        Comparator<Document> documentAccessComparator = new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                Integer o1AccessNumber = o1.accessNumber;
                Integer o2AccessNumber = o2.accessNumber;
                return o1AccessNumber.compareTo(o2AccessNumber);
            }
        };
        List<Document> docsWithSameTags = new ArrayList<>();
        tags.remove(0);
        int tagsPresent = 0;
        for (Document document : documents) {
            List<String> tagsPresentInTheList = SortingDocuments.tagPresentInDoc(document);
            for (int i = 0; i < tags.size(); i++) {
                List<String> tagsSplit = new ArrayList<>(List.of(tags.get(i).split(StringsFinalRequired.EQUAL_SIGN)));
                if (tagsSplit.get(1).equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                    if (!tagsPresentInTheList.contains(tagsSplit.get(0))) {
                        tagsPresent++;
                    }
                } else if (tagsPresentInTheList.contains(tagsSplit.get(0))) {
                    for (int j = 0; j < document.tags.size(); j++) {
                        if (document.tags.get(j).valuePresent == null) {
                            if (document.tags.get(j).nameOfTag.equals(tagsSplit.get(0))) {
                                tagsPresent++;
                                break;
                            }
                        } else if (document.tags.get(j).nameOfTag.equals(tagsSplit.get(0)) && document.tags.get(j).
                                valuePresent.equals(tagsSplit.get(1))) {
                            tagsPresent++;
                            break;
                        }
                    }
                }
                if (tagsPresent == tags.size()) {
                    docsWithSameTags.add(document);
                }
            }
            tagsPresent = 0;
        }
        Collections.sort(docsWithSameTags, documentAccessComparator);
        for (Document document : docsWithSameTags) {
            if (!document.docPrinted) {
                document.docPrinted = true;
                return document.getDirectory();
            }
        }
        return null;
    }
    /**
     * Gives the total number of accesses in a particular list of document.
     * @param documents Documents present in the text file.
     * @return Total number of access in a particular list of document.
     */
    protected static double totalNumberOfAccesses(ArrayList<Document> documents) {
        int total = 0;
        for (Document document : documents) {
            total += document.accessNumber;
        }
        return total;
    }
    /**
     * Gives a list of the values of the specific tag.
     * @param documents Documents present in the text file.
     * @param tag Specified tag
     * @return List of values of the tag.
     */
    protected static ArrayList<String> valuesOfTagsOfDocs(ArrayList<Document> documents, Tags tag) {
        List<String> valuesOfTheTag = new ArrayList<>();
        boolean tagExisitingInDoc = false;
        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < documents.get(i).tags.size(); j++) {
                if (documents.get(i).tags.get(j).nameOfTag.equals(tag.nameOfTag)) {
                    if (!valuesOfTheTag.contains(documents.get(i).tags.get(j).valuePresent)) {
                        if (documents.get(i).tags.get(j).valuePresent instanceof Integer) {
                            if (!valuesOfTheTag.contains(StringsFinalRequired.EMPTYSTRING + documents.get(i).tags.get(j).valuePresent)) {
                                valuesOfTheTag.add(StringsFinalRequired.EMPTYSTRING + documents.get(i).tags.get(j).valuePresent);
                            }
                        } else {
                            valuesOfTheTag.add((String) documents.get(i).tags.get(j).valuePresent);
                        }
                    }
                    tagExisitingInDoc = true;
                    break;
                }
            }
            if (!tagExisitingInDoc && !valuesOfTheTag.contains(StringsFinalRequired.UNDEFINED_BY_USER)) {
                valuesOfTheTag.add(StringsFinalRequired.UNDEFINED_BY_USER);
            }
            tagExisitingInDoc = false;
        }
        return SortingDocuments.reorganisedValuesBasedOfAccess((ArrayList<String>) valuesOfTheTag, tag, documents);
    }
    /**
     * Gives a list based on different types of values of a specific tag.
     * @param tag Specified tag
     * @param documents Documents present in the text file.
     * @return List based on different types of values of a specific tag.
     */
    protected static ArrayList<ArrayList<Document>> listDivisionBasedOnValues(Tags tag, ArrayList<Document> documents) {
        List<String> valuesOfTag = valuesOfTagsOfDocs(documents, tag);
        ArrayList<ArrayList<Document>> sortedDocs = new ArrayList<>();
        List<Document> docToAdd = new ArrayList<>();
        if (tag.isMultiValue) {
            if (tag.isNumeric) {
                return SortingDocuments.listDivisionBasedOnNumericValues(tag, documents);
            } else {
                boolean toAdd = false;
                for (int j = 0; j < valuesOfTag.size(); j++) {
                    if (!valuesOfTag.get(j).equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                        for (Document document : documents) {
                            for (int k = 0; k < document.tags.size(); k++) {
                                if (document.tags.get(k).nameOfTag.equals(tag.nameOfTag) && document.tags.get(k).
                                        valuePresent.equals(valuesOfTag.get(j))) {
                                    docToAdd.add(document);
                                    toAdd = true;
                                    break;
                                }
                            }
                        }
                        if (toAdd && !docToAdd.isEmpty()) {
                            List<Document> docCopy = new ArrayList<>(docToAdd);
                            sortedDocs.add((ArrayList<Document>) docCopy);
                            docToAdd.clear();
                        }
                        toAdd = false;
                    } else {
                        toAdd = true;
                        for (Document document : documents) {
                            for (int k = 0; k < document.tags.size(); k++) {
                                if (document.tags.get(k).nameOfTag.equals(tag.nameOfTag)) {
                                    toAdd = false;
                                    break;
                                }
                            }
                            if (toAdd) {
                                docToAdd.add(document);
                            }
                            toAdd = true;
                        }
                        if (toAdd && !docToAdd.isEmpty()) {
                            List<Document> docCopy = new ArrayList<>(docToAdd);
                            sortedDocs.add((ArrayList<Document>) docCopy);
                            docToAdd.clear();
                        }
                        toAdd = true;
                    }
                }
            }

        } else {
            return listDivisionBasedOnLabels(tag, documents);
        }
        return sortedDocs;
    }
    /**
     * Creates a list on the basis of labels.
     * @param tag Specified tag.
     * @param documents Documents present in the text file.
     * @return Creates a list on the basis of labels.
     */
    private static ArrayList<ArrayList<Document>> listDivisionBasedOnLabels(Tags tag, ArrayList<Document> documents) {
        List<String> valuesOfTag = valuesOfTagsOfDocs(documents, tag);
        List<ArrayList<Document>> sortedDocs = new ArrayList<>();
        List<Document> docToAdd = new ArrayList<>();
        boolean toAdd = false;
        if (tag.isLabel) {
            for (int j = 0; j < valuesOfTag.size(); j++) {
                toAdd = false;
                if (valuesOfTag.get(j) == null) {
                    for (Document document : documents) {
                        for (int k = 0; k < document.tags.size(); k++) {
                            if (document.tags.get(k).nameOfTag.equals(tag.nameOfTag)) {
                                docToAdd.add(document);
                                toAdd = true;
                                break;
                            }
                        }
                    }
                    if (toAdd && !docToAdd.isEmpty()) {
                        List<Document> docCopy = new ArrayList<>(docToAdd);
                        sortedDocs.add((ArrayList<Document>) docCopy);
                        docToAdd.clear();
                    }
                } else {
                    boolean toAddUndefined = true;
                    for (Document document : documents) {
                        for (int k = 0; k < document.tags.size(); k++) {
                            if (document.tags.get(k).nameOfTag.equals(tag.nameOfTag)) {
                                toAddUndefined = false;
                                break;
                            }
                        }
                        if (toAddUndefined) {
                            docToAdd.add(document);
                        }
                        toAddUndefined = true;
                    }
                    if (toAddUndefined && !docToAdd.isEmpty()) {
                        List<Document> docCopy = new ArrayList<>(docToAdd);
                        sortedDocs.add((ArrayList<Document>) docCopy);
                        docToAdd.clear();
                    }
                }
            }
        }
        return (ArrayList<ArrayList<Document>>) sortedDocs;
    }
}
