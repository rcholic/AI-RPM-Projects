package ravensproject.models;

import ravensproject.RavensObject;

/**
 *
 * This class is the set of transformations identified in the
 * corresponding RavensObjects with the matched RavensFigure (A-B or A-C)
 *
 * Transformation examples:
 * fill <-> no-fill
 * angles changes <-> reflection or rotation
 * number of objects: decrease or increase by N
 * shape transformation: e.g. circle -> square
 *
 *
 * Idea: assign a certain number for each transformation,
 * so the final list of numbers make up a list of pixels (pseudo), each
 * pixel has a certain value
 *
 * <b>
 * The combination of all such pixels make up a figure that can be compared with
 * another one for their similarity in terms of color tones or pixel distributions
 * </b>
 *
 *
 * Created by guoliangwang on 6/24/15.
 */
public class ObjectTransformation {
    private int degreeDiff;
    private boolean fillDisappear;  // true: 10, false: 1
    private boolean fillAppear;     // true: 10, false: 1 (for example)
    private boolean increasedSize;  // B - A or C - A, B or C is getting bigger
    private boolean decreasedSize;  // B - A or C - A, B or C is getting smaller
    private String shapeChange;   //if not the same shape, concatenate the two shape names for comparison

    private RavensObject ravensObject1;
    private RavensObject ravensObject2;


    public ObjectTransformation() {}

    public ObjectTransformation(RavensObject ravensObject1, RavensObject ravensObject2) {
        this.ravensObject1 = ravensObject1;
        this.ravensObject2 = ravensObject2;
    }

    /**
     * this may not make any sense, because it would be better to compare two
     * RavensObjects for equality (????)
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ObjectTransformation)) {
            return false;
        }

        ObjectTransformation that = (ObjectTransformation) obj;



        return false;
    }


    /**
     * compare the object's attributes and add up the number of same
     * attributes -> get a similarity score, which is used for ranking
     * RavensObject so we can find matched RavensObject
     *
     * @param obj
     * @return
     */
    public int similarityScore(Object obj) {
        //TODO:

        return 0;
    }


}
