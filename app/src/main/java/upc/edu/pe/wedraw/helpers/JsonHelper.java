package upc.edu.pe.wedraw.helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andres Revolledo on 13/04/2016.
 * Esta clase se encarga de simplificar la creacion de mensajes JSON
 * para ser enviados a la aplicacion web.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 */
public class JsonHelper {

    public static JSONObject ConnectTv(){
        return getDefaultAction(StringsHelper.CONNECT_TV);
    }

    public static JSONObject ConnectPlayer(String player){
        try {
            JSONObject jsonObject = getDefaultAction(StringsHelper.CONNECT_PLAYER);
            jsonObject.put(StringsHelper.PLAYER, player);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }

    public static JSONObject requestGameStart(){
        return getDefaultAction(StringsHelper.REQUEST_START);
    }

    private static JSONObject getDefaultAction(String action){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(StringsHelper.ACTION, action);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }
}