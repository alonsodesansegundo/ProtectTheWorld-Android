package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Esta clase será utilizada para generar las balas de los marcianos
 */
public class BalaMarciano {
    private Bitmap img;
    private RectF contenedor;
    private double vBalaMarciano;

    /**
     *
     * @param x Entero que representa el punto X donde comienza la bala del marciano (contenedor)
     * @param y Entero que representa el punto Y donde comienza la bala del marciano (contenedor)
     * @param ancho Entero que representa el ancho que tendrá la bala del marciano (contenedor)
     * @param alto Entero que representa el alto que tendrá la bala del marciano (contenedor)
     * @param img Bitmap que será la imagen de nuestra bala
     * @param vBalaMarciano Double que será la velocidad con la que se moverá la bala
     */
    public BalaMarciano(int x,int y,int ancho,int alto,Bitmap img,double vBalaMarciano) {
        this.contenedor = new RectF(x,y,x+ancho,y+alto);
        this.vBalaMarciano=vBalaMarciano;
        this.img= img;
    }

    /**
     *
     * @return Nos devuelve el RectF contenedor del marciano
     */
    public RectF getContenedor() {
        return contenedor;
    }

    /**
     * Este método será llamado en el caso que los marcianos tengan que descender, y descenderán el alto que ocupen
     */
    public void bajar(){
        this.contenedor.top+=vBalaMarciano;
        this.contenedor.bottom+=vBalaMarciano;
    }

    /**
     * Método encargado de dibujar la imagen del marciano
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujar(Canvas c){
        c.drawBitmap(img,contenedor.left,contenedor.top,null);
    }
}