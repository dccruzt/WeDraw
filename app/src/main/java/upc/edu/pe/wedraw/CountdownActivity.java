package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.w3c.dom.Text;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

public class CountdownActivity extends Activity {

    private static final int INTERVAL = 1000; // 1 seg
    private int count;
    private TextView txtCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        ConnectionHelper.sDesaplgListener.setCountdownActivity(this);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.conectado);
        mp.start();

        txtCount = (TextView) findViewById(R.id.txtCount);
        count = 3;
        decreaseCounter();

    }

    private void decreaseCounter(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(count>0) {
                    txtCount.setText(String.valueOf(count));
                    count--;
                    decreaseCounter();
                }else{
                    //Go to drawing
                    if(StatusHelper.isMyTurnToDraw){
                        Intent i = new Intent(CountdownActivity.this,DrawActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(CountdownActivity.this,GuessActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }, INTERVAL);
    }
    @Override
    public void onBackPressed() {}
}