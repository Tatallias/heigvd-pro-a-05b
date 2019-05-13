package com.example.painttest;


import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2DTest {
    @Test
    public void distanceBetweenSamePoint(){
        Vector2 v1= new Vector2(2f,2f);
        assertEquals(0f,v1.dist2(v1),0.005f);
    }
}
