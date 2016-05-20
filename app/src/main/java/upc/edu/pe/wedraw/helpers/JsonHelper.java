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
        return getDefaultAction(StringsHelper.CONNECT_TV, null);
    }

    public static JSONObject ConnectPlayer(String player){
        try {
            JSONObject jsonObject = getDefaultAction(StringsHelper.CONNECT_PLAYER, null);
            jsonObject.put(StringsHelper.PLAYER, player);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }

    public static JSONObject requestGameStart(){
        return getDefaultAction(StringsHelper.REQUEST_START, null);
    }

    public static JSONObject test(){
        return getDefaultAction("test", null);
    }

    public static JSONObject makeDraw(int x, int y){
        try {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("x", x);
            jsonResult.put("y", y);

            JSONObject jsonObject = getDefaultAction(StringsHelper.MAKE_DRAW, jsonResult);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }

    //<editor-fold desc="To send difficulty selected by user">

    /**
     * Enum que contiene las dificultades actualmente soportadas, y su
     * respectivo id que será pasado al servicio
     */
    public enum Difficulties{
        EASY("facil"),MEDIUM("intermedio"),HARD("avanzado");
        private String mId;
        Difficulties(String id){
            id = mId;
        }
        public String getId(){
            return mId;
        }
    }

    /**
     * Crea el mensaje JSON a ser invocado al ejecutar la acción de selección de dificultad
     * @param difficulty enum con la dificultad seleccionada
     * @return JSONObject a ser mandado para ejecutar la acción de selección de dificultad
     */
    public static JSONObject selectGameDifficulty(Difficulties difficulty){
        JSONObject action= getDefaultAction(StringsHelper.SEND_DIFFICULTY, null);
        try {
            action.put(StringsHelper.DIFFICULTY,difficulty.getId());
        } catch (JSONException e) {}
        return action;
    }

    //</editor-fold>

    /**
     * Para obtener un object básico conteniendo la acción especificada.
     * Luego podría añadirsele más parámetros al JSONObject si se desea.
     * @param action acción que se le mandará al servidor
     * @return JSONObject cuyo único parámetro es la acción que se pasó en el parámetro
     */
    private static JSONObject getDefaultAction(String action, JSONObject result){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(StringsHelper.ACTION, action);
            jsonObject.put(StringsHelper.RESULT, result);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }
}