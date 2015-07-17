package ravensproject;

import ravensproject.models.RavenFigureLevel.RFTransformation;
import ravensproject.models.RavenObjectLevel.ROTransformation;

import java.util.ArrayList;
import java.util.Arrays;
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
    private int[] rfTransformationScores;
    // private List<Integer> answerRFTransformationScores;

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

        System.out.println("solving problem: " + this.ravensProblem.getName());
        if (this.ravensProblem.getProblemType().contains("2x2") && this.ravensProblem.hasVerbal()) {
            solve2x2RPM(ravensProblem);
        } else if (this.ravensProblem.getProblemType().contains("3x3") && this.ravensProblem.hasVerbal()) {
            solve3x3RPM(ravensProblem);
        }

        //return answerChoice;
    }

    public int solve2x2RPM(RavensProblem ravensProblem) {
        initTransformContainers();
        int maxRFTransformScore = 0;

        ravensFigure1 = ravensProblem.getFigures().get("A");
        ravensFigure2 = ravensProblem.getFigures().get("B");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        // int numKnownCompiledTransforms = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        ravensFigure1 = ravensProblem.getFigures().get("C");
        for (int i = 1; i <= 6; i++) {
            ravensFigure2 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure1, ravensFigure2);
            /*
            if (rfTransformation1.equals(rfTransformation2)) {
                System.out.println("found two equal RFTransformations!! " + ravensFigure1.getName() + " - " + ravensFigure2.getName());
                this.answerChoice = i;
                break;
            } else {
                */
                int scoreCurrRFTransform = rfTransformation1.scoreRFTransformations(rfTransformation2);
                System.out.println("scoreCurrRFTransformation = " + scoreCurrRFTransform + ", maxRFScore = " + maxRFTransformScore);
                if (scoreCurrRFTransform >= maxRFTransformScore) {  //what if equal ?? -- how to break tie??
                    this.answerChoice = i;
                    maxRFTransformScore = scoreCurrRFTransform;
                }
            //}
        }

        int correctAnswer = ravensProblem.checkAnswer(this.answerChoice);
        System.out.println("answer correct? my answer: " + this.answerChoice + ", correct: " + correctAnswer +
                " ?? " + (this.answerChoice == correctAnswer));

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




       // List<List<RFTransformation>> transformationInFirstTwoRows = new ArrayList<>();
       // List<RFTransformation> transformationInFirstRow = new ArrayList<RFTransformation>();
       // List<RFTransformation> transformationInSecondRow = new ArrayList<RFTransformation>();
       // List<RFTransformation> transformationInThirdRow = new ArrayList<RFTransformation>();
        ravensFigure1 = ravensProblem.getFigures().get("A");
        ravensFigure2 = ravensProblem.getFigures().get("B");
        ravensFigure3 = ravensProblem.getFigures().get("C");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        // int numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        // numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        rfTransformationScores[0] = rfTransformation1.scoreRFTransformations(rfTransformation2);
        rfTransformationScores[1] = rfTransformation2.scoreRFTransformations(rfTransformation3);
        rfTransformationScores[2] = rfTransformation1.scoreRFTransformations(rfTransformation3);
//        transformationInFirstRow.add(rfTransformation1);
//        transformationInFirstRow.add(rfTransformation2);
//        transformationInFirstRow.add(rfTransformation3);
        // transformationInFirstTwoRows.add(transformationInFirstRow);


        ravensFigure1 = ravensProblem.getFigures().get("D");
        ravensFigure2 = ravensProblem.getFigures().get("E");
        ravensFigure3 = ravensProblem.getFigures().get("F");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        //numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        //numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);

        rfTransformationScores[3] = rfTransformation1.scoreRFTransformations(rfTransformation2);
        rfTransformationScores[4] = rfTransformation2.scoreRFTransformations(rfTransformation3);
        rfTransformationScores[5] = rfTransformation1.scoreRFTransformations(rfTransformation3);

//        numKnownTransformations.add(numTrans + rfTransformation3.compileROTransformationsInMatchedObjects().size());
//        transformationInSecondRow.add(rfTransformation1);
//        transformationInSecondRow.add(rfTransformation2);
//        transformationInSecondRow.add(rfTransformation3);
//        transformationInFirstTwoRows.add(transformationInSecondRow);


        ravensFigure1 = ravensProblem.getFigures().get("G");
        ravensFigure2 = ravensProblem.getFigures().get("H");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
//        numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();
//        numKnownTransformations.add(rfTransformation1.compileROTransformationsInMatchedObjects().size());

        /*
        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        numAnswerTransformations.add(rfTransformation2.compileROTransformationsInMatchedObjects().size());

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        numAnswerTransformations.add(rfTransformation3.compileROTransformationsInMatchedObjects().size());
        */
//        transformationInThirdRow.add(rfTransformation1);
//        transformationInFirstTwoRows.add(transformationInFirstRow);


        //NOW check the answer figures
//        Collections.sort(numKnownTransformations);
//        int transDiff = Integer.MAX_VALUE;

        int[] answerTransformScores;
        double similarityScore = 0;
        for (int i = 1; i <= 8; i++) {
            answerTransformScores = new int[3];
            ravensFigure3 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
            rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);

            answerTransformScores[0] = rfTransformation1.scoreRFTransformations(rfTransformation2);
            answerTransformScores[1] = rfTransformation2.scoreRFTransformations(rfTransformation3);
            answerTransformScores[2] = rfTransformation1.scoreRFTransformations(rfTransformation3);

            System.out.println("first two row scores: " + Arrays.toString(rfTransformationScores));
            System.out.println("third row scores: " + Arrays.toString(answerTransformScores));

            double curSimilarityScore = getSimilarityScores(answerTransformScores);
            System.out.println("curSimilarityScore = " + curSimilarityScore + ", previous similarityScore = " + similarityScore);
            if (similarityScore <= curSimilarityScore) {  //how to handle tie??
                similarityScore = curSimilarityScore;
                this.answerChoice = i;
            }
        }

        int correctAnswer = ravensProblem.checkAnswer(this.answerChoice);
        System.out.println("answer correct? my answer: " + this.answerChoice + ", correct: " + correctAnswer +
                " ?? " + (this.answerChoice == correctAnswer));

        return this.answerChoice;
    }

    /**
     *
     * @param answerTransformScores int array of size 3
     * @return
     */
    private double getSimilarityScores(int[] answerTransformScores) {
        double simScore = 0;

        for (int i = 0; i < 3; i++) {
            int firstRowNum = rfTransformationScores[i];
            int secondRowNum = rfTransformationScores[i + 3];
            int thirdRowNum = answerTransformScores[i];

            double tmpSimScore = Math.abs(firstRowNum - thirdRowNum) + Math.abs(secondRowNum - thirdRowNum);
            simScore += (double) tmpSimScore / 2.0;
        }


        return simScore;
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
        this.rfTransformationScores = new int[6];
       // this.answerRFTransformationScores = new ArrayList<>();
//        this.numAnswerTransformations = new ArrayList<>();
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
      //  System.out.println(myList);
        Collections.sort(myList);
      //  System.out.println(myList);
    }

}
