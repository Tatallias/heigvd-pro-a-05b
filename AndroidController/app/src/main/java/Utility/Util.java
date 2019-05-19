package Utility;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.Log;

import java.util.Scanner;

/**
 * A class containing multiples Utilities variables and methods
 */
public class Util {

    public static int directAttackDistance=10000;
    public static int maxTolerableDistance=45000;
    public static int maxPerfectDistance=12000;
    public static float minTolerableLength=500;
    public static String earthPathString="l60.856-136.018c2.589-4.994,9.17-4.994,11.759,0l55.137,124.986c2.553,4.925-0.701,11.032-5.879,11.032h-50.467c-3.732,0-6.758-3.308-6.758-7.388v-28.601";
    public static String waterPathString="c0,98.619-39.515,90.282-37.735,134.769c0.634,15.855,12.781,41.085,41.721,41.085c18.811,0,34.06-15.249,34.06-34.06c0-12.227-9.913-22.007-22.139-22.139c-9.881-0.107-16.541,7.504-17.175,19.871";
    public static String firePathString="c0,0-26.481-31-7.251-85.333c42.875,83.667,89.848-3.333,75.346-85.667c32.156,29.333,28.222,153.333-1.892,171";
    public static String lightningPathString="c-6.7,41.2-49.2,92.8-49.2,92.8l88.5-21c0,0-39.9,55.8-49.2,91.8";

    public static String shieldPathString="c-50.7,5.4-90.2,48.3-90.2,100.4c0,55.8,45.2,101,101,101s101-45.2,101-101c0-49.9-36.2-91.3-83.8-99.5";


    float prevX;
    float prevY;
    float prevXControl;
    float prevYControl;


    /**
     *  parses the svg string to a Path
     * @param svgDesc the svgDescription
     * @return the built Path
     */
    public Path pathFromSVGPath(String svgDesc){
        return pathFromSVGPath(svgDesc,0,0);
    }

    /**
     * parses the svg string to a Path starting at a specific location
     * @param svgDesc the svg string
     * @param x the x position
     * @param y the y position
     * @return the built path
     */
    public Path pathFromSVGPath(String svgDesc,float x, float y){


        Path path= new Path();

        path.moveTo(x,y);
        PeekableScanner s= new PeekableScanner(new Scanner(svgDesc).useDelimiter(""));

        while (s.hasNext()){
            String str= s.next();

            switch (str){
                case "M":
                    Log.d("dlog",str+"");
                    moveTo(path,s);
                    break;
                case "l":
                    Log.d("dlog",str+"");
                    lineTo(path,s);
                    break;
                case "c":
                    Log.d("dlog",str+"");
                    cubicTo(path,s);
                    break;
                case "h":
                    Log.d("dlog",str+"");
                    hLineTo(path,s);
                    break;
                case "v":
                    Log.d("dlog",str+"");
                    vLineTo(path,s);
                    break;
                case "s":
                    Log.d("dlog",str+"");
                    sCubicTo(path,s);
                    break;
                default:

                    Log.d("dlog","dont know what to do with: "+str);
                    break;
            }

        }
        return path;
    }


    private void sCubicTo(Path p,PeekableScanner s){
        float x1=prevX-prevXControl;
        float y1=  prevY-prevYControl;
        float x2=nextFloat(s);
        float y2= nextFloat(s);
        float x3=nextFloat(s);
        float y3= nextFloat(s);

        p.rCubicTo(x1,y1,x2,y2,x3,y3);
        prevXControl=x2;
        prevYControl=y2;
        prevY=y3;
        prevX=x2;
    }
    private void cubicTo(Path p,PeekableScanner s){
        float x1=nextFloat(s);
        float y1= nextFloat(s);
        float x2=nextFloat(s);
        float y2= nextFloat(s);
        float x3=nextFloat(s);
        float y3= nextFloat(s);

        p.rCubicTo(x1,y1,x2,y2,x3,y3);
        prevX=x3;
        prevY=y3;
        prevXControl=x2;
        prevYControl=y2;
    }

    private void hLineTo(Path p,PeekableScanner s) {
        float x=nextFloat(s);
        float y= 0;

        p.rLineTo(x,y);
        prevX=x;
        prevY=y;
    }
    private void vLineTo(Path p,PeekableScanner s) {
        float x=0;
        float y=nextFloat(s);

        p.rLineTo(x,y);
        prevX=x;
        prevY=y;
    }

    private void lineTo(Path p,PeekableScanner s){
        float x=nextFloat(s);
        float y= nextFloat(s);

        p.rLineTo(x,y);
        prevX=x;
        prevY=y;
    }
    private void moveTo(Path p,PeekableScanner s){
        float x=nextFloat(s);
        float y= nextFloat(s);

        p.moveTo(x,y);
        prevX=x;
        prevY=y;
    }


    private float nextFloat(PeekableScanner s){
        String fullString=new String();

       while(s.hasNext()) {


           String c = s.peek();

           if (c.equals(".")||c.equals("0")||c.equals("1")||c.equals("2")||c.equals("3")||c.equals("4")||c.equals("5")||c.equals("6")||c.equals("7")||c.equals("8")||c.equals("9")){
               fullString += c;
               s.next();
           } else if (c.equals("-")) {
               if (fullString.length() == 0) {
                   fullString += c;
                   s.next();
               } else {

                   return Float.parseFloat(fullString);
               }
           }else if(c.equals(",")) {
               s.next();
               return Float.parseFloat(fullString);
           }else{

               return Float.parseFloat(fullString);
           }

       }
        return Float.parseFloat(fullString);
    }

    public static Path pathFromVectorArray(Vector2[] vectors){
        Path p = new Path();
        if(vectors.length>0) {
            p.moveTo(vectors[0].getX(), vectors[0].getY());
        }
        for(int i=1;i<vectors.length;i++){
            p.lineTo(vectors[i].getX(),vectors[i].getY());
        }
        return p;
    }


    public static Vector2[] pathToVectorArray(Path p,int subDiv){
        PathMeasure pm = new PathMeasure(p, false);

        float length= pm.getLength();
        float lengthSubDiv= length/subDiv;



        Vector2[] result= new Vector2[subDiv+1];
        for (int i = 0; i< subDiv+1; i ++)
        {
            float aCoordinates[] = {0f, 0f};
            pm.getPosTan(lengthSubDiv * i, aCoordinates, null);
            result[i]=new Vector2(aCoordinates[0],aCoordinates[1]);
        }


        return result;
    }

    /**
     * returns whether or not the app is running in an emulator*
     * @return true if in emulator or false if it isn't
     */
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
