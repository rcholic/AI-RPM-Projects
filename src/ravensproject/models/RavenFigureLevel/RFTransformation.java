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
            compiledObjTransfomsBetweenFigures.addAll(curROTransformation.getChangedTransformations());
        }

        return compiledObjTransfomsBetweenFigures;
    }


    /**
     * unchanged transformations between objects (e.g. square to square)
     * @return
     */
    public List<ROTransformationInterface> compileUnChangedROTransformationInMatchedObjects() {

        ROTransformation curROTransformation = null;
        List<ROTransformationInterface> compiledObjUnchangedTransfomsBetweenFigures = new ArrayList<>();

        for (CorrespondingRO cro : this.matchedRavensObjects()) {
            curROTransformation = new ROTransformation(cro.getRavensObject1(), cro.getRavensObject2());
            compiledObjUnchangedTransfomsBetweenFigures.addAll(curROTransformation.getUnchangedTransformations());
        }

        return compiledObjUnchangedTransfomsBetweenFigures;

    }

    /**
     * this may be flawed for equation!
     * @param o
     * @return
     */
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

    /**
     * Score the transformation at the matched object level between two Raven's Figures
     * @param transformation
     * @return int
     */
    public int scoreRFTransformations(RFTransformation transformation) {
        List<ROTransformationInterface> anotherCompiledTransForm = transformation.compileROTransformationsInMatchedObjects();
        int score = 0;
        int shorterLength = Math.min(anotherCompiledTransForm.size(), this.compileROTransformationsInMatchedObjects().size());
        ROTransformationInterface transform1;
        ROTransformationInterface transform2;
        for (int i = 0; i < shorterLength; i++) {
            transform1 = anotherCompiledTransForm.get(i);
            transform2 = this.compileROTransformationsInMatchedObjects().get(i);

            if (transform1.getAttributeKeyName().equals(transform2.getAttributeKeyName())) {
               // System.out.println("transforms key are the same: " + transform1.getAttributeKeyName());
                 score += 1; //if the key (e.g. size) are the same, increment the score by 1

                if (transform1.equals(transform2)) {
                    System.out.println("transform are equal in Changed: " + transform1.getAttributeKeyName() + ": " + transform1.toString());
                    score += 2; // increment by 4
                }
            }
        }

        List<ROTransformationInterface> anotherCompiledUnchangedTransform = transformation.compileUnChangedROTransformationInMatchedObjects();
        shorterLength = Math.min(anotherCompiledUnchangedTransform.size(), this.compileUnChangedROTransformationInMatchedObjects().size());
        for (int i = 0; i < shorterLength; i++) {
            transform1 = anotherCompiledUnchangedTransform.get(i);
            transform2 = this.compileUnChangedROTransformationInMatchedObjects().get(i);
            if (transform1.getAttributeKeyName().equals(transform2.getAttributeKeyName())) {
                score += 1;
                if (transform1.equals(transform2)) {
                    System.out.println("transform are equal in Unchanged in : " + transform1.getAttributeKeyName() + ": " + transform1.toString());
                    score += 7;
                }
            }
        }

        if (this.numDiffObjects == transformation.numDiffObjects) {
          //  System.out.println("numDiffObjects are the same!");
            score += 3;  // number of different objects are the same, increment by 5
        }

        if (this.getUnmatchedRavensObjects().size() == transformation.getUnmatchedRavensObjects().size()) {
          //  System.out.println("number of unmatched objects are the same!");
            score += 4;
        }

        return score;
    }






}
