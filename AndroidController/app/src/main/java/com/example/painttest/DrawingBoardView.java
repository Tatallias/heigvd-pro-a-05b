package com.example.painttest;
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
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Connection.Handler;
import Connection.MessageSender;

public class DrawingBoardView extends View {


    public static int BRUSH_SIZE = 40;
    public static final int DEFAULT_COLOR = Color.GRAY;


    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<DrawnPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = Color.TRANSPARENT;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private List<Element> channeledElement;
    private Spell currentSpell=null;

    private Bitmap fireDrawable;
    private Bitmap waterDrawable;
    private Bitmap earthDrawable;
    private Bitmap lightningDrawable;

    private Drawable spellDrawable;

    public String serverIp ="10.192.94.175";
    Handler client;
    public DrawingBoardView(Context context) {
        this(context, null);
    }

    public DrawingBoardView(Context context, AttributeSet attrs) {


        super(context, attrs);
        channeledElement = new ArrayList<>();






        loadGraphics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    private void loadGraphics(){
        fireDrawable = BitmapFactory.decodeResource(getResources(),
                R.drawable.fire);

        waterDrawable= BitmapFactory.decodeResource(getResources(),
                R.drawable.water);

        lightningDrawable =  BitmapFactory.decodeResource(getResources(),
                R.drawable.lightning);

        earthDrawable = BitmapFactory.decodeResource(getResources(),
                R.drawable.earth);


    }
    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void connect(String ip ,int port) {
        client= new Handler(ip,port,false);
    }



    public void clear() {
        mCanvas.drawColor(backgroundColor, PorterDuff.Mode.CLEAR);
        paths.clear();
        normal();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("test","drawed");
        canvas.save();

        mCanvas.drawColor(backgroundColor, PorterDuff.Mode.CLEAR);

        for (DrawnPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);

        }

        drawChanneledElements(mCanvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }



    private void touchStart(float x, float y) {
        clear();
        mPath = new Path();
        DrawnPath fp = new DrawnPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
         Spell result= Spell.computePathToSpell(mPath);
        if(result!=null){
            castSpell(result);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                System.out.println("hey");


                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);

                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }

    private void drawChanneledElements(Canvas c){
        int i = 0;
        //ConstraintLayout box= findViewById(R.id.channeledBox);
        int centerHor= getRight()/2;
        int centerVert= getBottom()/2;
        for(Element e : channeledElement){
            Bitmap bm= bitmapForElement(e);
            spellDrawable= getResources().getDrawable( e.getDrawableId(),null);
            float ratio= (float)bm.getWidth()/(float)bm.getHeight();

            spellDrawable.setBounds(centerHor-(int)(600*ratio/2),centerVert-300,centerHor+(int)(600*ratio/2),centerVert+300);
            spellDrawable.draw(c);
            i++;
        }

    }
    private Bitmap bitmapForElement(Element e){
        if(e.equals(Element.EARTH)){
            return earthDrawable;
        }
        if(e.equals(Element.FIRE)){
            return fireDrawable;
        }
        if(e.equals(Element.WATER)){
            return waterDrawable;
        }
        if(e.equals(Element.LIGHTNING)){
            return lightningDrawable;
        }
        return null;
    }
    private void castSpell(Spell s){
        if(s instanceof ChanneledElement){
            channeledElement.add(((ChanneledElement)s).getElement());
        }else{
            channeledElement.clear();
        }
       // TextView myAwesomeTextView = (TextView)findViewById(R.id.debugInfo);
       // myAwesomeTextView.setText(s.getRequest());
        new MessageSender(client).execute("SEND",s.getRequest());
        //client.sendSpell(s.getRequest());
    }


}