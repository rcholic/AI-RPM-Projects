package ravensproject.models.Image;

/**
 * Created by guoliangwang on 7/18/15.
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {}

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate that = (Coordinate) o;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public String toString() {
        return "x = " + this.x + ", y = " + this.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
