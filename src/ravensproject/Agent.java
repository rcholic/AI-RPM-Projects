package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */

    private List<String> providedChoices;
    private int numChoices;
    private ravensproject.RavensFigureSimilarity ravensFigureSimilarity;
//    private Map<String, Integer> scoreTable;
    public Agent() {
        ravensFigureSimilarity = null; //to be constructed on the fly
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {



        /*
        System.out.println("num of figures is: " + problem.getFigures().size());

        System.out.println("Figures keyset:is: " + problem.getFigures().keySet().toString());
//        System.out.println("Figures valueSet is: " + problem.getFigures().entrySet().toString());

        for (Map.Entry<String, RavensFigure> entry : problem.getFigures().entrySet()) {
//            System.out.println(entry.getValue().getName() + " has the shape: " + problem.getFigures().get(entry.getKey()).getObjects());
            System.out.println("problem name: " + problem.getName() + " has the figures: " + entry.getValue().getName() + " and it has the objects: ");

            for (Map.Entry<String, RavensObject> object : entry.getValue().getObjects().entrySet()) {
                System.out.println("the object has key: " + object.getKey().toString());
                System.out.println(object.getValue().getAttributes().keySet().toString());
            }
            System.out.println("----------- Finished and----");
        }

        if (problem.getName().contains("Challenge")) {
            return -1;
        }
        */


        int chosenAnswer = -1;

        if (problem.getProblemType().contains("2x1")) {
            System.out.println("problem type is 2x1");
            solve2x1Problem(problem);
        } else if (problem.getProblemType().contains("2x2")) {
            numChoices = 6;
           // System.out.println("problem type is 2x2");
            chosenAnswer = solve2x2Problem(problem);
        } else if (problem.getProblemType().contains("3x3")) {
            numChoices = 9;
            System.out.println("problem type is 3x3");
            solve3x3Problem(problem);
        }

        //populate the choices with numbers of String representation
        providedChoices = new ArrayList<>(numChoices);
        for (int i = 1; i <= numChoices; i++) {
            providedChoices.add(Integer.toString(i));
        }

        return chosenAnswer;
    }

    private int solve2x2Problem(RavensProblem problem) {

        ravensFigureSimilarity = new ravensproject.RavensFigureSimilarity(problem);
        Map<String, Integer> horizonSimilarityMap = ravensFigureSimilarity.getHDifferenceTable();
        Map<String, Integer> verticalSimilarityMap = ravensFigureSimilarity.getVDifferenceTable();


        //compare the figure in each answer choices and match against the similarity score with figure B or Figure C
        int chosenChoice = checkAnswerChoices(problem, horizonSimilarityMap, verticalSimilarityMap);
      //  System.out.println("chosenChoice = " + chosenChoice);

        return chosenChoice;

    }

    private int solve2x1Problem(RavensProblem problem) {
        //TODO: may not need to implement this
        return -1;
    }

    private int solve3x3Problem(RavensProblem problem) {
        //TODO: skip for project 1
        return -1;
    }



    /**
     *
     * check individual answer choices for the best matching with the known similarity scores:
     * against the highest similarity scores with either vertically or horizontally aignled figures
     *
     * @param problem
     * @param horizonSimilarityMap: Figure objects change patterns in the top row(s)
     * @param verticalSimilarityMap Figure objects change patterns in the left column(s)
     * @return Integer, chosen answer choice
     */
    private int checkAnswerChoices(RavensProblem problem, Map<String, Integer> horizonSimilarityMap, Map<String, Integer> verticalSimilarityMap) {

        int answerChoice = -1;
        int matchedScore = 0;

        //iterate over each answer choice, and calculate the similarity score
        for (int i = 1; i <= numChoices; i++) {

            RavensFigure curRavensFigureAnswer = problem.getFigures().get(Integer.toString(i)); //figure in the answer choice
            //row-wise
            Map<String, Integer> rowAnswerSimilarity = ravensFigureSimilarity.identifyFigureSimilarities(problem.getFigures().get("C"), curRavensFigureAnswer);
            int rowMatchScore = calculateAnswerMatchScore(rowAnswerSimilarity, horizonSimilarityMap);
            if (rowMatchScore > matchedScore) {
                matchedScore = rowMatchScore;
                answerChoice = i;
            }

            /*
            //column-wise comparison with answer
            Map<String, Integer> colAnswerSimilarity = ravensFigureSimilarity.identifyFigureSimilarities(problem.getFigures().get("B"), curRavensFigureAnswer);
            int colMatchScore = calculateAnswerMatchScore(colAnswerSimilarity, verticalSimilarityMap);
            if (colMatchScore > matchedScore) {
                matchedScore = colMatchScore;
                answerChoice = i;
            }
            */


        }

        return answerChoice;

    }

    /**
     * Calculate the match score of each answer choice for the patterns identified in the row or column
     *
     * @param answerSimilarity, either rowAnswerSimilarity or colAnswerSimilarity in the checkAnswerChoice method above
     * @param referenceSimilarity, either horizonSimilarityMap or verticalSimilarityMap in the parameters of checkAnswerChoice method above
     * @return the matched score
     */
    private int calculateAnswerMatchScore(Map<String, Integer> answerSimilarity, Map<String, Integer> referenceSimilarity) {
        int matchScore = 0;

        for (String key : answerSimilarity.keySet()) {
            if (referenceSimilarity.containsKey(key) && answerSimilarity.get(key) == referenceSimilarity.get(key)) {
                matchScore++;
            }
        }

        return matchScore;
    }

}
