package StateLogger;

/**
 * Created by scott on 6/22/15.
 */
public class Position {
    private float x;
    private float y;

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    private float z;

    public Position (){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
