package upc.edu.pe.wedraw;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;

public class DifficultyActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonEasy, buttonMedium, buttonHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

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

    @Override
    public void onClick(View v) {
        //Set difficulty depending on
        JsonHelper.Difficulties difficulty =
                v.getId() == R.id.buttonEasy ? JsonHelper.Difficulties.EASY :
                v.getId() == R.id.buttonMedium ? JsonHelper.Difficulties.MEDIUM : JsonHelper.Difficulties.HARD;

        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.selectGameDifficulty(difficulty), new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object object) {

            }
            @Override
            public void onError(ServiceCommandError error) {

            }
        });
    }
}
