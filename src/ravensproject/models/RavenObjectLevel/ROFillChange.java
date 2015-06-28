package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoliangwang on 6/27/15.
 *
 * Model class for changes in Fill of two Raven's Objects
 *
 */
public class ROFillChange implements ROTransformationInterface {
    private final static String RO_ATTRIBUTE_KEY = "fill";
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;
    private String ravensObject1Fill;
    private String ravensObject2Fill;
    private boolean fillDeterminable = true;
    private static final Map<String, String> oppositeFills = new HashMap<String, String>();


    //these two variables for indicating the Fill changes in ravensObject2
    // ravensObject2 filled by default (true)
    private boolean secondObjFilled = true;
    // ravensObject2 is NOT the same as ravensObject 1 for Fill by default (true)
    private boolean fillChanged = true;

    static {
        oppositeFills.put("yes", "no");
        oppositeFills.put("no", "yes");
    }

    public ROFillChange() {}

    public ROFillChange(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;

        if (this.ravensObject1.getAttributes().containsKey(RO_ATTRIBUTE_KEY) &&
                this.ravensObject2.getAttributes().containsKey(RO_ATTRIBUTE_KEY)) {
            this.ravensObject1Fill = this.ravensObject1.getAttributes().get(RO_ATTRIBUTE_KEY);
            this.ravensObject2Fill = this.ravensObject2.getAttributes().get(RO_ATTRIBUTE_KEY);

            if (this.ravensObject2Fill.equalsIgnoreCase("no") && this.ravensObject1Fill.equalsIgnoreCase("no")) {
                this.secondObjFilled = false;
                this.fillChanged = false;
            } else if (this.ravensObject2Fill.equalsIgnoreCase("yes") && this.ravensObject1Fill.equalsIgnoreCase("no")) {
                this.secondObjFilled = true;
                this.fillChanged = true;
            } else if (this.ravensObject2Fill.equalsIgnoreCase("no") && this.ravensObject1Fill.equalsIgnoreCase("yes")) {
                this.secondObjFilled = false;
                this.fillChanged = true;
            } else {
                this.secondObjFilled = true;
                this.fillChanged = false;
            }
        } else {
            //indicate unable to determine the fill status
            this.fillDeterminable = false;
        }
    }


    @Override
    public String getAttributeKeyName() {
        return RO_ATTRIBUTE_KEY;
    }

    /**
     * Compare the fill changes between two ROFillChange objects
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ROFillChange)) {
            return false;
        }

        //if fill status is not determinable
        if (!fillDeterminable) {
            return false;
        }

        //cast the object to ROFillChange
        ROFillChange that = (ROFillChange) o;

        return that.fillChanged == this.fillChanged &&
                that.secondObjFilled == this.secondObjFilled;
    }


    /**
     * Given an existing ROFillChange pattern from two known RavensObjects and a first RavensObject (named 'C', e. g. in 2x2 RPM),
     * predict the Fill for the corresponding second RavensObject (named 'D' .e.g. in 2x2 RPM)
     *
     * @param roFillChange
     * @param firstRavensObject
     * @return "yes" or "no"
     */
    public static String predictFillInSecondRO(ROFillChange roFillChange, RavensObject firstRavensObject) {
        String predictedFill = "no"; //default

        if (roFillChange.isFillDeterminable() && firstRavensObject.getAttributes().containsKey("fill")) {
            // boolean secondKnownROFilled = roFillChange.isSecondObjFilled();
            boolean knownFillChanged = roFillChange.isFillChanged();

            if (knownFillChanged) {
                predictedFill = oppositeFills.get(firstRavensObject.getAttributes().get("fill"));
            } else {
                //if no change in Fill
                predictedFill = firstRavensObject.getAttributes().get("fill");
            }
        }

        return predictedFill;
    }



    //getters and setters


    public RavensObject getRavensObject1() {
        return ravensObject1;
    }

    public void setRavensObject1(RavensObject ravensObject1) {
        this.ravensObject1 = ravensObject1;
    }

    public RavensObject getRavensObject2() {
        return ravensObject2;
    }

    public void setRavensObject2(RavensObject ravensObject2) {
        this.ravensObject2 = ravensObject2;
    }

    public String getRavensObject1Fill() {
        return ravensObject1Fill;
    }

    public void setRavensObject1Fill(String ravensObject1Fill) {
        this.ravensObject1Fill = ravensObject1Fill;
    }

    public String getRavensObject2Fill() {
        return ravensObject2Fill;
    }

    public void setRavensObject2Fill(String ravensObject2Fill) {
        this.ravensObject2Fill = ravensObject2Fill;
    }

    public boolean isFillDeterminable() {
        return fillDeterminable;
    }

    public void setFillDeterminable(boolean fillDeterminable) {
        this.fillDeterminable = fillDeterminable;
    }

    public boolean isSecondObjFilled() {
        return secondObjFilled;
    }

    public void setSecondObjFilled(boolean secondObjFilled) {
        this.secondObjFilled = secondObjFilled;
    }

    public boolean isFillChanged() {
        return fillChanged;
    }

    public void setFillChanged(boolean fillChanged) {
        this.fillChanged = fillChanged;
    }
}
