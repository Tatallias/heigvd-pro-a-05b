package Utility;

/**
 * class representing a 2d vector
 */
public class Vector2 {
    private float x;
    private float y;

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

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * subtract the Vector v from this and return the result
     * @param v
     * @return the result
     */
    public Vector2 sub(Vector2 v){
        return new Vector2(x-v.getX(),y-v.getY());
    }

    /**
     * add the Vector v from this and return the result
     * @param v
     * @return the result
     */
    public Vector2 add(Vector2 v){
        return new Vector2(x+v.getX(),y+v.getY());
    }

    /**
     * computes the squared distance between this and v2
     * @param v2
     * @return
     */
    public float dist2(Vector2 v2){
        return (float) (Math.pow(getX()-v2.getX(),2)+Math.pow(getY()-v2.getY(),2));
    }
    public float dist(Vector2 v2){
        return (float) Math.sqrt(dist2(v2));
    }
}
