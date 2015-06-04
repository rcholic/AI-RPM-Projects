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
    private Map<String, Integer> scoreTable;

    public Agent() {
        providedChoices = new ArrayList<String>(9);  // max number of choices is 9
        //populate the choices with numbers of String representation
        for (int i = 1; i < 10; i++) {
            providedChoices.add(Integer.toString(i));
        }

        //key is the attribute, value is 1 (constant)
        scoreTable = new HashMap<String, Integer>();
        scoreTable.put("shape", 10);
        scoreTable.put("size", 9);
        scoreTable.put("fill", 8);
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


        /**** print out for testing/learning purpose only
        System.out.println("Figures keyset:is: " + problem.getFigures().keySet().toString());
//        System.out.println("Figures valueSet is: " + problem.getFigures().entrySet().toString());

        for (Map.Entry<String, RavensFigure> entry : problem.getFigures().entrySet()) {
            System.out.println("problem name: " + problem.getName() + " has the figures: " + entry.getValue().getName() + " and it has the objects: ");

            for (Map.Entry<String, RavensObject> object : entry.getValue().getObjects().entrySet()) {
                System.out.println(object.getValue().getAttributes().keySet().toString());
            }
            System.out.println("----------- Finished and----");
        }

        if (problem.getName().contains("Challenge")) {
            return -1;
        }

        */

        if (problem.getProblemType().contains("2x1")) {
            System.out.println("problem type is 2x1");
            return solve2x1Problem(problem);
        } else if (problem.getProblemType().contains("2x2")) {
            System.out.println("problem type is 2x2");
            return solve2x2Problem(problem);
        } else if (problem.getProblemType().contains("3x3")) {
            System.out.println("problem type is 3x3");
            return solve3x3Problem(problem);
        }

        return -1;

    }

    private int solve2x2Problem(RavensProblem problem) {
        return 3;
    }

    private int solve2x1Problem(RavensProblem problem) {
        //TODO
        return -1;
    }

    private int solve3x3Problem(RavensProblem problem) {
        //TODO
        return -1;
    }
}
