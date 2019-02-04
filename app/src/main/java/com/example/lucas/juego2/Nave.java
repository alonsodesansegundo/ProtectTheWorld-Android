package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Nave {
    public PointF pos;
    public Bitmap imagen;
    public int salud;
    public RectF contenedor;
    //para el proyectil
    public boolean hayBala;
    public RectF bala;
    public Paint p;
    public float vBala;
    public Nave(Bitmap imagen,float x,float y,float velocidadBala) {
        this.pos=new PointF(x,y);
        this.imagen = imagen;
        this.hayBala=false;
        this.vBala=velocidadBala;
        this.p=new Paint();
        p.setColor(Color.DKGRAY);

        contenedor=new RectF(pos.x,pos.y,pos.x+imagen.getWidth(),pos.y+imagen.getWidth());
    }
    //metodo al que le paso la posicion x y se encarga de mover la nave (su pos x)
    public void moverNave(float nuevaX){
        this.pos.x=nuevaX-imagen.getWidth()/2;
        contenedor.right=pos.x+imagen.getWidth();
        contenedor.left=pos.x;
    }
    public boolean disparar(){
        if(!hayBala){
            //genero el proyectil a traves de la posicion de la nave
            bala=new RectF(pos.x,pos.y-50,pos.x+imagen.getWidth(),pos.y);
            hayBala=true;
            return true;
        }
        return false;
    }
    public void actualizaProyectil(){
       bala.top-=vBala;
        bala.bottom-=vBala;
        //si cuando actualizo el proyectil, llego al alto de la pantalla
        if(bala.bottom<=0){
            hayBala=false;
        }
    }
    public void dibujar(Canvas c){
        if(hayBala){
            //dibujo la bala
            c.drawRect(this.bala,p);
        }
        c.drawRect(contenedor,p);
        c.drawBitmap(imagen,pos.x,pos.y,null);
    }

}
