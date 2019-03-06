package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class Creditos extends Pantalla {

    private boolean hayDedo;
    private int modo;
    private int posY;
    private Bitmap imgVolver;
    private Boton back;
    private String txtCreditos, txtMusica, txtFuente, txtImagenes, txtImg, txtImg2, txtMusic, txtFont, txtCanal,txtYotube,txtHecho, txtAyuda;
    private Paint pTexto, pTexto2;

    public Creditos(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);

        hayDedo = false;
        modo = 0;
        posY = altoPantalla / 4;
        pTexto = new Paint();
        pTexto.setTypeface(getTypeFace());
        pTexto.setColor(Color.WHITE);
        pTexto.setTextAlign(Paint.Align.CENTER);
        pTexto.setTextSize(altoPantalla / 20);
        pTexto2 = new Paint();
        pTexto2.setTypeface(getTypeFace());
        pTexto2.setColor(Color.WHITE);
        pTexto2.setTextAlign(Paint.Align.CENTER);
        pTexto2.setTextSize(altoPantalla / 30);
        //--------------STRINGS--------------
        txtCreditos = contexto.getString(R.string.creditos);
        txtMusica = contexto.getString(R.string.musica);
        txtFuente = contexto.getString(R.string.fuente);
        txtImagenes = contexto.getString(R.string.img);
        txtImg = contexto.getString(R.string.img1);
        txtImg2 = contexto.getString(R.string.img2);
        txtMusic = contexto.getString(R.string.music1);
        txtFont = contexto.getString(R.string.font1);
        txtHecho = contexto.getString(R.string.hecho);
        txtAyuda = contexto.getString(R.string.conAyuda);
txtYotube=contexto.getString(R.string.youtube);
txtCanal=contexto.getString(R.string.canalYT);
        //-------------FONDO-------------
        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo2);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);
        //--------------MUSICA--------------
        musica = preferencias.getBoolean("musica", true);
        configuraMusica(R.raw.submenus);
        if (musica) {
            suenaMusica();
        }
        back = new Boton(anchoPantalla - anchoPantalla / 10, 0, anchoPantalla, anchoPantalla / 10, Color.TRANSPARENT);
        imgVolver = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla / 10, anchoPantalla / 10, true);
        back.setImg(imgVolver);


    }

    public void dibujaImagenes(Canvas c) {
        //texto de agradecimientos
        c.drawText(txtImagenes, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtImg, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
        c.drawText(txtImg2, anchoPantalla / 2, posY + pTexto.getTextSize() * 2, pTexto2);
    }

    public void dibujaMusica(Canvas c) {
        //texto de agradecimientos
        c.drawText(txtMusica, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtMusic, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
        c.drawText(txtYotube,anchoPantalla/2,posY+pTexto.getTextSize()+pTexto2.getTextSize(),pTexto2);
        c.drawText(txtCanal,anchoPantalla/2,posY+pTexto.getTextSize()+pTexto2.getTextSize()*2,pTexto2);
    }

    public void dibujaFuente(Canvas c) {
//texto de agradecimientos
        c.drawText(txtFuente, anchoPantalla / 2, posY, pTexto);
        c.drawText(txtFont, anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);
    }

    public void dibujaUltimo(Canvas c) {
    //dibujo el texto
        c.drawText(txtAyuda, anchoPantalla / 2, posY, pTexto);
        c.drawText("Javier Conde", anchoPantalla / 2, posY + pTexto.getTextSize(), pTexto2);

        c.drawText(txtHecho,anchoPantalla/2,posY + pTexto.getTextSize()*3,pTexto);
        c.drawText("Lucas Alonso",anchoPantalla / 2, posY + pTexto.getTextSize()*4, pTexto2);
    }

    @Override
    public void dibujar(Canvas c) {
        try {
            //fondo
            c.drawBitmap(fondo, 0, 0, null);
            //titulo
            c.drawText(txtCreditos, anchoPantalla / 2, altoPantalla / 8, pTitulo);

            switch (modo) {
                case 0:
                    dibujaImagenes(c);
                    break;
                case 1:
                    dibujaMusica(c);
                    break;
                case 2:
                    dibujaFuente(c);
                    break;
                case 3:
                    dibujaUltimo(c);
                    break;
            }

            if (!hayDedo) {
                posY += altoPantalla / 200;
                if (posY > altoPantalla + pTexto.getTextSize()) {
                    posY = altoPantalla / 4;
                    modo++;
                    if (modo == 4) {
                        modo = 0;
                    }
                }
            }
            //boton volver
            back.dibujar(c);
        } catch (Exception e) {

        }
    }

    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                if (pulsa(back.getRectangulo(), event)) {
                    back.setBandera(true);
                }
                hayDedo = true;
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si pulso la opcion volver
                if (pulsa(back.getRectangulo(), event) && back.getBandera()) {
                    acabaMusica();
                    return 0;
                }
                hayDedo = false;
                back.setBandera(false);
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
}

