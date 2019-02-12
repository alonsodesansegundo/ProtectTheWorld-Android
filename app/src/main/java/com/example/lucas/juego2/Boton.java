package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Boton {
    private Paint p,pTextoBtn;
    private Rect rectangulo;
    private boolean bandera;
    private Bitmap img;
    private String texto;
    public Boton(int left,int top,int right,int bottom, int color) {
        this.rectangulo = new Rect(left,top,right,bottom);
        bandera=false;
        p=new Paint();
        p.setColor(color);

        pTextoBtn=new Paint();
        pTextoBtn.setTextAlign(Paint.Align.CENTER);
        this.img=null;
    }

    public void setTexto(String texto,int tamaño,int color) {
        this.texto = texto;
        pTextoBtn.setColor(color);
        pTextoBtn.setTextSize(tamaño);
    }

    public void setColor(int col){
        p.setColor(col);
    }
    public void setImg(Bitmap img) {
        this.img = img;
    }

    public boolean getBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public Rect getRectangulo() {
        return rectangulo;
    }

    public void dibujar(Canvas c){
    c.drawRect(this.rectangulo,p);
    if(img!=null){
        c.drawBitmap(img,this.rectangulo.left,this.rectangulo.top,null);
    }
    if(texto!=null){
        //dibujo el texto del boton jugar
        c.drawText(texto, this.getRectangulo().centerX(), this.getRectangulo().centerY() - ((pTextoBtn.descent() + pTextoBtn.ascent()) / 2), pTextoBtn);
    }
    }
}
