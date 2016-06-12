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

    public static JSONObject test(){
        return getDefaultAction("test");
    }

    public static JSONObject makeDraw(int x, int y){
        try {
            JSONObject jsonObject = getDefaultAction(StringsHelper.MAKE_DRAW);
            jsonObject.put("x", x);
            jsonObject.put("y", y);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }


    //<editor-fold desc="Para enviar la dificultad escogida por el usuario">

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
        JSONObject jsonObject= getDefaultAction(StringsHelper.SEND_DIFFICULTY);
        try {
            jsonObject.put(StringsHelper.RESULT,difficulty.getId());
            return jsonObject;
        } catch (JSONException e) {
            return null;
        }
    }

    //</editor-fold>

    public static JSONObject guessWord(String word){
        JSONObject action= getDefaultAction(StringsHelper.GUESS_WORD);
        try {
            action.put(StringsHelper.WORD,word);
            return action;
        } catch (JSONException e) {}
        return action;
    }



    /**
     * Para obtener un object básico conteniendo la acción especificada.
     * Luego podría añadirsele más parámetros al JSONObject si se desea.
     * @param action acción que se le mandará al servidor
     * @return JSONObject cuyo único parámetro es la acción que se pasó en el parámetro
     */
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