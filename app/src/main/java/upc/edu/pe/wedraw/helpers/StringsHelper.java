package upc.edu.pe.wedraw.helpers;

/**
 * Created by vicva on 4/12/2016.
 * Esta clase se encarga de definir todas las acciones y resultados posibles
 * que la aplicacion web entiende.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 */
public class StringsHelper {
    public static String ACTION = "accion";
    public static String RESULT = "resultado";
    public static String PLAYER = "jugador";


    //Acciones enviadas
    public static final String CONNECT_TV = "conectarTV";
    public static final String CONNECT_PLAYER = "conectarJugador";
    public static final String REQUEST_START = "solicitarInicio";
    public static final String DISCONNECT_PLAYER = "desconectarJugador";

    //Acciones recibidas
    public static final String ENABLE_START = "habilitarInicio";
    public static final String START_GAME = "empezarJuego";

}
