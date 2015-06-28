package ravensproject.models.RavenFigureLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.models.RavenObjectLevel.ROSpatialDescCompositeKey;

import java.util.*;

/**
 * Created by guoliangwang on 6/27/15.
 * <p>
 * Given a Raven's Figure, describe the Spatial Relationships for all the Raven's Object in the Figure
 * so that corresponding Raven's Object can be identified (??)
 * <p>
 * Model class for describing a Raven's Object and its associated RO in space, which could be:
 * left-of, inside, above, overlaps, etc
 * <p>
 * Note that not all RO has spatial relationships ( must have > 1 RO inside a Raven's Figure)
 */
public class ROSpatialRelationshipsInRF implements Comparable<ROSpatialRelationshipsInRF> {

    public static Map<String, String> oppositeAttributes = new HashMap<String, String>();
    public boolean hasSpatialRelationships = false; //default: No spatial relationships in the Figure
    public static List<String> spatialAttributes = new ArrayList<String>();
    public Map<ROSpatialDescCompositeKey, Set<String>> relatedRavensObjects; //key: ROSpatialDescCompositeKey, value: List<RavensObject>
    public List<RavensObject> unrelatedRavensObjects;  //RavensObject who does not have <b>spatial relationship</b>

    private RavensFigure ravensFigure;

    //populate the spatialAttributes and oppsotieAttributes with prior knowledge
    static {
        spatialAttributes.add("above");
        spatialAttributes.add("below");
        spatialAttributes.add("left-of");
        spatialAttributes.add("right-of");
        spatialAttributes.add("inside");
        spatialAttributes.add("outside");
        spatialAttributes.add("overlaps");

        oppositeAttributes.put("above", "below");
        oppositeAttributes.put("below", "above");
        oppositeAttributes.put("left-of", "right-of");
        oppositeAttributes.put("right-of", "left-of");
        oppositeAttributes.put("overlaps", "overlaps");
        oppositeAttributes.put("inside", "outside");
        oppositeAttributes.put("outside", "inside");
    }

    public ROSpatialRelationshipsInRF() {
    }

    /**
     * In the constructor:
     * <p>
     * Break down the given RavensFigure, iterate over the RavensObjects contained in this Figure
     * If only one RavensObject, then there is no spatial relationship (this.hasSpatialRelationships = false)
     * <p>
     * If multiple RavensObjects exist, try to identify the spatial Relationships in the following way:
     * Iterate over the RavensObject, for each RavensObject, collect the associated RavensObjects as a List, and use the
     * subject RavensObject and its spatial description (e.g. 'above') as the composite key for saving in the
     *
     * @param ravensFigure
     */
    public ROSpatialRelationshipsInRF(RavensFigure ravensFigure) {
        this.ravensFigure = ravensFigure;
        this.relatedRavensObjects = new HashMap<ROSpatialDescCompositeKey, Set<String>>();
        this.unrelatedRavensObjects = new ArrayList<RavensObject>();  //spatially unrelated only!

        //this.ravensObjects = new ArrayList(ravensFigure.getObjects().values()); //save the RavensObjects in this RavensFigure
        if (this.ravensFigure.getObjects().size() == 1) {
            hasSpatialRelationships = false; // one Ravens Object cannot have spatial relationships
            this.unrelatedRavensObjects = new ArrayList(this.ravensFigure.getObjects().values()); // new HashSet<>(convertRavensObjectsToNames(this.ravensFigure.getObjects().values()));
        } else {

            //iterate the RavensObjects to see if any of them has spatial relationships --> may need to change data structure
            for (RavensObject ro : this.ravensFigure.getObjects().values()) {

                List<AttributeTuple> availableSpatialAttrs = spatialAttributesInRO(ro.getAttributes(), spatialAttributes);
                //ro contains spatial attributes
                if (availableSpatialAttrs.size() > 0) {

                    hasSpatialRelationships = true;

                    //iterate over the found spatial attributes in the RavensObject
                    for (AttributeTuple attrTuple : availableSpatialAttrs) {
                        String spatialDesc = attrTuple.spatialAttributeName;

                        //ROSpatialDescCompositeKey indicates the subject RavensObject and its spatial relationship with the associated RO
                        ROSpatialDescCompositeKey ROSpatialDescCompositeKey = new ROSpatialDescCompositeKey(ro, spatialDesc);

                        // get a String of RavensObject names, delimited by comma, then convert them to RavensObject, save to the map
                        String associatedRavensObjectNamesStr = ro.getAttributes().get(spatialDesc);
                        Set<String> associatedRavensObjectNames = new HashSet<>(convertRONamesStrToListOfRONames(associatedRavensObjectNamesStr));
                        this.relatedRavensObjects.put(ROSpatialDescCompositeKey, new HashSet<>(associatedRavensObjectNames));


                        //put the opposite attributes in the relatedRavensObject
                        //e.g. object a above b, c, d
                        // should put b, c, or d as key and indicate they are below a
                        String oppositeSpatialDesc = oppositeAttributes.get(spatialDesc);
                        //put the opposite relation RO into the map

                        if (oppositeSpatialDesc != null) {
                            for (String oppositeROName : associatedRavensObjectNames) {
                                RavensObject oppositeRO = this.ravensFigure.getObjects().get(oppositeROName);
                                ROSpatialDescCompositeKey = new ROSpatialDescCompositeKey(oppositeRO, oppositeSpatialDesc);
                                if (this.relatedRavensObjects.containsKey(ROSpatialDescCompositeKey)) {
                                    associatedRavensObjectNames = this.relatedRavensObjects.get(ROSpatialDescCompositeKey);
                                } else {
                                    associatedRavensObjectNames = new HashSet<String>();
                                }
                                associatedRavensObjectNames.add(ro.getName());
                                this.relatedRavensObjects.put(ROSpatialDescCompositeKey, associatedRavensObjectNames);
                            }
                        }
                    }
                } else {
                    //add ro to unrelated RavensObjects (spatially)
                    unrelatedRavensObjects.add(ro);
                }

            }
        }
    }


