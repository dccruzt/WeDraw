package upc.edu.pe.wedraw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by aqws3 on 4/13/16.
 */
public class StartGameActivity extends AppCompatActivity{

    Button mStartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mStartButton = (Button) findViewById(R.id.activity_start_game_start_button);
    }
}
