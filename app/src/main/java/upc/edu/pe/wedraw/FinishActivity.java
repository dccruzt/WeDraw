package upc.edu.pe.wedraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import upc.edu.pe.wedraw.components.CustomTextView;
import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;
import upc.edu.pe.wedraw.helpers.StatusHelper;

public class FinishActivity extends Activity {

    CustomTextView lblFinalJuego;
    Button btnSalir;
    Button btnVolver;

    public static final String PARAM_MESSAGE = "mensaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        lblFinalJuego = (CustomTextView) findViewById(R.id.lblFinalJuego);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnVolver = (Button) findViewById(R.id.btnVolver);

        if (getIntent().getExtras()!=null && getIntent().getExtras().getString(PARAM_MESSAGE)!= null) {
            String mensaje = getIntent().getExtras().getString(PARAM_MESSAGE);
            lblFinalJuego.setText(mensaje);
        }
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAJugar();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });

    }

    private void volverAJugar(){
        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.playAgain(), new ResponseListener<Object>() {
            @Override
            public void onError(ServiceCommandError error) {
            }

            @Override
            public void onSuccess(Object object) {
            }
        });

    }

    private void salir(){

        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.close(), new ResponseListener<Object>() {
            @Override
            public void onError(ServiceCommandError error) {
            }

            @Override
            public void onSuccess(Object object) {
            }
        });
    }



    @Override
    protected void onDestroy() {

        ConnectionHelper.sDesaplgListener.setFinishActivity(null);
        super.onDestroy();
        StatusHelper.unbindDrawables(findViewById(R.id.layoutFinalJuego));
        System.gc();
    }

    @Override
    public void onBackPressed() {}


}
