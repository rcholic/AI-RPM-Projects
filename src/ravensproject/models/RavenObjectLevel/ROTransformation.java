package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * @author guoliangwang
 *
 * Model for Raven's Object Transformation
 *
 * Given two Raven's Object, find out their transformations
 *
 * Comparing order: from left to right (e.g. Figure A compared to Figure B)
 * or from top to bottom (e.g. Figure A compared to Figure C if 2x2 or
 * Figure A compared to Figure D if 3x3
 *
 */
public class ROTransformation implements Comparator<ROTransformation> {

    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    private ROShapeTransformation roShapeTransformation;
    private ROSizeChange roSizeChange;
    private ROAngleChange roAngleChange;
    private ROFillChange roFillChange;


    public ROTransformation() {}
    public ROTransformation(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
        this.roShapeTransformation = new ROShapeTransformation(ravensObject1, ravensObject2);
        this.roSizeChange = new ROSizeChange(ravensObject1, ravensObject2);
        this.roAngleChange = new ROAngleChange(ravensObject1, ravensObject2);
        this.roFillChange = new ROFillChange(ravensObject1, ravensObject2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ROTransformation)) {
            return false;
        }

        ROTransformation that = (ROTransformation) o;
        return this.roAngleChange.equals(that.roAngleChange) &&
                this.roShapeTransformation.equals(that.roShapeTransformation) &&
                this.roSizeChange.equals(that.roSizeChange) &&
                this.roFillChange.equals(that.roSizeChange);
    }

    @Override
    public int compare(ROTransformation o1, ROTransformation o2) {

        if (o1.roSizeChange.sizeDifference() < o2.roSizeChange.sizeDifference()) {
            return -1;
        }
        if (o1.roSizeChange.sizeDifference() > o2.roSizeChange.sizeDifference()) {
            return 1;
        }
        if (o1.roAngleChange.getAngleDiff() < o2.roAngleChange.getAngleDiff()) {
            return -1;
        }
        if (o1.roAngleChange.getAngleDiff() > o2.roAngleChange.getAngleDiff()) {
            return 1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return (this.ravensObject1.getName() +
                this.ravensObject2.getName()).hashCode();
    }

    public List<ROTransformationInterface> getDiffInROTransformations(ROTransformation anotherROTransformation) {
        List<ROTransformationInterface> differences = new ArrayList<>();
        if (!this.equals(anotherROTransformation)) {
            if (!this.roAngleChange.equals(anotherROTransformation.roAngleChange)) {
                differences.add(this.roAngleChange);
            }
        }

        return differences;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.roAngleChange.getAttributeKeyName() + " : " + this.roAngleChange.getAngleDiff());
        sb.append(this.roSizeChange.getAttributeKeyName() + " : " + this.roSizeChange.sizeDifference());
        sb.append(this.roShapeTransformation.getAttributeKeyName() + " : " + this.roShapeTransformation.toString());
        sb.append(this.roAngleChange.getAttributeKeyName() + " : " + this.roAngleChange.getAngleDiff());
        return sb.toString();
    }


    //getters
    public RavensObject getRavensObject1() {
        return ravensObject1;
    }

    public RavensObject getRavensObject2() {
        return ravensObject2;
    }


    public ROShapeTransformation getRoShapeTransformation() {
        return roShapeTransformation;
    }

    public ROSizeChange getRoSizeChange() {
        return roSizeChange;
    }

    public ROAngleChange getRoAngleChange() {
        return roAngleChange;
    }

    public ROFillChange getRoFillChange() {
        return roFillChange;
    }
}
