package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Ayuda extends Pantalla {
    Rect back;
    public Ayuda(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        back=new Rect(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10);
    }

    @Override
    public void dibujar(Canvas c) {
        try{
            c.drawColor(Color.BLACK);
            pTexto.setTextSize(altoPantalla/10);
            c.drawText("Ayuda",anchoPantalla/2,altoPantalla/5,pTexto);
            c.drawRect(back,pBoton);
        }catch (Exception e){

        }
    }

    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                //si pulso la opcion jugar
                if (pulsa(back, event)) {
                    return 0;
                }

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
}

