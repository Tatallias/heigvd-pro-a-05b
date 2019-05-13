package com.example.painttest;

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

    public Vector2 sub(Vector2 v){
        return new Vector2(x-v.getX(),y-v.getY());
    }


    public void rotate(float angle){
        float theta=(float)Math.toRadians(angle);
        double cs= Math.cos(theta);
        double sn= Math.sin(theta);

        x =(float)( x * cs - y * sn);
        y = (float)( x * sn + y * cs);

    }
    public void scale(float s){
        x= s*x;
        y=s*y;
    }

    public float dist2(Vector2 v2){
        return (float) (Math.pow(getX()-v2.getX(),2)+Math.pow(getY()-v2.getY(),2));
    }
    public float dist(Vector2 v2){
        return (float) Math.sqrt(dist2(v2));
    }
}
