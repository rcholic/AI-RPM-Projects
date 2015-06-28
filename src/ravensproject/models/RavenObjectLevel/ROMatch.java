package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.models.RavenFigureLevel.ROSpatialRelationshipsInRF;

import java.util.*;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Given two RavensFigures, find out the matched RavensObjects
 *
 */
public class ROMatch {
    private RavensFigure ravensFigure1;
    private RavensFigure ravensFigure2;

    private ROSpatialRelationshipsInRF figure1Spatial;
    private ROSpatialRelationshipsInRF figure2Spatial;
    public Set<CorrespondingRO> matchedROs;
    // public List<RavensObject> unmatchedROs;
    public Set<RavensFigObjNameComposite> unmatchedROs;

    public ROMatch() {}

    public ROMatch(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
        this.ravensFigure1 = ravensFigure1;
        this.figure1Spatial = new ROSpatialRelationshipsInRF(this.ravensFigure1);
        this.ravensFigure2 = ravensFigure2;
        this.figure2Spatial = new ROSpatialRelationshipsInRF(this.ravensFigure2);

        matchedROs = new HashSet<>();
//        unmatchedROs = new ArrayList<RavensObject>();
        unmatchedROs = new HashSet<>();
    }

    /**
     *
     * Get a List of matched RavensObjects, data type is List<CorrespondingRO>
     * @return Set<CorrespondingRO>, could be empty matches
     */
    public Set<CorrespondingRO> getMatchedROs() {
        //spatially unrelated RavensObject should be no more than one in each Figure (??!!) -- big assumption
        System.out.println("spatially unrelated objects, figure 1 has " + figure1Spatial.unrelatedRavensObjects.size()
        + ", figure 2 has " + figure2Spatial.unrelatedRavensObjects.size());

        if (figure1Spatial.unrelatedRavensObjects.size() == figure2Spatial.unrelatedRavensObjects.size()) {
            for (int i = 0; i < figure1Spatial.unrelatedRavensObjects.size(); i++) {
                String objectNameInFig1 = this.figure1Spatial.unrelatedRavensObjects.get(i).getName();
                String objectNameInFig2 = this.figure2Spatial.unrelatedRavensObjects.get(i).getName();
                matchedROs.add(new CorrespondingRO(this.ravensFigure1, objectNameInFig1, this.ravensFigure2, objectNameInFig2));
            }
        }

        //handle the spatially related RavensObjects below
        //convert the keySet to List for better iteration
        List<ROSpatialDescCompositeKey> figure1RelatedObjectKeys = new ArrayList<>();
        figure1RelatedObjectKeys.addAll(figure1Spatial.relatedRavensObjects.keySet()); //convert set to List
        Collections.sort(figure1RelatedObjectKeys);
        System.out.println("spatially related ObjectKeys1: " + figure1RelatedObjectKeys);

        List<ROSpatialDescCompositeKey> figure2RelatedObjectKeys = new ArrayList<>();
        figure2RelatedObjectKeys.addAll(figure2Spatial.relatedRavensObjects.keySet()); //convert set to List
        Collections.sort(figure2RelatedObjectKeys);
        System.out.println("spatially related ObjectKeys2: " + figure2RelatedObjectKeys);

        if (figure1RelatedObjectKeys.size() == figure2RelatedObjectKeys.size()) {
            System.out.println("the number of spatially related RO in Figure 1 IS EQUAL to those in Figure 2!");

            for (int i = 0; i < figure1RelatedObjectKeys.size(); i++) {
                ROSpatialDescCompositeKey key1 = figure1RelatedObjectKeys.get(i);
                ROSpatialDescCompositeKey key2 = figure2RelatedObjectKeys.get(i);

                //check if key1 is equal to key2 (in terms of spatial relationship: e.g. above) and
                // their associated RavensObjects' names (a Set<String> for equality)
                if (key1.equals(key2) && figure1Spatial.relatedRavensObjects.get(key1).size() == (figure2Spatial.relatedRavensObjects.get(key2).size())) {
                    RavensObject relatedObjectInFig1 = key1.getRavensObject();
                    RavensObject relatedObjectInFig2 = key2.getRavensObject();

                    //add the matched RavensObject
                    matchedROs.add(new CorrespondingRO(this.ravensFigure1,
                            relatedObjectInFig1.getName(), this.ravensFigure2, relatedObjectInFig2.getName()));
                } else {
                    //the unmatchedObject must in the RavensFigure that has more RavensObjects -- assumption!
                    RavensObject unmatchedObject = null;
                    RavensFigure ownerRF = null;
                    if (this.ravensFigure1.getObjects().size() > this.ravensFigure2.getObjects().size()) {
                        unmatchedObject = key1.getRavensObject();
                        ownerRF = this.ravensFigure1;
                    } else if (this.ravensFigure1.getObjects().size() <= this.ravensFigure2.getObjects().size()) {
                        unmatchedObject = key2.getRavensObject();
                        ownerRF = this.ravensFigure2;
                    }
                    unmatchedROs.add(new RavensFigObjNameComposite(ownerRF, unmatchedObject.getName()));
                }
            }

        } else {
            System.out.println("the number of spatially related RO in Figure 1 is not equal to those in Figure 2!");
        }

        return matchedROs;

    }


