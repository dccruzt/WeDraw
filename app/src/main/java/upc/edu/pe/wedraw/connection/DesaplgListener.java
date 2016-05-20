package upc.edu.pe.wedraw.connection;

import com.connectsdk.core.JSONDeserializable;
import com.connectsdk.service.sessions.WebAppSession;
import com.connectsdk.service.sessions.WebAppSessionListener;

import org.json.JSONObject;

import upc.edu.pe.wedraw.ConnectActivity;
import upc.edu.pe.wedraw.DrawActivity;
import upc.edu.pe.wedraw.InputNameActivity;
import upc.edu.pe.wedraw.SplashActivity;
import upc.edu.pe.wedraw.StartGameActivity;
import upc.edu.pe.wedraw.helpers.StringsHelper;

/**
 * Created by Andres Revolledo on 4/12/16.
 * Esta clase implementa WebAppSessionListener encargada de procesar los
 * mensajes recividos desde la aplicacion web.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 * @
 */
public class DesaplgListener implements WebAppSessionListener{

    private SplashActivity mSplashActivity;
    private ConnectActivity mConnectActivity;
    private InputNameActivity mInputNameActivity;
    private StartGameActivity mStartGameActivity;
    private DrawActivity mDrawActivity;

    public SplashActivity getSplashActivity() {
        return mSplashActivity;
    }

    public void setSplashActivity(SplashActivity splashActivity) {
        mSplashActivity = splashActivity;
    }

    public ConnectActivity getConnectActivity() {
        return mConnectActivity;
    }

    public void setConnectActivity(ConnectActivity connectActivity) {
        mConnectActivity = connectActivity;
    }

    public InputNameActivity getInputNameActivity() {
        return mInputNameActivity;
    }

    public void setInputNameActivity(InputNameActivity inputNameActivity) {
        mInputNameActivity = inputNameActivity;
    }

    public StartGameActivity getStartGameActivity() {
        return mStartGameActivity;
    }

    public void setStartGameActivity(StartGameActivity startGameActivity) {
        mStartGameActivity = startGameActivity;
    }

    public DrawActivity getDrawActivity() {
        return mDrawActivity;
    }

    public void setDrawActivity(DrawActivity drawActivity) {
        mDrawActivity = drawActivity;
    }

    @Override
    public void onReceiveMessage(WebAppSession webAppSession, Object message) {
        try{
            JSONObject json = new JSONObject(message.toString());
            String accion = json.getString(StringsHelper.ACTION);
            if(accion.equals(StringsHelper.ENABLE_START)){
                habilitarInicio(json.getBoolean("habilitarInicio"));
            }else if(accion.equals(StringsHelper.START_GAME)){
                comenzarJuego();
            }

        }catch (Exception e){

        }
    }

    public void habilitarInicio(boolean habilitado){
        if(getStartGameActivity()!=null){
            getStartGameActivity().activarJugar(habilitado);
        }
    }

    public void comenzarJuego(){
        if(getStartGameActivity()!=null){
            getStartGameActivity().startGame();
        }
    }

    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }


}
