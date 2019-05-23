package com.example.Wizards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Connection.Handler;
import Spell.*;
import Utility.DrawnPath;


/**
 * The casting board view where the player draws the spells
 *
 */
public class CastingBoardView extends View {


    public static int BRUSH_SIZE = 40;
    public static final int DEFAULT_COLOR = Color.WHITE;


    boolean showPath=false;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<DrawnPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = Color.TRANSPARENT;
    private int strokeWidth;

    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private List<Element> channeledElement;

    private List<ImageView> channeledElementImageViews;
    private Activity hostActivity;
    private Handler client;

    private ParticleSystem particleSystem ;
    public CastingBoardView(Context context) {
        this(context, null);
    }


    /**
     * constructs the casting board view and sets the default values for all the attributes
     * @param context the activity context
     * @param attrs the attributes of the view
     */
    public CastingBoardView(Context context, AttributeSet attrs) {


        super(context, attrs);
        channeledElement = new ArrayList<>();


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);



        mBlur = new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL);
    }


    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    /**
     * sets the client handler to the given handler
     * @param handler the connection handler set up to communicate with the server
     */
    public void connect(Handler handler) {
        client = handler;
    }


    /**
     * clear the drawn path(s)
     */
    public void clear() {
        mCanvas.drawColor(backgroundColor, PorterDuff.Mode.CLEAR);
        paths.clear();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        mCanvas.drawColor(backgroundColor, PorterDuff.Mode.CLEAR);

        if(showPath) {
            for (DrawnPath fp : paths) {
                mPaint.setColor(fp.color);
                mPaint.setStrokeWidth(10);

                mPaint.setMaskFilter(mBlur);

                mCanvas.drawPath(fp.path, mPaint);

            }
        }


        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }


    /**
     * creates a new path starting at pos (x,y) when a new touch happens
     * @param x the x pos in pixel of the touch
     * @param y the y pos in pixel of the touch
     */
    private void touchStart(float x, float y) {
        showPath=false;
        setUpParticleEmitter();
        particleSystem.emit((int)x,(int)y,50);
        clear();
        mPath = new Path();
        DrawnPath fp = new DrawnPath(currentColor, false, false, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * adds to the currently drawn path if  the distance between this and the last position is greater than
     * TOUCH_TOLERANCE
     * @param x the x pos in pixel of the touch
     * @param y the y pos in pixel of the touch
     */
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        particleSystem.updateEmitPoint((int)x,(int)y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;


        }
    }

    /**
     * Ends a path when we lift the finger from the screen and computes the corresponding
     * spell. then proceeds to cast it
     */
    private void touchUp() {
        showPath=true;
        particleSystem.stopEmitting();
        mPath.lineTo(mX, mY);
        Spell result = Spell.computePathToSpell(mPath);
        if (result != null) {
            castSpell(result);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("hey");


                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }

        return true;
    }

    /**
     * if the spell is a channeled Element adds it to the channeled element list
     * otherwise empties it
     * also sends the request to be transmitted to the server
     * @param s the spell to cast
     */
    private void castSpell(Spell s) {
        if (s instanceof ChanneledElement) {
            addChanneledElement((ChanneledElement) s);

        } else {
            emptyChanneledElement();
        }
        client.request(s.getRequest());
    }

    /**
     * host activity setter
     * @param hostAct
     */
    public void setHostActivity(Activity hostAct) {
        hostActivity = hostAct;

    }


    /**
     * Creates a new Particle emitter
     */
    public void setUpParticleEmitter(){
        particleSystem= new ParticleSystem(hostActivity, 100,R.drawable.sparkle2, 250l);
        particleSystem.setFadeOut(100l);
        particleSystem.setScaleRange(0.5f,.75f);
        particleSystem.setSpeedRange(0.17f, 0.3f);
    }


    /**
     * adds the newly channeled element to the screen
     * @param e
     */
    public void addChanneledElement(ChanneledElement e){

        if(channeledElement.size()<=3 ){
            channeledElement.add(e.getElement());
            channeledElementImageViews.get(channeledElement.size()-1).setImageResource(e.getElement().getDrawableId());
        }
    }

    /**
     * clears all channeled elements from the screen
     */
    public void emptyChanneledElement(){
        channeledElement.clear();
        for (ImageView i: channeledElementImageViews ) {
            i.setImageResource(android.R.color.transparent);
        }
    }

    /**
     * channeledElementImageViews setter
     * @param i the new channeledElementImageViews
     */
    public void setChanneledElementImageViews(List<ImageView > i){
        channeledElementImageViews=i;
    }

}