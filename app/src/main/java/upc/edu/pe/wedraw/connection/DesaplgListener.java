package upc.edu.pe.wedraw.connection;

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
import upc.edu.pe.wedraw.GuessActivity;
import upc.edu.pe.wedraw.DrawActivity;
import upc.edu.pe.wedraw.InputNameActivity;
import upc.edu.pe.wedraw.LoadingActivity;
import upc.edu.pe.wedraw.SplashActivity;
import upc.edu.pe.wedraw.StartGameActivity;
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


            if(accion.equals(StringsHelper.LOAD_INPUT)){
                cargarInputNameActivity();
            }else if(accion.equals(StringsHelper.ENABLE_START)){
                habilitarInicio(true);
            }else if(accion.equals(StringsHelper.START_GAME)){
                comenzarJuego(json.getBoolean(StringsHelper.RESULT));
            }else if(accion.equals(StringsHelper.GET_HINT)){
                parsearPalabra(json);
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
    }

    //Función para activar la imagen "tuadivinas" o "tudibujas"
    public void comenzarJuego(boolean dibujante){
        if(getStartGameActivity()!=null){
            getStartGameActivity().startGame(dibujante);
        }
    }

    public void parsearPalabra(JSONObject response) throws JSONException{
        if(getGuessActivity()==null)
            return;
        if(getStartGameActivity()!=null){
            Intent i = new Intent(getStartGameActivity(),GuessActivity.class);
            i.putExtra(GuessActivity.PARAM_HINT, response.getString("pista"));
            getStartGameActivity().startActivity(i);
        }
        /*JSONArray hint = response.getJSONArray("pista");
        List<String> listOfCharacters = new ArrayList<>();
        for(int i=0;i<hint.length();i++){
            String value = (String) hint.get(0);
            listOfCharacters.add(value);
        }
        String[] arr = listOfCharacters.toArray(new String[listOfCharacters.size()]);
        if(getStartGameActivity()!=null){
            Intent i = new Intent(getStartGameActivity(),GuessActivity.class);
            i.putExtra(GuessActivity.PARAM_HINT,arr);
            getStartGameActivity().startActivity(i);
        }*/

    }



    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }


}