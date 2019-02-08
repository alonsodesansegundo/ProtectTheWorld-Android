package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Opciones extends Pantalla {
    String selNave;
    Bitmap n,n1,n2;
    int anchoSelectNave;
    Rect back,selectNave;
    Boton nave,nave1,nave2;
    public Opciones(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        selectNave=new Rect();
        back=new Rect(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10);

        //imagenes nave
        n=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
        n1=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
        n2=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);

        //escalado de las imagenes de las naves
        n = Bitmap.createScaledBitmap(n, anchoPantalla / 8, altoPantalla / 15, true);
        n1 = Bitmap.createScaledBitmap(n1, anchoPantalla / 8, altoPantalla / 15, true);
        n2 = Bitmap.createScaledBitmap(n2, anchoPantalla / 8, altoPantalla / 15, true);

        //----------------STRINGS---------------
        selNave="Seleccionar nave";

        //para obtener el ancho de la cadena seleccionar nave
        pTexto.getTextBounds(selNave, 0, selNave.length(), selectNave);
        //ancho seleccionar nave
        anchoSelectNave=selectNave.width();

        //----------------BOTONES SELECCIONAR NAVE---------------
        nave=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/3+altoPantalla/20,(anchoPantalla-anchoSelectNave)/2+n.getWidth(),
                altoPantalla/3+altoPantalla/20+n.getHeight(), Color.BLUE);
        nave.setImg(n);

        nave1=new Boton(anchoPantalla/2-n1.getWidth()/2,altoPantalla/3+altoPantalla/20,anchoPantalla/2+n1.getWidth()/2,
                altoPantalla/3+altoPantalla/20+n1.getHeight(), Color.BLUE);
        nave1.setImg(n1);

        nave2=new Boton(anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2,altoPantalla/3+altoPantalla/20,
                anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2+n2.getWidth(),altoPantalla/3+altoPantalla/20+n2.getHeight(), Color.BLUE);
        nave2.setImg(n2);


    }

    @Override
    public void dibujar(Canvas c) {
        try{
            //dibujo el fondo
            c.drawColor(Color.BLACK);
            //tamaño texto opciones
            pTexto.setTextSize(altoPantalla/10);
            //dibujo el texto opciones
            c.drawText("OPCIONES",anchoPantalla/2,altoPantalla/5,pTexto);
            //dibujo el boton para volver hacia atras
            c.drawRect(back,pBoton);
            //tamaño texto seleccionar nave
            pTexto.setTextSize(altoPantalla/20);
            //dibujo el texto seleccionar nave
            c.drawText(selNave,anchoPantalla/2,altoPantalla/3,pTexto);
            //dibujo los botones de las diferentes imagenes
            nave.dibujar(c);
            nave1.dibujar(c);
            nave2.dibujar(c);

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
