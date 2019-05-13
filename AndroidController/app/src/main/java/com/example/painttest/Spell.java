package com.example.painttest;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;

public abstract class Spell {

    public abstract String getRequest();


    /**
     * decides what a path p means and what to do with it
     * @param p
     * @return
     */
    public static Spell computePathToSpell(Path p){
        Vector2[] subdividedPath= Util.pathToVectorArray(p,20);
        float attackDistance= distanceFromStraight(subdividedPath);
        RectF pBounds = new RectF();
        PathMeasure pm = new PathMeasure(p, false);

        float length= pm.getLength();
        if( length<Util.minTolerableLength){
            return null;
        }

        if(attackDistance<= Util.directAttackDistance){
            Vector2 dir= subdividedPath[subdividedPath.length-1].sub(subdividedPath[0]);
            return new Attack(dir);
        }

        float minDistance=Float.MAX_VALUE;
        Element[] elements= {Element.FIRE, Element.WATER, Element.EARTH,Element.LIGHTNING,Element.SHIELD};


        p.computeBounds(pBounds,true);
        float pHeight=pBounds.height();
        float pWidth=pBounds.width();

        Element elementFound=null;

        for (int i=0; i <elements.length;i++){
            float eWidth= elements[i].getBounds().width();
            float eHeight= elements[i].getBounds().height();

            float widthRatio=pWidth/eWidth;
            float heightRatio= pHeight/eHeight;
            float scale= Math.max(widthRatio,heightRatio);
            float xTranslate=pBounds.centerX()-elements[i].getBounds().centerX();
            float yTranslate=pBounds.centerY()-elements[i].getBounds().centerY();
            Path ePath= new Path(elements[i].getPath());
            Matrix s= new Matrix();

            s.setTranslate(xTranslate,yTranslate);

            ePath.transform(s);
            s.setScale(scale,scale,elements[i].getBounds().centerX()+xTranslate,elements[i].getBounds().centerY()+yTranslate);
            ePath.transform(s);
            Vector2 [] eVec= Util.pathToVectorArray(ePath,20);
            float dist= new FrechetDistance().distance(subdividedPath,eVec);
            for(int j = 0 ; j< eVec.length/2;j++){
                Vector2 temp= eVec[j];
                eVec[j]=eVec[eVec.length-1-j];
                eVec[eVec.length-1-j]=temp;
            }
            dist= Math.min(dist,new FrechetDistance().distance(subdividedPath,eVec));

            if(dist<minDistance){
                elementFound=elements[i];
                minDistance=dist;

            }
        }

        if(elementFound==Element.SHIELD){
            return new Shield();
        }else {
            SpellQuality sq=SpellQuality.qualityFromDistance((int)minDistance,(int)(Util.maxTolerableDistance),(int)(Util.maxPerfectDistance));
            if(sq!=SpellQuality.FAILED) {
                return new ChanneledElement(elementFound, sq);
            }
        }

        return null;
    }



    public static float distanceFromStraight(Vector2[] subdividedPath){
        Vector2[] straightPath={subdividedPath[0],subdividedPath[subdividedPath.length-1]};
        Vector2[] straightenedSubdividedPath= Util.pathToVectorArray(Util.pathFromVectorArray(straightPath),20);
        float attackDistance=new FrechetDistance().distance(straightenedSubdividedPath,subdividedPath);
        return attackDistance;
    }






}
