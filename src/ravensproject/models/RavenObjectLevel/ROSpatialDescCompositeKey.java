package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;
import ravensproject.models.RavenFigureLevel.ROSpatialRelationshipsInRF;

import java.util.*;

/**
 * for generating a composite key for describing the spatial relationship between a
 * RavensObject and the associated RavensObjects
 *
 * e.g. 'RavensObject A' "above" RavensObject B
 */
public class ROSpatialDescCompositeKey implements Comparable<ROSpatialDescCompositeKey>,
        Comparator<ROSpatialDescCompositeKey> {
    private RavensObject ravensObject;
    private String spatialDesc;
    private String ravensObjectName;

    public ROSpatialDescCompositeKey() {}

    public ROSpatialDescCompositeKey(RavensObject ravensObject, String spatialDesc) {
        this.ravensObject = ravensObject;
        this.spatialDesc = spatialDesc;
        this.ravensObjectName = this.ravensObject.getName();
    }

    public ROSpatialDescCompositeKey(String ravensObjectName, String spatialDesc) {
        this.ravensObjectName = ravensObjectName;
        this.spatialDesc = spatialDesc;
    }

    /**
     * for custom comparator - sort by both object names and spatialDesc
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(ROSpatialDescCompositeKey o1, ROSpatialDescCompositeKey o2) {

        return (o1.ravensObjectName + o1.spatialDesc).compareTo(o2.ravensObjectName + o2.spatialDesc);
    }

    /**
     * for sorting by objectName only
     * @param that
     * @return
     */
    @Override
    public int compareTo(ROSpatialDescCompositeKey that) {
        return this.ravensObject.getName().compareTo(that.ravensObject.getName());
    }

    /**
     * Compares the spatialDesc for equality regardless of RavensObject's name
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROSpatialDescCompositeKey)) {
            return false;
        }

        ROSpatialDescCompositeKey that = (ROSpatialDescCompositeKey) o;
        return that.spatialDesc.equalsIgnoreCase(this.spatialDesc);

        //&& that.ravensObject.getName().equals(this.ravensObject.getName());
    }

    @Override
    public int hashCode() {
        return (this.ravensObjectName + this.spatialDesc).hashCode();
    }


    @Override
    public String toString() {
        return "Object name: " + this.ravensObjectName +
                "| spatialDesc: " + this.spatialDesc;
    }

    public RavensObject getRavensObject() {
        return ravensObject;
    }

    public String getSpatialDesc() {
        return spatialDesc;
    }

    public static void main(String[] args) {
        //some tests
        ROSpatialDescCompositeKey key1 = new ROSpatialDescCompositeKey("object1", "above");
        ROSpatialDescCompositeKey key2 = new ROSpatialDescCompositeKey("object1", "left-of");
        ROSpatialDescCompositeKey key3 = new ROSpatialDescCompositeKey("object3", "below");

        List<ROSpatialDescCompositeKey> keys = new ArrayList<>();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);

        if (keys.contains(key3)) {
            System.out.println("keys contains key3");
        }

        Map<ROSpatialDescCompositeKey, String> myMap = new HashMap<>();
        myMap.put(key1, key1.getSpatialDesc());
        myMap.put(key2, key2.getSpatialDesc());
        myMap.put(key3, key3.getSpatialDesc());

        System.out.println(myMap.get(key2));

        System.out.println(key1.equals(key2));
    }
}
