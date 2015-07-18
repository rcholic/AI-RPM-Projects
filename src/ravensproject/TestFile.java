package ravensproject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by guoliangwang on 5/31/15.
 */
public class TestFile {

    public static void main(String[] args) {
        int[] standard1 = {67, 96, 134};
        int[] standard2 = {151, 52, 52};

        int[][] answers = new int[8][3];
        answers[0] = new int[]{49, 171, 73};
        answers[1] = new int[]{70, 191, 49};
        answers[2] = new int[]{149, 108, 49};
        answers[3] = new int[]{132, 264, 156};
        answers[4] = new int[]{52, 108, 70};
        answers[5] = new int[]{49, 57, 172};
        answers[6] = new int[]{199, 56, 76};
        answers[7] = new int[]{175, 175, 261};

        int answerNum = -1;
        int similarityScore = Integer.MAX_VALUE;

        for (int i = 0; i < 8; i++) {
            int[] answer = answers[i];
            int simScore = 0;
            for (int j = 0; j < 3; j++) {
                simScore += (answer[j] - standard1[j]) + (answer[j] - standard2[j]);
            }

            System.out.println("for answer " + i + ", the similarity score = " + simScore);

            if (simScore < similarityScore) {
                similarityScore = simScore;
                answerNum = i;
            }
        }

        System.out.println("answerNum = " + answerNum + ", similarity score: " + similarityScore);

        File curImage = new File("Problems/Basic Problems D/Basic Problem D-02/2.png");

        BufferedImage image = null;
        int[][]pixelMatrix;
        try {
            image = ImageIO.read(curImage);
            System.out.println("image width = " + image.getWidth() + ", height = " + image.getHeight());
            pixelMatrix = new int[image.getHeight()][image.getWidth()];
            for (int y = image.getMinY(); y < image.getHeight(); y++) {
                for (int x = image.getMinX(); x < image.getWidth(); x++) {

                    int rgb = image.getRGB(x, y);
                    int alpha = (rgb & 0xFF000000) >>> 24;
                    int red = (rgb) & 0xFF;
                    int green = (rgb>>8) & 0xFF;
                    int blue = (rgb>>16) & 0xFF;
                    // int curColor = (red == 0 && green == 0 && blue == 0) ? 1 : 0;
                    int curColor = (rgb != -1) ? 1 : 0;
                    pixelMatrix[y][x] = curColor;
                    if (curColor == 1) {
                        System.out.println(red == 0 && green == 0 && blue == 0); // black ?? some true, some false
                    }
                }
            }

            //print out the matrix
            for (int i = 0; i < pixelMatrix.length; i++) {
              //  System.out.println(Arrays.toString(pixelMatrix[i]));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
