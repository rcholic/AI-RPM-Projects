package ravensproject.models.RavenFigureLevel;

import ravensproject.RavensFigure;

import java.util.List;

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

    public RFTransformation() {}

    public RFTransformation(RavensFigure ravensFigure1, RavensFigure ravensFigure2) {
        this.ravensFigure1 = ravensFigure1;
        this.ravensFigure2 = ravensFigure2;
    }




}
