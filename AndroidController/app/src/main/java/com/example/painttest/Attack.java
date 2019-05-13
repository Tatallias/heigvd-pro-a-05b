package com.example.painttest;


import android.graphics.Path;

import Connection.Protocol;

public class Attack extends Spell {
    Vector2 direction;

    public Attack( Vector2 direction) {
        this.direction=direction;
    }


    public String getRequest(){
        String s = "ATT "+ direction.getX()+" "+direction.getY()+"$";
        return s;
    }
}
