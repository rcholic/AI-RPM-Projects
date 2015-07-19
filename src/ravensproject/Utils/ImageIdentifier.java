package ravensproject.Utils;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.RavensProblem;
import ravensproject.models.Image.Coordinate;
import ravensproject.models.Image.IdentifiedObject;
//import ravensproject.models.Image.RavensObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Given a RavensFigure image, extract shapes from PNG images and create
 * verbal description of the RavensFigure
 *
 * Created by guoliangwang on 7/18/15.
 */

public class ImageIdentifier {
   // private RavensFigure ravensFigure;
   // private ArrayList<RavensObject> ravensObjects;
    private RavensProblem ravensProblem;
    private String problemSetName; //e.g. Basic Problems B
    private String figureName;
    private BufferedImage bufferedImage;

    public ImageIdentifier() {}


    /**
     * convert the PNG image to RavensFigure object
     * @return
     */
    public RavensFigure convertImageToRF() {

        RavensProblem problem = this.ravensProblem;
        String figureName = this.figureName;
        String problemSetName = this.problemSetName;
        BufferedImage inputImage = this.bufferedImage;

        RavensFigure ravensFigure = new RavensFigure(figureName, problem.getName(), problemSetName);
        List<RavensObject> ravensObjects = new ArrayList<>();
        int numObject = 0;

//        int topRightX = inputImage.getWidth();
//        int topRightY = inputImage.getHeight();
//        int bottomLeftX = inputImage.getMinX();
//        int bottomLeftY = inputImage.getMinY();

        Coordinate bottomRightPoint = new Coordinate(inputImage.getWidth(), inputImage.getHeight());
        Coordinate topLeftPoint = new Coordinate(inputImage.getMinX(), inputImage.getMinY());

        //this pixcel Matrix uses 1 for black and - for white pixels (binary colors)
        int[][]pixelMatrix = new int[bottomRightPoint.getY() - topLeftPoint.getY()][bottomRightPoint.getX() - topLeftPoint.getX()];

        //fill the pixelMatrix, from top to bottom, left to right (upperLeft corner has (0, 0) coordinate)
        for (int y = topLeftPoint.getY(); y < bottomRightPoint.getY(); y++) {
            for (int x = topLeftPoint.getX(); x < bottomRightPoint.getX(); x++) {
                int rgbValue = inputImage.getRGB(x, y);
                int currColor = (rgbValue != -1) ? 1 : 0; //1 for black, 0 for white
                // int currColor = (red == 0 && green == 0 && blue == 0) ? 1 : 0; //1 for black, 0 for white
                pixelMatrix[y][x] = currColor;
            }
        }
        
        //copied below
        IdentifiedObject currentIdentifiedObj = null;
        IdentifiedObject lastIdentifiedObj = null;
        String spatialRelatedToLastObject = null;
        for(int y = topLeftPoint.getY(); y < bottomRightPoint.getY(); y++)  {
            for(int x = topLeftPoint.getX(); x < bottomRightPoint.getX(); x++)  {
                int leftOf = 0;
                int topLeftOf = 0;
                int topLeftOfPlus1 = 0;
                int topLeftOfPlus2 = 0;
                int topLeftOfPlus3 = 0;
                int topOf = 0;
                int topRightOf = 0;
                int topRightOfPlus1 = 0;
                int topRightOfPlus2 = 0;
                int topRightOfPlus3 = 0;

                try {
                    leftOf = pixelMatrix[y][x-1];
                    topLeftOf = pixelMatrix[y-1][x-1];
                    topLeftOfPlus1 = pixelMatrix[y-1][x-2];
                    topLeftOfPlus2 = pixelMatrix[y-1][x-3];
                    topLeftOfPlus3 = pixelMatrix[y-1][x-7];
                    topOf = pixelMatrix[y-1][x];
                    topRightOf = pixelMatrix[y-1][x+1];
                    topRightOfPlus1 = pixelMatrix[y-1][x+2];
                    topRightOfPlus2 = pixelMatrix[y-1][x+3];
                    topRightOfPlus3 = pixelMatrix[y-1][x+7];
                }catch(ArrayIndexOutOfBoundsException e) {

                }
                int current = pixelMatrix[y][x];

                //examine if at the top of a new object/shape
                if(current == 1 && (leftOf == 0 && topLeftOf == 0 && topOf == 0 && topRightOf == 0 &&
                        topRightOfPlus1 == 0 && topLeftOfPlus1 == 0  &&  topRightOfPlus2 == 0 && topLeftOfPlus2 == 0  &&
                        topRightOfPlus3 == 0 && topLeftOfPlus3 == 0)) {

                    //make sure nothing to the left
                    boolean foundaOne = false;
                    for(int toLeft = x-1; toLeft > 0; toLeft--) {
                        int leftOfCheck = pixelMatrix[y][toLeft];
                        if(leftOfCheck ==  1) {
                            foundaOne = true;
                            break;
                        }
                    }
                    if(!foundaOne)  {
                        //New shape has been found - create an object to represent it
                        numObject++;
                        RavensObject ravenObj = new RavensObject(String.valueOf(numObject)); //named by the numObject (e.g. 4 for 5th)


                        //identify shape by checking how far right it goes
                        int topRight = x;
                        int topLeft = x;
                        while(pixelMatrix[y][topRight] == 1) {
                            topRight++;
                        }

                        //find out the height
                        int height = 0;
                        int objectTop = y;
                        int middle = topLeft + ((topRight - topLeft) / 2);


                        //find out the height along the outer border of the shape
                        int[] cords =  new int[2];
                        int[] last = new int[2];
                        //x coord
                        cords[0] = topRight;

                        //Y coord
                        cords[1] = objectTop;
                        last = cords;
                        int curveCount = 0;
                        ArrayList<Coordinate> edges = new ArrayList<Coordinate>();
                        while(true) {
                            int[] tmp = findNextInOutline(cords, pixelMatrix, last);
                            last = cords;
                            cords = tmp;
                            edges.add(new Coordinate(cords[0], cords[1]));
                            if(cords[0] != last[0] && cords[1] != last[1]) {
                                curveCount++;
                            }
                            if(cords[0] == middle && Math.abs(cords[1] - objectTop) > 5)  {
                                //found our way to the bottom middle
                                break;
                            }else if(cords[0] == -99 && cords[1] == -99)  {
                                //found our way into a pickle....
                                cords = last;
                                cords[0] = cords[0] + 1;
                                cords[1] = cords[1] + 1;
                                break;
                            }

                        }

                        height = cords[1] - objectTop;

                        //find out the width of the bottom
                        int bottomLeft = middle;
                        int bottomRight = middle;

                        //See how far right we can go from the bottom middle point
                        while(pixelMatrix[objectTop + (height)][bottomRight] == 1) {
                            bottomRight++;
                        }

                        //move from bottom middle point to the left
                        while(pixelMatrix[objectTop + (height)][bottomLeft] == 1) {
                            bottomLeft--;
                        }

                        //width of the bottom middle
                        int middleRight = middle;
                        int centerY = objectTop + (height/2);

                        //bottom mid point to the far right
                        for(Coordinate point : edges)  {
                            if(point.getY() == centerY && middleRight < point.getX()) {
                                middleRight = point.getX();
                            }
                        }

                        int middleLeft = middle - (middleRight - middle);

                        //find out isFilled or not
                        int fillCheck = 1;
                        boolean isFilled = false;
                        while(pixelMatrix[objectTop+fillCheck][middle] == 1) {
                            fillCheck++;
                        }
                        isFilled = fillCheck > 5;

                        //check if curved border exists
                        boolean followsCurve = curveCount > 10;

                        int topWidth = topRight - topLeft;
                        int bottomWidth = bottomRight - bottomLeft;
                        int middleWidth = middleRight - middleLeft;

                        IdentifiedObject object = new IdentifiedObject(new Coordinate(topLeft, objectTop), new Coordinate(topRight, objectTop),
                                new Coordinate(middleLeft, (objectTop + (height/2))), new Coordinate(middleRight, (objectTop + (height/2))),
                                new Coordinate(bottomLeft, (objectTop + height)), new Coordinate(bottomRight, (objectTop + height)), topWidth,
                                middleWidth, bottomWidth, height, followsCurve, isFilled);

                        currentIdentifiedObj = object;

                        if (lastIdentifiedObj == null) {
                            lastIdentifiedObj = currentIdentifiedObj;
                        } else {
                            //TODO: label the current object position with the last object as reference

                            if (currentIdentifiedObj.topRight.getY() > lastIdentifiedObj.topRight.getY() &&
                                    currentIdentifiedObj.topLeft.getY() > lastIdentifiedObj.topLeft.getY() &&
                                    currentIdentifiedObj.bottomLeft.getY() < lastIdentifiedObj.bottomLeft.getY() &&
                                    currentIdentifiedObj.bottomRight.getY() < lastIdentifiedObj.bottomRight.getY() &&

                                    currentIdentifiedObj.topRight.getX() < lastIdentifiedObj.topRight.getX() &&
                                    currentIdentifiedObj.topLeft.getX() > lastIdentifiedObj.topLeft.getX() &&
                                    currentIdentifiedObj.bottomLeft.getX() > lastIdentifiedObj.bottomLeft.getX() &&
                                    currentIdentifiedObj.bottomRight.getX() < lastIdentifiedObj.bottomRight.getX()) {

                                spatialRelatedToLastObject = "inside";

                            } else if (currentIdentifiedObj.topRight.getY() > lastIdentifiedObj.bottomRight.getY()) {
                                spatialRelatedToLastObject = "below";
                            } else if (currentIdentifiedObj.bottomRight.getY() < lastIdentifiedObj.topRight.getY()) {
                                spatialRelatedToLastObject = "above";
                            } else if (currentIdentifiedObj.bottomRight.getX() < lastIdentifiedObj.bottomLeft.getX()) {
                                spatialRelatedToLastObject = "left-of";
                            } else if (currentIdentifiedObj.bottomLeft.getX() > lastIdentifiedObj.bottomRight.getX()) {
                                spatialRelatedToLastObject = "right-of";
                            } else {
                                spatialRelatedToLastObject = "overlap";
                            }

                            lastIdentifiedObj = currentIdentifiedObj; //update the lastIdentifiedObj to current
                        }


                        ravensObjects.add(describeRavensObject(ravenObj, object, spatialRelatedToLastObject, Integer.toString(numObject-1)));
                        //mark the edges as used
                        for(Coordinate coordinate : edges) {
                            try {
                                pixelMatrix[coordinate.getY()][coordinate.getX()] = numObject;
                            } catch(ArrayIndexOutOfBoundsException e) {
                              //  System.out.println("index our of bound");
                              //  e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        //populate the Ravens Figure with RavensObjects
        for (int i = 0; i < ravensObjects.size(); i++) {
            RavensObject curRavensObject = ravensObjects.get(i);
            ravensFigure.getObjects().put(curRavensObject.getName(), curRavensObject);
        }
        
        
        //copied above
        return ravensFigure;
    }

    //getters and setters
    public RavensProblem getRavensProblem() {
        return ravensProblem;
    }

    public void setRavensProblem(RavensProblem ravensProblem) {
        this.ravensProblem = ravensProblem;
    }

    public String getProblemSetName() {
        return problemSetName;
    }

    public void setProblemSetName(String problemSetName) {
        this.problemSetName = problemSetName;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    /**
     * Find the next adjacent pixel in the boundary of the shape.
     * moving from top-center to bottom-center
     * @param cords - int[], where x-Cord is 0 and y-Cord is 1
     * @param map - int[][], representation of the image's pixels (0=white, 1=black)
     *
     * @return int[] - where x-Cord is 0, y-Cord is 1
     **/
    private int[] findNextInOutline(int[] cords, int[][] map, int[] last) {
        int[] toReturn = new int[2];
        int x =  cords[0];
        int y = cords[1];
        //Start looking for the next pixel in the outline, give preference to going 
        //right and down since we are starting at the top and working to the bottom going right

        try {
            //look to the right
            if(map[y][x+1] == 1 && (last[0] != (x+1))) {
                toReturn[0] = x+1;
                toReturn[1] = y;

                //look down and to the right
            }else if(map[y+1][x+1] == 1  && (last[0] != (x+1) && last[1] != (y+1))) {
                toReturn[0] = x+1;
                toReturn[1] = y+1;

                //Look down
            }else if(map[y+1][x] == 1  && (last[1] != (y+1))) {
                toReturn[0] = x;
                toReturn[1] = y+1;

                //look down and to the left
            }else if(map[y+1][x-1] == 1  && (last[0] != (x-1) && last[1] != (y+1))) {
                toReturn[0] = x-1;
                toReturn[1] = y+1;

                //look to the left  -- Should this be removed??
            }else if(map[y][x-1] == 1  && (last[0] != (x-1) && last[1] != (y))) {
                toReturn[0] = x-1;
                toReturn[1] = y;

                //look ?
            }else {
                //Ok.... there isn't a clear path forward when only looking one pixel away, stretch it out....
                //Find the farthest right 1 that is cloest to the last x in the next row and call it good;
                boolean oneFound = false;
                int bestX = 0;
                for(int i = 0; i < map[y+1].length; i++) {
                    if(map[y+1][i] == 1)  {
                        if(i > bestX && ((Math.abs(bestX - i) <= Math.abs(bestX - x)))) {
                            bestX = i;
                            oneFound = true;
                        }
                    }
                }


                //No 1 exists in the next row, so look in the same row, but move left
                if(!oneFound)  {
                    for(int i = 0; i < map[y].length; i++) {
                        if(map[y][i] == 1)  {
                            if(i > bestX && ((Math.abs(bestX - i) <= Math.abs(bestX - x)))) {
                                bestX = i;
                            }
                        }
                    }
                    toReturn[1] = y;
                    toReturn[0] = bestX-1;
                }else {
                    toReturn[1] = y+1;
                    toReturn[0] = bestX;
                }

            }
        } catch(ArrayIndexOutOfBoundsException e) {
            //System.out.println("Index Out Of Bounds Finding Next 1");
            toReturn[0] = -99;
            toReturn[1] = -99;
        }
        return toReturn;
    }

    /**
     * Describe a RavensObject verbally from the infromation gathered to map out an object
     * The only attributes supported at this point are fill, size and shape
     * This method depends on pass by reference.
     *
     * @param ravenObj - RavensObject to be populated with gathered knowledge
     * @param object - IdentifiedObject which contains info from prior knowledge
     * @param spatialRelation - spatial relationship (e.g. below)
     * @param spatialReference - reference object for the spatial relationship
     */
    private RavensObject describeRavensObject(RavensObject ravenObj, IdentifiedObject object, String spatialRelation, String spatialReference) {
        HashMap<String, String> attributes = new HashMap<>();
        //ArrayList<ROAttributeValuepoint> attrs = new ArrayList<>();

        ravenObj.getAttributes().put("shape", object.getShape().toString());
        ravenObj.getAttributes().put("fill", object.isFilled ? "yes" : "no");
        //lacking angle changes!!
        //Set the size
        if(object.getHeight() >= 140)  {
            ravenObj.getAttributes().put("size", "large");
        }else if(object.getHeight() >= 90  && object.getHeight() < 140)  {
            ravenObj.getAttributes().put("size", "medium");
        }else if(object.getHeight() < 90)  {
            ravenObj.getAttributes().put("size", "small");
        }

        if (spatialRelation != null) {
            ravenObj.getAttributes().put(spatialRelation, spatialReference);
        }

        return ravenObj;
    }


}