    /**
     * custom compareTo method to return the differences in ROSpatialRelationships in
     * current RavensFigure and the other RavensFigure
     *
     * @param that
     * @return
     */
    @Override
    public int compareTo(ROSpatialRelationshipsInRF that) {

        if (this.ravensFigure.getObjects().size() < that.ravensFigure.getObjects().size()) {
            return -1;
        } else if (this.ravensFigure.getObjects().size() > that.ravensFigure.getObjects().size()) {
            return 1;
        }

        if (this.relatedRavensObjects.keySet().size() < that.relatedRavensObjects.keySet().size()) {
            return -1;
        } else if (this.relatedRavensObjects.keySet().size() > that.relatedRavensObjects.keySet().size()) {
            return 1;
        }

        int numRelatedObjectsInThis = countNumRelatedObjects(this.relatedRavensObjects);
        int numRelatedObjectsInThat = countNumRelatedObjects(that.relatedRavensObjects);
        if (numRelatedObjectsInThis < numRelatedObjectsInThat) {
            return -1;
        } else if (numRelatedObjectsInThis > numRelatedObjectsInThat) {
            return 1;
        }

        //spatially unrelated objects
        if (this.unrelatedRavensObjects.size() < that.unrelatedRavensObjects.size()) {
            return -1;
        } else if (this.unrelatedRavensObjects.size() > that.unrelatedRavensObjects.size()) {
            return 1;
        }

        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROSpatialRelationshipsInRF)) {
            return false;
        }

        ROSpatialRelationshipsInRF that = (ROSpatialRelationshipsInRF) o;
        if (that.hasSpatialRelationships != this.hasSpatialRelationships) {
            return false;
        }

        //iterate through the relatedRavensObjects for comparing the spatialDesc in the key and the size of
        //the associated List of RavensObjects (value)
        Set<ROSpatialDescCompositeKey> thisKeySet = this.relatedRavensObjects.keySet();
        Set<ROSpatialDescCompositeKey> thatKeySet = that.relatedRavensObjects.keySet();
        if (thisKeySet.size() != thatKeySet.size()) return false;

        int numRelatedObjectsInThis = countNumRelatedObjects(this.relatedRavensObjects);
        int numRelatedObjectsInThat = countNumRelatedObjects(that.relatedRavensObjects);

        if (numRelatedObjectsInThat != numRelatedObjectsInThis) return false;
        if (this.unrelatedRavensObjects.size() != that.unrelatedRavensObjects.size()) return false;

        return true;
    }


    //getter
    public RavensFigure getRavensFigure() {
        return ravensFigure;
    }


    /****** private helper methods ******/

    /**
     * @param RavensOjectNamesStr, String, RavensObject names delimited by comma
     * @return List of RavensObject Names contained in current RavensFigure, could an empty List (?)
     */
    private List<String> convertRONamesStrToListOfRONames(String RavensOjectNamesStr) {
        List<String> RavensObjectsInThisRF = new ArrayList<String>();
        Map<String, RavensObject> currRavensObjectsMap = this.ravensFigure.getObjects();
        String[] RONames = RavensOjectNamesStr.trim().split(","); //RavensObject names array of String
        for (String name : RONames) {
            if (currRavensObjectsMap.containsKey(name)) {
                RavensObjectsInThisRF.add(name);
            }
        }

        return RavensObjectsInThisRF;
    }


    private List<String> convertRavensObjectsToNames(Collection<RavensObject> ravensObjects) {
        List<String> ravenObjectNames = new ArrayList<String>();
        for (RavensObject object : ravensObjects) {
            ravenObjectNames.add(object.getName());
        }
        return ravenObjectNames;
    }


    private int countNumRelatedObjects(Map<ROSpatialDescCompositeKey, Set<String>> relatedRavensObjects) {
        int numRavensObjects = 0;

        if (relatedRavensObjects.size() > 0) {
            for (Set<String> ravensObjectNames : relatedRavensObjects.values()) {
                numRavensObjects += ravensObjectNames.size();
            }
        }
        return numRavensObjects;
    }


    /**
     * @param curRavensObjectAttributes
     * @return
     */
    private List<AttributeTuple> spatialAttributesInRO(Map<String, String> curRavensObjectAttributes, List<String> curSpatialAttributes) {

        List<AttributeTuple> availableSpatialAttrs = new ArrayList<>();

        for (String spatialAttr : curSpatialAttributes) {
            if (curRavensObjectAttributes.containsKey(spatialAttr)) {
                availableSpatialAttrs.add(new AttributeTuple(spatialAttr, true));
            }
        }

        return availableSpatialAttrs;
    }


    /**
     * Tuple for indicating if an attribute in a RavensObject exists or not
     */
    private class AttributeTuple {
        public String spatialAttributeName;
        public boolean exists;

        public AttributeTuple() {
        }

        public AttributeTuple(String spatialAttributeName, boolean exists) {
            this.spatialAttributeName = spatialAttributeName;
            this.exists = exists;
        }
    }

}
