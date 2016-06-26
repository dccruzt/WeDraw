package upc.edu.pe.wedraw;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

public class DifficultyActivity extends SensorActivity implements View.OnClickListener{

    Button buttonEasy, buttonMedium, buttonHard;
    TextView txtSensor;
    JsonHelper.Difficulties currentDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        ConnectionHelper.sDesaplgListener.setDifficultyActivity(this);

        txtSensor = (TextView) findViewById(R.id.txtSensor);
        buttonEasy = (Button) findViewById(R.id.buttonEasy);
        buttonMedium = (Button) findViewById(R.id.buttonMedium);
        buttonHard = (Button) findViewById(R.id.buttonHard);

        buttonEasy.setOnClickListener(this);
        buttonMedium.setOnClickListener(this);
        buttonHard.setOnClickListener(this);

        buttonEasy.setOnTouchListener(new PressingListenerHandler());
        buttonMedium.setOnTouchListener(new PressingListenerHandler());
        buttonHard.setOnTouchListener(new PressingListenerHandler());

        if(isSensorAvailable)
        {
            txtSensor.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Metodo para manejar el click en cada botón. Cuando se presione, almacenar la dificultad seleccionada
     * @param v
     */
    @Override
    public void onClick(View v) {
        //Set difficulty depending on
        currentDifficulty =
                v.getId() == R.id.buttonEasy ? JsonHelper.Difficulties.EASY :
                        v.getId() == R.id.buttonMedium ? JsonHelper.Difficulties.MEDIUM : JsonHelper.Difficulties.HARD;

        buttonEasy.setSelected(v.getId()==R.id.buttonEasy);
        buttonMedium.setSelected(v.getId()==R.id.buttonMedium);
        buttonHard.setSelected(v.getId()==R.id.buttonHard);

        if(!isSensorAvailable)
            startGame();

    }

    /**
     * Listener que controla que solo un botón pueda seleccionarse al mismo tiempo
     */
    class PressingListenerHandler implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonEasy.setEnabled(v.getId()==R.id.buttonEasy);
                buttonMedium.setEnabled(v.getId()==R.id.buttonMedium);
                buttonHard.setEnabled(v.getId()==R.id.buttonHard);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                buttonEasy.setEnabled(true);
                buttonMedium.setEnabled(true);
                buttonHard.setEnabled(true);
            }
            return false;
        }
    }

    private void startGame(){
        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.selectGameDifficulty(currentDifficulty), null);
    }

    //<editor-fold desc="Detección de giro de 90º para iniciar el juego">
    @Override
    protected int getSensorType() {
        return Sensor.TYPE_GYROSCOPE;
    }

    @Override
    protected String getSensorFeature() {
        return PackageManager.FEATURE_SENSOR_GYROSCOPE;
    }

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture mTask;
    private boolean positiveSpin = false;
    private int negativeSpinCount = 0;
    private int c=0;
    @Override
    public void detectedSensorChange(SensorEvent event) {
        double var = Math.toDegrees(event.values[2]);
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

                if(currentDifficulty != null)
                    startGame();
                else
                    Toast.makeText(getApplicationContext(), "No ha seleccionado la dificultad.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {

        ConnectionHelper.sDesaplgListener.setDifficultyActivity(null);
        super.onDestroy();
        StatusHelper.unbindDrawables(findViewById(R.id.layoutDifficulty));
        System.gc();
    }
    //</editor-fold>
    @Override
    public void onBackPressed() {}
}