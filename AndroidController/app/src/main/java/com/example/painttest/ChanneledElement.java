package com.example.painttest;

import android.graphics.Path;

public class ChanneledElement extends Spell {
    private Element element;
    private SpellQuality quality;
    public ChanneledElement(Element element, SpellQuality quality) {
        this.element = element;
        this.quality = quality;

    }

    public Element getElement() {
        return element;
    }

    public SpellQuality getQuality() {
        return quality;
    }

    @Override
    public String getRequest() {
        return "CHA "+element.getProtocolInt()+" "+quality.ordinal()+"$";
    }
}
