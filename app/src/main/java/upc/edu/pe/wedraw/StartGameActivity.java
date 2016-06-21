package upc.edu.pe.wedraw;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import upc.edu.pe.wedraw.components.CustomTextView;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

/**
 * Created by aqws3 on 4/13/16.
 */
public class StartGameActivity extends AppCompatActivity{

    Button mStartButton;
    CustomTextView txtPlayerName,txtHint;

    boolean currentStatus = false;
    int count =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        ConnectionHelper.sDesaplgListener.setStartGameActivity(this);

        txtPlayerName = (CustomTextView) findViewById(R.id.txtPlayerName);
        txtPlayerName.setText(StatusHelper.playerName);
        txtHint = (CustomTextView) findViewById(R.id.txtHint);

        mStartButton = (Button) findViewById(R.id.activity_start_game_start_button);

        if(StatusHelper.btnJugar_activo){
            activarJugar(true);
        }
    }

    public void jugar(View v){

        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.requestGameStart(), new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError(ServiceCommandError error) {

            }
        });
    }

    /**
     * Metodo invocado cuando la TV mande una señal para indicar su se puede iniciar el juego o no,
     * y por ende activar el boton "JUGAR" o no
     * @param value indica si se puede comenzar a jugar o no
     */
    public void activarJugar(boolean value){
        TransitionDrawable transition = (TransitionDrawable) mStartButton.getBackground();
        if(currentStatus == value)
            return;
        currentStatus = value;
        if(currentStatus) { // Activar boton
            transition.startTransition(500);
            mStartButton.setEnabled(true);
            txtHint.setText(getString(R.string.start_game));
        }else{ //Desactivar boton
            transition.reverseTransition(500);
            mStartButton.setEnabled(false);
            txtHint.setText(getString(R.string.waiting_players));
        }
    }
    @Override
    public void onBackPressed() {}
}