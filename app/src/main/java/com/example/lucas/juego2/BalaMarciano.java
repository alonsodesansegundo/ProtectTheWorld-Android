package com.example.lucas.juego2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BalaMarciano {
   private RectF contenedor;
    private float vBalaMarciano;
    private Paint p;
    public BalaMarciano(int x,int y,int ancho,int alto) {
        this.contenedor = new RectF(x,y,x+ancho,y+alto);
        vBalaMarciano=3;
        p=new Paint();
        p.setColor(Color.RED);
    }

    public RectF getContenedor() {
        return contenedor;
    }

    public void bajar(){
        this.contenedor.top+=vBalaMarciano;
        this.contenedor.bottom+=vBalaMarciano;
    }
    public void dibujar(Canvas c){
        c.drawRect(this.contenedor,p);
    }
}
