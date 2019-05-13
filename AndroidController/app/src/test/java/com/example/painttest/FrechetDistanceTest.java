package com.example.painttest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FrechetDistanceTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void samePathHaveA0Distance(){
        FrechetDistance f= new FrechetDistance();
        Vector2 v1= new Vector2(1,3);
        Vector2 v2= new Vector2(2,3);
        Vector2 v3= new Vector2(4,5);
        Vector2 v4= new Vector2(1,2);

        Vector2 []path1={v1,v2,v3,v4};
        Vector2 []path2={v1,v2,v3,v4};

        float dist =f.distance(path1,path2);

        assertEquals(0,dist,0.005f);
    }

    @Test
    public void closePathShouldHaveDistance1(){
        FrechetDistance f= new FrechetDistance();
        Vector2 v1= new Vector2(1,3);
        Vector2 v2= new Vector2(2,3);
        Vector2 v3= new Vector2(3,3);
        Vector2 v4= new Vector2(4,3);
        Vector2 v5= new Vector2(4,4);
        Vector2 []path1={v1,v2,v3,v4};
        Vector2 []path2={v1,v2,v3,v5};

        float dist =f.distance(path1,path2);

        assertEquals(1,dist,0.005f);
    }
}
