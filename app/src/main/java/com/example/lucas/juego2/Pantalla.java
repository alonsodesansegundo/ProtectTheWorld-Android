package com.example.lucas.juego2;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Clase padre de la que heredan todas las pantallas
 * @author Lucas Alonso de San Segundo
 */
public class Pantalla {
    private String fuente;
    public SharedPreferences preferencias;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    public boolean musica;
    public Context contexto;
    public int idPantalla, altoPantalla, anchoPantalla;
    public Bitmap fondo;
    public Paint pTitulo;
    public Vibrator miVibrador;
    private Typeface typeFace;

    /**
     * Constructor de la clase Pantalla
     * @param contexto Objeto contexto
     * @param idPantalla Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla Entero que representa el alto de la pantalla
     */
    public Pantalla(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        miVibrador = (Vibrator) contexto.getSystemService(Context.VIBRATOR_SERVICE);
        fuente = "fuentes/miFuente.ttf";
        typeFace = Typeface.createFromAsset(contexto.getAssets(), fuente);
        this.contexto = contexto;
        this.idPantalla = idPantalla;
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        pTitulo = new Paint();
        pTitulo.setTypeface(typeFace);
        pTitulo.setColor(Color.LTGRAY);
        pTitulo.setTextAlign(Paint.Align.CENTER);
        pTitulo.setTextSize(altoPantalla / 10);


    }

    /**
     * Método que se usará para cambiar la fuente de los diferentes paint del juego
     * @return Objeto Typeface con el que cambiar la fuente de los paint
     */
    public Typeface getTypeFace() {
        return typeFace;
    }

    /**
     * Método con el que obtener el AudioManager para el sonido de la explosión de la nave
     * @return Objeto AudioManager
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * Método en el que actualizamos la física de los elementos en pantalla
     */
    public void actualizarFisica() {

    }

    /**
     * Rutina de dibujo en el lienzo. Se le llamará desde el hilo
     * @param c Objeto Canvas para poder dibujar
     */
    public void dibujar(Canvas c) {
        try {

        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }

    /**
     * Método que captura los diferentes eventos de pulsación que se pueden producir en la pantalla
     * @param event MotionEvent evento de pulsación
     * @return Entero que representa el código de la pantalla
     */
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

    /**
     * Método que nos servirá para detectar si hemos pulsado en la región donde está un rect o no
     * @param boton Rect que simula el botón
     * @param evento MotionEvent del que obtendremos el punto X,Y
     * @return True si hemos pulsado en dicho Rect, false de lo contrario
     */
    public boolean pulsa(Rect boton, MotionEvent evento) {
        if (boton.contains((int) evento.getX(), (int) evento.getY())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que sirve para realizar la "configuración" de la música
     * @param cancion Entero que representa la canción que que tendrá dicha pantalla
     */
    public void configuraMusica(int cancion) {
        mediaPlayer = MediaPlayer.create(contexto, cancion);
        audioManager = (AudioManager) contexto.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer.setLooping(true);
        suenaMusica();
        paraMusica();
    }

    /**
     * Método que inicia la música
     */
    public void suenaMusica() {
        mediaPlayer.start();
    }

    /**
     * Método que pausa la musica
     */
    public void paraMusica() {
        mediaPlayer.pause();
    }

    /**
     * Método que libera la musica
     */
    public void acabaMusica() {
        mediaPlayer.release();
    }
}
