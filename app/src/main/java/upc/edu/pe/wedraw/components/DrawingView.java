package upc.edu.pe.wedraw.components;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.ArrayList;
import java.util.List;

import upc.edu.pe.wedraw.R;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.WedrawUtils;

public class DrawingView extends View {

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private Paint mPaint;
    private Bitmap mBackground;
    public DrawingView(Context c, AttributeSet attrs){
        super(c,attrs);
        init(c);
    }

    public DrawingView(Context c) {
        super(c);
        init(c);
    }

    /**
     * Inicializa el bitmap que se utlizará como referencia para la detección de los puntos sobre los cuales se puede dibujar
     * @param width
     * @param height
     */
    public void initBitmap(int width, int height){
        mBackground =  WedrawUtils.getBitmapFromView(this);
        mBackground = Bitmap.createScaledBitmap(mBackground, width, height, false);

    }

    /**
     * Inicializa los valores del canvas y pincel utilizados para realizar el dibujo
     * @param c
     */
    private void init(Context c){

        //mBackground = WedrawUtils.getBitmapFromView(this);

        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    /**
     * Sirve para poder actualizar el color del pincel con el que se esta dibujando
     * @param color color que se quiere utilizar
     * @param isWhite indica si el nuevo color es blanco
     */
    public void setColor(int color, boolean isWhite) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth( isWhite ? 30 : 12);
    }

    /**
     * Actualiza los valores de las diensiones cuando varía el tamaño de la pantalla. Es decir, al momento de robar el dispositivo
     * @param w nuevo ancho
     * @param h nuevo alto
     * @param oldw ancho anterior
     * @param oldh alto anterior
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    /**
     * Actualiza el canvas del dibujo para que muestre los trazos que se realizan sobre el mismo
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /**
     * Inicializa el path en un punto en especifico cuando se inicia el touch sobre la vista
     * @param x coordenada x
     * @param y coordenada y
     */
    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * Dibuja una linea desde el último punto en el cual se estaba posicionado hacia la nueva posición para dar el efecto de trazo continuo
     * @param x coordenada x
     * @param y coordenada y
     */
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mPath.lineTo(mX, mY);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    /**
     * Resetea el path cuando se deja de presionar la vista
     */
    private void touch_up() {
        Log.i("WEDRAW","Destiny x: " + mX + "  Y: " + mY);
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }


    /**
     * Dibuja un punto cuando es la realiza el touch por primera vez
     * @param x coordenada x
     * @param y coordenada y
     */
    private void drawPoint(float x, float y){
        mCanvas.drawPoint(x,y,mPaint);
    }

    boolean isFirst = false;

    /**
     * Detecta cuando se toca la vista para determina que accion debe tomar.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return true;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isDrawableSpot(x,y)) {
                    isFirst = true;
                    touch_start(x, y);
                    mCanvas.drawPoint(x,y,mPaint);
                    ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.setPosition(x * 1.0 / getWidth(), y * 1.0 / getHeight()), null);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("WEDRAW","Action move");
                if(!isDrawableSpot(x,y)){
                    isFirst = false;
                    break;
                }

                if(!isFirst){
                    isFirst = true;
                    touch_start(x, y);
                    mCanvas.drawPoint(x,y,mPaint);
                }

                touch_move(x, y);
                touch_up();
                touch_start(x,y);
                ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.makeDraw(x * 1.0 / getWidth(), y * 1.0 / getHeight()), null);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isFirst = false;
                if(isDrawableSpot(x,y))
                    touch_up();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Decide si el lugar donde se ha presionado puede colorearse o no
     * @param x coordenada x donde se le dio click
     * @param y coordenada y donde se le dio click
     * @return
     */
    private boolean isDrawableSpot(float x, float y) {
        try {
            int pixel = mBackground.getPixel((int) x, (int) y);
            int G = (pixel >> 8) & 0xff;
            return G*2>255;
        }catch (IllegalArgumentException ex){
            return false;
        }
    }

    /**
     * Borra todos los dibujos que se han hecho sobre la imagen
     */
    public void clearDrawing()
    {
        setDrawingCacheEnabled(false);
        // don't forget that one and the match below,
        // or you just keep getting a duplicate when you save.

        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
    }
}