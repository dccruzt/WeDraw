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
        try {
            JSONObject jsonObject = new JSONObject() {
                {
                    put(StringsHelper.ACTION, StringsHelper.CONNECT_TV);
                }
            };
            return jsonObject;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static JSONObject ConnectPlayer(String player){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(StringsHelper.ACTION, StringsHelper.CONNECT_PLAYER);
            jsonObject.put(StringsHelper.PLAYER, player);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }

}