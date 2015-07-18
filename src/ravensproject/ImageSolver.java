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

    private int answerChoice = 1;
    private ImageIdentifier imageIdentifier;
    private BufferedImage inputImage = null;
    public List<RavensFigure> describedFigures;

    public ImageSolver() {
        this.imageIdentifier = new ImageIdentifier();
    }

    public int solve2x2RPMVisually(RavensProblem problem) {

        describedFigures = new ArrayList<>();
        imageIdentifier.setRavensProblem(problem);

        // BufferedImage image = problem.getFigures();
        for (RavensFigure figure : problem.getFigures().values()) {
            System.out.println("figure info: " + figure.getName() + ", visual: " + figure.getVisual());
            String figureName = figure.getName();
            String figurePath = figure.getVisual();

            File pngFile = null;
            try {
                pngFile = new File(figurePath);
                inputImage = ImageIO.read(pngFile);
            } catch (IOException e) {
                System.out.println("problem reading the image file");
                e.printStackTrace();
            }

            if (inputImage != null) {
                imageIdentifier.setBufferedImage(inputImage);
                imageIdentifier.setFigureName(figureName);
                imageIdentifier.setProblemSetName("empty");
            }

            RavensFigure describedRF = imageIdentifier.convertImageToRF();
            describedFigures.add(describedRF);

            //convertImageToRF(figureName, figurePath);
        }
        System.out.println("number of describedFigures: " + describedFigures.size());
        return this.answerChoice;
    }


    public int solve3x3RPMVisually(RavensProblem problem) {
        describedFigures = new ArrayList<>();
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
//                printWriter = new PrintWriter(descriptionFile);
//                printWriter.write(problem.getName());
//                printWriter.write(figure.getName());
//                printWriter.write(describedRF.getObjects().keySet().toString());
//                for (RavensObject object : describedRF.getObjects().values()) {
//                    printWriter.write(object.getAttributes().values().toString());
//                }
//                printWriter.close();
                describedFigures.add(describedRF);

            } catch (IOException e) {
                System.out.println("problem reading the image file");
                e.printStackTrace();
            }


        }
        System.out.println("number of describedFigures: " + describedFigures.size());
        return this.answerChoice;
    }


    private void convertImageToRF(String figureName, String figurePath) {
        RavensFigure currRavensFigure = null;
        File pngFile = null;
        BufferedImage pngImage = null;
        try {
            pngFile = new File(figurePath);
            pngImage = ImageIO.read(pngFile);
            BufferedImage outputImage = new BufferedImage(pngImage.getWidth(),
                    pngImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g = pngImage.createGraphics();
//            g.drawImage(pngImage, 0, 0, null);
//            g.dispose();

            System.out.println("pngImage width: " + pngImage.getWidth() + ", height: " + pngImage.getHeight());
            // currRavensFigure =
        } catch (IOException e) {
            System.err.println("cannot read image file for figure: " + figureName);
            e.printStackTrace();
        }

        if (pngImage != null) {
            for (int i = 0; i < pngImage.getWidth(); i++) {
                for (int j = 0; j < pngImage.getHeight(); j++) {
                    Integer pixelRGB = pngImage.getRGB(i, j);
                    if (pixelRGB != -1) {
                        // System.out.println("black pixel RGB: " + pixelRGB);
                    }
                    int r = pixelRGB &0xFF;
                    int g = (pixelRGB>>8)&0XFF;
                    int b = (pixelRGB>>16)&0xFF;
                    if (r == 0 && g == 0 && b == 0) {
                        System.out.println("black color: 1");
                    } else {
                        System.out.println("white color: 0");
                    }

                }
            }
            //Raster RGB = pngImage.getRaster();
            //System.out.println("Raster: " + RGB.toString());
        }

        // return currRavensFigure;
    }


}
