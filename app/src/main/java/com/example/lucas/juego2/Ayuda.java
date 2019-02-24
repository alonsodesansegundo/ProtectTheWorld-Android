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

public class Ayuda extends Pantalla {
    Bitmap imgVolver;
    Boton back;
    private String txtAyuda;
    public Ayuda(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        //--------------STRINGS--------------
        txtAyuda=contexto.getString(R.string.ayuda);
        //--------------MUSICA--------------
        musica=preferencias.getBoolean("musica",true);
        configuraMusica(R.raw.submenus);
        if(musica){
            suenaMusica();
        }
        back=new Boton(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10, Color.TRANSPARENT);
        imgVolver= BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla/10, anchoPantalla/10, true);
        back.setImg(imgVolver);
    }

    @Override
    public void dibujar(Canvas c) {
        try{
            //fondo
            c.drawColor(Color.BLACK);
            //titulo
            c.drawText(txtAyuda,anchoPantalla/2,altoPantalla/8,pTitulo);
            //boton volver
            back.dibujar(c);
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
                //si pulso la opcion jugar
                if (pulsa(back.getRectangulo(), event)) {
                    acabaMusica();
                    return 0;
                }
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último


                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
}