    /**
     * Only used when the number of RavensObjects is not the same in two RavensFigures
     *
     * this method should be called after calling getMatchedROs()
     *
     * @return
     */
    public Set<RavensFigObjNameComposite> getUnmatchedROs() {
        Set<CorrespondingRO> matchedObjects = getMatchedROs();
        List<String> objectNamesInFig1 = new ArrayList(this.ravensFigure1.getObjects().keySet());
        List<String> objectNamesInFig2 = new ArrayList(this.ravensFigure2.getObjects().keySet());
        System.out.println("total num of object names in Fig1: " + objectNamesInFig1.size());
        System.out.println("total num of object name in Fig2: " + objectNamesInFig2.size());

        //remove matched RavensObjects, leave unmatched ROs
        for (CorrespondingRO cro : matchedObjects) {
            String object1Name = cro.getRavensObject1().getName();
            String object2Name = cro.getRavensObject2().getName();
            objectNamesInFig1.remove(object1Name);
            objectNamesInFig2.remove(object2Name);
        }

        for (String name : objectNamesInFig1) {
            unmatchedROs.add(new RavensFigObjNameComposite(this.ravensFigure1, name));
            System.out.println(name);
        }
        for (String name : objectNamesInFig2) {
            System.out.println(name);
            unmatchedROs.add(new RavensFigObjNameComposite(this.ravensFigure2, name));
        }

        return unmatchedROs;
    }


    private List<List<String>> getAllMatchedRavensObjects() {
        List<List<String>> matchedRONamesByFigures = new ArrayList<List<String>>();

        List<String> roNamesInFig1 = new ArrayList<>();
        List<String> roNamesInFig2 = new ArrayList<>();

        if (matchedROs != null && matchedROs.size() > 0) {
            for (CorrespondingRO matchedRO : matchedROs) {
                roNamesInFig1.add(matchedRO.getRavensObject1().getName());
                roNamesInFig2.add(matchedRO.getRavensObject2().getName());

            }
        }

        matchedRONamesByFigures.add(0, roNamesInFig1); //index 0
        matchedRONamesByFigures.add(1, roNamesInFig2); //index 1
        return matchedRONamesByFigures;
    }

    public static void main(String[] args) {
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        set1.add("b");
        set1.add("a");
        set1.add("z");

        set2.add("z");
        set2.add("a");
        set2.add("b");

        List<String> names = new ArrayList<>();
        names.add(0, "Tom");
        names.add(1, "tony");

        System.out.println(names);
        System.out.println(set1.equals(set2));

    }
}
