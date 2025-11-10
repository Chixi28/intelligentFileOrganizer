package objectsforsorting;

/**
 * Class which deals with all the aspects of a Tag.
 * @author ushug
 */
public class Tags extends Files {
    /**
     * value of the Tag.
     */
    public Object valuePresent;
    /**
     * name of the Tag.
     */
    public String nameOfTag;
    /**
     * if the Tag is a label.
     */
    public boolean isLabel = false;
    /**
     * if the tag is a multi-value tag.
     */
    public boolean isMultiValue = false;
    /**
     * If the tag is numeric.
     */
    public boolean isNumeric = false;
    /**
     * Numeric value of the numeric Tag.
     */
    public int numericVal = 0;
    /**
     * public default constructor of the Tag.
     */

    public Tags() {
    }
}
