package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Menu extends Pantalla {
    Boton jugar, ayuda, opciones, records;
    int alto, ancho, espacio;

    public Menu(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo1);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        alto = altoPantalla / 10;
        ancho = anchoPantalla / 10;
        espacio = altoPantalla / 50;

        jugar = new Boton(ancho, alto, ancho * 5, alto * 2, Color.BLACK);
        opciones = new Boton(ancho, alto * 2 + espacio, ancho * 5, alto * 2 + espacio + alto, Color.BLACK);
        records = new Boton(ancho, alto * 2 + 2 * espacio + alto, ancho * 5, alto * 2 + 2 * espacio + 2 * alto, Color.BLACK);
        ayuda = new Boton(ancho, alto * 2 + 3 * espacio + 2 * alto, ancho * 5, alto * 2 + 3 * espacio + 3 * alto, Color.BLACK);
    }

    // Actualizamos la física de los elementos en pantalla
    public void actualizarFisica() {
    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {
            //dibujo la imagen de fondo
            c.drawBitmap(fondo, 0, 0, null);
            //BOTON JUGAR
            //dibujo el boton jugar
            pBoton.setColor(jugar.getColor());
            c.drawRect(jugar.getRectangulo(), pBoton);
            //dibujo el texto del boton jugar
            c.drawText("Jugar", jugar.getRectangulo().centerX(), jugar.getRectangulo().centerY() - ((pTexto.descent() + pTexto.ascent()) / 2), pTexto);

            //BOTON OPCIONES
            //dibujo el boton opciones
            pBoton.setColor(opciones.getColor());
            c.drawRect(opciones.getRectangulo(), pBoton);
            //dibujo el texto del boton opciones
            c.drawText("Opciones", opciones.getRectangulo().centerX(), opciones.getRectangulo().centerY() - ((pTexto.descent() + pTexto.ascent()) / 2), pTexto);

            //BOTON RECORDS
            pBoton.setColor(records.getColor());
            c.drawRect(records.getRectangulo(), pBoton);
            c.drawText("Records", records.getRectangulo().centerX(), records.getRectangulo().centerY() - ((pTexto.descent() + pTexto.ascent()) / 2), pTexto);

            //BOTON AYUDA
            pBoton.setColor(ayuda.getColor());
            c.drawRect(ayuda.getRectangulo(), pBoton);
            c.drawText("Ayuda", ayuda.getRectangulo().centerX(), ayuda.getRectangulo().centerY() - ((pTexto.descent() + pTexto.ascent()) / 2), pTexto);

        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }

    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                if (pulsa(jugar.getRectangulo(), event)) {
                   jugar.setBandera(true);
                }
                if (pulsa(opciones.getRectangulo(), event)) {
                   opciones.setBandera(true);
                }
                if (pulsa(records.getRectangulo(), event)) {
                    records.setBandera(true);
                }
                if (pulsa(ayuda.getRectangulo(), event)) {
                    ayuda.setBandera(true);
                }
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                //si pulso la opcion jugar
                if (pulsa(jugar.getRectangulo(), event) && jugar.getBandera()) {
                    return 1;
                }
                //si pulso la opcion opciones
                if (pulsa(opciones.getRectangulo(), event)&&opciones.getBandera()) {
                    return 2;
                }
                if (pulsa(records.getRectangulo(), event)&&records.getBandera()) {
                    return 3;
                }
                if (pulsa(ayuda.getRectangulo(), event)&&ayuda.getBandera()) {
                    return 4;
                }
                //pongo todas las banderas de los botones a false
                jugar.setBandera(false);
                opciones.setBandera(false);
                ayuda.setBandera(false);
                records.setBandera(false);
                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos
                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
}
