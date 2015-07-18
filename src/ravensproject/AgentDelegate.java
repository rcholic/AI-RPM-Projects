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

    public AgentDelegate() {
    }

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

            int scoreCurrRFTransform = rfTransformation1.scoreRFTransformations(rfTransformation2);
            System.out.println("scoreCurrRFTransformation = " + scoreCurrRFTransform + ", maxRFScore = " + maxRFTransformScore);
            if (scoreCurrRFTransform >= maxRFTransformScore) {  //what if equal ?? -- how to break tie??
                this.answerChoice = i;
                maxRFTransformScore = scoreCurrRFTransform;
            }
        }

        int correctAnswer = ravensProblem.checkAnswer(this.answerChoice);
        System.out.println("answer correct? my answer: " + this.answerChoice + ", correct: " + correctAnswer +
                " ?? " + (this.answerChoice == correctAnswer));

        return this.answerChoice;
    }

    public int solve3x3RPM(RavensProblem ravensProblem) {
        initTransformContainers();

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


        ravensFigure1 = ravensProblem.getFigures().get("G");
        ravensFigure2 = ravensProblem.getFigures().get("H");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);


        int[] answerTransformScores;
        int similarityScore = Integer.MAX_VALUE;
        System.out.println("first two row scores: " + Arrays.toString(rfTransformationScores));
        int[][] answerScoresMatrix = new int[8][3]; //8 answers, 3 RFtransformation scores
        for (int i = 1; i <= 8; i++) {
            answerTransformScores = new int[3];
            ravensFigure3 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
            rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);

            answerTransformScores[0] = rfTransformation1.scoreRFTransformations(rfTransformation2);
            answerTransformScores[1] = rfTransformation2.scoreRFTransformations(rfTransformation3);
            answerTransformScores[2] = rfTransformation1.scoreRFTransformations(rfTransformation3);
            answerScoresMatrix[i-1] = answerTransformScores;

            System.out.println("third row scores: " + Arrays.toString(answerTransformScores));

            int curSimilarityScore = getSimilarityScores(answerTransformScores);
            System.out.println("curSimilarityScore = " + curSimilarityScore + ", previous similarityScore = " + similarityScore);
            if (similarityScore > curSimilarityScore) {  //how to handle tie??
                similarityScore = curSimilarityScore;
                this.answerChoice = i;
            }

            else if (similarityScore == curSimilarityScore) {
                solve3x3RPMTie(ravensProblem);
//                breakTie(this.answerChoice, i, answerScoresMatrix);
            }

        }

        int correctAnswer = ravensProblem.checkAnswer(this.answerChoice);
        System.out.println("answer correct? my answer: " + this.answerChoice + ", correct: " + correctAnswer +
                " ?? " + (this.answerChoice == correctAnswer));

        return this.answerChoice;
    }


    public void solve3x3RPMTie(RavensProblem ravensProblem) {
        initTransformContainers();

        ravensFigure1 = ravensProblem.getFigures().get("A");
        ravensFigure2 = ravensProblem.getFigures().get("D");
        ravensFigure3 = ravensProblem.getFigures().get("G");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        // int numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        // numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);
        rfTransformationScores[0] = rfTransformation1.scoreRFTransformations(rfTransformation2);
        rfTransformationScores[1] = rfTransformation2.scoreRFTransformations(rfTransformation3);
        rfTransformationScores[2] = rfTransformation1.scoreRFTransformations(rfTransformation3);


        ravensFigure1 = ravensProblem.getFigures().get("B");
        ravensFigure2 = ravensProblem.getFigures().get("E");
        ravensFigure3 = ravensProblem.getFigures().get("H");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);
        //numTrans = rfTransformation1.compileROTransformationsInMatchedObjects().size();

        rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
        //numTrans += rfTransformation2.compileROTransformationsInMatchedObjects().size();

        rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);

        rfTransformationScores[3] = rfTransformation1.scoreRFTransformations(rfTransformation2);
        rfTransformationScores[4] = rfTransformation2.scoreRFTransformations(rfTransformation3);
        rfTransformationScores[5] = rfTransformation1.scoreRFTransformations(rfTransformation3);


        ravensFigure1 = ravensProblem.getFigures().get("C");
        ravensFigure2 = ravensProblem.getFigures().get("F");
        rfTransformation1 = new RFTransformation(ravensFigure1, ravensFigure2);


        int[] answerTransformScores;
        int similarityScore = Integer.MAX_VALUE;
        System.out.println("first two row scores: " + Arrays.toString(rfTransformationScores));
        int[][] answerScoresMatrix = new int[8][3]; //8 answers, 3 RFtransformation scores
        for (int i = 1; i <= 8; i++) {
            answerTransformScores = new int[3];
            ravensFigure3 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation2 = new RFTransformation(ravensFigure2, ravensFigure3);
            rfTransformation3 = new RFTransformation(ravensFigure1, ravensFigure3);

            answerTransformScores[0] = rfTransformation1.scoreRFTransformations(rfTransformation2);
            answerTransformScores[1] = rfTransformation2.scoreRFTransformations(rfTransformation3);
            answerTransformScores[2] = rfTransformation1.scoreRFTransformations(rfTransformation3);
            answerScoresMatrix[i-1] = answerTransformScores;

            System.out.println("third row scores: " + Arrays.toString(answerTransformScores));

            int curSimilarityScore = getSimilarityScores(answerTransformScores);
            System.out.println("curSimilarityScore = " + curSimilarityScore + ", previous similarityScore = " + similarityScore);
            if (similarityScore >= curSimilarityScore) {  //how to handle tie??
                similarityScore = curSimilarityScore;
                this.answerChoice = i;
            }
            /*
            else if (similarityScore == curSimilarityScore) {
                breakTie(this.answerChoice, i, answerScoresMatrix);
            }
            */
        }
    }


    /**
     * for 3x3 RPM, the similarity score is the smaller, the more similar it is!
     *
     * @param answerTransformScores
     * @return
     */
    private int getSimilarityScores(int[] answerTransformScores) {
        int[] firstRowNum = new int[3];
        int[] secondRowNum = new int[3];
        int[] thirdRowNum = answerTransformScores;
        for (int i = 0; i < 3; i++) {
            firstRowNum[i] = rfTransformationScores[i];
            secondRowNum[i] = rfTransformationScores[i + 3];
        }
        int minSimilarScore = 0;
        for (int i = 0; i < 3; i++) {
            minSimilarScore += (Math.abs(thirdRowNum[i] - firstRowNum[i]) + Math.abs(thirdRowNum[i] - secondRowNum[i]));
        }
        return minSimilarScore;
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

    private void breakTie(int answerChoice, int tie, int[][] answerScoresMatrix) {
        int[] firstRowNum = new int[3];
        int[] secondRowNum = new int[3];

        for (int i = 0; i < 3; i++) {
            firstRowNum[i] = rfTransformationScores[i];
            secondRowNum[i] = rfTransformationScores[i + 3];
        }

        int[] answerChoiceScores = answerScoresMatrix[answerChoice - 1];
        int[] tieChoiceScores = answerScoresMatrix[tie - 1];
        boolean pattern11 = firstRowNum[1] - firstRowNum[0] < 0;
        boolean pattern12 = firstRowNum[2] - firstRowNum[1] < 0;
        boolean pattern21 = secondRowNum[1] - secondRowNum[0] < 0;
        boolean pattern22 = secondRowNum[2] - secondRowNum[1] < 0;
        boolean combinedPattern1 = pattern11 && pattern21;
        boolean combinedPattern2 = pattern12 && pattern22;
        boolean ansPattern31 = answerChoiceScores[1] - answerChoiceScores[0] < 0;
        boolean ansPattern32 = answerChoiceScores[2] - answerChoiceScores[1] < 0;
        boolean tiePattern41 = tieChoiceScores[1] - tieChoiceScores[0] < 0;
        boolean tiePattern42 = tieChoiceScores[2] - tieChoiceScores[1] < 0;

        if (combinedPattern1 == ansPattern31 && combinedPattern2 == ansPattern32) {
            this.answerChoice = answerChoice;
            return;
        }

        this.answerChoice = tie;
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
