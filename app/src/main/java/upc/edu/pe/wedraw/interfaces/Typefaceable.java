package upc.edu.pe.wedraw.interfaces;

import android.graphics.Typeface;

/**
 * Definición de una interfaz que será invocada cuando se desee setear
 * una fuente personalizada. Por lo general, será aplicada a un View o alguna extensión
 */
public interface Typefaceable {

    Typeface getTypeface();

    void setTypeface(Typeface typeface);

    void setTypeface(Typeface typeface, int i);
}
