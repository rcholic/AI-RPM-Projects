package ravensproject;

import java.io.File;

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



    }
}
