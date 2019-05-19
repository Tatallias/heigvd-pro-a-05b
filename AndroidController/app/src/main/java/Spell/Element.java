package Spell;

import android.graphics.Path;
import android.graphics.RectF;

import com.example.Wizards.R;
import Utility.Util;

import Connection.Protocol;

/**
 * class representing the different elements with a path, name, drawable id, and protocol integer
 */
public class Element {
    public final  static Element FIRE = new Element(new Util().pathFromSVGPath(Util.firePathString),"Fire", R.drawable.fire,Protocol.CMD_FIRE_INT);
    public final static Element EARTH = new Element(new Util().pathFromSVGPath(Util.earthPathString),"Earth",R.drawable.earth,Protocol.CMD_EARTH_INT);
    public final static Element WATER = new Element(new Util().pathFromSVGPath(Util.waterPathString),"Water",R.drawable.water,Protocol.CMD_WATER_INT);
    public final static Element LIGHTNING = new Element(new Util().pathFromSVGPath(Util.lightningPathString),"Lightning",R.drawable.lightning,Protocol.CMD_ELECTRICITY_INT);
    public final static Element SHIELD = new Element(new Util().pathFromSVGPath(Util.shieldPathString),"Shield",0,-1);

    private final Path path;
    private final String name;
    private RectF bounds;

    private int drawableId;


    private int protocolInt;

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

    private Element(Path path, String name,int id,int protocolInt) {
        this.drawableId=id;
        this.path = path;
        this.name=name;
        this.bounds=new RectF();
        path.computeBounds(bounds,true);

        this.protocolInt= protocolInt;
    }

    public int getProtocolInt() {
        return protocolInt;
    }



}
