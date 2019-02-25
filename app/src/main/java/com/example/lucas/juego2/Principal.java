package com.example.lucas.juego2;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class Principal extends AppCompatActivity {
Juego juego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //setContentView(R.layout.activity_principal);
        View decorView = getWindow().getDecorView();
        int opciones = View.SYSTEM_UI_FLAG_FULLSCREEN        // pone la pantalla en modo pantalla completa ocultando elementos no criticos como la barra de estado.
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // oculta la barra de navegación
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(opciones);
        //fijo la orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        juego = new Juego(this);
        juego.setKeepScreenOn(true);
        setContentView(juego);
    }

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

    }


    @Override
    protected void onStop() {
        super.onStop();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();
        }
        Log.i("HOLA","STOP");

    }

    @Override
    protected void onPause() { //Another activity is taking focus (this activity is about to be "paused").
        super.onPause();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();

        }
        Log.i("HOLA","PAUSE");

    }

    @Override
    protected void onDestroy() { // The activity is about to become visible.
        super.onDestroy();
        if(juego.pantallaActual.musica){
            juego.pantallaActual.paraMusica();
        }
        Log.i("HOLA","DESTROY");
    }

}
