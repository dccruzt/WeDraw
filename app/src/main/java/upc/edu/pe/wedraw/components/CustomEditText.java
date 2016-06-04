package upc.edu.pe.wedraw.components;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import upc.edu.pe.wedraw.helpers.FontHelper;
import upc.edu.pe.wedraw.interfaces.Typefaceable;

/**
 * Clase base a ser utilizada por los TextViews para asignarles
 *  un comportamiento por defecto de forma sencilla
 */
public class CustomEditText extends EditText implements Typefaceable {


    public CustomEditText(Context context) {
        super(context);
        init(context,null);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/international_playboy.ttf"));
        this.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //init(context,attrs);
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