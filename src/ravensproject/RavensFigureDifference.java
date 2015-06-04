package ravensproject;

import java.util.*;

/**
 * Created by guoliangwang on 6/3/15.
 */
public class RavensFigureDifference {


    private Map<String, String> HDifferenceTable; //horizontal difference
    private Map<String, String> VDifferenceTable; //vertical difference
    private RavensProblem problem;

    public RavensFigureDifference(RavensProblem problem) {
        this.problem = problem;
        this.HDifferenceTable = new HashMap<>();
        this.VDifferenceTable = new HashMap<>();
    }


    /**
     * identify the pattern of the changes in the horizontal figures
     * @return
     */
    private Map<String, String> getHDifferenceTable() {

        RavensFigure figure1 = null;
        RavensFigure figure2 = null;

        //2x2 (figures including the 6 answer choices)
        if (curProblem().getFigures().size() == 9) {
            figure1 = curProblem().getFigures().get("A");
            figure2 = curProblem().getFigures().get("B");
            this.HDifferenceTable = identifyFigureDifferences(figure1, figure2);

            //3x3 (figures including the 8 answer choices)
        } else if (curProblem().getFigures().size() == 16) {
            Map<String, String> tempDifferenceTable1 = new HashMap<>();
            Map<String, String> tempDifferenceTable2 = new HashMap<>();
            Map<String, String> firstRowDifferenceTable = new HashMap<>();
            Map<String, String> secondRowDifferenceTable = new HashMap<>();
            Map<String, String> thirdRowDifferenceTablePartial = new HashMap<>();

            //first row
            figure1 = curProblem().getFigures().get("A");
            figure2 = curProblem().getFigures().get("B");
            tempDifferenceTable1 = identifyFigureDifferences(figure1, figure2);
            figure1 = curProblem().getFigures().get("B");
            figure2 = curProblem().getFigures().get("C");
            tempDifferenceTable2 = identifyFigureDifferences(figure1, figure2);
            firstRowDifferenceTable = findCommonKeyValuePairs(tempDifferenceTable1, tempDifferenceTable2);

            //second row
            figure1 = curProblem().getFigures().get("D");
            figure2 = curProblem().getFigures().get("E");
            tempDifferenceTable1 = identifyFigureDifferences(figure1, figure2);
            figure1 = curProblem().getFigures().get("E");
            figure2 = curProblem().getFigures().get("F");
            tempDifferenceTable2 = identifyFigureDifferences(figure1, figure2);
            secondRowDifferenceTable = findCommonKeyValuePairs(tempDifferenceTable1, tempDifferenceTable2);

            //first two figures in the third row
            figure1 = curProblem().getFigures().get("G");
            figure2 = curProblem().getFigures().get("H");
            thirdRowDifferenceTablePartial = identifyFigureDifferences(figure1, figure2);


            //this.HDifferenceTable

        }


        return this.HDifferenceTable;
    }


    /**
     * identify the pattern in changes of the vertical figures
     * @return
     */
    public Map<String, String> getVDifferenceTable() {
        return this.VDifferenceTable;
    }





    /**
     * return the current RavensProblem
     * @return
     */
    private RavensProblem curProblem() {
        return this.problem;
    }


    /* private helper methods*/

    private Map<String, String> identifyFigureDifferences(RavensFigure figure1, RavensFigure figure2) {

        //TODO:

        return null;
    }



    private Map<String, String> findCommonKeyValuePairs(Map<String, String> map1, Map<String, String> map2) {
        //TODO:
        Map<String, String> commonKVPairs = new HashMap<>();

        if (map1.keySet().size() == map2.keySet().size()) {
            for (String key : map1.keySet()) {
                if (map1.get(key).equalsIgnoreCase(map2.get(key))) {
                    commonKVPairs.put(key, map1.get(key));
                }
            }
        }

        return commonKVPairs;
    }


    /**
     * compare the RavensObject, assign weight to the differences in the attributes
     *
     */
    private static class RavensObjectComparator implements Comparator<RavensFigure> {

        private static Map<String, Integer> scoreMap;
        private RavensFigure figure1;
        private RavensFigure figure2;

        static {
            scoreMap = new HashMap<String, Integer>();
            scoreMap.put("shape", 10);
            scoreMap.put("size", 9);
            scoreMap.put("fill", 8);
            scoreMap.put("inside", 7);
            scoreMap.put("above", 7);
            scoreMap.put("angle", 5);
        }

        public RavensObjectComparator(RavensFigure fig1, RavensFigure fig2) {
            this.figure1 = fig1;
            this.figure2 = fig2;
        }

        @Override
        public int compare(RavensFigure fig1, RavensFigure fig2) {
            if (fig1.getObjects().size() < fig2.getObjects().size()) {
                return -1;
            }

            return 0;
        }
    }



}
