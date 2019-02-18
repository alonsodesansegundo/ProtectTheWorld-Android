package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BalaMarciano {
    private Bitmap img;
    private RectF contenedor;
    private float vBalaMarciano;
    private Paint p;
    public BalaMarciano(int x,int y,int ancho,int alto,Bitmap img) {
        this.contenedor = new RectF(x,y,x+ancho,y+alto);
        vBalaMarciano=3;
        p=new Paint();
        p.setColor(Color.RED);

        this.img= img;
    }

    public RectF getContenedor() {
        return contenedor;
    }

    public void bajar(){
        this.contenedor.top+=vBalaMarciano;
        this.contenedor.bottom+=vBalaMarciano;
    }
    public void dibujar(Canvas c){
        // c.drawRect(this.contenedor,p);
        c.drawBitmap(img,contenedor.left,contenedor.top,null);
    }
}