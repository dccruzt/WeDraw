package upc.edu.pe.wedraw.helpers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by vicva on 4/12/2016.
 */
public class StatusHelper {

    public static String playerName;
    public static boolean btnJugar_activo = false;
    public static String currentHint;
    public static boolean isMyTurnToDraw = false;
    public static String word = "";
    public static boolean conexionExitosa = false;

    public static void unbindDrawables(View view) 
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}