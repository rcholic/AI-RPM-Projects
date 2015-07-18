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
    private static Map<String, String> dimensionToSizeStr; //key: concatenation of widthheight
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

        dimensionToSizeStr = new HashMap<>();
        dimensionToSizeStr.put("largesmall", "large");
        dimensionToSizeStr.put("hugesmall", "huge");
        dimensionToSizeStr.put("smalllarge", "small");
        dimensionToSizeStr.put("hugelarge", "huge");
        dimensionToSizeStr.put("smallhuge", "small");
        dimensionToSizeStr.put("largehuge", "large");
        dimensionToSizeStr.put("hugesmall", "large");

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

        String object1Size = null;
        String object2Size = null;
        String height1, width1, height2, width2;

        if (ravensObject1.getAttributes().containsKey(RO_ATTRIBUTE_KEY) &&
                ravensObject2.getAttributes().containsKey(RO_ATTRIBUTE_KEY)) {
            object1Size = ravensObject1.getAttributes().get(RO_ATTRIBUTE_KEY);
            object2Size = ravensObject2.getAttributes().get(RO_ATTRIBUTE_KEY);

        } else if(ravensObject1.getAttributes().containsKey("width") && ravensObject2.getAttributes().containsKey("width")
                && ravensObject1.getAttributes().containsKey("height") && ravensObject2.getAttributes().containsKey("height")) {
            //check on height & width if available
            height1 = ravensObject1.getAttributes().get("height");
            width1 = ravensObject1.getAttributes().get("width");
            height2 = ravensObject2.getAttributes().get("height");
            width2 = ravensObject2.getAttributes().get("width");

            if (dimensionToSizeStr.containsKey(width1+height1) &&
                    dimensionToSizeStr.containsKey(width2+height2)) {
                object1Size = dimensionToSizeStr.get(width1 + height1);
                object2Size = dimensionToSizeStr.get(width2 + height2);
                System.out.println("with width & height, object1Size: " + object1Size +
                ", object2Size: " + object2Size);
            }
        }

        if (object1Size != null && object2Size != null &&
                sizeStringIntegerTable.containsKey(object1Size) && sizeStringIntegerTable.containsKey(object2Size)) {
            return sizeStringIntegerTable.get(object2Size) - sizeStringIntegerTable.get(object1Size);
        }

        return NON_COMPARABLE_SIZEDIFF;
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
        if (this.sizeDifference() == NON_COMPARABLE_SIZEDIFF ||
                that.sizeDifference() == NON_COMPARABLE_SIZEDIFF) {
            return false;
        }
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
