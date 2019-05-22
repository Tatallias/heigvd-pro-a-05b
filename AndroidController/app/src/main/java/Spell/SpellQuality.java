package Spell;



import Utility.Util;

/**
 * enum of the different quality of spells
 */
public enum SpellQuality {PERFECT,GOOD,OK,FAILED;

    /**
     * returns the apropriate quality based on the distance between the drawn path and the stored element path
     * @param dist the distance between the drawn path and the stored element path
     * @return the corresponding spell quality
     */
    public static SpellQuality qualityFromDistance(int dist){
        float step= (Util.maxTolerableDistance-Util.maxPerfectDistance)/2f;
        if(dist<Util.maxPerfectDistance){
            return PERFECT;
        }else if(dist<Util.maxPerfectDistance+step*1){
            return GOOD;
        }else if(dist<Util.maxPerfectDistance+step*2){
            return OK;
        }else{
            return FAILED;
        }
    }

}
