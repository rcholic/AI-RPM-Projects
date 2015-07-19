package ravensproject.Utils;

import ravensproject.RavensFigure;
import ravensproject.RavensProblem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by guoliangwang on 7/19/15.
 */
public class ImageProcessor {

    private List<int[][]> pixelMatrices; //List of pixel matrix
    private static List<String> twoByTwoComparison;
    private static List<String> threeByThreeComparison;

    static {
        twoByTwoComparison = new ArrayList<>();
        threeByThreeComparison = new ArrayList<>();

        twoByTwoComparison.add("AB");
        twoByTwoComparison.add("C1"); //Figure C compared to answer 1...
        twoByTwoComparison.add("C2");
        twoByTwoComparison.add("C3");
        twoByTwoComparison.add("C4");
        twoByTwoComparison.add("C5");
        twoByTwoComparison.add("C6");

        threeByThreeComparison.add("ABC");
        threeByThreeComparison.add("DEF");
        threeByThreeComparison.add("GH1"); //Figure H compared to answer 1...
        threeByThreeComparison.add("GH2");
        threeByThreeComparison.add("GH3");
        threeByThreeComparison.add("GH4");
        threeByThreeComparison.add("GH5");
        threeByThreeComparison.add("GH6");
        threeByThreeComparison.add("GH7");
        threeByThreeComparison.add("GH8");
    }

    public ImageProcessor() {}

    public int solveByPixelCounts(RavensProblem ravensProblem) {

        System.out.println("solveByPixelCounts, problem has num of figures: " + ravensProblem.getFigures().keySet().size());

        pixelMatrices = new ArrayList<>();

        if (ravensProblem.getFigures().keySet().size() == 9) {
            System.out.println("in solvingByPixelCounts: 2x2");
            //TODO: solve 2x2
            for (String comparison : twoByTwoComparison) {
                System.out.println("comparison string char: " + String.valueOf(comparison.charAt(0)));
                RavensFigure figure1 = ravensProblem.getFigures().get(String.valueOf(comparison.charAt(0)));
                RavensFigure figure2 = ravensProblem.getFigures().get(String.valueOf(comparison.charAt(1)));
                //extract the difference pixels between the two figures and sae it
                System.out.println("figure1 visual: " + figure1.getVisual());
                pixelMatrices.add(extractDifferentPixelsFromTwoImages(figure1.getVisual(), figure2.getVisual()));
            }
        } else if (ravensProblem.getFigures().keySet().size() == 16) {
            System.out.println("in solvingByPixelCounts: 2x2");
            //TODO: solve 3x3
            for (String comparison : threeByThreeComparison) {
                System.out.println("comparison string: " + String.valueOf(comparison.charAt(0)));
                RavensFigure figure1 = ravensProblem.getFigures().get(String.valueOf(comparison.charAt(0)));
                RavensFigure figure2 = ravensProblem.getFigures().get(String.valueOf(comparison.charAt(1)));
                RavensFigure figure3 = ravensProblem.getFigures().get(String.valueOf(comparison.charAt(2)));

                int[][] tempMatrix12 = extractDifferentPixelsFromTwoImages(figure1.getVisual(), figure2.getVisual());
                int[][] tempMatrix23 = extractDifferentPixelsFromTwoImages(figure2.getVisual(), figure3.getVisual());
                //compare the two temporary matrices and save the comparison results
                pixelMatrices.add(pixelDiffInTwoPixelMatrices(tempMatrix12, tempMatrix23));
            }

        }

        System.out.println("size of pixelMatrices: " + this.pixelMatrices.size());

        int answer = determineAnswer(pixelMatrices);

        return answer;
    }

