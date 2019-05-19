package Spell;

import android.graphics.Path;
import android.graphics.RectF;

import com.example.Wizards.R;
import Utility.Util;

import Connection.Protocol;

public class Element {
    public final  static Element FIRE = new Element(new Util().pathFromSVGPath(Util.firePathString),"Fire", R.drawable.fire, Protocol.CMD_FIRE,Protocol.CMD_FIRE_INT);
    public final static Element EARTH = new Element(new Util().pathFromSVGPath(Util.earthPathString),"Earth",R.drawable.earth,Protocol.CMD_EARTH,Protocol.CMD_EARTH_INT);
    public final static Element WATER = new Element(new Util().pathFromSVGPath(Util.waterPathString),"Water",R.drawable.water,Protocol.CMD_WATER,Protocol.CMD_WATER_INT);
    public final static Element LIGHTNING = new Element(new Util().pathFromSVGPath(Util.lightningPathString),"Lightning",R.drawable.lightning,Protocol.CMD_ELECTRICITY,Protocol.CMD_ELECTRICITY_INT);
    public final static Element SHIELD = new Element(new Util().pathFromSVGPath(Util.shieldPathString),"Shield",0,Protocol.CMD_ELECTRICITY,Protocol.CMD_ELECTRICITY_INT);

    private final Path path;
    private final String name;
    private RectF bounds;

    private int drawableId;

    private String protocolString;

    private int protocolInt;
    public String getProtocolString() {
        return protocolString;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public RectF getBounds() {
        return bounds;
    }

    private Element(Path path, String name,int id,String protocolString,int protocolInt) {
        this.drawableId=id;
        this.path = path;
        this.name=name;
        this.bounds=new RectF();
        path.computeBounds(bounds,true);
        this.protocolString=protocolString;
        this.protocolInt= protocolInt;
    }

    public int getProtocolInt() {
        return protocolInt;
    }



}
