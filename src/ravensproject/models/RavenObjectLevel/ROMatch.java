package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.models.RavenFigureLevel.ROSpatialRelationshipsInRF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<CorrespondingRO> matchedROs;
    public List<RavensObject> unmatchedROs;

    public ROMatch() {}

    public ROMatch(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
        this.ravensFigure1 = ravensFigure1;
        this.figure1Spatial = new ROSpatialRelationshipsInRF(this.ravensFigure1);
        this.ravensFigure2 = ravensFigure2;
        this.figure2Spatial = new ROSpatialRelationshipsInRF(this.ravensFigure2);
    }

    /**
     * could be empty matches
     * @return Map
     */
    public List<CorrespondingRO> getMatchedROs() {
        matchedROs = new ArrayList<CorrespondingRO>();

        if (figure1Spatial.unrelatedRavensObjects.size() == figure2Spatial.unrelatedRavensObjects.size()) {
            for (int i = 0; i < figure1Spatial.unrelatedRavensObjects.size(); i++) {
                matchedROs.add(new CorrespondingRO(figure1Spatial.unrelatedRavensObjects.get(i), figure2Spatial.unrelatedRavensObjects.get(i)));
            }
        }

        //convert the keySet to List for better iteration
        List<ROSpatialDescCompositeKey> figure1RelatedObjectKeys = new ArrayList<>();
        figure1RelatedObjectKeys.addAll(figure1Spatial.relatedRavensObjects.keySet());

        List<ROSpatialDescCompositeKey> figure2RelatedObjectKeys = new ArrayList<>();
        figure2RelatedObjectKeys.addAll(figure2Spatial.relatedRavensObjects.keySet());





        return matchedROs;

    }

    public List<RavensObject> getUnmatchedROs() {
        unmatchedROs = new ArrayList<RavensObject>();


        return unmatchedROs;

    }
}
