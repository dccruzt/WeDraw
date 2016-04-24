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

    private enum Fonts{
        BIKO("circular_bold.otf");
        private String mFont;
        Fonts(String font){
            mFont = font;
        }
        public static Typeface get(Context context, int font){
            return FontHelper.getTypeface(context,Fonts.values()[font].mFont);
        }
    }


    public static void apply(Context context, AttributeSet attrs, Typefaceable view) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontAttributes);
        Typeface typeface = null;
        if (array != null)
        {
            final int font = array.getInt(
                    R.styleable.FontAttributes_customFont,0);
           typeface = Fonts.get(context,font);
            array.recycle();
        }
        if(typeface==null)
            typeface = Fonts.get(context,0);
        if(view!=null)
            view.setTypeface(typeface);
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
