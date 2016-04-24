package upc.edu.pe.wedraw;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by aqws3 on 4/13/16.
 */
public class StartGameActivity extends AppCompatActivity{

    Button mStartButton;

    boolean inactive = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mStartButton = (Button) findViewById(R.id.activity_start_game_start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionDrawable transition = (TransitionDrawable) mStartButton.getBackground();
                if(inactive) {
                    transition.startTransition(500);
                }else{
                    transition.reverseTransition(500);
                }
                inactive = !inactive;
            }
        });
    }


}
