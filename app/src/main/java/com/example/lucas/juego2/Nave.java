package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Clase que genera un objeto de tipo Nave
 */
public class Nave {
    private PointF pos;
    private Bitmap imagen;
    private RectF contenedor;
    //para el proyectil
    private boolean hayBala;
    private RectF bala;
    private Bitmap imgBala;
    private double vBala;
    //------------------------CONSTRUCTOR------------------------

    /**
     *
     * @param imagen Bitmap escalado que representará la nave
     * @param x Float punto X de la pos del marciano
     * @param y Float punto Y de la pos del marciano
     * @param velocidadBala Double que representa la velocidad con la que se moverá la bala de la nave
     * @param imgBala Bitmap escalado que representará la bala de la nave
     */
    public Nave(Bitmap imagen,float x,float y,double velocidadBala,Bitmap imgBala) {
        this.pos=new PointF(x,y);
        this.imagen = imagen;
        this.hayBala=false;
        this.vBala=velocidadBala;
        this.imgBala=imgBala;
        contenedor=new RectF(pos.x,pos.y,pos.x+imagen.getWidth(),pos.y+imagen.getWidth());
    }

    //------------------------GETTER AND SETTER------------------------

    /**
     * Método para ver si tengo que generar una bala o no
     * @return True si hay una bala, false de lo contrario
     */
    public boolean getHayBala() {
        return hayBala;
    }

    /**
     * Método para cambiar la imagen de la nave, se utilizará para la explosión
     * @param imagen Bitmap escalado de la imagen que queremos que represente a la nave
     */
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    /**
     * Método que me devuelve la imagen de la nave
     * @return Bitmap imagen de la nave
     */
    public Bitmap getImagen() {
        return imagen;
    }

    /**
     * Método que cambia la booleana hayBala
     * @param hayBala Nuevo valor de la propiedad hayBala
     */
    public void setHayBala(boolean hayBala) {
        this.hayBala = hayBala;
    }

    /**
     * Método que me devuelve el RectF contenedor de la nave, para ver si intersecta con alguna balaMarciano
     * @return RectF contenedor de la nave
     */
    public RectF getContenedor() {
        return contenedor;
    }

    /**
     * Método que me devuelve el RectF contenedor de la bala, para ver si intersecta con algo
     * @return RectF contenedor de la bala
     */
    public RectF getBala() {
        return bala;
    }

    //------------------------MOVER LA NAVE EN EL EJE X------------------------
    //metodo al que le paso la posicion x y se encarga de mover la nave (su pos x)

    /**
     * Método que se encarga de mover la nave en el eje X
     * @param nuevaX Float que representa la nueva posición x de la nave
     */
    public void moverNave(float nuevaX){
        this.pos.x=nuevaX-imagen.getWidth()/2;
        contenedor.right=pos.x+imagen.getWidth();
        contenedor.left=pos.x;
    }

    //------------------------DISPARO DE LA NAVE------------------------

    /**
     * Método que se encarga de generar un nuevo proyectil en el caso de que no haya bala
     * @return True si dispara, false de lo contrario
     */
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

    /**
     * Método que se encarga de mover la bala de la nave, en el caso de que la bala desaparezca de la pantalla, pone la bandera hayBala a false
     */
    public void actualizaProyectil(){
       bala.top-=vBala;
        bala.bottom-=vBala;
        //si cuando actualizo el proyectil, llego al alto de la pantalla
        if(bala.bottom<=0){
            hayBala=false;
        }
    }

    //------------------------DIBUJO LA NAVE Y SU PROYECTIL------------------------

    /**
     * Método que se encarga de dibujar la bala y la nave
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujar(Canvas c){
        if(hayBala){
            //dibujo la bala
            c.drawBitmap(imgBala,bala.centerX()-imgBala.getWidth()/2,bala.top,null);
        }
        c.drawBitmap(imagen,pos.x,pos.y,null);
    }

}
