package upc.edu.pe.wedraw.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import upc.edu.pe.wedraw.R;
import upc.edu.pe.wedraw.helpers.FontHelper;
import upc.edu.pe.wedraw.interfaces.Typefaceable;

/**
 * Created by brunoaybar on 6/21/16.
 */
public class CustomButton extends Button implements Typefaceable {

    public CustomButton(Context context) {
        super(context);
        init(context,null);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);


        if(attrs!=null){
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontAttributes);
            if (array != null)
            {
                final String typefaceAssetPath = array.getString(
                        R.styleable.FontAttributes_customTypeface);
                if (typefaceAssetPath != null)
                {
                    this.setTypeface(Typeface.createFromAsset(context.getAssets(), typefaceAssetPath));
                    array.recycle();
                    return;
                }
            }
        }

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/international_playboy.ttf"));
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
