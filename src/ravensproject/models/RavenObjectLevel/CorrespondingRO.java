package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoliangwang on 6/27/15.
 */
public class CorrespondingRO {

    private RavensFigure ravensFigure1;
    private RavensObject ravensObject1;

    private RavensFigure ravensFigure2;
    private RavensObject ravensObject2;

    public CorrespondingRO() {}

    /*
    public CorrespondingRO(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }
    */

    public CorrespondingRO(RavensFigure ravensFigure1, String ravensObjectNameInFig1,
                           RavensFigure ravensFigure2, String ravensObjectNameInFig2) {
        this.ravensFigure1 = ravensFigure1;
        this.ravensObject1 = ravensFigure1.getObjects().get(ravensObjectNameInFig1);
        this.ravensFigure2 = ravensFigure2;
        this.ravensObject2 = ravensFigure2.getObjects().get(ravensObjectNameInFig2);
    }

    public List<RavensObject> getBothMatchedRavensObjects() {
        List<RavensObject> matchedROs = new ArrayList<>();
        if (ravensObject1 != null && ravensObject2 != null) {
            matchedROs.add(ravensObject1);
            matchedROs.add(ravensObject2);
        }
        return matchedROs;
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

    public RavensFigure getRavensFigure1() {
        return ravensFigure1;
    }

    public void setRavensFigure1(RavensFigure ravensFigure1) {
        this.ravensFigure1 = ravensFigure1;
    }

    public RavensFigure getRavensFigure2() {
        return ravensFigure2;
    }

    public void setRavensFigure2(RavensFigure ravensFigure2) {
        this.ravensFigure2 = ravensFigure2;
    }
}
