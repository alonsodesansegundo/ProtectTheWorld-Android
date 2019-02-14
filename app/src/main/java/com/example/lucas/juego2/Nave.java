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
    private PointF pos;
    private Bitmap imagen;
    private RectF contenedor;
    //para el proyectil
    private boolean hayBala;
    private RectF bala;
    private Paint p;
    private Bitmap imgBala;
    private float vBala;
    //------------------------CONSTRUCTOR------------------------
    public Nave(Bitmap imagen,float x,float y,float velocidadBala,Bitmap imgBala) {
        this.pos=new PointF(x,y);
        this.imagen = imagen;
        this.hayBala=false;
        this.vBala=velocidadBala;
        this.imgBala=imgBala;
        this.p=new Paint();
        p.setColor(Color.DKGRAY);
        contenedor=new RectF(pos.x,pos.y,pos.x+imagen.getWidth(),pos.y+imagen.getWidth());
    }

    //------------------------GETTER AND SETTER------------------------
    public boolean getHayBala() {
        return hayBala;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setHayBala(boolean hayBala) {
        this.hayBala = hayBala;
    }

    public RectF getContenedor() {
        return contenedor;
    }

    public RectF getBala() {
        return bala;
    }

    //------------------------MOVER LA NAVE EN EL EJE X------------------------
    //metodo al que le paso la posicion x y se encarga de mover la nave (su pos x)
    public void moverNave(float nuevaX){
        this.pos.x=nuevaX-imagen.getWidth()/2;
        contenedor.right=pos.x+imagen.getWidth();
        contenedor.left=pos.x;
    }

    //------------------------DISPARO DE LA NAVE------------------------
    public boolean disparar(){
        if(!hayBala){
            //genero el proyectil a traves de la posicion de la nave
            bala=new RectF(contenedor.centerX()-imgBala.getWidth()/2,pos.y-imgBala.getHeight(),contenedor.centerX()+imgBala.getWidth()/2,pos.y);
            hayBala=true;
            return true;
        }
        return false;
    }

    //------------------------MOVIMIENTO PROYECTIL NAVE------------------------
    public void actualizaProyectil(){
       bala.top-=vBala;
        bala.bottom-=vBala;
        //si cuando actualizo el proyectil, llego al alto de la pantalla
        if(bala.bottom<=0){
            hayBala=false;
        }
    }

    //------------------------DIBUJO LA NAVE Y SU PROYECTIL
    public void dibujar(Canvas c){
        if(hayBala){
            //dibujo la bala
            c.drawRect(this.bala,p);
            c.drawBitmap(imgBala,bala.centerX()-imgBala.getWidth()/2,bala.top,null);
        }
        c.drawRect(contenedor,p);
        c.drawBitmap(imagen,pos.x,pos.y,null);
    }

}
