package ravensproject;

import java.io.File;

/**
 * Created by guoliangwang on 5/31/15.
 */
public class TestFile {

    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath() + "/src/ravensproject/Problems/ProblemSetList.txt";
        System.out.println("filePath = " + filePath);
        File f = new File(filePath);

        if (f.exists()) {
            System.out.println("file exists");
        }
    }
}
