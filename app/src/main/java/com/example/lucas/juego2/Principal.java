package com.example.lucas.juego2;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

/**
 * Clase que se encarga de ver el estado de la aplicación
 * @author Lucas Alonso de San Segundo
 */
public class Principal extends AppCompatActivity {
    /**
     * Objeto Juego
     */
    private Juego juego;

    /**
     * Constructor de la clase Principal
     * @param savedInstanceState Objeto Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();
        int opciones = View.SYSTEM_UI_FLAG_FULLSCREEN        // pone la pantalla en modo pantalla completa ocultando elementos no criticos como la barra de estado.
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // oculta la barra de navegación
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(opciones);
        //fijo la orientacion

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        juego = new Juego(this);
        juego.setKeepScreenOn(true);
        setContentView(juego);
    }

    /**
     * Método que se ejecuta cuando se reanuda la aplicación
     */
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int opciones = View.SYSTEM_UI_FLAG_FULLSCREEN        // pone la pantalla en modo pantalla completa ocultando elementos no criticos como la barra de estado.
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // oculta la barra de navegación
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(opciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    /**
     * Método que se ejecuta cuando se para la aplicación
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();
        }
        Log.i("HOLA","STOP");

    }

    /**
     * Método que se ejecuta cuando se pausa la aplicación
     */
    @Override
    protected void onPause() { //Another activity is taking focus (this activity is about to be "paused").
        super.onPause();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();
        }

    }

    /**
     * Método que se ejecuta cuando se "destruye" la aplicación
     */
    @Override
    protected void onDestroy() { // The activity is about to become visible.
        super.onDestroy();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();
        }
        Log.i("HOLA","DESTROY");
    }

}