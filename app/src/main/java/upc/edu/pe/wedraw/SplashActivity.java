package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.connectsdk.discovery.DiscoveryManager;

import java.util.Timer;
import java.util.TimerTask;

import upc.edu.pe.wedraw.connection.DesaplgListener;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectionHelper.sContext = getApplicationContext();
        ConnectionHelper.sDesaplgListener = new DesaplgListener();

        startDiscovery();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, ConnectActivity.class);
                startActivity(i);
                finish();
            }
        };

        ConnectionHelper.sDesaplgListener.setSplashActivity(this);

        new Timer().schedule(task, 5000);
    }

    public void startDiscovery() {
        DiscoveryManager.init(getApplicationContext());
        ConnectionHelper.sDiscoveryManager = DiscoveryManager.getInstance();
        ConnectionHelper.sDiscoveryManager.start();
    }

    @Override
    protected void onDestroy() {
        ConnectionHelper.sDesaplgListener.setSplashActivity(null);
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
}
