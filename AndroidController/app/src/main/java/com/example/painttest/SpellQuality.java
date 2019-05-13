package com.example.painttest;

import android.graphics.Color;

public enum SpellQuality {PERFECT,GOOD,OK,FAILED;

    public static SpellQuality qualityFromDistance(int dist,int maxDist,int perfectDist){
        int step= (maxDist-perfectDist)/2;
        if(dist<perfectDist){
            return PERFECT;
        }else if(dist<perfectDist+step*1){
            return GOOD;
        }else if(dist<perfectDist+step*2){
            return OK;
        }else{
            return FAILED;
        }
    }
    public static int colorFromQuality(SpellQuality sq){
        switch (sq){
            case PERFECT: return Color.GREEN;
            case GOOD:return Color.MAGENTA;
            case OK: return Color.YELLOW;
            default: return Color.BLACK;
        }
    }
}
