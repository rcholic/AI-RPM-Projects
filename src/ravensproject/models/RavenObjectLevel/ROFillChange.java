package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

/**
 * Created by guoliangwang on 6/27/15.
 */
public class ROFillChange {
    private RavensObject ravensObject1;
    private RavensObject ravensObject2;
    private String ravensObject1Fill;
    private String ravensObject2Fill;

    //these two variables for indicating the Fill changes in ravensObject2
    private boolean becomeFilled = false; // ravensObject2 not filled by default (false)
    private boolean fillChanged = false; // ravensObject2 is the same as ravensObject 1 for Fill

    public ROFillChange() {}

    public ROFillChange(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;

        if (this.ravensObject1.getAttributes().containsKey("fill") &&
                this.ravensObject2.getAttributes().containsKey("fill")) {
            this.ravensObject1Fill = this.ravensObject1.getAttributes().get("fill");
            this.ravensObject2Fill = this.ravensObject2.getAttributes().get("fill");
        }
    }


}
