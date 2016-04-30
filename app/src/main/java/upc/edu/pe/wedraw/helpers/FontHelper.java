package upc.edu.pe.wedraw.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

import upc.edu.pe.wedraw.R;
import upc.edu.pe.wedraw.interfaces.Typefaceable;

/**
 * Clase auxiliar que nos permitira, en base a los attributos recibidos, setear
 * una fuente a una vista
 */
public class FontHelper {


    /**
     * Setea un tipo de fuente a una vista que implemente la interfaz Typefaceable, en base a los atributos que se pasen
     * @param context contexto de la aplicación
     * @param attrs atributos de la vista
     * @param view vista a la cual se le aplicará la fuente
     */
    public static void apply(Context context, AttributeSet attrs, Typefaceable view) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontAttributes);
        Typeface typeface = null;
        if (array != null)
        {
            final int font = array.getInt(R.styleable.FontAttributes_customFont,0);
           typeface = Fonts.get(context,font);
            array.recycle();
        }
        if(typeface==null)
            typeface = Fonts.get(context,0);
        if(view!=null)
            view.setTypeface(typeface);
    }

    /**
     * Fuentes que soportará nuestra aplicación
     */
    private enum Fonts{
        BIKO("Biko_Regular.otf"),
        INTERNATIONAL("international_playboy.ttf");
        private String mFont;
        Fonts(String font){
            mFont = font;
        }
        public static Typeface get(Context context, int font){
            return getTypeface(context,Fonts.values()[font].mFont);
        }


        private static Map<String, Typeface> mTypefaces;
        /**
         * Tratará de obtener una fuente de los assets. Los almacenerá en
         * un mapa para optimizar la reutilización de las fuentes.
         * @param context
         * @param typefaceName
         * @return
         */
        public static Typeface getTypeface(Context context, String typefaceName){
            Typeface typeface = null;
            //Inicializar mapa
            if(mTypefaces==null)
                mTypefaces = new HashMap<>();
            //Buscar el si la fuente ya había sido preguntar
            if (mTypefaces.containsKey(typefaceName)) {
                typeface = mTypefaces.get(typefaceName);
            } else
            {
                AssetManager assets = context.getAssets();
                typeface = Typeface.createFromAsset(assets, typefaceName);
                mTypefaces.put(typefaceName, typeface);
            }
            return typeface;
        }

    }
}
