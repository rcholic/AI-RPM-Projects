package ravensproject.models;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

import java.util.List;
import java.util.Map;

/**
 * Created by guoliangwang on 6/24/15.
 */
public class FigureTransformationRule {

    private RavensFigure ravensFigure1 = null;
    private RavensFigure ravensFigure2 = null;
    private int numObjectDiff;
    private ObjectTransformation objectTransformation = null; // should be a List or Map ?


    public FigureTransformationRule(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
        this.ravensFigure1 = ravensFigure1;
        this.ravensFigure2 = ravensFigure2;
        this.numObjectDiff = 0; //no difference in number of RavensObject in the two figures

        //TODO: perform the comparison
    }

    /**
     * get matched RavensObject by ranking the similarities of RavensObject
     * in the two RavensFigure(s) -- uses the similarityScore method in
     * ObjectTransformation class ==> ranking the objects to get the best match
     *
     * key: the index of RavensObject in each RavensFigure
     * value: the two matched RavensObject
     *
     * @return
     */
    public Map<Integer, List<RavensObject>> getMatchingObjects() {

        Map<Integer, List<RavensObject>> matchedObjects = null;
        if (this.ravensFigure1 == null || this.ravensFigure2 == null) {
            return matchedObjects;
        }


        return matchedObjects;
    }


}
