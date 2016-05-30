package upc.edu.pe.wedraw;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.regex.Pattern;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

public class GuessActivity extends Activity implements View.OnClickListener{

    EditText eteWord;
    Button butGuess;
    TextView txtPlayerName,txtHint;

    public static final String PARAM_HINT = "param_hint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        eteWord = (EditText) findViewById(R.id.activity_guess_word);
        butGuess = (Button) findViewById(R.id.activity_guess_button);
        txtPlayerName = (TextView) findViewById(R.id.txtPlayerName);
        txtHint = (TextView) findViewById(R.id.txtHint);
        butGuess.setOnClickListener(this);

        txtPlayerName.setText(StatusHelper.playerName);
        txtHint.setText(StatusHelper.currentHint);
    }

    public void actualizarPista(){
        txtHint.setText(StatusHelper.currentHint);
    }

    /**
     * Evalua la palabra que se está adivinando y la envía a la TV
     * @param v
     */
    @Override
    public void onClick(View v) {
        String word = eteWord.getText().toString().trim();
        if(word.length()<0)
            return;

        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.guessWord(word), new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object object) {
                //Do something (or not...)
            }
            @Override
            public void onError(ServiceCommandError error) {

            }
        });
    }

}
