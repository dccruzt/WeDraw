package upc.edu.pe.wedraw.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import upc.edu.pe.wedraw.helpers.FontHelper;
import upc.edu.pe.wedraw.interfaces.Typefaceable;

/**
 * Clase base a ser utilizada por los TextViews para asignarles
 *  un comportamiento por defecto de forma sencilla
 */
public class CustomTextView extends TextView implements Typefaceable {


    public CustomTextView(Context context) {
        super(context);
        init(context,null);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Metodo que realizará la inicializaciones necesarias en base a los atributos seteados.
     * Si no recibe atributos, se le asigna el comportamiento por defecto.
     */
    private void init(Context context, AttributeSet attrs){
        if(!isInEditMode()){
            FontHelper.apply(context,attrs,this);
        }
    }

}
