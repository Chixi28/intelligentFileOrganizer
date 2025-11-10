package commandhandling;
import objectsforsorting.Document;
import objectsforsorting.StringsFinalRequired;
import objectsforsorting.Tags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class helps in sorting the documents in the list.
 * @author ushug
 */
public final class SortingDocuments {
    private SortingDocuments() {
    }
    /**
     * Reorganises the value of a specific tag based on the number of accesses.
     * @param values Values of the specified tag
     * @param tag Specified tag.
     * @param documents Documents present in the text file.
     * @return A reorganised list of the values.
     */
    protected static ArrayList<String> reorganisedValuesBasedOfAccess(ArrayList<String> values, Tags tag, ArrayList<Document> documents) {
        Map<String, Double> probabilityToValue = new LinkedHashMap<>();
        List<Double> probability = new ArrayList<>();
        List<String> reorganisedValues = new ArrayList<>();
        Comparator<Double> comparator = new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return -o1.compareTo(o2);
            }
        };
        for (int i = 0; i < values.size(); i++) {
            double probabilitySpecificTagAndVal = SortingDocuments.probabilityTagsValue(documents, tag, values.get(i));
            probability.add(probabilitySpecificTagAndVal);
            probabilityToValue.put(values.get(i), probabilitySpecificTagAndVal);
        }
        probability.sort(comparator);
        for (int i = 0; i < probability.size(); i++) {
            reorganisedValues.add(keyReturn((HashMap<String, Double>) probabilityToValue, probability.get(i)));
            probabilityToValue.remove(keyReturn((HashMap<String, Double>) probabilityToValue, probability.get(i)),
                    probabilityToValue.get(keyReturn((HashMap<String, Double>) probabilityToValue, probability.get(i))));
        }
        return (ArrayList<String>) reorganisedValues;
    }
    /**
     * Returns the key of the particular value from the hash map.
     * @param sortedMap Hashmap of information gain
     * @param probability Probability of the tags
     * @return A key
     */
    protected static String keyReturn(HashMap<String, Double> sortedMap, Double probability) {
        List<String> entryKeys = new ArrayList<>(sortedMap.keySet());
        for (int i = 0; i < entryKeys.size(); i++) {
            if (Objects.equals(sortedMap.get(entryKeys.get(i)), probability)) {
                return entryKeys.get(i);
            }
        }
        return null;
    }
    /**
     * Gives the total Uncertainty of the list.
     * @param documents Documents present in the text file.
     * @return Total Uncertainty of the List.
     */
    protected static double totalUncertaintyInTheList(ArrayList<Document> documents) {
        double result = 0;
        for (Document document : documents) {
            double probability = (double) document.accessNumber / RunCommandExtend.totalNumberOfAccesses(documents);
            result += (probability) * (RunCommandExtend.log(probability, 2));
        }
        return -result;
    }
    /**
     * Creates a list of document with specific value and tags
     * @param documents Documents present in the txt file.
     * @param tag Specified Tag
     * @param value Value of the Tag.
     * @return List of documents with specific value and tags.
     */
    protected static ArrayList<Document> docsWithTagsAndValue(ArrayList<Document> documents, Tags tag, Object value) {
        List<Document> specificDocs = new ArrayList<>();
        boolean toAddUndefined = true;
        if (tag.isMultiValue) {
            if (!value.equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag) && document.tags.get(i).valuePresent.equals(value)) {
                            specificDocs.add(document);
                        }
                    }
                }
                return (ArrayList<Document>) specificDocs;
            } else {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                            if (document.tags.get(i).valuePresent.equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                                toAddUndefined = true;
                            } else {
                                toAddUndefined = false;
                            }
                        }
                    }
                    if (toAddUndefined) {
                        specificDocs.add(document);
                    }
                    toAddUndefined = true;
                }
                return (ArrayList<Document>) specificDocs;
            }
        } else {
            if (value == null && tag.isLabel) {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                            specificDocs.add(document);
                        }
                    }
                }
                return (ArrayList<Document>) specificDocs;
            } else {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                            toAddUndefined = false;
                        }
                    }
                    if (toAddUndefined) {
                        specificDocs.add(document);
                    }
                    toAddUndefined = true;
                }
                return (ArrayList<Document>) specificDocs;
            }
        }
    }
    /**
     * Gives all the tags present in a document.
     * @param document The specified document.
     * @return List of tags present in the document
     */
    protected static ArrayList<String> tagPresentInDoc(Document document) {
        List<String> tagsPresent = new ArrayList<>();
        boolean toAdd = true;
        for (int i = 0; i < document.tags.size(); i++) {
            if (!tagsPresent.contains(document.tags.get(i))) {
                if (!tagsPresent.isEmpty()) {
                    for (int j = 0; j < tagsPresent.size(); j++) {
                        if (tagsPresent.get(j).equals(document.tags.get(i).nameOfTag)) {
                            toAdd = false;
                            break;
                        }
                    }
                }
                if (toAdd) {
                    tagsPresent.add(document.tags.get(i).nameOfTag);
                }
                toAdd = true;
            }
        }
        return (ArrayList<String>) tagsPresent;
    }
    /**
     * Finds probability of the value of the specific numeric tag.
     * @param documents Documents present in text file
     * @param tag The specified tag
     * @param value The value of the tag
     * @return Probability of the specific tag.
     */
    protected static double probabilityNumericTagsValue(ArrayList<Document> documents, Tags tag, String value) {
        double probability = 0;
        boolean toAddprobablityUndefined = true;
        if (!value.equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
            int numericValue = Integer.parseInt(value);
            for (Document document : documents) {
                for (int i = 0; i < document.tags.size(); i++) {
                    if (document.tags.get(i).isNumeric) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag) && document.tags.get(i).numericVal == numericValue) {
                            probability += document.accessNumber;
                        }
                    }
                }
            }
            return probability / RunCommandExtend.totalNumberOfAccesses(documents);
        } else {
            for (Document document : documents) {
                for (int i = 0; i < document.tags.size(); i++) {
                    if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                        toAddprobablityUndefined = false;
                    }
                }
                if (toAddprobablityUndefined) {
                    probability += document.accessNumber;
                }
                toAddprobablityUndefined = true;
            }
            return probability / RunCommandExtend.totalNumberOfAccesses(documents);
        }
    }
    /**
     * Creates lists of different type of values of the same tag.
     * @param tag The specified Tag
     * @param documents Documents present in the text file
     * @return Lists of different type of values of the same tag.
     */
    protected static ArrayList<ArrayList<Document>> listDivisionBasedOnNumericValues(Tags tag, ArrayList<Document> documents) {
        List<String> valuesOfTag = RunCommandExtend.valuesOfTagsOfDocs(documents, tag);
        List<ArrayList<Document>> sortedDocs = new ArrayList<>();
        List<Document> docToAdd = new ArrayList<>();
        boolean toAdd = false;
        for (int j = 0; j < valuesOfTag.size(); j++) {
            if (!valuesOfTag.get(j).equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                toAdd = false;
                for (Document document : documents) {
                    for (int k = 0; k < document.tags.size(); k++) {
                        if (document.tags.get(k).isNumeric) {
                            if (document.tags.get(k).nameOfTag.equals(tag.nameOfTag) && document.tags.get(k).
                                    numericVal == Integer.parseInt(valuesOfTag.get(j))) {
                                docToAdd.add(document);
                                toAdd = true;
                                break;
                            }
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
        return (ArrayList<ArrayList<Document>>) sortedDocs;
    }
    /**
     * Gives probability of the particular tag in the list of documents.
     * @param documents Documents present in the txt file
     * @param tag Specified tag
     * @param value The value of the tag
     * @return Probability of the particular tag in the list of documents.
     */
    protected static double probabilityTagsValue(ArrayList<Document> documents, Tags tag, Object value) {
        double probability = 0;
        boolean toAddProbablityUndefined = true;
        if (tag.isMultiValue) {
            if (tag.isNumeric) {
                return SortingDocuments.probabilityNumericTagsValue(documents, tag, (String) value);
            } else {
                if (!value.equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                    for (Document document : documents) {
                        for (int i = 0; i < document.tags.size(); i++) {
                            if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag) && document.tags.get(i).valuePresent.equals(value)) {
                                probability += document.accessNumber;
                            }
                        }
                    }
                    return probability / RunCommandExtend.totalNumberOfAccesses(documents);
                } else {
                    for (Document document : documents) {
                        for (int i = 0; i < document.tags.size(); i++) {
                            if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                                toAddProbablityUndefined = false;
                            }
                        }
                        if (toAddProbablityUndefined) {
                            probability += document.accessNumber;
                        }
                        toAddProbablityUndefined = true;
                    }
                    return probability / RunCommandExtend.totalNumberOfAccesses(documents);
                }
            }
        } else {
            if (value == null & tag.isLabel) {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                            probability += document.accessNumber;
                        }
                    }
                }
                return probability / RunCommandExtend.totalNumberOfAccesses(documents);
            } else {
                for (Document document : documents) {
                    for (int i = 0; i < document.tags.size(); i++) {
                        if (document.tags.get(i).nameOfTag.equals(tag.nameOfTag)) {
                            toAddProbablityUndefined = false;
                        }
                    }
                    if (toAddProbablityUndefined) {
                        probability += document.accessNumber;
                    }
                    toAddProbablityUndefined = true;
                }
                return probability / RunCommandExtend.totalNumberOfAccesses(documents);
            }
        }
    }
    /**
     * Lexicographic assignment of the tags.
     * @param sortedMap Hashmap linked from tags to their information gain.
     * @return Lexicographic assortment of the tags.
     */
    protected static LinkedHashMap<Tags, Double> lexicographicAssignment(HashMap<Tags, Double> sortedMap) {
        List<Tags> tagsSameValue = new ArrayList<>();
        List<Tags> tagsSortedMap = new ArrayList<>(sortedMap.keySet());
        Map<Tags, Double> lexicographicSortedMap = new LinkedHashMap<>();
        Comparator<Tags> lexicographicComparator = new Comparator<Tags>() {
            @Override
            public int compare(Tags o1, Tags o2) {
                return o1.nameOfTag.compareTo(o2.nameOfTag);
            }
        };
        if (!tagsSortedMap.isEmpty()) {
            for (int i = 0; i < sortedMap.size(); i++) {
                if (i < sortedMap.size() - 1) {
                    if (Objects.equals(sortedMap.get(tagsSortedMap.get(i)), sortedMap.get(tagsSortedMap.get(i + 1)))) {
                        if (!tagsSameValue.contains(tagsSortedMap.get(i))) {
                            tagsSameValue.add(tagsSortedMap.get(i));
                        }
                        if (!tagsSameValue.contains(tagsSortedMap.get(i + 1))) {
                            tagsSameValue.add(tagsSortedMap.get(i + 1));
                        }
                    } else {
                        if (!tagsSameValue.isEmpty()) {
                            Collections.sort(tagsSameValue, lexicographicComparator);
                            for (int j = 0; j < tagsSameValue.size(); j++) {
                                lexicographicSortedMap.put(tagsSameValue.get(j), sortedMap.get(tagsSameValue.get(j)));
                            }
                            tagsSameValue.clear();
                        } else {
                            lexicographicSortedMap.put(tagsSortedMap.get(i), sortedMap.get(tagsSortedMap.get(i)));
                            if (i == sortedMap.size() - 2) {
                                lexicographicSortedMap.put(tagsSortedMap.get(i + 1), sortedMap.get(tagsSortedMap.get(i + 1)));
                            }
                        }

                    }
                } else {
                    if (lexicographicSortedMap.size() != sortedMap.size()) {
                        lexicographicSortedMap.put(tagsSortedMap.get(sortedMap.size() - 1),
                                sortedMap.get(tagsSortedMap.get(sortedMap.size() - 1)));
                    }
                }
            }
            if (!tagsSameValue.isEmpty()) {
                Collections.sort(tagsSameValue, lexicographicComparator);
                for (int j = 0; j < tagsSameValue.size(); j++) {
                    lexicographicSortedMap.put(tagsSameValue.get(j), sortedMap.get(tagsSameValue.get(j)));
                }
                tagsSameValue.clear();
            }
        } else {
            Collections.sort(tagsSortedMap, lexicographicComparator);
            for (int i = 0; i < tagsSortedMap.size(); i++) {
                lexicographicSortedMap.put(tagsSortedMap.get(i), sortedMap.get(tagsSortedMap.get(i)));
            }
        }
        return (LinkedHashMap<Tags, Double>) lexicographicSortedMap;
    }
}
