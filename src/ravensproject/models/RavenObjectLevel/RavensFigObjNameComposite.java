package ravensproject.models.RavenObjectLevel;

import ravensproject.RavensFigure;

/**
 * Created by guoliangwang on 6/28/15.
 */
public class RavensFigObjNameComposite {
    private RavensFigure ravensFigure;
    private String ravensObjectName;

    public RavensFigObjNameComposite() {}

    public RavensFigObjNameComposite(RavensFigure ravensFigure, String ravensObjectName) {
        this.ravensFigure = ravensFigure;
        this.ravensObjectName = ravensObjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RavensFigObjNameComposite)) {
            return false;
        }

        RavensFigObjNameComposite that = (RavensFigObjNameComposite) o;
        return that.ravensFigure.getName().equals(this.ravensFigure.getName()) &&
                that.ravensObjectName.equals(this.ravensObjectName);
    }

    @Override
    public int hashCode() {
        String names = this.ravensObjectName + this.ravensFigure.getName();
        return names.hashCode();
    }


    public RavensFigure getRavensFigure() {
        return ravensFigure;
    }

    public void setRavensFigure(RavensFigure ravensFigure) {
        this.ravensFigure = ravensFigure;
    }

    public String getRavensObjectName() {
        return ravensObjectName;
    }

    public void setRavensObjectName(String ravensObjectName) {
        this.ravensObjectName = ravensObjectName;
    }
}
