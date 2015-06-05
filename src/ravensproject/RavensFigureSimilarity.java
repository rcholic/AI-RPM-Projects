package ravensproject;

import java.util.*;

/**
 * Created by guoliangwang on 6/3/15.
 */
public class RavensFigureSimilarity {


    private Map<String, Integer> HDifferenceTable; //horizontal difference
    private Map<String, Integer> VDifferenceTable; //vertical difference
    private RavensProblem problem;
    private static Map<String, Integer> scoreMap;
    private final static String[] ravenObjectAttributes = {"shape", "size", "fill", "inside", "above", "angle"};

    //assign weight to different attributes
    static {
        scoreMap = new HashMap<String, Integer>();
        scoreMap.put("shape", 10);
        scoreMap.put("size", 9);
        scoreMap.put("fill", 8);
        scoreMap.put("numObject", 7);
        scoreMap.put("inside", 6);
        scoreMap.put("above", 6);
        scoreMap.put("angle", 5);
    }

    public RavensFigureSimilarity() {}

    public RavensFigureSimilarity(RavensProblem problem) {
        this.problem = problem;
        this.HDifferenceTable = new HashMap<String, Integer>();
        this.VDifferenceTable = new HashMap<String, Integer>();
    }


    /**
     * identify the pattern of the changes in the horizontal figures
     * @return
     */
    public Map<String, Integer> getHDifferenceTable() {

        RavensFigure figure1 = null;
        RavensFigure figure2 = null;

        //2x2 (figures including the 6 answer choices)
        if (curProblem().getFigures().size() == 9) {
            figure1 = curProblem().getFigures().get("A");
            figure2 = curProblem().getFigures().get("B");
            this.HDifferenceTable = identifyFigureSimilarities(figure1, figure2);

            //3x3 (figures including the 8 answer choices)
        } else if (curProblem().getFigures().size() == 16) {
            //TODO: to be improved for project 2 delivery (project 1 has no 3x3)
            Map<String, Integer> tempDifferenceTable1 = new HashMap<>();
            Map<String, Integer> tempDifferenceTable2 = new HashMap<>();
            Map<String, Integer> firstRowDifferenceTable = new HashMap<>();
            Map<String, Integer> secondRowDifferenceTable = new HashMap<String, Integer>();
            Map<String, Integer> thirdRowDifferenceTablePartial = new HashMap<String, Integer>();

            //first row
            figure1 = curProblem().getFigures().get("A");
            figure2 = curProblem().getFigures().get("B");
            tempDifferenceTable1 = identifyFigureSimilarities(figure1, figure2);
            figure1 = curProblem().getFigures().get("B");
            figure2 = curProblem().getFigures().get("C");
            tempDifferenceTable2 = identifyFigureSimilarities(figure1, figure2);
         //   firstRowDifferenceTable = findCommonKeyValuePairs(tempDifferenceTable1, tempDifferenceTable2);

            //second row
            figure1 = curProblem().getFigures().get("D");
            figure2 = curProblem().getFigures().get("E");
            tempDifferenceTable1 = identifyFigureSimilarities(figure1, figure2);
            figure1 = curProblem().getFigures().get("E");
            figure2 = curProblem().getFigures().get("F");
            tempDifferenceTable2 = identifyFigureSimilarities(figure1, figure2);
         //   secondRowDifferenceTable = findCommonKeyValuePairs(tempDifferenceTable1, tempDifferenceTable2);

            //first two figures in the third row
            figure1 = curProblem().getFigures().get("G");
            figure2 = curProblem().getFigures().get("H");
            thirdRowDifferenceTablePartial = identifyFigureSimilarities(figure1, figure2);


            //this.HDifferenceTable

        }


        return this.HDifferenceTable;
    }


    /**
     * identify the pattern in changes of the vertical figures
     * @return
     */
    public Map<String, Integer> getVDifferenceTable() {
        RavensFigure figure1 = null;
        RavensFigure figure2 = null;

        //2x2 (figures including the 6 answer choices)
        if (curProblem().getFigures().size() == 9) {
            figure1 = curProblem().getFigures().get("A");
            figure2 = curProblem().getFigures().get("C");
            this.VDifferenceTable = identifyFigureSimilarities(figure1, figure2);

            //3x3 (figures including the 8 answer choices)
        } else if (curProblem().getFigures().size() == 16) {
            //TODO:
        }

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

    /**
     * <b>This is a key method for identifying the change patterns between the two figures </b>
     *
     * Identify the similarities between the two figures
     *
     * @param figure1
     * @param figure2
     * @return a Map of attributes and the associated score as defined in the <em>scoreMap</em>
     */
    public Map<String, Integer> identifyFigureSimilarities(RavensFigure figure1, RavensFigure figure2) {
        Map<String, Integer> diffMap = new HashMap<>();

        if (figure1.getObjects().size() == figure2.getObjects().size()) {
            diffMap.put("numObject", scoreMap.get("numObject"));

            //if same number of objects in the figure, compare other attributes for each object
            List<RavensObject> ravensObjectList1 = breakFigureToObjects(figure1);
            List<RavensObject> ravensObjectList2 = breakFigureToObjects(figure2);

            for (int i = 0; i < ravensObjectList1.size(); i++) {
                RavensObject curRavensObj1 = ravensObjectList1.get(i);
                RavensObject curRavensObj2 = ravensObjectList2.get(i);

                for (String attribute : ravenObjectAttributes) {
                    if (curRavensObj1.getAttributes().containsKey(attribute) && curRavensObj2.getAttributes().containsKey(attribute)) {
                        diffMap.put(attribute, scoreMap.get(attribute));
                    }
                }
            }


        } else {
            System.out.println("number of objects is different, figure 1: " + figure1.getName() + " figure 2: " + figure2.getName());
            //TODO: does this make sense?
            diffMap.put("numObject", figure1.getObjects().size() - figure2.getObjects().size()); //get the difference
        }



/*

        for (Map.Entry<String, RavensObject> object : figure1.getObjects().entrySet()) {
            Map<String, String> objectAttributes = object.getValue().getAttributes();
            for (Map.Entry<String, String> attribute : objectAttributes) {
                if (scoreMap.containsKey(attribute.getKey())) {
                    diffMap
                }
            }
        }
        */





        return diffMap;
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
     * Break down the RavensObject in the RavensFigure to a Set
     *
     * @param curFigure
     * @return List
     */
    private List<RavensObject> breakFigureToObjects(RavensFigure curFigure) {
        List<RavensObject> ravensObjectList = new ArrayList<RavensObject>();

        for (Map.Entry<String, RavensObject> entry : curFigure.getObjects().entrySet()) {
            ravensObjectList.add(entry.getValue());
        }

        return ravensObjectList;
    }




    /**
     * compare the RavensObject, assign weight to the differences in the attributes
     *
     */
    /*
    private static class RavensObjectComparator implements Comparator<RavensFigure> {

        private static Map<String, Integer> scoreMap;
        private RavensFigure figure1;
        private RavensFigure figure2;

        static {
            scoreMap = new HashMap<String, Integer>();
            scoreMap.put("shape", 10);
            scoreMap.put("size", 9);
            scoreMap.put("fill", 8);
            scoreMap.put("numObject", 7);
            scoreMap.put("inside", 6);
            scoreMap.put("above", 6);
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
    */



}
