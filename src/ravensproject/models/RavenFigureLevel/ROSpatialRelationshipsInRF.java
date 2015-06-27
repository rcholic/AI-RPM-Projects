package ravensproject.models.RavenFigureLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Given a Raven's Figure, describe the Spatial Relationships for all the Raven's Object in the Figure
 *
 * Model class for describing a Raven's Object and its associated RO in space, which could be:
 * left-of, inside, above, overlaps, etc
 *
 * Note that not all RO has spatial relationships ( must have > 1 RO inside a Raven's Figure)
 *
 *
 */
public class ROSpatialRelationshipsInRF {


    private RavensObject ravensObject;
    private RavensFigure ravensFigure;
    private boolean hasSpatialRelationships;
    private static List<String> spatialAttributes = new ArrayList<String>();
    private List<RavensObject> relatedRavensObjects;
    private List<String> foundSpatialRelationship = null; // null if no spatialRelationships


    //populate the spatialAttributes with prior knowledge
    static {
        spatialAttributes.add("above");
        spatialAttributes.add("left-of");
        spatialAttributes.add("inside");
        spatialAttributes.add("overlaps");
        spatialAttributes.add("right-of");
        spatialAttributes.add("below");
        spatialAttributes.add("under");
        spatialAttributes.add("underneath");
    }

    public ROSpatialRelationshipsInRF() {}

    public ROSpatialRelationshipsInRF(RavensFigure ravensFigure) {
        this.ravensFigure = ravensFigure;
        if (this.ravensFigure.getObjects().size() == 1) {
            hasSpatialRelationships = false; // one Ravens Object cannot have spatial relationships
        } else {
            //iterate the RavensObjects to see if any of them has spatial relationships --> may need to change data structure
            for (RavensObject ro : this.ravensFigure.getObjects().values()) {
                for (String spatialDesc : spatialAttributes) {
                    if (ro.getAttributes().containsKey(spatialDesc)) {
                        if (!hasSpatialRelationships) {
                            //initialize only once
                            foundSpatialRelationship = new ArrayList<String>();
                        }
                        hasSpatialRelationships = true;
                        foundSpatialRelationship.add(spatialDesc);
                    }
                }
            }
        }
    }



}
