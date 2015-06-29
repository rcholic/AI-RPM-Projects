package ravensproject;

import ravensproject.models.RavenFigureLevel.RFTransformation;
import ravensproject.models.RavenObjectLevel.ROTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guoliangwang on 6/28/15.
 */
public class AgentDelegate {
    private RavensProblem ravensProblem;
    public int answerChoice;
    private RavensFigure ravensFigure1;
    private RavensFigure ravensFigure2;
    private RavensFigure ravensFigure3; //for 3x3 RPM
    private RFTransformation rfTransformation1;
    private RFTransformation rfTransformation2;
    private RFTransformation rfTransformation3; //for 3x3 RPM

    public final static List<String> givenFigureNames = new ArrayList<>();
    public final static List<String> answerFigureNames = new ArrayList<>();


    //for storing the number of transformations from the given figures
    private List<Integer> numKnownTransformations;

    //for storing the list of ROTransformation from Fig C and each answer Figure
    //or the transformation between Fig H and each answer Figure
    private List<ROTransformation> transformationsWithAnswerFigs;
    // number of transformation for the compiled transformation from ROTransformation
    private List<Integer> numAnswerTransformations;


    //populate the figure names
    static {
        givenFigureNames.add("A");
        givenFigureNames.add("B");
        givenFigureNames.add("C");
        givenFigureNames.add("D");
        givenFigureNames.add("E");
        givenFigureNames.add("F");
        givenFigureNames.add("G");
        givenFigureNames.add("H");

        for (int i = 1; i < 9; i++) {
            answerFigureNames.add(Integer.toString(i));
        }
    }

    public AgentDelegate() {}
    public AgentDelegate(RavensProblem ravensProblem) {
        this.ravensProblem = ravensProblem;
    }

    public void solve() {
        // int answerChoice = 6;

        if (this.ravensProblem.getProblemType().contains("2x2")) {
            solve2x2RPM(ravensProblem);
        } else if (this.ravensProblem.getProblemType().contains("3x3")) {
            solve3x3RPM(ravensProblem);
        }

        //return answerChoice;
    }

    public int solve2x2RPM(RavensProblem ravensProblem) {
        initTransformContainers();
        ravensFigure1 = ravensProblem.getFigures().get("A");
        ravensFigure2 = ravensProblem.getFigures().get("B");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        int numKnownCompiledTransforms = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        ravensFigure1 = ravensProblem.getFigures().get("C");
        for (int i = 1; i <= 6; i++) {
            ravensFigure2 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure1, ravensFigure2);
            if (rfTransformation1.equals(rfTransformation2)) {
                System.out.println("found two equal RFTransformations!! " + ravensFigure1.getName() + " - " + ravensFigure2.getName());
                this.answerChoice = i;
                break;
            }
            if (numKnownCompiledTransforms >= rfTransformation2.compileROTransformationsInMatchedObjects().size()) {
                this.answerChoice = i;
            }
        }

