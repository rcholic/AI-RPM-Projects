package ravensproject.models.RavenFigureLevel;

import ravensproject.RavensFigure;
import ravensproject.models.RavenObjectLevel.CorrespondingRO;
import ravensproject.models.RavenObjectLevel.ROTransformation;
import ravensproject.models.RavenObjectLevel.ROTransformationInterface;
import ravensproject.models.RavenObjectLevel.RavensFigObjNameComposite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * @author guoliangwang
 *
 * Model class for Raven's Figure Transformation
 *
 *
 */
public class RFTransformation {

    // private List<ROSpatialRelationshipsInRF> spatialRelationships;
    private RavensFigure ravensFigure1;
    private RavensFigure ravensFigure2;
    private ROMatcher roMatcher;
    private int numDiffObjects;

    public RFTransformation() {}

    public RFTransformation(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
        this.ravensFigure1 = ravensFigure1;
        this.ravensFigure2 = ravensFigure2;
        this.roMatcher = new ROMatcher(ravensFigure1, ravensFigure2);
        this.numDiffObjects = this.ravensFigure2.getObjects().size() - this.ravensFigure1.getObjects().size();
    }

    /**
     * Calculate the difference in the number of
     * RavensObjects contained between the two RavensFigures
     *
     * @return int
     */
    public int numObjectsDiff() {
        return this.numDiffObjects;
    }

    public Set<RavensFigObjNameComposite> getUnmatchedRavensObjects() {
        return this.roMatcher.getUnmatchedROs();
    }

    public Set<CorrespondingRO> matchedRavensObjects() {
        return this.roMatcher.getMatchedROs();
    }


    /**
     * Compile the transformations for all the matched RavensObjects in
     * the two RavensFigures
     *
     * @return
     */
    public List<ROTransformationInterface> compileROTransformationsInMatchedObjects() {
        ROTransformation curROTransformation = null;
        List<ROTransformationInterface> compiledObjTransfomsBetweenFigures = new ArrayList<>();

        for (CorrespondingRO cro : this.matchedRavensObjects()) {
            curROTransformation = new ROTransformation(cro.getRavensObject1(), cro.getRavensObject2());
            compiledObjTransfomsBetweenFigures.addAll(curROTransformation.getAllTransformations());
        }

        return compiledObjTransfomsBetweenFigures;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RFTransformation)) {
            return false;
        }
        RFTransformation that = (RFTransformation) o;
        if (that.numDiffObjects != this.numDiffObjects) {
            return false;
        }
        if (this.compileROTransformationsInMatchedObjects().size() != that.compileROTransformationsInMatchedObjects().size()) {
            return false;
        }
        return this.matchedRavensObjects().size() == that.matchedRavensObjects().size();
    }

    public int scoreDifferenceInTransformation(RFTransformation transformation) {
        //TODO: score the differences between Figure Transformations
        List<ROTransformationInterface> anotherCompiledTransForm = transformation.compileROTransformationsInMatchedObjects();
        int score = 0;
        int shorterLength = Math.min(anotherCompiledTransForm.size(), this.compileROTransformationsInMatchedObjects().size());
        for (int i = 0; i < shorterLength; i++) {
            ROTransformationInterface transform1 = anotherCompiledTransForm.get(i);
            ROTransformationInterface transform2 = this.compileROTransformationsInMatchedObjects().get(i);
        }


        return score;
    }






}
