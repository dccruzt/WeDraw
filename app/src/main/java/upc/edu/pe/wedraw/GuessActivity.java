package upc.edu.pe.wedraw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
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
import upc.edu.pe.wedraw.helpers.StringsHelper;

public class GuessActivity extends Activity implements View.OnClickListener{

    private ViewGroup layoutGuess;
    EditText eteWord;
    Button butGuess;
    TextView txtPlayerName,txtHint;

    public static final String PARAM_HINT = "param_hint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        layoutGuess = (ViewGroup) findViewById(R.id.layoutGuess);
        eteWord = (EditText) findViewById(R.id.activity_guess_word);
        butGuess = (Button) findViewById(R.id.activity_guess_button);
        txtPlayerName = (TextView) findViewById(R.id.txtPlayerName);
        txtHint = (TextView) findViewById(R.id.txtHint);
        butGuess.setOnClickListener(this);

        txtPlayerName.setText(StatusHelper.playerName);
        txtHint.setText(StatusHelper.currentHint);
    }

    public void habilitarElementos(boolean estado){

        setearElementos(layoutGuess, estado);
    }

    public void setearElementos(ViewGroup layout, boolean estado){

        layout.setEnabled(estado);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setearElementos((ViewGroup) child, estado);
            } else {

                child.setEnabled(estado);

                /*if(!estado){
                    if(child instanceof Button)
                        ((Button)child).getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }else{
                    if(child instanceof Button)
                        ((Button)child).getBackground().setColorFilter(null);
                }*/
            }
        }
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

        if(word.equals(StatusHelper.word))
            ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.guessWord(true, ""), null);
        else
            ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.guessWord(false, word), null);
    }

}