        return this.answerChoice;
    }

    public int solve3x3RPM(RavensProblem ravensProblem) {
        initTransformContainers();
//        int turnFlag = 0;
//        for (int i = 0; i < givenFigureNames.size() - 1; i++) {
//            String figureName = givenFigureNames.get(i);
//            ravensFigure1 = ravensProblem.getFigures().get(givenFigureNames.get(i));
//
//            ravensFigure2 = ravensProblem.getFigures().get(givenFigureNames.get(i+1));
//            turnFlag++;
//
//        }




        List<List<RFTransformation>> transformationInFirstTwoRows = new ArrayList<>();
        List<RFTransformation> transformationInFirstRow = new ArrayList<RFTransformation>();
        List<RFTransformation> transformationInSecondRow = new ArrayList<RFTransformation>();
        List<RFTransformation> transformationInThirdRow = new ArrayList<RFTransformation>();
        ravensFigure1 = ravensProblem.getFigures().get("A");
        ravensFigure2 = ravensProblem.getFigures().get("B");
        ravensFigure3 = ravensProblem.getFigures().get("C");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        int numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        numKnownTransformations.add(numTrans + rfTransformation3.compileROTransformationsInMatchedObjects().size());
        transformationInFirstRow.add(rfTransformation1);
        transformationInFirstRow.add(rfTransformation2);
        transformationInFirstRow.add(rfTransformation3);
        transformationInFirstTwoRows.add(transformationInFirstRow);


        ravensFigure1 = ravensProblem.getFigures().get("D");
        ravensFigure2 = ravensProblem.getFigures().get("E");
        ravensFigure3 = ravensProblem.getFigures().get("F");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        numKnownTransformations.add(numTrans + rfTransformation3.compileROTransformationsInMatchedObjects().size());
        transformationInSecondRow.add(rfTransformation1);
        transformationInSecondRow.add(rfTransformation2);
        transformationInSecondRow.add(rfTransformation3);
        transformationInFirstTwoRows.add(transformationInSecondRow);


        ravensFigure1 = ravensProblem.getFigures().get("G");
        ravensFigure2 = ravensProblem.getFigures().get("H");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();
        numKnownTransformations.add(rfTransformation1.compileROTransformationsInMatchedObjects().size());

        /*
        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        numAnswerTransformations.add(rfTransformation2.compileROTransformationsInMatchedObjects().size());

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        numAnswerTransformations.add(rfTransformation3.compileROTransformationsInMatchedObjects().size());
        */
        transformationInThirdRow.add(rfTransformation1);
        transformationInFirstTwoRows.add(transformationInFirstRow);


        //NOW check the answer figures
        Collections.sort(numKnownTransformations);
        int transDiff = Integer.MAX_VALUE;

        for (int i = 1; i <= 8; i++) {
            ravensFigure3 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
            int curNumTrans = rfTransformation2.compileROTransformationsInMatchedObjects().size();
            transformationInThirdRow.add(1, rfTransformation2);
            rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
            curNumTrans += rfTransformation3.compileROTransformationsInMatchedObjects().size();
            transformationInThirdRow.add(2, rfTransformation3);

            //compute the differences in transformations row-wise
            int curDiff = compareRowTransformations(transformationInFirstTwoRows, transformationInThirdRow);
            if (curDiff == 0) {
                System.out.println("in 3x3, found identical transformation!");
                this.answerChoice = i;
                break;
            }

            if (transDiff > curDiff) {
                this.answerChoice = i;
                transDiff = curDiff;
            }

            /*
            if (numKnownTransformations.get(0) == numTrans + curNumTrans) {
                this.answerChoice = i;
            }
            */

            /*
            if (numAnswerTransformations.get(0) >= rfTransformation2.compileROTransformationsInMatchedObjects().size()) {
                this.answerChoice = i;
            }
            */
        }

        return this.answerChoice;
    }

    private int compareRowTransformations(List<List<RFTransformation>> firstTwoRowTransforms, List<RFTransformation> thirdRowTransforms) {
        int diff = 0;
        for (int i = 0; i < firstTwoRowTransforms.size(); i++) {
            List<RFTransformation> curRowTransforms = firstTwoRowTransforms.get(i);
            for (int j = 1; j < 3; j++) {
                RFTransformation upperRowTrans = curRowTransforms.get(j);
                RFTransformation thirdRowTrans = thirdRowTransforms.get(j);
                if (!upperRowTrans.equals(thirdRowTrans)) {
                    diff++;
                }
            }
        }

        return diff;
    }


    private void initTransformContainers() {
        this.numKnownTransformations = new ArrayList<>();
        this.numAnswerTransformations = new ArrayList<>();
        this.transformationsWithAnswerFigs = new ArrayList<>();
        this.answerChoice = 6; //initialized to 6
    }

    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<>();
        myList.add(1000);
        myList.add(10);
        myList.add(-10);
        myList.add(-9);
        myList.add(0);
//        myList.remove(10);
        System.out.println(myList);
        Collections.sort(myList);
        System.out.println(myList);
    }

}
