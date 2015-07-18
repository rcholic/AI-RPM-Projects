package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Model class for describing the size changes between Raven's Objects
 *
 *
 *
 */
public class ROSizeChange implements ROTransformationInterface {

    private final static String RO_ATTRIBUTE_KEY = "size";
    private final static int NON_COMPARABLE_SIZEDIFF = -100;
    private static Map<String, Integer> sizeStringIntegerTable;
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    //initialize the sizeStringIntegerTable with prior knowledge for the size description of Raven's Object,
    // for quantifying the String description to Integers for accurate size comparisons
    static {
        sizeStringIntegerTable = new HashMap<String, Integer>();
        sizeStringIntegerTable.put("tiny", 0);
        sizeStringIntegerTable.put("very small", 1);
        sizeStringIntegerTable.put("small", 2);
        sizeStringIntegerTable.put("medium", 3);
        sizeStringIntegerTable.put("large", 4);
        sizeStringIntegerTable.put("very large", 5);
        sizeStringIntegerTable.put("huge", 6);
        sizeStringIntegerTable.put("gigantic", 7);
    }

    //default constructor
    public ROSizeChange() {}

    //constructor using two Raven's Objects
    public ROSizeChange(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }


    /**
     * Get size difference in the order of (Raven's Object 2 - Raven's Object 1)
     * size difference or NON_COMPARABLE_SIZEDIFF if the size attribute does not exist in the
     * Raven's Object(s) or the sizeStringIntegerTable doesn't contain their size description
     *
     * @return Integer
     */
    public int sizeDifference() {

        if (ravensObject1.getAttributes().containsKey(RO_ATTRIBUTE_KEY) &&
                ravensObject2.getAttributes().containsKey(RO_ATTRIBUTE_KEY)) {
            String object1Size = ravensObject1.getAttributes().get(RO_ATTRIBUTE_KEY);
            String object2Size = ravensObject2.getAttributes().get(RO_ATTRIBUTE_KEY);

            if (sizeStringIntegerTable.containsKey(object1Size) && sizeStringIntegerTable.containsKey(object2Size)) {
                return sizeStringIntegerTable.get(object2Size) - sizeStringIntegerTable.get(object1Size);
            } else {
                return NON_COMPARABLE_SIZEDIFF;
            }
        } else {
            return NON_COMPARABLE_SIZEDIFF; //no size attribute, no need to compare size
        }
    }

    @Override
    public String getAttributeKeyName() {
        return RO_ATTRIBUTE_KEY;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROSizeChange)) {
            return false;
        }

        ROSizeChange that = (ROSizeChange) o;
        return this.sizeDifference() == that.sizeDifference();
    }



    /**
     * Given the firstObjectSize (in the Raven's Figure on the left or the top) and sizeDifference,
     * predict the size description for the corresponding Raven's Object in the Raven's Figure on the right or the bottom
     *
     * @param firstObjectSizeStr, String
     * @param sizeDifference, int
     * @return
     */
    public static String predictSecondObjectSize(String firstObjectSizeStr, int sizeDifference) {

        String secondObjectSizeStr = firstObjectSizeStr; //default to firstObjectSize
        int firstObjectSize = convertSizeDescriptionToInt(firstObjectSizeStr);

        //if the sizeDifference is NON_COMPARABLE_SIZEDIFF, return the firstObjectSize
        if (sizeDifference == NON_COMPARABLE_SIZEDIFF || firstObjectSize == NON_COMPARABLE_SIZEDIFF) {
            return secondObjectSizeStr;
        } else {

            int secondObjectSize = firstObjectSize + sizeDifference;

            for (Map.Entry<String, Integer> mapEntry : sizeStringIntegerTable.entrySet()) {
                if (mapEntry.getValue() == secondObjectSize) {
                    //found the size for the second Raven's Object
                    secondObjectSizeStr = mapEntry.getKey();
                    break;
                }
            }

        }

        return secondObjectSizeStr;
    }

    @Override
    public String toString() {
        return "sizeDifference: " + this.sizeDifference();
    }



    /**** private helper methods below *****/

    /**
     * Given a size description String, convert it to integer value
     *
     * @param sizeDescription
     * @return
     */
    private static int convertSizeDescriptionToInt(String sizeDescription) {
        if (sizeStringIntegerTable.containsKey(sizeDescription)) {
            return sizeStringIntegerTable.get(sizeDescription);
        }

        return NON_COMPARABLE_SIZEDIFF;
    }
}