    private int determineAnswer(List<int[][]> matrices) {

        if (matrices.size() == 0 || matrices == null) {
            throw new ExceptionInInitializerError("matrices have NOT been initialized!");
        }

        int answer = 3;

        Map<String, Integer> pixelCountsDiffInAnswers = new HashMap<>();
        int maxAnswerChoiceNumber = 0;
        int firstRowMatrixCounts = countPixels(matrices.get(0));
        int secondRowMatrixCounts = -1;

        if (matrices.size() == 10) {
            //3x3 RPM
            maxAnswerChoiceNumber = 8;
            secondRowMatrixCounts = countPixels(matrices.get(1));

            //reference for pixel counts difference
            int refPixelDiff = secondRowMatrixCounts - firstRowMatrixCounts;
            for (int i = 1; i <= maxAnswerChoiceNumber; i++) {
                //save the counts difference between each answer choice and the first row
                 pixelCountsDiffInAnswers.put(Integer.toString(i), (countPixels(matrices.get(i+1)) - firstRowMatrixCounts));
            }

            int diffInDiff = Integer.MAX_VALUE; //difference between first-second pixel counts and first-answer pixel counts

            for (Map.Entry<String, Integer> entry : pixelCountsDiffInAnswers.entrySet()) {
                int tempDiff = entry.getValue() - refPixelDiff;
                if (tempDiff < refPixelDiff) { //this seems endless, boundary???
                    answer = Integer.parseInt(entry.getKey());
                    diffInDiff = tempDiff;
                }
            }

        } else if (matrices.size() == 7) {
            //2X2 RPM
            maxAnswerChoiceNumber = 6;
            int diffWithFirstRow = Integer.MAX_VALUE;
            for (int j = 1; j <= maxAnswerChoiceNumber; j++) {
                int tempDiffWithFirstRow = countPixels(matrices.get(j)) - firstRowMatrixCounts;
                if (tempDiffWithFirstRow <= diffWithFirstRow) { //break tie?
                    answer = j;
                    diffWithFirstRow = tempDiffWithFirstRow;
                }
            }
        }



        return answer;
    }


    public static int[][] extractDifferentPixelsFromTwoImages(String imagePath1, String imagePath2) {
        File imageFile1 = new File(imagePath1);
        File imageFile2 = new File(imagePath2);
        BufferedImage image1 = null;
        BufferedImage image2 = null;
        int[][]pixelMatrix1;
        int[][]pixelMatrix2;
        int[][]retDiffPixels = null;

        try {
            image1 = ImageIO.read(imageFile1);
            image2 = ImageIO.read(imageFile2);
            if (image1 != null && image2 != null) {
                pixelMatrix1 = convertImageToPixelMatrix(image1);
                pixelMatrix2 = convertImageToPixelMatrix(image2);

                retDiffPixels = pixelDiffInTwoPixelMatrices(pixelMatrix1, pixelMatrix2);
            }

        } catch (IOException e) {
            System.out.println("not able to read file to image");
        }

        return retDiffPixels;
    }


    /**
     * Convert image to pixel matrix of black (1) or white (0) pixels
     * @param image
     * @return
     */
    private static int[][] convertImageToPixelMatrix(BufferedImage image) {
        int maxX = image.getWidth();
        int maxY = image.getHeight();
        int minX = image.getMinX();
        int minY = image.getMinY();
        int[][] pixelMatrix = new int[maxY-minY][maxX-minX];

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                int rgbValue = image.getRGB(x, y);
                int currColor = (rgbValue != -1) ? 1 : 0; //1 for black, 0 for white
                pixelMatrix[y][x] = currColor;
            }
        }

        return pixelMatrix;
    }

    private static int[][] pixelDiffInTwoPixelMatrices(int[][] pixelMatrix1, int[][] pixelMatrix2) {
        int shorterHeight = Math.min(pixelMatrix1.length, pixelMatrix2.length); //y
        int shorterWidth = Math.min(pixelMatrix1[0].length, pixelMatrix2[0].length); //x
        int[][] retDiffPixels = new int[shorterHeight][shorterWidth];

        for (int y = 0; y < shorterHeight; y++) {
            for (int x = 0; x < shorterWidth; x++) {

                if (pixelMatrix1[y][x] == 1 && pixelMatrix1[y][x] == pixelMatrix2[y][x]) {
                    retDiffPixels[y][x] = 1; //originally only have this line
                } else if (pixelMatrix1[y][x] == 0 && pixelMatrix2[y][x] == 1) {
                    retDiffPixels[y][x] = -1;
                } else if (pixelMatrix1[y][x] == 1 && pixelMatrix2[y][x] == 0) {
                    retDiffPixels[y][x] = -2;
                }
            }
        }

        return retDiffPixels;
    }

    /**
     * Sum up the pixel values in the 2-D matrix (possible values: 0, 1, -1, -2)
     * @param pixelMatrix
     * @return
     */
    private int countPixels (int[][]pixelMatrix) {
        int counts = 0;

        for (int i = 0; i < pixelMatrix.length; i++) {
            for (int j = 0; j < pixelMatrix[i].length; j++) {
                counts += pixelMatrix[i][j];
            }
        }

        return counts;

    }


    public static void main(String[] args) {
        String file1 = "Problems/Basic Problems D/Basic Problem D-02/A.png";
        String file2 = "Problems/Basic Problems D/Basic Problem D-02/B.png";

        int[][] diff = extractDifferentPixelsFromTwoImages(file1, file2);
        for (int i = 0; i < diff.length; i++) {
            System.out.println(Arrays.toString(diff[i]));
        }

    }
}
