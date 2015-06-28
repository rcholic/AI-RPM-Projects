package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.*;

/**
 * Created by guoliangwang on 6/28/15.
 */
public class SpatialAttrFactory {

    public static List<String> spatialAttributes = new ArrayList<>();
    public static Map<String, String> oppositeAttributes = new HashMap<String, String>();


    public static List<String> getSpatialAttributes() {
        spatialAttributes.add("above");
        spatialAttributes.add("below");
        spatialAttributes.add("left-of");
        spatialAttributes.add("right-of");
        spatialAttributes.add("inside");
        spatialAttributes.add("outside");
        spatialAttributes.add("overlaps");

        return spatialAttributes;
    }

    public static Map<String, String> getOppositeAttributes() {
        oppositeAttributes.put("above", "below");
        oppositeAttributes.put("below", "above");
        oppositeAttributes.put("left-of", "right-of");
        oppositeAttributes.put("right-of", "left-of");
        oppositeAttributes.put("overlaps", "overlaps");
        oppositeAttributes.put("inside", "outside");
        oppositeAttributes.put("outside", "inside");

        return oppositeAttributes;
    }

    public static List<String> findSpatialAttrInRavensObject(RavensObject ravensObject) {
        List<String> foundSpatialAttrs = new ArrayList<>();
        Set<String> objectAttrs = ravensObject.getAttributes().keySet();

        for (String spatialAttr : getSpatialAttributes()) {
            if (objectAttrs.contains(spatialAttr)) {
                foundSpatialAttrs.add(spatialAttr);
            }
        }
        return foundSpatialAttrs;
    }

    public static String convertListOfStringToString(List<String> listOfStrings) {
        String results = "";
        for (String str : listOfStrings) {
            results += str + "\t";
        }

        return results;
    }


}
