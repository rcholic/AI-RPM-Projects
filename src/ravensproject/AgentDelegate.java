package ravensproject;

import ravensproject.models.RavenFigureLevel.RFTransformation;
import ravensproject.models.RavenObjectLevel.ROTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoliangwang on 6/28/15.
 */
public class AgentDelegate {
    private RavensProblem ravensProblem;
    public int answerChoice;
    private RavensFigure ravensFigure1;
    private RavensFigure ravensFigure2;
    private RFTransformation rfTransformation;

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
        rfTransformation = new RFTransformation(ravensFigure1, ravensFigure2);
        int numKnownCompiledTransforms = rfTransformation.compileROTransformationsInMatchedObjects().size();

        ravensFigure1 = ravensProblem.getFigures().get("C");
        for (int i = 1; i <= 6; i++) {
            ravensFigure2 = ravensProblem.getFigures().get(Integer.toString(i));
            rfTransformation = new RFTransformation(ravensFigure1, ravensFigure2);
            if (numKnownCompiledTransforms >= rfTransformation.compileROTransformationsInMatchedObjects().size()) {
                this.answerChoice = i;
            }
        }

        return this.answerChoice;
    }

    public int solve3x3RPM(RavensProblem ravensProblem) {
        initTransformContainers();
        for (int i = 0; i < givenFigureNames.size() - 1; i++) {
            String figureName = givenFigureNames.get(i);
            ravensFigure1 = ravensProblem.getFigures().get(givenFigureNames.get(i));

        }

        return this.answerChoice;
    }


    private void initTransformContainers() {
        this.numKnownTransformations = new ArrayList<>();
        this.numAnswerTransformations = new ArrayList<>();
        this.transformationsWithAnswerFigs = new ArrayList<>();
        this.answerChoice = 6; //initialized to 6
    }

}
