package objectsforsorting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class saves all the required information of the documents loaded.
 * @author ushug
 */
public final class DocumentsInfo {
    /**
     * List of the files present.
     */
    public static List<Document> listOfFiles = new ArrayList<>();
    /**
     * File id given to each file.
     */
    public static int id = 0;
    /**
     * A hash map from id of the file to documents present in the specific file.
     */
    public static Map<Integer, ArrayList<Document>> idToDoc = new HashMap<>();
    /**
     * A hash map from the directory of the file to the id of the file.
     */
    public static Map<String, Integer> fileToId = new HashMap<>();
    /**
     * A list of all the tags present in the documents.
     */
    public static ArrayList<ArrayList<String>> tagsPresentInDoc = new ArrayList<>();
    private DocumentsInfo() {
    }


}
