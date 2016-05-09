package upc.edu.pe.wedraw;

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
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Bus;
import de.halfbit.tinybus.Subscribe;
import de.halfbit.tinybus.TinyBus;
import de.halfbit.tinybus.wires.ShakeEventWire;
import upc.edu.pe.wedraw.components.DrawingView;

public class DrawActivity extends AppCompatActivity implements SensorEventListener{

    DrawingView drawingView;
    Button mSelectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawingView = (DrawingView) findViewById(R.id.drawingView);

        //Setup bus to detect shake
        setupBus();
    }

    public void colorClicked(View v) {
        int colorId;
        switch (v.getId()){
            case R.id.buttonBlack:  colorId = R.color.paint_black;  break;
            case R.id.buttonBlue:  colorId = R.color.paint_blue;  break;
            case R.id.buttonGreen:  colorId = R.color.paint_green;  break;
            case R.id.buttonRed:  colorId = R.color.paint_red;  break;
            case R.id.buttonYellow:  colorId = R.color.paint_yellow;  break;
            default: colorId=R.color.paint_black;
        }

        if(mSelectedColor!=null){
            mSelectedColor.setAlpha(1.0f);
        }
        mSelectedColor = (Button) v;
        mSelectedColor.setAlpha(0.5f);

        drawingView.setColor(ContextCompat.getColor(this,colorId));

    }

    //<editor-fold desc="Sensor managing detector">

    protected SensorManager mSensorManager;
    protected Sensor mSensor;
    private long mLastUpdate;
    protected int UPDATE_THRESHOLD = 300;

    private void setupBus(){

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //validate that sensor is available, before showing the activity
        if(null == (mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
            finish();
    }


    private int countPos=0,countNeg = 0;

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture mTask;
    @Override
    public void onSensorChanged(SensorEvent event) {
        //We check the detected event change corresponds to this activity sensor type
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //We determine if enough time has elapsed since last update
            long actualTime = System.currentTimeMillis();
            if(actualTime > mLastUpdate - UPDATE_THRESHOLD){
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
                        ((DrawingView) findViewById(R.id.drawingView)).clearDrawing();
                    }
                }
            }
        }else{

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
        mSensorManager.registerListener(this,mSensor, SensorManager.SENSOR_DELAY_UI);
        mLastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    //</editor-fold>
}
