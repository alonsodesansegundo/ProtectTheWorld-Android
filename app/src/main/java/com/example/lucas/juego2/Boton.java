package com.example.lucas.juego2;

import android.graphics.Rect;

public class Boton {
    private Rect rectangulo;
    private int color;

    public Boton(int left,int top,int right,int bottom, int color) {
        this.rectangulo = new Rect(left,top,right,bottom);
        this.color = color;
    }

    public Rect getRectangulo() {
        return rectangulo;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
