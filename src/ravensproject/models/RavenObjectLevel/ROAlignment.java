package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

/**
 * Created by guoliangwang on 7/16/15.
 */
public class ROAlignment implements ROTransformationInterface {

    private final static String RO_ATTRIBUTE_KEY = "alignment";
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    public ROAlignment() {}
    public ROAlignment(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }

    /**
     * true for different
     * false for not different (same)
     * @return
     */
    public boolean isSameAlignment() {

        if (ravensObject1.getAttributes().containsKey(RO_ATTRIBUTE_KEY) &&
                ravensObject2.getAttributes().containsKey(RO_ATTRIBUTE_KEY)) {

            return ravensObject1.getAttributes().get(RO_ATTRIBUTE_KEY).equals(ravensObject2.getAttributes().get(RO_ATTRIBUTE_KEY));
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROAlignment)) {
            return false;
        }

        ROAlignment that = (ROAlignment) o;
        return this.isSameAlignment() == that.isSameAlignment(); //could be more accurate
    }

    @Override
    public String getAttributeKeyName() {
        return null;
    }
}
