package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import upc.edu.pe.wedraw.connection.DesaplgListener;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;

public class DifficultyActivity extends /*Sensor*/Activity implements View.OnClickListener{

    Button buttonEasy, buttonMedium, buttonHard;
    JsonHelper.Difficulties currentDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        ConnectionHelper.sDesaplgListener.setDifficultyActivity(this);

        buttonEasy = (Button) findViewById(R.id.buttonEasy);
        buttonMedium = (Button) findViewById(R.id.buttonMedium);
        buttonHard = (Button) findViewById(R.id.buttonHard);

        buttonEasy.setOnClickListener(this);
        buttonMedium.setOnClickListener(this);
        buttonHard.setOnClickListener(this);

        buttonEasy.setOnTouchListener(new PressingListenerHandler());
        buttonMedium.setOnTouchListener(new PressingListenerHandler());
        buttonHard.setOnTouchListener(new PressingListenerHandler());
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
                Toast.makeText(DifficultyActivity.this,"DOWN",Toast.LENGTH_SHORT).show();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                buttonEasy.setEnabled(true);
                buttonMedium.setEnabled(true);
                buttonHard.setEnabled(true);
                Toast.makeText(DifficultyActivity.this,"UP",Toast.LENGTH_SHORT).show();
            }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
                Toast.makeText(DifficultyActivity.this,"RELEASED",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    private void startGame(){
        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.selectGameDifficulty(currentDifficulty), null);
    }

    //<editor-fold desc="Detección de giro de 90º para iniciar el juego">
    /*@Override
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
                startGame();
            }
        }
    }
    //</editor-fold>*/
}