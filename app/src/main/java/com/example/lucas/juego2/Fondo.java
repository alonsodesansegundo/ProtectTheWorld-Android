package com.example.lucas.juego2;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Fondo {
    public PointF posicion;
    public Bitmap imagen;
    public Fondo(Bitmap imagen, float x, float y) { // Constructores
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }
    public Fondo(Bitmap imagen, int altoPantalla) {
        this(imagen, 0, altoPantalla - imagen.getHeight());
    }
    public void mover(double velocidad) { // Desplazamiento
        posicion.y += velocidad;
    }
}
