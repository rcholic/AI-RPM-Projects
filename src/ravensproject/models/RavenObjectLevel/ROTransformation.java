package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

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
public class ROTransformation {

    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    private ROShapeTransformation roShapeTransformation;
    private ROSizeChange roSizeChange;


    public ROTransformation() {}

    public ROTransformation(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }





}
