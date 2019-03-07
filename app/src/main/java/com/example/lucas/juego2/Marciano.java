package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Clase que genera objetos de tipo Marciano
 * @author Lucas Alonso de San Segundo
 */
public class Marciano {
    private PointF pos;
    private Bitmap imagen;
    private int salud;
    private double vMovimiento;
    private RectF contenedor, aux;
    private int puntuacion;

    //------------------------CONSTRUCTOR------------------------

    /**
     * Constructor de la clase Marciano
     * @param imagen Bitmap escalado que representa al marciano
     * @param x Float punto X de la pos del marciano
     * @param y Float punto Y de la pos del marciano
     * @param salud Entero que representa la salud del marciano
     * @param velocidad Double que representa la velocidad con la que se moverá el marciano de manera lateral
     * @param puntuacion Entero que representa la puntuación que se obtendrá tras eliminar dicho marciano
     */
    public Marciano(Bitmap imagen, float x, float y, int salud, double velocidad, int puntuacion) {
        this.pos = new PointF(x, y);
        this.imagen = imagen;
        this.salud = salud;
        this.vMovimiento = velocidad;
        this.puntuacion = puntuacion;
        //inicializo el contenedor
        this.contenedor = new RectF(this.pos.x, this.pos.y, this.pos.x + this.imagen.getWidth(), this.pos.y + this.imagen.getHeight());
    }

    //------------------------GETTER AND SETTER------------------------

    /**
     * Método que me devuelve la puntuación
     * @return Entero que representa la puntuación del marciano
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Método que devuelve el contenedor (RectF) del marciano
     * @return RectF que es el contenedor del marciano
     */
    public RectF getContenedor() {
        return contenedor;
    }

    /**
     * Nos devuelve el PointF de la pos del marciano
     * @return PointF de la posición del marciano
     */
    public PointF getPos() {
        return pos;
    }

    /**
     * Método que me devuelve la imágen del marciano
     * @return Bitmap que tiene el marciano asociado
     */
    public Bitmap getImagen() {
        return imagen;
    }

    /**
     * Método que cambia la imagen del marciano
     * @param imagen Bitmap escalado que tendrá el marciano
     */
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    /**
     * Método que me devuelve la salud del marciano
     * @return Entero que representa la salud del marciano
     */
    public int getSalud() {
        return salud;
    }

    /**
     * Método que cambia la salud del marciano
     * @param salud Entero que representa la salud que va a tener nuestro marciano
     */
    public void setSalud(int salud) {
        this.salud = salud;
    }

    //------------------------MOVIMIENTO DE LOS MARCIANOS------------------------
    //metodo mover abajo

    /**
     * Método que se encarga de descender el marciano un nivel en el caso de que la bandera que recibe sea true
     * @param abajo Booleana que si está a true significa que el marciano debe descender un nivel
     */
    public void moverAbajo(boolean abajo) {
        if (abajo) {
            //aumento la posY el alto de la imagen
            this.pos.y += imagen.getHeight() / 2;
            //actualizo el contenedor
            this.contenedor.top = this.pos.y;
            this.contenedor.bottom = this.pos.y + this.imagen.getHeight();
        }
    }

    /**
     * Método que nos sirve para ver si algún marciano ha invadido la nave
     * @param limiteY Entero que representa hasta donde pueden llegar los marcianos
     * @return True en el caso de que los marcianos hayan invadido a la nave, false en caso contrario.
     */
    public boolean limiteAbajo(int limiteY) {
        if (this.contenedor.bottom > limiteY) {
            return true;
        }
        return false;
    }

    //metodo para mover al marciano de forma lateral
    //si la boolean que recibo está a true, voy hacia la izquierda
    //si no está a true voy a la derecha

    /**
     * Método que se encarga de mover al marciano de manera lateral según la bandera que recibe
     * @param izq Booleana que indica si el marciano se debe mover hacia la izquierda (true) o hacia la derecha (false)
     */
    public void moverLateral(boolean izq) {
        if (izq) {
            this.pos.x -= vMovimiento;
            //acutalizo la pos x del contenedor
            this.contenedor.left = this.pos.x;
            this.contenedor.right = this.pos.x + imagen.getWidth();
        } else {
            this.pos.x += vMovimiento;
            //acutalizo la pos x del contenedor
            this.contenedor.left = this.pos.x;
            this.contenedor.right = this.pos.x + imagen.getWidth();
        }
    }

    //metodos para cambiar la bandera direccion

    /**
     * Método que nos permite ver si hemos llegado al limite derecho o no
     * @param anchoPantalla Entero que representa el ancho de nuestra pantalla
     * @return True si he llegado al limite lateral derecho, false de lo contrario
     */
    public boolean limiteDerecha(int anchoPantalla) {
        if (this.pos.x >= anchoPantalla - imagen.getWidth()) {
            return true;
        }
        return false;
    }

    /**
     * Método que nos indica si hemos llegado al limite lateral izquierdo o no
     * @return True si hemos llegado al limite lateral izquierdo, false de lo contrario
     */
    public boolean limiteIzquierda() {
        if (pos.x <= 0) {
            return true;
        }
        return false;
    }

    //------------------------RECIBE PROYECTIL------------------------

    /**
     * Método que se encarga de comprobar si al marciano le impacta la bala de la nave o no
     * @param bala RectF que representa la bala de la nave
     * @return True si hay colisión, false de lo contrario
     */
    public boolean colisiona(RectF bala) {
        aux = new RectF(this.pos.x, this.pos.y, this.pos.x + this.imagen.getWidth(), this.pos.y + this.imagen.getHeight());
        //si la bala que recibo colisiona con el
        if (bala.intersect(this.contenedor)) {
            contenedor = aux;
            return true;
        }
        return false;
    }

    //------------------------DIBUJO EL MARCIANO------------------------

    /**
     * Método que se encarga de dibujar el marciano
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujar(Canvas c) {
        //dibujo la imagen marciano
        c.drawBitmap(imagen, pos.x, pos.y, null);
    }

    //------------------------PROBABILIDAD DISPARO MARCIANO------------------------

    /**
     * Método que se encarga de ver si el marciano tiene que disparar o no
     * @param probabilidad Entero que representa la probabilidad de disparo del marciano
     * @return True si el marciano dispara, false de lo contrario.
     */
    public boolean dispara(int probabilidad) {
        //probabilidad de disparo
        //numero aleatorio entre 1 y 100

        int numero = (int) (Math.random() * 100) + 1;
        if (numero <= probabilidad) {
            return true;
        }
        return false;
    }
}
