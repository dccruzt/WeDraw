package upc.edu.pe.wedraw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

/**
 * Created by Daniela on 02/02/2016.
 */
public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando);

        ConnectionHelper.sDesaplgListener.setLoadingActivity(this);

        setLoadingAnimation();

        /*String mensaje = getIntent().getExtras().getString("mensaje");
        if (mensaje != null)
            setMensaje(mensaje);*/
    }

    public void setMensaje(String mensaje) {

        TextView lblMensaje = (TextView)findViewById(R.id.lblMensaje);
        lblMensaje.setText(mensaje);
    }

    private void setLoadingAnimation() {
        Animation cargando0 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando0 = (View)findViewById(R.id.vCargando0);
        vCargando0.startAnimation(cargando0);

        Animation cargando1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando1 = (View)findViewById(R.id.vCargando1);
        cargando1.setStartOffset(150);
        vCargando1.startAnimation(cargando1);

        Animation cargando2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando2 = (View)findViewById(R.id.vCargando2);
        cargando2.setStartOffset(300);
        vCargando2.startAnimation(cargando2);

        Animation cargando3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando3 = (View)findViewById(R.id.vCargando3);
        cargando3.setStartOffset(450);
        vCargando3.startAnimation(cargando3);

        Animation cargando4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando4 = (View)findViewById(R.id.vCargando4);
        cargando4.setStartOffset(600);
        vCargando4.startAnimation(cargando4);

        Animation cargando5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando5 = (View)findViewById(R.id.vCargando5);
        cargando5.setStartOffset(750);
        vCargando5.startAnimation(cargando5);

        Animation cargando6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cargando);
        View vCargando6 = (View)findViewById(R.id.vCargando6);
        cargando6.setStartOffset(900);
        cargando6.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setLoadingAnimation();
            }
        });
        vCargando6.startAnimation(cargando6);
    }

    @Override
    protected void onDestroy() {

        ConnectionHelper.sDesaplgListener.setLoadingActivity(null);
        super.onDestroy();
        StatusHelper.unbindDrawables(findViewById(R.id.layoutCargando));
        System.gc();
    }

    @Override
    public void onBackPressed() {}
}