package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.Map;

/**
 * Created by guoliangwang on 6/27/15.
 */
public class ROAngleChange {

    private final static String RO_ATTRIBUTE_KEY = "angle";
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;
    private int  angleDiff;

    public ROAngleChange() {}

    public ROAngleChange(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }



    /**
     * get the angle difference between the two RavensObject
     * Always use the angle in the second RavensObject to subtract from the first one
     *
     * @return int
     */
    private int getAngleDiff() {
        this.angleDiff = 0;
        if (ravensObject1.getAttributes().containsKey(RO_ATTRIBUTE_KEY) &&
                ravensObject2.getAttributes().containsKey(RO_ATTRIBUTE_KEY)) {
            int angle1 = Integer.parseInt(ravensObject1.getAttributes().get(RO_ATTRIBUTE_KEY));
            int angle2 = Integer.parseInt(ravensObject2.getAttributes().get(RO_ATTRIBUTE_KEY));

            angleDiff = angle2 - angle1;
        }
        return angleDiff;
    }
}
