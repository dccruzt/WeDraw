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

    //Parameters
    public static String ACTION = "accion";
    public static String RESULT = "resultado";
    public static String PLAYER = "jugador";
    public static String DIFFICULTY = "dificultad";
    public static String WORD = "palabra";

    //Acciones enviadas
    public static final String CONNECT_TV = "conectarTV";
    public static final String CONNECT_PLAYER = "conectarJugador";
    public static final String REQUEST_START = "empezarJuego";
    public static final String SEND_DIFFICULTY = "enviarDificultad";
    public static final String SET_POSITION = "setearPosicion";
    public static final String MAKE_DRAW = "dibujar";
    public static final String ERASE_DRAW = "borrarDibujo";
    public static final String GUESS_WORD = "enviarIntento";
    public static final String CHANGE_COLOR = "cambiarColor";
    public static final String FINISH_GAME = "salir";
    public static final String PLAY_AGAIN = "volverAjugar";


    public static final String DISCONNECT_PLAYER = "desconectarJugador";

    //Acciones recibidas
    public static final String LOAD_INPUT = "cargarInicio";
    public static final String ENABLE_START = "puedeIniciar";
    public static final String IS_DRAWER = "esDibujante";
    public static final String START_TURN = "empezarTurno";
    public static final String GET_HINT = "enviarPista";
    public static final String END_TURN = "terminarTurno";
    public static final String GAME_WINNER = "ganadorJuego";
}