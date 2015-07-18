package ravensproject.models.Image;

/**
 * Created by guoliangwang on 7/18/15.
 */
public class IdentifiedObject {
    public Coordinate topLeft, topRight, bottomLeft, bottomRight;
    public Coordinate middleLeft, middleRight;
    public int topWidth;
    public int middleWidth;
    public int bottomWidth;
    public int height;
    public boolean curved;
    public boolean isFilled;
    public Shape shape;

    public final static int TOLERANCE = 8;

    public IdentifiedObject() {}

    public IdentifiedObject(Coordinate topLeft, Coordinate topRight,
                            Coordinate bottomLeft, Coordinate bottomRight,
                            Coordinate middleLeft, Coordinate middleRight,
                            int topWidth, int middleWidth, int bottomWidth,
                            int height, boolean curved, boolean isFilled) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.middleLeft = middleLeft;
        this.middleRight = middleRight;
        this.topWidth = topWidth;
        this.middleWidth = middleWidth;
        this.bottomWidth = bottomWidth;
        this.height = height;
        this.curved = curved;
        this.isFilled = isFilled;
        this.shape = determineShape();
    }

    private Shape determineShape() {
        Shape toReturn = Shape.UNKNOWN;
        if((Math.abs(topWidth - bottomWidth) < TOLERANCE) && ((middleWidth == 0) || (Math.abs(bottomWidth - middleWidth) < TOLERANCE))  && (Math.abs(height - topWidth) < TOLERANCE) && !curved)  {
            toReturn = Shape.SQUARE;
        }else if(topWidth < middleWidth && ((middleWidth < bottomWidth) || bottomWidth == 0) && (Math.abs(topLeft.getX() - topRight.getX()) < TOLERANCE)) {
            toReturn = Shape.TRIANGLE;
        }else if(bottomWidth < middleWidth && ((middleWidth < topWidth)) && (Math.abs(bottomLeft.getX() - bottomRight.getX()) < TOLERANCE)) {
            toReturn = Shape.INVERTEDTRIANGLE;
        }else if(Math.abs(middleWidth - height) < TOLERANCE && curved) {
            if(height >= 140 && topWidth > 50 && bottomWidth > 50) {
                toReturn = Shape.HEXAGON;
            }else {
                toReturn = Shape.CIRCLE;
            }
        }else if(Math.abs(middleWidth - height) < TOLERANCE && (Math.abs(bottomWidth - topWidth) < TOLERANCE) && !curved && Math.abs(middleWidth - bottomWidth) > TOLERANCE) {
            toReturn = Shape.CROSS;
        }else if(Math.abs(middleWidth - bottomWidth) < TOLERANCE && (Math.abs((topWidth / 3) - bottomWidth) < TOLERANCE)) {
            toReturn = Shape.CROSS_BOTTOM;
        }
        return toReturn;
    }

    public Coordinate getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Coordinate topLeft) {
        this.topLeft = topLeft;
    }

    public Coordinate getTopRight() {
        return topRight;
    }

    public void setTopRight(Coordinate topRight) {
        this.topRight = topRight;
    }

    public Coordinate getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Coordinate bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Coordinate getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Coordinate bottomRight) {
        this.bottomRight = bottomRight;
    }

    public Coordinate getMiddleLeft() {
        return middleLeft;
    }

    public void setMiddleLeft(Coordinate middleLeft) {
        this.middleLeft = middleLeft;
    }

    public Coordinate getMiddleRight() {
        return middleRight;
    }

    public void setMiddleRight(Coordinate middleRight) {
        this.middleRight = middleRight;
    }

    public int getTopWidth() {
        return topWidth;
    }

    public void setTopWidth(int topWidth) {
        this.topWidth = topWidth;
    }

    public int getMiddleWidth() {
        return middleWidth;
    }

    public void setMiddleWidth(int middleWidth) {
        this.middleWidth = middleWidth;
    }

    public int getBottomWidth() {
        return bottomWidth;
    }

    public void setBottomWidth(int bottomWidth) {
        this.bottomWidth = bottomWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isCurved() {
        return curved;
    }

    public void setCurved(boolean curved) {
        this.curved = curved;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public static int getTOLERANCE() {
        return TOLERANCE;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
