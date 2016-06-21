package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import upc.edu.pe.wedraw.connection.DesaplgListener;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

public class TurnHintActivity extends Activity {

    boolean isMyTurnToDraw;
    ImageView iviHint;

    private static final int DISPLAY_TIME = 5000;
    private static final String PARAM_IS_MY_TURN = "param_is_my_turn";

    public static void startActivity(Activity activity, boolean isMyTurnToDraw){
        if(activity==null)  return;
        Intent i = new Intent(activity, TurnHintActivity.class);
        StatusHelper.isMyTurnToDraw = isMyTurnToDraw;
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_hint);
        ConnectionHelper.sDesaplgListener.setTurnHintActivity(this);

        iviHint = (ImageView) findViewById(R.id.iviHint);

        //isMyTurnToDraw = getIntent().getBooleanExtra(PARAM_IS_MY_TURN,false);
        isMyTurnToDraw = StatusHelper.isMyTurnToDraw;
        if(isMyTurnToDraw){
            iviHint.setImageResource(R.drawable.tudibujas);
            //Mostrar vista para que escoja dificultad después de un par de segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TurnHintActivity.this, DifficultyActivity.class);
                    startActivity(i);
                    finish();
                }
            }, DISPLAY_TIME);
        }else{
            iviHint.setImageResource(R.drawable.tuadivinas); //TODO: replace with real image
        }
    }

    @Override
    protected void onDestroy() {
        ConnectionHelper.sDesaplgListener.setTurnHintActivity(null);
        System.gc();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {}

}