package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Opciones extends Pantalla {
    Bitmap n,n1,n2;
    Rect back;
    public Opciones(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        back=new Rect(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10);

        //imagenes nave
        n=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
        n1=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
        n2=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);

        //escalado de las imagenes
        n = Bitmap.createScaledBitmap(n, anchoPantalla / 8, altoPantalla / 15, true);
        n1 = Bitmap.createScaledBitmap(n1, anchoPantalla / 8, altoPantalla / 15, true);
        n2 = Bitmap.createScaledBitmap(n2, anchoPantalla / 8, altoPantalla / 15, true);
    }

    @Override
    public void dibujar(Canvas c) {
        try{
            c.drawColor(Color.BLACK);
            pTexto.setTextSize(altoPantalla/10);
            c.drawText("OPCIONES",anchoPantalla/2,altoPantalla/5,pTexto);
            c.drawRect(back,pBoton);
            pTexto.setTextSize(altoPantalla/20);
            c.drawText("Seleccionar nave",anchoPantalla/2,altoPantalla/3,pTexto);
            //dibujo imagenes nave
            c.drawBitmap(n,0,altoPantalla/3+altoPantalla/20,null);
            c.drawBitmap(n1,anchoPantalla/2-n1.getWidth()/2,altoPantalla/3+altoPantalla/20,null);
            c.drawBitmap(n2,anchoPantalla-n2.getWidth(),altoPantalla/3+altoPantalla/20,null);


        }catch (Exception e){

        }
    }

    @Override
    public void actualizarFisica() {
        super.actualizarFisica();
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
                    //vuelvo al menu
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
