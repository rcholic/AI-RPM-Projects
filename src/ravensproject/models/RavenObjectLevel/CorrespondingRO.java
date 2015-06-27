package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensObject;

/**
 * Created by guoliangwang on 6/27/15.
 */
public class CorrespondingRO {

    private RavensObject ravensObject1;
    private RavensObject ravensObject2;

    public CorrespondingRO() {}

    public CorrespondingRO(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }

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
}
