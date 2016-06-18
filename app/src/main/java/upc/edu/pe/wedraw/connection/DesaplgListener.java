package upc.edu.pe.wedraw.connection;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;

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
import upc.edu.pe.wedraw.FinishActivity;
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
    private GuessActivity mGuessActivity;
    private DrawActivity mDrawActivity;
    private DifficultyActivity mDifficultyActivity;
    private TurnHintActivity mTurnActivity;
    private CountdownActivity mCountdownActivity;
    private FinishActivity mFinishActivity;

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

    public GuessActivity getGuessActivity() {
        return mGuessActivity;
    }

    public void setGuessActivity(GuessActivity guessActivity) {
        mGuessActivity = guessActivity;
    }

    public DifficultyActivity getDifficultyActivity() {
        return mDifficultyActivity;
    }

    public void setDifficultyActivity(DifficultyActivity difficultyActivity) {
        mDifficultyActivity = difficultyActivity;
    }

    public CountdownActivity getCountdownActivity() {
        return mCountdownActivity;
    }

    public void setCountdownActivity(CountdownActivity countdownActivity) {
        mCountdownActivity = countdownActivity;
    }

    public TurnHintActivity getTurnHintActivity() {
        return mTurnActivity;
    }

    public void setTurnHintActivity(TurnHintActivity turnActivity) {

        mTurnActivity = turnActivity;
    }

    public DrawActivity getDrawActivity() {
        return mDrawActivity;
    }

    public void setDrawActivity(DrawActivity drawActivity) {
        mDrawActivity = drawActivity;
    }

    public FinishActivity getFinishActivity() {
        return mFinishActivity;
    }

    public void setFinishActivity(FinishActivity finishActivity) {
        mFinishActivity = finishActivity;
    }


    @Override
    public void onReceiveMessage(WebAppSession webAppSession, Object message) {
        try{
            JSONObject json = new JSONObject(message.toString());
            String accion = json.getString(StringsHelper.ACTION);


            if(accion.equals(StringsHelper.LOAD_INPUT)){
                cargarInputNameActivity();
            }else if(accion.equals(StringsHelper.ENABLE_START)){
                habilitarInicio(true);
            }else if(accion.equals(StringsHelper.IS_DRAWER)){
                validarRol(json.getBoolean(StringsHelper.RESULT));
            }else if(accion.equals(StringsHelper.START_TURN)){
                empezarTurno(json.getString(StringsHelper.RESULT));
            } else if(accion.equals(StringsHelper.GET_HINT)){
                parsearPalabra(json);
            }else if(accion.equals(StringsHelper.END_TURN)){
                terminarTurno();
            }else if (accion.equals(StringsHelper.GAME_WINNER)) {
                ganadorJuego(json.getJSONArray("resultado"));
            }


        }catch (Exception e){

        }
    }

    //Función para que se cargue el InputNameActivity luego del LoadingActivity
    public void cargarInputNameActivity(){

        if(mLoadingActivity != null) {
            Intent i = new Intent(mLoadingActivity, InputNameActivity.class);
            mLoadingActivity.startActivity(i);
            mLoadingActivity.finish();
        }
    }

    //Función para activar el botón jugar del StartGameActivity
    public void habilitarInicio(boolean habilitado){

        if(getStartGameActivity()!=null){

            getStartGameActivity().activarJugar(habilitado);
        }
        StatusHelper.btnJugar_activo = true;
    }

    //Función para activar la imagen "tuadivinas" o "tudibujas"
    public void validarRol(boolean dibujante){

        if (getStartGameActivity() != null){
            StatusHelper.isMyTurnToDraw = dibujante;

            Intent i = new Intent(getStartGameActivity(), TurnHintActivity.class);
            getStartGameActivity().startActivity(i);
            getStartGameActivity().finish();
        }
    }

    public void empezarTurno(String palabra){

        StatusHelper.word = palabra;
        String hint = "";
        for (int i=0;i<palabra.length();i++)
            hint = hint.concat("_");
        StatusHelper.currentHint = hint;

        if(StatusHelper.isMyTurnToDraw){

            if (getDifficultyActivity() != null){
                Intent i = new Intent(getDifficultyActivity(), CountdownActivity.class);
                getDifficultyActivity().startActivity(i);
                getDifficultyActivity().finish();
            }
        }else {

            if (getTurnHintActivity() != null){

                Intent i = new Intent(getTurnHintActivity(), CountdownActivity.class);
                getTurnHintActivity().startActivity(i);
                getTurnHintActivity().finish();
            }
        }
    }

    public void terminarTurno(){

        if(StatusHelper.isMyTurnToDraw){

            if (getDrawActivity() != null){

                getDrawActivity().habilitarElementos(false);
            }
        }else {

            if (getGuessActivity() != null){

                getGuessActivity().habilitarElementos(false);
            }
        }
    }


    public void parsearPalabra(JSONObject response) throws JSONException{
        if(getGuessActivity()!=null) {
            StatusHelper.currentHint = response.getString("pista");
            getGuessActivity().actualizarPista();
        }
    }

    public void ganadorJuego(JSONArray ganadores) throws JSONException{

        String lblFinal = "";
        String nombres = "";

        for (int i = 0; i < ganadores.length(); i++)
            if(i > 0)
                nombres += " Y " + ganadores.getString(i);
            else
                nombres = ganadores.getString(i);


        if(ganadores.length() > 1)
            lblFinal = "¡FELICITACIONES A " + nombres + "!\n¡SON LOS GANADORES DE TIE-A-WORD!";
        else
            lblFinal = "¡FELICITACIONES A " + nombres + "!\n¡ES EL GANADOR DE TIE-A-WORD!";


        Activity activity = StatusHelper.isMyTurnToDraw ? getDrawActivity() : getGuessActivity();
        Intent i = new Intent(activity, FinishActivity.class);
        i.putExtra(FinishActivity.PARAM_MESSAGE, lblFinal);
        activity.startActivity(i);
        activity.finish();

    }


    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }
}