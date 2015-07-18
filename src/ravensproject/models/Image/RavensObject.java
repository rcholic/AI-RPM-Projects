package ravensproject.models.Image;

import java.util.HashMap;

/**
 * Extension of the provided RavensObject
 *
 * Created by guoliangwang on 7/18/15.
 */
public class RavensObject extends ravensproject.RavensObject {

    private String name;
    private HashMap<String, String> attributes;

    public RavensObject(String name) {
        super(name);
    }

    //new added for testing
    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

}
