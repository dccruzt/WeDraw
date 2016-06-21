package upc.edu.pe.wedraw;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import upc.edu.pe.wedraw.components.DrawingView;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;
import upc.edu.pe.wedraw.helpers.WedrawUtils;

public class DrawActivity extends AppCompatActivity implements SensorEventListener{

    private ViewGroup layoutDraw;
    DrawingView drawingView;
    Button mSelectedColor;
    TextView txtWord;
    private double ASPECT_RATIO = 1.71;
    //private double mAspectRatio = 827 / 1332;// 1332 / 827; //width / height

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ConnectionHelper.sDesaplgListener.setDrawActivity(this);

        layoutDraw = (ViewGroup) findViewById(R.id.layoutDraw);
        txtWord = (TextView) findViewById(R.id.txtWord);
        txtWord.setText(StatusHelper.word);
        drawingView = (DrawingView) findViewById(R.id.drawingView);
        drawingView.post(new Runnable() {
            @Override
            public void run() {
                resizeToAdjustAspectRatio();
                drawingView.initBitmap();
            }
        });
        //Setup sensors to detect shake
        setupSensors();
    }

    /**
     * Ajusta el tamaño de LA pizarra para que mantenga la misma proporciona que el de la pizarra
     */
    private void resizeToAdjustAspectRatio(){
        int maxHeight = drawingView.getHeight();
        int maxWidth = drawingView.getWidth();
        ViewGroup.LayoutParams params = drawingView.getLayoutParams();
        //Use fixed width
        int expectedHeightIfFixedWidth = (int) (maxWidth / ASPECT_RATIO);
        params.width = maxWidth;
        params.height = expectedHeightIfFixedWidth;
        //Use fixed height
        int expectedWidthIfFixedHeight = (int) (maxHeight * ASPECT_RATIO);
        params.height = maxHeight;
        params.width = expectedWidthIfFixedHeight;
        if(ASPECT_RATIO>1){
            if(expectedHeightIfFixedWidth>maxHeight){
                params.width = expectedWidthIfFixedHeight;
                params.height = maxHeight;
            }else{
                params.width = maxWidth;
                params.height = expectedHeightIfFixedWidth;
            }
        }else{
            if(expectedWidthIfFixedHeight>maxWidth){
                params.height = expectedHeightIfFixedWidth;
                params.width = maxWidth;
            }else{
                params.width = expectedWidthIfFixedHeight;
                params.height = maxHeight;
            }
        }

        drawingView.setLayoutParams(params);
    }

    public void habilitarElementos(boolean estado){

        setearElementos(layoutDraw, estado);
    }

    public void setearElementos(ViewGroup layout, boolean estado){

        layout.setEnabled(estado);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setearElementos((ViewGroup) child, estado);
            } else
                child.setEnabled(estado);
        }
    }

    /**
     * Cambia el color de la linea que se pinta en la pizarra. Ocurre cuando se le da click a cualquiera de los  botones para cambiar color
     * @param v Boton al cual se le dio click
     */
    public void colorClicked(View v) {
        int colorId;
        String color = "";
        switch (v.getId()){
            case R.id.buttonBlack:  colorId = R.color.paint_black; color = "black"; break;
            case R.id.buttonBlue:  colorId = R.color.paint_blue; color = "blue"; break;
            case R.id.buttonGreen:  colorId = R.color.paint_green; color = "green"; break;
            case R.id.buttonRed:  colorId = R.color.paint_red; color = "red"; break;
            case R.id.buttonYellow:  colorId = R.color.paint_yellow; color = "yellow"; break;
            default: colorId=R.color.paint_black;
        }

        if(mSelectedColor!=null){
            mSelectedColor.setAlpha(1.0f);
        }
        mSelectedColor = (Button) v;
        mSelectedColor.setAlpha(0.5f);

        drawingView.setColor(ContextCompat.getColor(this, colorId));

        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.changeColor(color), null);
    }

    //<editor-fold desc="Sensor managing detector">

    protected SensorManager mSensorManager;
    protected Sensor mAccelerometerSensor,mGyroSensor;
    private long mLastUpdateAccelerometer,mLastUpdateGyro;
    protected int UPDATE_THRESHOLD = 300;
    private boolean accelerometerAvailable,gyroscopeAvailable;

    /**
     * Inicializa los sensores que se utilizaran para detectar los movimientos del acelerometro y el giroscopio
     */
    private void setupSensors(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerAvailable = mAccelerometerSensor !=null;
        gyroscopeAvailable = mGyroSensor !=null;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //We check the detected event change corresponds to this activity sensor type
        if(accelerometerAvailable && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //We determine if enough time has elapsed since last update
            long actualTime = System.currentTimeMillis();
            if(actualTime > mLastUpdateAccelerometer - UPDATE_THRESHOLD){
               handleAccelerometerEvent(event);
            }
        }else if(gyroscopeAvailable && event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //We determine if enough time has elapsed since last update
            long actualTime = System.currentTimeMillis();
            if(actualTime > mLastUpdateGyro - UPDATE_THRESHOLD){
                handleGyroEvent(event);
            }
        }
    }

    private int countPos=0,countNeg = 0;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture mTask;
    /**
     * Limpia la pantalla cuando el celular se mueve de atras hacia adelante repetidas veces en el eje Z dentro de un lapso de tiempo determinado
     * @param event Evento del sensor que tiene los datos del movimiento detectados por el sensor
     */
    private void handleAccelerometerEvent(SensorEvent event){
        //Shake logic
        if(Math.abs(event.values[2])>=13){
            Log.i("SHAKE","Pass");
            if(countPos==0){
                if(mTask!=null)
                    mTask.cancel(true);
                Runnable task = new Runnable() {
                    public void run() {
                        countPos = countNeg = 0;
                        mTask = null;
                    }
                };
                mTask = worker.schedule(task, 1500, TimeUnit.MILLISECONDS);
            }

            if(event.values[2]>0)
                countPos++;
            else
                countNeg++;
            if((countPos>=2 && countNeg>1) || (countPos>=1 && countNeg>2)){
                countPos = countNeg = 0;

                ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.eraseDraw(), new ResponseListener<Object>() {

                    @Override
                    public void onError(ServiceCommandError error) {

                    }
                    @Override
                    public void onSuccess(Object object) {

                        ((DrawingView) findViewById(R.id.drawingView)).clearDrawing();
                    }
                });
            }
        }
    }


    private boolean positiveSpin = false;
    private int negativeSpinCount = 0;
    /**
     * Limpia la pantalla cuando el celular se gira hasta 60º (y regresa) en el eje Y del celular dentro de un lapso de tiempo determinado
     * @param event Evento del sensor que tiene los datos del movimiento detectados por el sensor
     */
    private void handleGyroEvent(SensorEvent event){
        double var = Math.toDegrees(event.values[1]);
        if(var>400){
            positiveSpin = true;
            if(mTask!=null)
                mTask.cancel(true);
            Runnable task = new Runnable() {
                public void run() {
                    positiveSpin = false;
                    negativeSpinCount = 0;
                    mTask = null;
                }
            };
            mTask = worker.schedule(task, 500, TimeUnit.MILLISECONDS);
        }else if(positiveSpin && var<-400) {
            if(negativeSpinCount==0)
                negativeSpinCount++;
            else{
                positiveSpin = false;
                negativeSpinCount = 0;
                if(mTask!=null)
                    mTask = null;
                else
                    mTask.cancel(true);
                ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.eraseDraw(), new ResponseListener<Object>() {

                    @Override
                    public void onError(ServiceCommandError error) {

                    }

                    @Override
                    public void onSuccess(Object object) {

                        ((DrawingView) findViewById(R.id.drawingView)).clearDrawing();
                    }
                });
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //</editor-fold>

    //<editor-fold desc="Register and unregister the sensor listener, to optimize battery usage">
    @Override
    protected void onResume(){
        super.onResume();
        if(accelerometerAvailable)
            mSensorManager.registerListener(this,mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if(gyroscopeAvailable)
            mSensorManager.registerListener(this,mGyroSensor, SensorManager.SENSOR_DELAY_UI);
        mLastUpdateAccelerometer = mLastUpdateGyro = System.currentTimeMillis();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mSensorManager!=null)
            mSensorManager.unregisterListener(this);
    }
    //</editor-fold>
}