package ravensproject;

import ravensproject.Utils.ImageIdentifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoliangwang on 7/18/15.
 */
public class ImageSolver {

    private ImageIdentifier imageIdentifier;
    private BufferedImage inputImage = null;
    //public List<RavensFigure> describedFigures;

    public ImageSolver() {
        this.imageIdentifier = new ImageIdentifier();
    }

    public RavensProblem solve2x2RPMVisually(RavensProblem problem) {

        imageIdentifier.setRavensProblem(problem);

        for (RavensFigure figure : problem.getFigures().values()) {
            System.out.println("figure info: " + figure.getName() + ", visual: " + figure.getVisual());
            String figureName = figure.getName();
            String figurePath = figure.getVisual();

            File pngFile = null;
            try {
                pngFile = new File(figurePath);
                inputImage = ImageIO.read(pngFile);
                if (inputImage != null) {
                    imageIdentifier.setBufferedImage(inputImage);
                    imageIdentifier.setFigureName(figureName);
                    imageIdentifier.setProblemSetName("empty");
                }

                RavensFigure describedRF = imageIdentifier.convertImageToRF();
                problem.getFigures().put(figure.getName(), describedRF);

            } catch (IOException e) {
                System.out.println("problem reading the image file");
                e.printStackTrace();
            }
        }
        return problem;
    }


    public RavensProblem solve3x3RPMVisually(RavensProblem problem) {
        imageIdentifier.setRavensProblem(problem);

//        File descriptionFile = new File("/Users/guoliangwang/Desktop/describedRF.txt");
//        descriptionFile.getParentFile().mkdirs();
//        if (descriptionFile.exists()) {
//            descriptionFile.delete();
//        }
//        PrintWriter printWriter;
        for (RavensFigure figure : problem.getFigures().values()) {
            System.out.println("figure info: " + figure.getName() + ", visual: " + figure.getVisual());
            String figureName = figure.getName();
            String figurePath = figure.getVisual();

            // convertImageToRF(figureName, figurePath);
            File pngFile = null;
            try {
                pngFile = new File(figurePath);
                inputImage = ImageIO.read(pngFile);
                if (inputImage != null) {
                    imageIdentifier.setBufferedImage(inputImage);
                    imageIdentifier.setFigureName(figureName);
                    imageIdentifier.setProblemSetName("empty");
                }

                RavensFigure describedRF = imageIdentifier.convertImageToRF();
                problem.getFigures().put(figure.getName(), describedRF);

//                printWriter = new PrintWriter(descriptionFile);
//                printWriter.write(problem.getName());
//                printWriter.write(figure.getName());
//                printWriter.write(describedRF.getObjects().keySet().toString());
//                for (RavensObject object : describedRF.getObjects().values()) {
//                    printWriter.write(object.getAttributes().values().toString());
//                }
//                printWriter.close();

            } catch (IOException e) {
                System.out.println("problem reading the image file");
                e.printStackTrace();
            }


        }
        return problem;
    }


}
