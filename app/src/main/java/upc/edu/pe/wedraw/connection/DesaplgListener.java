package upc.edu.pe.wedraw.connection;

import android.app.Activity;
import android.content.Intent;

import com.connectsdk.core.JSONDeserializable;
import com.connectsdk.service.sessions.WebAppSession;
import com.connectsdk.service.sessions.WebAppSessionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import upc.edu.pe.wedraw.ConnectActivity;
import upc.edu.pe.wedraw.CountdownActivity;
import upc.edu.pe.wedraw.DifficultyActivity;
import upc.edu.pe.wedraw.GuessActivity;
import upc.edu.pe.wedraw.DrawActivity;
import upc.edu.pe.wedraw.InputNameActivity;
import upc.edu.pe.wedraw.LoadingActivity;
import upc.edu.pe.wedraw.SplashActivity;
import upc.edu.pe.wedraw.StartGameActivity;
import upc.edu.pe.wedraw.TurnHintActivity;
import upc.edu.pe.wedraw.helpers.StatusHelper;
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


    //<editor-fold desc="Se guardan las activites para luego poder invocar sus métodos">

    private SplashActivity mSplashActivity;
    private ConnectActivity mConnectActivity;
    private LoadingActivity mLoadingActivity;
    private InputNameActivity mInputNameActivity;
    private StartGameActivity mStartGameActivity;
    private DifficultyActivity mDifficultyActivity;
    private GuessActivity mGuessActivity;
    private DrawActivity mDrawActivity;
    private TurnHintActivity mTurnHintActivity;

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

    public LoadingActivity getLoadingActivity() {
        return mLoadingActivity;
    }

    public void setLoadingActivity(LoadingActivity loadingActivity) {
        mLoadingActivity = loadingActivity;
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


    public DifficultyActivity getDifficultyActivity() {
        return mDifficultyActivity;
    }

    public void setDifficultyActivity(DifficultyActivity difficultyActivity) {
        mDifficultyActivity = difficultyActivity;
    }

    public GuessActivity getGuessActivity() {
        return mGuessActivity;
    }

    public void setGuessActivity(GuessActivity guessActivity) {
        mGuessActivity = guessActivity;
    }

    public TurnHintActivity getTurnHintActivity() {
        return mTurnHintActivity;
    }

    public void setTurnHintActivity(TurnHintActivity turnHintActivity) {
        mTurnHintActivity = turnHintActivity;
    }
    //</editor-fold>


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
            }else if(accion.equals(StringsHelper.TU_DIBUJAS)){
                TurnHintActivity.startActivity(getStartGameActivity(),true);
            }else if(accion.equals(StringsHelper.TU_ADIVINAS)){
                TurnHintActivity.startActivity(getStartGameActivity(),false);
            }else if(accion.equals(StringsHelper.START_GAME)){
                comenzarJuego();
            }else if(accion.equals(StringsHelper.UPDATE_HINT)){
                actualizarPista(json);
            }else if(accion.equals(StringsHelper.LOAD_INPUT)){
                cargarInputNameActivity();
            }

        }catch (Exception e){

        }
    }

    public void habilitarInicio(boolean habilitado){
        if(getStartGameActivity()!=null){
            getStartGameActivity().activarJugar(habilitado);
        }
    }

    /**
     * Método ue redirigirá a la vista de conteo regresivo para el comienzo del juego
     */
    public void comenzarJuego(){
        Activity activity = StatusHelper.isMyTurnToDraw ? getDifficultyActivity() : getTurnHintActivity();
        if(activity==null) return;
        Intent i = new Intent(activity, CountdownActivity.class);
        activity.startActivity(i);
    }

    public void actualizarPista(JSONObject response) throws JSONException{
        String pista = response.getString("pista");
        StatusHelper.currentHint = pista;
        if(getGuessActivity()==null)
            return;
        getGuessActivity().actualizarPista();
    }

    public void cargarInputNameActivity(){

        if(mLoadingActivity != null) {
            Intent i = new Intent(mLoadingActivity, InputNameActivity.class);
            mLoadingActivity.startActivity(i);
            mLoadingActivity.finish();
        }
    }

    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }


}
