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


    //<editor-fold desc="Se guardan las activites para luego poder invocar sus métodos">

    private SplashActivity mSplashActivity;
    private ConnectActivity mConnectActivity;
    private InputNameActivity mInputNameActivity;
    private StartGameActivity mStartGameActivity;
    private GuessActivity mGuessActivity;

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

    public GuessActivity getGuessActivity() {
        return mGuessActivity;
    }

    public void setGuessActivity(GuessActivity guessActivity) {
        mGuessActivity = guessActivity;
    }

    //</editor-fold>

    @Override
    public void onReceiveMessage(WebAppSession webAppSession, Object message) {
        try{
            JSONObject json = new JSONObject(message.toString());
            String accion = json.getString(StringsHelper.ACTION);
            if(accion.equals(StringsHelper.ENABLE_START)){
                habilitarInicio(json.getBoolean("habilitarInicio"));
            }else if(accion.equals(StringsHelper.START_GAME)){
                comenzarJuego();
            }else if(accion.equals(StringsHelper.GET_HINT)){
                parsearPalabra(json);
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

    public void parsearPalabra(JSONObject response) throws JSONException{
        if(getGuessActivity()==null)
            return;
        if(getStartGameActivity()!=null){
            Intent i = new Intent(getStartGameActivity(),GuessActivity.class);
            i.putExtra(GuessActivity.PARAM_HINT,response.getString("pista"));
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
