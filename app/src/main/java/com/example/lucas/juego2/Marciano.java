package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

public class Marciano {
    private PointF pos;
    private Bitmap imagen;
    private int salud;
    private double movimiento;
    private ArrayList<RectF> balas;
    private RectF contenedor, aux;
    private Paint p;
    private int vBala = 5;


    //------------------------CONSTRUCTOR------------------------
    public Marciano(Bitmap imagen, float x, float y, int salud, double velocidad) {
        this.pos = new PointF(x, y);
        this.imagen = imagen;
        this.salud = salud;
        //inicializo el movimiento a un numero aleatorio entre 1 y cinco
        //this.movimiento=(int)Math.random()*5+1;
        this.movimiento = velocidad;

        //inicializo el contenedor
        contenedor = new RectF(this.pos.x, this.pos.y, this.pos.x + this.imagen.getWidth(), this.pos.y + this.imagen.getHeight());

        //inicializo el paint
        p = new Paint();
        p.setColor(Color.RED);
        //p.setStyle(Paint.Style.STROKE);

        balas = new ArrayList<RectF>();

    }
//------------------------GETTER AND SETTER------------------------


    public PointF getPos() {
        return pos;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    //------------------------MOVIMIENTO------------------------
    //metodo mover abajo
    public void moverAbajo(boolean abajo) {
        if (abajo) {
            //aumento la posY el alto de la imagen
            this.pos.y += imagen.getHeight();
            //actualizo el contenedor
            this.contenedor.top = this.pos.y;
            this.contenedor.bottom = this.pos.y + this.imagen.getHeight();
        }
    }

    public boolean limiteAbajo(int limiteY) {
        if (this.contenedor.bottom > limiteY) {
            return true;
        }
        return false;
    }

    //metodo para mover al marciano de forma lateral
    //si la boolean que recibo está a true, voy hacia la izquierda
    //si no está a true voy a la derecha
    public void moverLateral(boolean izq) {
        if (izq) {
            this.pos.x -= movimiento;
            //acutalizo la pos x del contenedor
            this.contenedor.left = this.pos.x;
            this.contenedor.right = this.pos.x + imagen.getWidth();
        } else {
            this.pos.x += movimiento;
            //acutalizo la pos x del contenedor
            this.contenedor.left = this.pos.x;
            this.contenedor.right = this.pos.x + imagen.getWidth();
        }
    }

    //metodos para cambiar la bandera direccion
    public boolean limiteDerecha(int anchoPantalla) {
        if (this.pos.x >= anchoPantalla - imagen.getWidth()) {
            return true;
        }
        return false;
    }

    public boolean limiteIzquierda() {
        if (pos.x <= 0) {
            return true;
        }
        return false;
    }

    //------------------------RECIBE PROYECTIL------------------------
    public boolean colisiona(RectF bala) {
        aux = new RectF(this.pos.x, this.pos.y, this.pos.x + this.imagen.getWidth(), this.pos.y + this.imagen.getHeight());
        //si la bala que recibo colisiona con el
        if (bala.intersect(this.contenedor)) {
            contenedor = aux;
            return true;
        }
        return false;
    }

    //------------------------DIBUJO EL MARCIANO (Y SU CONTENEDOR)------------------------
    public void dibujar(Canvas c) {
        //dibujo el contenedor
        c.drawRect(contenedor, p);
        //dibujo la imagen marciano
        c.drawBitmap(imagen, pos.x, pos.y, null);

        //recorro las balas y las dibujo
        for (int i = 0; i < balas.size(); i++) {
            c.drawRect(balas.get(i), p);
        }
    }

    //    public Boolean colisionNave(Nave miNave){
//        //recorro las balas
//        for(int i=0;i<balas.size();i++){
//            if(balas.get(i).intersect(miNave.contenedor)){
//                return true;
//            }
//        }
//        return false;
//    }
//    public void actualizaBalas(){
//        for(int i=balas.size()-1;i>=0;i--){
//            balas.get(i).top+=vBala;
//            balas.get(i).bottom+=vBala;
//
//            //si cuando actualizo el proyectil, llego al alto de la pantalla
//            if(balas.get(i).bottom<=0){
//              //la elimino
//                balas.remove(i);
//            }
//        }
//    }
    public boolean dispara() {
        //probabilidad de disparo
        //numero aleatorio entre 1 y 10
        int numero = (int) (Math.random() * 100) + 1;
        if (numero == 1) {
            return true;
        }
        return false;
    }
}
