package ravensproject.models.RavenFigureLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.models.RavenFigureLevel.ROSpatialRelationshipsInRF;
import ravensproject.models.RavenObjectLevel.CorrespondingRO;
import ravensproject.models.RavenObjectLevel.ROSpatialDescCompositeKey;
import ravensproject.models.RavenObjectLevel.RavensFigObjNameComposite;

import java.util.*;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Given two RavensFigures, find out the matched RavensObjects
 * by Spatial Relationships
 *
 */
public class ROMatcher {
    private RavensFigure ravensFigure1;
    private RavensFigure ravensFigure2;

    private ROSpatialRelationshipsInRF figure1Spatial;
    private ROSpatialRelationshipsInRF figure2Spatial;
    private Set<CorrespondingRO> matchedROs;
    // public List<RavensObject> unmatchedROs;
    private Set<RavensFigObjNameComposite> unmatchedROs;

    public ROMatcher() {}

    public ROMatcher(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
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
        Collections.sort(figure1RelatedObjectKeys, new ROSpatialDescCompositeKey()); //use comparator
        System.out.println("spatially related ObjectKeys1: " + figure1RelatedObjectKeys);

        List<ROSpatialDescCompositeKey> figure2RelatedObjectKeys = new ArrayList<>();
        figure2RelatedObjectKeys.addAll(figure2Spatial.relatedRavensObjects.keySet()); //convert set to List
        Collections.sort(figure2RelatedObjectKeys, new ROSpatialDescCompositeKey()); //use comparator for sorting
        System.out.println("spatially related ObjectKeys2: " + figure2RelatedObjectKeys);
        matchedROs.addAll(findMatchedROsInFigures(figure1RelatedObjectKeys, figure2RelatedObjectKeys));

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


    /**
     * Get all the matched RavensObjects as categoried by their owning RavensFigures
     * index 0 stores the matched RavensObjects in RavensFigure 1
     * index 1 stores the matched RavensObjects in RavensFigure 1
     * @return
     */
    private List<List<String>> getAllMatchedRavensObjectNames() {
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


    /**
     * Given two RavensFigures and the Keys (ROSpatialCompositeKey) of the identified spatially related RavensObjects,
     * convert the related RavensObjects to matched objects of CorrespondingRO data type
     *
     * @param figure1RelatedObjectKeys
     * @param ravensFigure1
     * @param figure2RelatedObjectKeys
     * @param ravensFigure2
     * @return List<CorrespondingRO>
     */
    private List<CorrespondingRO> findMatchedROsInFigures(List<ROSpatialDescCompositeKey> figure1RelatedObjectKeys,
                                                          List<ROSpatialDescCompositeKey> figure2RelatedObjectKeys
                                                          ) {

        List<CorrespondingRO> foundMatches = new ArrayList<>();
        int shortestLength = Math.min(figure1RelatedObjectKeys.size(), figure2RelatedObjectKeys.size());

        for (int i = 0; i < shortestLength; i++) {
            ROSpatialDescCompositeKey key1 = figure1RelatedObjectKeys.get(i);
            ROSpatialDescCompositeKey key2 = figure2RelatedObjectKeys.get(i);
            if (key1.getSpatialDesc().equals(key2.getSpatialDesc()) && figure1Spatial.relatedRavensObjects.get(key1).size() == (figure2Spatial.relatedRavensObjects.get(key2).size())) {
              //  System.out.println("found matched RavensObjects: Figure 1 has " + key1.toString() + ", Figure 2 has " + key2.toString());
                foundMatches.add(new CorrespondingRO(this.ravensFigure1, key1.getRavensObject().getName(), this.ravensFigure2, key2.getRavensObject().getName()));
            } else {
              //  System.out.println("unmatched RavensObjects: Figure has " + key1.toString() + ", Figure 2 has " + key2.toString());
            }
        }

        return foundMatches;
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
