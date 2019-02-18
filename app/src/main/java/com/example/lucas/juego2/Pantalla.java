package com.example.lucas.juego2;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class Pantalla {
    int cancion;
    SharedPreferences preferencias;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    boolean musica;
    Context contexto;
    int idPantalla, altoPantalla, anchoPantalla;
    Bitmap fondo;
    Boolean perdi,pausa;
    Paint pTexto, pTexto2, pBoton;
    Vibrator miVibrador;
    public Pantalla(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        pausa=false;
        perdi=false;
        miVibrador= (Vibrator) contexto.getSystemService(Context.VIBRATOR_SERVICE);
        this.contexto = contexto;
        this.idPantalla = idPantalla;
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        pTexto = new Paint();
        pTexto.setColor(Color.LTGRAY);
        pTexto.setTextAlign(Paint.Align.CENTER);
        pTexto.setTextSize(altoPantalla / 20);


        pTexto2 = new Paint();
        pTexto2.setColor(Color.WHITE);
        pTexto2.setTextAlign(Paint.Align.CENTER);
        pTexto2.setTextSize(altoPantalla / 20);

        pBoton = new Paint();
        pBoton.setColor(Color.DKGRAY);


    }

    // Actualizamos la física de los elementos en pantalla
    public void actualizarFisica() {

    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {

        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }

    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }

    public boolean pulsa(Rect boton, MotionEvent evento) {
        if (boton.contains((int) evento.getX(), (int) evento.getY())) {
            return true;
        } else {

            return false;
        }
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public int getIdPantalla() {
        return idPantalla;
    }

    public void setIdPantalla(int idPantalla) {
        this.idPantalla = idPantalla;
    }

    public int getAltoPantalla() {
        return altoPantalla;
    }

    public void setAltoPantalla(int altoPantalla) {
        this.altoPantalla = altoPantalla;
    }

    public int getAnchoPantalla() {
        return anchoPantalla;
    }

    public void setAnchoPantalla(int anchoPantalla) {
        this.anchoPantalla = anchoPantalla;
    }

    public Bitmap getFondo() {
        return fondo;
    }

    public void setFondo(Bitmap fondo) {
        this.fondo = fondo;
    }
    public void configuraMusica(int cancion){
        mediaPlayer= MediaPlayer.create(contexto, cancion);
        audioManager=(AudioManager)contexto.getSystemService(Context.AUDIO_SERVICE);
        int v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(v/2,v/2);

        mediaPlayer.setLooping(true);
        suenaMusica();
        paraMusica();
    }
    public void suenaMusica(){
        mediaPlayer.start();
    }
    public void paraMusica (){
        mediaPlayer.pause();
    }
public void acabaMusica(){
    mediaPlayer.release();
}
}
