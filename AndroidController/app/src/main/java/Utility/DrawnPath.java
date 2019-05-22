package Utility;
import android.graphics.Path;

public class DrawnPath {

    public int color;
    public boolean emboss;
    public boolean blur;
    public int strokeWidth;
    public Path path;

    public DrawnPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }

    public void setPath(Path path) {
        this.path = path;
    }


}