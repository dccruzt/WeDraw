package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.connectsdk.discovery.DiscoveryManager;

import upc.edu.pe.wedraw.connection.DesaplgListener;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;

/*
 * Actividad Splash, se encarga de inicializar el contexto y el listener de la aplicacion
 * ademas de iniciar el DiscoveryManager para iniciar la busqueda de TVs en la red.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 */
public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectionHelper.sContext = getApplicationContext();
        ConnectionHelper.sDesaplgListener = new DesaplgListener();

        startDiscovery();
        ConnectionHelper.sDesaplgListener.setSplashActivity(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent i = new Intent(SplashActivity.this, ConnectActivity.class);
                Intent i = new Intent(SplashActivity.this, DifficultyActivity.class);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
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
