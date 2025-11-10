package commandhandling;
import objectsforsorting.Document;
import objectsforsorting.DocumentsInfo;
import objectsforsorting.StringsFinalRequired;
import objectsforsorting.Tags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class deals with the sorting of the given documents.
 * @author ushug
 */
public class RunCommand implements Command {
    /**
     * List of addresses of all the documents.
     */
    protected static List<String> addressOfDocs = new ArrayList<>();
    private static List<List<Document>> sortedDocument = new ArrayList<>();
    private static double surprise = 0;
    private static double expectedUncertainty = 0;
    private static void infoGainStartingPhase(ArrayList<Document> documents) {
        List<Tags> tagsPresent = RunCommandExtend.tagsPresent(documents);
        Map<Tags, Double> tagsToInfo = new HashMap<>();
        for (int i = 0; i < tagsPresent.size(); i++) {
            for (int j = 0; j < RunCommandExtend.valuesOfTagsOfDocs(documents, tagsPresent.get(i)).size(); j++) {
                List<String> valuesOfSpecificTag = RunCommandExtend.valuesOfTagsOfDocs(documents, tagsPresent.get(i));
                List<Document> docWithSpecifiedTagAndValue = SortingDocuments.docsWithTagsAndValue(documents, tagsPresent.get(i),
                        valuesOfSpecificTag.get(j));
                if (docWithSpecifiedTagAndValue.size() > 1) {
                    double probabilityOfTagsAndValue = SortingDocuments.probabilityTagsValue(documents, tagsPresent.get(i),
                            valuesOfSpecificTag.get(j));
                    for (int k = 0; k < docWithSpecifiedTagAndValue.size(); k++) {
                        double probability = (double) docWithSpecifiedTagAndValue.get(k).accessNumber / RunCommandExtend.
                                totalNumberOfAccesses((ArrayList<Document>) docWithSpecifiedTagAndValue);
                        surprise -= (probability * RunCommandExtend.log(probability, 2));
                    }
                    expectedUncertainty += surprise * probabilityOfTagsAndValue;
                    surprise = 0;
                }
            }
            tagsToInfo.put(tagsPresent.get(i), (double) SortingDocuments.totalUncertaintyInTheList(documents) - expectedUncertainty);
            surprise = 0;
            expectedUncertainty = 0;
        }
        infoLimitCheckStartPhase(documents, (HashMap<Tags, Double>) tagsToInfo);
    }
    private static void infoLimitCheckStartPhase(ArrayList<Document> documents, HashMap<Tags, Double> tagsToInfo) {
        Map<Tags, Double> sortedMap = infoMapSort(tagsToInfo, SortingDocuments.totalUncertaintyInTheList(documents));
        RunCommandExtend.infoPrint((HashMap<Tags, Double>) sortedMap);
        outputArrange(RunCommandExtend.print);
        List<Tags> keys = new ArrayList<>(sortedMap.keySet());
        if (!keys.isEmpty()) {
            ArrayList<ArrayList<Document>> sortedDocs = RunCommandExtend.listDivisionBasedOnValues(keys.get(0), documents);
            for (ArrayList<Document> documentsForIG : sortedDocs) {
                boolean addUndefined = true;
                String value = StringsFinalRequired.EMPTYSTRING;
                if (keys.get(0).isLabel && (documentsForIG.get(0).tags.contains(keys.get(0))) && documentsForIG.get(0).
                        tags.get(documentsForIG.get(0).tags.indexOf(keys.get(0))).valuePresent == null) {
                    value = StringsFinalRequired.DEFINED;
                } else if (keys.get(0).isLabel && (!documentsForIG.get(0).tags.contains(keys.get(0)))) {
                    value = StringsFinalRequired.UNDEFINED_BY_USER;
                } else if (keys.get(0).isMultiValue) {
                    for (int i = 0; i < documentsForIG.get(0).tags.size(); i++) {
                        if (documentsForIG.get(0).tags.get(i).nameOfTag.equals(keys.get(0).nameOfTag)) {
                            if (documentsForIG.get(0).tags.get(i).valuePresent.equals(StringsFinalRequired.UNDEFINED_BY_USER)) {
                                value = StringsFinalRequired.UNDEFINED_BY_USER;
                            } else {
                                value = StringsFinalRequired.EMPTYSTRING + documentsForIG.get(0).tags.get(i).valuePresent;
                            }
                            addUndefined = false;
                        }
                    }
                    if (addUndefined) {
                        value = StringsFinalRequired.UNDEFINED_BY_USER;
                    }
                }
                String address = StringsFinalRequired.SLASH_SIGN + keys.get(0).nameOfTag + StringsFinalRequired.EQUAL_SIGN + value;
                if (documentsForIG.size() == 1 && infoGainFromMapStart(keys.get(0).nameOfTag,
                        (HashMap<Tags, Double>) sortedMap) < Math.pow(StringsFinalRequired.
                        INFO_GAIN_LIMIT, StringsFinalRequired.INFO_GAIN_POWER)) {
                    sortedDocument.add(documentsForIG);
                    addressOfDocs.add(StringsFinalRequired.SLASH_SIGN + keys.get(0).nameOfTag + StringsFinalRequired.EQUAL_SIGN + value);
                } else {
                    infoGainMiddlePhase(documentsForIG, address);
                }
            }
            System.out.println(StringsFinalRequired.DATA_SEPEARATOR);
            for (String output : addressOfDocs) {
                RunCommandExtend.print += output + StringsFinalRequired.SLASH_SIGN + StringsFinalRequired.QUOTATION_SIGN
                        + RunCommandExtend.getDocNameThroughTags(output, documents) + StringsFinalRequired.QUOTATION_SIGN
                        + StringsFinalRequired.NEXT_LINE;
            }
            sortedDocs.clear();
        } else {
            System.out.println(StringsFinalRequired.DATA_SEPEARATOR);
            for (int i = 0; i < documents.size(); i++) {
                RunCommandExtend.print += StringsFinalRequired.SLASH_SIGN + StringsFinalRequired.QUOTATION_SIGN + documents.get(i).
                        getDirectory() + StringsFinalRequired.QUOTATION_SIGN + StringsFinalRequired.NEXT_LINE;
            }
        }
        finalOutputLexicographicArrangement(RunCommandExtend.print);
        Document.documentPrintReset(documents);
        RunCommandExtend.print = StringsFinalRequired.EMPTYSTRING;
        addressOfDocs.clear();
    }
    private static double infoGainFromMapStart(String tagName, HashMap<Tags, Double> sortedmap) {
        List<Tags> keys = new ArrayList<>(sortedmap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).isMultiValue) {
                if (keys.get(i).nameOfTag.equals(tagName)) {
                    return sortedmap.get(keys.get(i));
                }
            } else {
                if (keys.get(i).nameOfTag.equals(tagName)) {
                    return sortedmap.get(keys.get(i));
                }
            }
        }
        return -1d;
    }
    private static void infoGainMiddlePhase(ArrayList<Document> documents, String address) {
        double totalUncertainty = SortingDocuments.totalUncertaintyInTheList(documents);
        List<Tags> tagsPresent = RunCommandExtend.tagsPresent(documents);
        double surprise = 0;
        double expectedUncertainty = 0;
        Map<Tags, Double> tagsToInfo = new HashMap<>();
        for (int i = 0; i < tagsPresent.size(); i++) {
            for (int j = 0; j < RunCommandExtend.valuesOfTagsOfDocs(documents, tagsPresent.get(i)).size(); j++) {
                List<String> valuesOfTheSpecificTag = RunCommandExtend.valuesOfTagsOfDocs(documents, tagsPresent.get(i));
                List<Document> docWithSpecifiedTagAndValue = SortingDocuments.docsWithTagsAndValue(documents, tagsPresent.get(i),
                        valuesOfTheSpecificTag.get(j));
                if (docWithSpecifiedTagAndValue.size() > 1) {
                    double probabilityOfTagsAndValue = SortingDocuments.probabilityTagsValue(documents, tagsPresent.get(i),
                            valuesOfTheSpecificTag.get(j));
                    for (int k = 0; k < docWithSpecifiedTagAndValue.size(); k++) {
                        double probability = (double) docWithSpecifiedTagAndValue.get(k).accessNumber / RunCommandExtend.
                                totalNumberOfAccesses((ArrayList<Document>) docWithSpecifiedTagAndValue);
                        surprise -= (probability * RunCommandExtend.log(probability, 2));
                    }
                    expectedUncertainty += surprise * probabilityOfTagsAndValue;
                    surprise = 0;
                }
            }
            tagsToInfo.put(tagsPresent.get(i), totalUncertainty - expectedUncertainty);
            surprise = 0;
            expectedUncertainty = 0;
        }
        infoLimitCheckMidPhase(documents, address, (HashMap<Tags, Double>) tagsToInfo);
    }
    private static void infoLimitCheckMidPhase(ArrayList<Document> documents, String address, HashMap<Tags, Double> tagsToInfo) {
        Map<Tags, Double> sortedMap = SortingDocuments.lexicographicAssignment(infoMapSort(
                tagsToInfo, SortingDocuments.totalUncertaintyInTheList(documents)));
        RunCommandExtend.infoPrint((HashMap<Tags, Double>) sortedMap, address);
        outputArrange(RunCommandExtend.print);
        List<Tags> keys = new ArrayList<>(sortedMap.keySet());
        if (!keys.isEmpty()) {
            ArrayList<ArrayList<Document>> sortedDocs = RunCommandExtend.listDivisionBasedOnValues(keys.get(0), documents);
            if (!RunCommandExtend.infoLimit((HashMap<Tags, Double>) sortedMap)) {
                for (ArrayList<Document> documentsForIG : sortedDocs) {
                    boolean addUndefined = true;
                    String value = StringsFinalRequired.EMPTYSTRING;
                    if (keys.get(0).isLabel && (documentsForIG.get(0).tags.contains(keys.get(0)))
                            && documentsForIG.get(0).tags.get(documentsForIG.get(0).tags.indexOf(keys.get(0))).valuePresent == null) {
                        value = StringsFinalRequired.DEFINED;
                    } else if (keys.get(0).isLabel && (!documentsForIG.get(0).tags.contains(keys.get(0)))) {
                        value = StringsFinalRequired.UNDEFINED_BY_USER;
                    } else if (keys.get(0).isMultiValue) {
                        for (int i = 0; i < documentsForIG.get(0).tags.size(); i++) {
                            if (documentsForIG.get(0).tags.get(i).nameOfTag.equals(keys.get(0).nameOfTag)) {
                                value = StringsFinalRequired.EMPTYSTRING + documentsForIG.get(0).tags.get(i).valuePresent;
                                addUndefined = false;
                            }
                        }
                        if (addUndefined) {
                            value = StringsFinalRequired.UNDEFINED_BY_USER;
                        }
                    }
                    if (documentsForIG.size() == 1) {
                        sortedDocument.add(documentsForIG);
                        addressOfDocs.add(address + StringsFinalRequired.SLASH_SIGN + keys.get(0).nameOfTag.toLowerCase()
                                + StringsFinalRequired.EQUAL_SIGN + value);
                    } else {
                        infoGainMiddlePhase(documentsForIG, address + StringsFinalRequired.SLASH_SIGN
                                + keys.get(0).nameOfTag + StringsFinalRequired.EQUAL_SIGN + value);
                    }
                }
            } else {
                for (int i = 0; i < documents.size(); i++) {
                    addressOfDocs.add(address);
                }
            }
        } else {
            for (int i = 0; i < documents.size(); i++) {
                addressOfDocs.add(address);
            }
        }
    }
    private static LinkedHashMap<Tags, Double> infoMapSort(HashMap<Tags, Double> sortedmap, double totalUncertainty) {
        List<Double> infoGain = new ArrayList<>(sortedmap.values());
        Comparator<Double> comparator = new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                Double uncertaintyOfOne = totalUncertainty - o1;
                Double uncertaintyOfAnother = totalUncertainty - o2;
                return uncertaintyOfOne.compareTo(uncertaintyOfAnother);
            }
        };
        Collections.sort(infoGain, comparator);
        Map<Tags, Double> sortInfoGain = new LinkedHashMap<>();
        for (int i = 0; i < infoGain.size(); i++) {
            sortInfoGain.put(RunCommandExtend.keyReturn(sortedmap, infoGain.get(i)), infoGain.get(i));
            sortedmap.remove(RunCommandExtend.keyReturn(sortedmap, infoGain.get(i)));
        }
        return (LinkedHashMap<Tags, Double>) sortInfoGain;
    }
    private static void finalOutputLexicographicArrangement(String print) {
        List<String> printList = new ArrayList<>(List.of(print.split(StringsFinalRequired.NEXT_LINE)));
        List<String> sameAddress = new ArrayList<>();
        List<Integer> indexesOfPreviousAddresses = new ArrayList<>();
        for (int i = 0; i < printList.size(); i++) {
            if (i < printList.size() - 1) {
                int indexOfLastSlash = printList.get(i).lastIndexOf(StringsFinalRequired.SLASH_SIGN.charAt(0));
                int indexOfSecondLastSlash = printList.get(i).substring(0, indexOfLastSlash).lastIndexOf(StringsFinalRequired.
                        SLASH_SIGN.charAt(0));
                int indexOfLastSlashOther = printList.get(i + 1).lastIndexOf(StringsFinalRequired.SLASH_SIGN.charAt(0));
                int indexOfSecondLastSlashOther = printList.get(i + 1).substring(0, indexOfLastSlashOther).lastIndexOf(
                        StringsFinalRequired.SLASH_SIGN.charAt(0));
                if (printList.get(i).substring(0, indexOfLastSlash).equals(printList.get(i + 1).substring(0, indexOfLastSlashOther))) {
                    if (!sameAddress.contains(printList.get(i))) {
                        sameAddress.add(printList.get(i));
                        if (!sameAddress.contains(printList.get(i + 1))) {
                            sameAddress.add(printList.get(i + 1));
                        }
                    } else if (!sameAddress.contains(printList.get(i + 1))) {
                        sameAddress.add(printList.get(i + 1));
                    }
                } else if ((printList.get(i).substring(indexOfSecondLastSlash, indexOfLastSlash).contains(StringsFinalRequired.LENGTH)
                        && printList.get(i + 1).substring(indexOfSecondLastSlashOther, indexOfLastSlashOther).contains(StringsFinalRequired.
                        LENGTH)) || (printList.get(i).substring(indexOfSecondLastSlash, indexOfLastSlash).contains(StringsFinalRequired.
                        SIZE) && printList.get(i + 1).substring(indexOfSecondLastSlashOther, indexOfLastSlashOther).
                        contains(StringsFinalRequired.SIZE)) || (printList.get(i).substring(indexOfSecondLastSlash, indexOfLastSlash).
                        contains(StringsFinalRequired.GENRE) && printList.get(i + 1).substring(indexOfSecondLastSlashOther,
                                indexOfLastSlashOther).contains(StringsFinalRequired.GENRE))) {
                    if (!sameAddress.contains(printList.get(i))) {
                        sameAddress.add(printList.get(i));
                        if (!sameAddress.contains(printList.get(i + 1))) {
                            sameAddress.add(printList.get(i + 1));
                        }
                    } else if (!sameAddress.contains(printList.get(i + 1))) {
                        sameAddress.add(printList.get(i + 1));
                    }
                } else if (!sameAddress.isEmpty()) {
                    List<String> lexicographicArrangedAddress = new ArrayList<>(sameAddress);
                    Collections.sort(lexicographicArrangedAddress);
                    for (int j = 0; j < sameAddress.size(); j++) {
                        indexesOfPreviousAddresses.add(printList.indexOf(sameAddress.get(j)));
                    }
                    for (int j = 0; j < indexesOfPreviousAddresses.size(); j++) {
                        printList.set(indexesOfPreviousAddresses.get(j), lexicographicArrangedAddress.get(j));
                    }
                    sameAddress.clear();
                    lexicographicArrangedAddress.clear();
                    indexesOfPreviousAddresses.clear();
                }
            }
        }
        if (!sameAddress.isEmpty()) {
            printList = finalOutputLexicographicArrangement(
                    (ArrayList<String>) sameAddress, (ArrayList<Integer>) indexesOfPreviousAddresses, (ArrayList<String>)
                            printList);
        }
        finalOutputArrangeAndPrint((ArrayList<String>) printList);
    }
    private static ArrayList<String> finalOutputLexicographicArrangement(ArrayList<String> sameAddress, ArrayList<Integer>
            indexesOfPreviousAddresses, ArrayList<String> printList) {
        List<String> lexicographicArrangedAddress = new ArrayList<>(sameAddress);
        Collections.sort(lexicographicArrangedAddress);
        for (int j = 0; j < sameAddress.size(); j++) {
            indexesOfPreviousAddresses.add(printList.indexOf(sameAddress.get(j)));
        }
        for (int j = 0; j < indexesOfPreviousAddresses.size(); j++) {
            printList.set(indexesOfPreviousAddresses.get(j), lexicographicArrangedAddress.get(j));
        }
        return printList;
    }
    private static void finalOutputArrangeAndPrint(ArrayList<String> printList) {
        for (int i = 0; i < printList.size(); i++) {
            System.out.println(printList.get(i).replace(StringsFinalRequired.UNDEFINED_BY_USER, StringsFinalRequired.UNDEFINED));
        }
    }
    private static void outputArrange(String print) {
        if (!print.isEmpty()) {
            List<String> printList = new ArrayList<>(List.of(print.split(StringsFinalRequired.NEXT_LINE)));
            for (int i = 0; i < printList.size(); i++) {
                if (i < printList.size() - 1) {
                    int indexOfEqualSign = printList.get(i).lastIndexOf(StringsFinalRequired.EQUAL_SIGN.charAt(0));
                    double infoGain = Double.parseDouble(printList.get(i).substring(indexOfEqualSign + 1));
                    int indexOfEqualSignOther = printList.get(i + 1).lastIndexOf(StringsFinalRequired.EQUAL_SIGN.charAt(0));
                    double infoGainOther = Double.parseDouble(printList.get(i + 1).substring(indexOfEqualSignOther + 1));
                    if (infoGainOther == infoGain) {
                        int indexOfSlashSign = printList.get(i).lastIndexOf(StringsFinalRequired.SLASH_SIGN.charAt(0));
                        int indexOfSlashSignOther = printList.get(i + 1).lastIndexOf(StringsFinalRequired.SLASH_SIGN.charAt(0));
                        String tag = printList.get(i).substring(indexOfSlashSign, indexOfEqualSign);
                        String tagAnother = printList.get(i + 1).substring(indexOfSlashSignOther, indexOfEqualSignOther);
                        List<String> tags = new ArrayList<>();
                        tags.add(tag);
                        tags.add(tagAnother);
                        Collections.sort(tags);
                        if (tags.indexOf(tag) > tags.indexOf(tagAnother)) {
                            String address = printList.get(i);
                            String addressAnother = printList.get(i + 1);
                            printList.set(i, addressAnother);
                            printList.set(i + 1, address);
                        }
                    }
                }
            }
            for (int i = 0; i < printList.size(); i++) {
                System.out.println(printList.get(i).replace(StringsFinalRequired.UNDEFINED_BY_USER, StringsFinalRequired.UNDEFINED));
            }
            RunCommandExtend.print = StringsFinalRequired.EMPTYSTRING;
        }
    }
    @Override
    public CommandResult execute(String[] commandArguments) {
        switch (commandArguments[0]) {
            case StringsFinalRequired.RUN_COMMAND:
                if (DocumentHandling.toLoadDocs) {
                    infoGainStartingPhase(DocumentsInfo.idToDoc.get((Integer.parseInt(commandArguments[1]))));
                } else {
                    System.out.println(StringsFinalRequired.ERROR_NO_FILE_FOUND);
                }
                break;
            default:
        }
        return new CommandResult(CommandResultType.SUCCESS, null);
    }
}
