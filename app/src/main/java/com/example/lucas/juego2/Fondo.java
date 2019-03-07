package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Clase que se utilizará en el gameplay para que el fondo de sensación de movimiento
 * @author Lucas Alonso de San Segundo
 */
public class Fondo {
    public PointF posicion;
    public Bitmap imagen;

    /**
     * Constructor de la Fondo
     * @param imagen Bitmap que será el fondo que queramos
     * @param x Punto X donde se empezará a dibujar el fondo
     * @param y Punto Y donde se empezará a dibujar el fondo
     */
    public Fondo(Bitmap imagen, float x, float y) { // Constructores
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }

    /**
     *
     * @param imagen Bitmap que sera el fondo que queremos
     * @param altoPantalla Entero que representa el alto de la pantalla
     */
    public Fondo(Bitmap imagen, int altoPantalla) {
        this(imagen, 0, altoPantalla - imagen.getHeight());
    }

    /**
     * Método encargado de dar la sensación de movimiento de los bitmap
     * @param velocidad Double que representa la velocidad con la que queremos que se mueva el fondo
     */
    public void mover(double velocidad) { // Desplazamiento
        posicion.y += velocidad;
    }
}
