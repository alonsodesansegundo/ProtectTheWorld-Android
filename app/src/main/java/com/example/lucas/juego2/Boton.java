package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Clase que se utilizará para simular el comportamiento de un botón
 */
public class Boton {
    private Paint p, pTextoBtn;
    private Rect rectangulo;
    private boolean bandera;
    private Bitmap img;
    private String texto;

    /**
     *
     * @param left Entero que representa el punto X donde comienza el rectangulo boton (Rect)
     * @param top Entero que representa el punto Y donde comienza el rectangulo boton (Rect)
     * @param right Entero que representa el punto X donde acaba el rectangulo boton (Rect)
     * @param bottom Entero que representa el punto Y donde acaba el rectangulo boton (Rect)
     * @param color Entero que representa el color que tendrá dicho boton (Rect)
     */
    public Boton(int left, int top, int right, int bottom, int color) {
        this.rectangulo = new Rect(left, top, right, bottom);
        bandera = false;
        p = new Paint();
        p.setColor(color);
        pTextoBtn = new Paint();
        pTextoBtn.setTextAlign(Paint.Align.CENTER);
        this.img = null;
    }

    /**
     * Método que se utilizará para introducir texto dentro del boton
     * @param texto Texto que queremos escribir
     * @param tamaño Tamaño que tendrá nuestro texto
     * @param color Entero que represente el color que tendrá nuestro texto
     * @param tf Objeto Typeface con el que cambiaremos la fuente
     */
    public void setTexto(String texto, int tamaño, int color,Typeface tf) {
        this.texto = texto;
        pTextoBtn.setColor(color);
        pTextoBtn.setTypeface(tf);
        pTextoBtn.setTextSize(tamaño);
    }

    /**
     * Método para cambiar el color del boton (Rect)
     * @param col Entero que representa el color que tendrá el boton
     */
    public void setColor(int col) {
        p.setColor(col);
    }

    /**
     * Método que permite introducir una imagen dentro de nuestro boton
     * @param img Bitmap ya escalado que queremos que aparezca en nuestro boton
     */
    public void setImg(Bitmap img) {
        this.img = img;
    }

    /**
     * Método para ver si donde levantamos el dedo coincide con donde hemos pulsado
     * @return True en el caso de que haya pulsado ahí
     */
    public boolean getBandera() {
        return bandera;
    }

    /**
     * Método utilizado para poner la bandera a true en el caso de haber pulsado en donde se encuentra dich boton. Pondremos dicha bandera a false cuando levantamos el dedo de la pantalla
     * @param bandera Valor boolean que tendrá nuestra bandera
     */
    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    /**
     * Método que nos devuelve el Rect del boton, utilizado para ver si hemos pulsado en el
     * @return Objeto Rect del boton
     */
    public Rect getRectangulo() {
        return rectangulo;
    }

    /**
     * Método encargado de dibujar dicho boton. Dibujará: el rect, el bitmap en caso de tenerlo y el texto en caso de tenerlo.
     * @param c Objeto Canvas para dibujar
     */
    public void dibujar(Canvas c) {
        c.drawRect(this.rectangulo, p);
        if (img != null) {
            c.drawBitmap(img, this.rectangulo.left, this.rectangulo.top, null);
        }
        if (texto != null) {
            //dibujo el texto del boton jugar
            c.drawText(texto, this.getRectangulo().centerX(), this.getRectangulo().centerY() - ((pTextoBtn.descent() + pTextoBtn.ascent()) / 2), pTextoBtn);
        }
    }
}
