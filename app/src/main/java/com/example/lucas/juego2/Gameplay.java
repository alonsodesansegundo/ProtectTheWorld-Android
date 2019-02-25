package com.example.lucas.juego2;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Gameplay extends Pantalla {
    //------------------------PROPIEDADES GAMEPLAY------------------------
    private Fondo[] fondo;
    private  Bitmap bitmapFondo;
    private int probabilidadDisparoMarcianos;
    private boolean estoyJugando;
    private boolean empece;
    private int nivel, filas, columnas, puntuacionGlobal;
    private Bitmap imgMarciano1, imgMarciano2, imgNave, proyectilMarciano, balaNave, explosion;
    private float primeraX, primeraY, tamañoPuntuacion;
    private double vMarciano,vBala,vBalaMarciano,vFondo;
    private Marciano marcianos[][];
    private Nave miNave;
    private boolean voyIzquierda, voyAbajo, mueveNave;
    private ArrayList misColumnas;
    private ArrayList<BalaMarciano> balasMarcianos;
    private Paint pPunutacion;
    private int tiempoVibracion;
    private Boton btnPausa, btnReanudar, btnSalir, btnMusica, btnJugar,btnNoJugar, btnSi, btnNo, btnAux;
    private Bitmap imgPausa, imgPlay, imgMusicaOn, imgMusicaOff;
    private int codNave;
    private int margenLateralPausa;
    private int altoMenuPausa;
    private int ultimaPuntuacion;
    private SharedPreferences.Editor editorPreferencias;
    private SharedPreferences preferencias;
    private boolean vibracion, giroscopio;

    //efectos sonoros
    private SoundPool efectos;
    private int sonidoDisparoNave, sonidoMuereNave;
    private int maxSonidosSimultaneos;


    //para las siglas
    private int pos;
    private int tamañoSiglas;
    private ArrayList<Character> abecedario;
    private boolean pideSiglas, tengoSiglas, hiceInsert;
    private char[] siglas;
    private int altoMenuIniciales;
    private Boton btnSiglaArriba, btnSiglaAbajo, btnSigla2Arriba,
            btnSigla2Abajo, btnSigla3Arriba, btnSigla3Abajo, btnEnviar;
    private Bitmap trianguloArriba, trianguloAbajo;

    //para la bd
    private int ultimoId;
    private String consultaUltima, consultaId;
    private BaseDeDatos bd;
    private SQLiteDatabase db;
    private Cursor c;
    private String txtContinuar, txtSalir, txtAccion, txtEmpezar, txtSi, txtRepetir, txtNo, txtSiglas, txtEnviar;
    //timer para disparo marcianos
    private TimerTask task;
    private Timer miTimer;

    //------------------------CONSTRUCTOR------------------------
    public Gameplay(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);

        bitmapFondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.universe2);
        bitmapFondo = Bitmap.createScaledBitmap( bitmapFondo, anchoPantalla, bitmapFondo.getHeight(), true);
        fondo = new Fondo[2];
        fondo[0] = new Fondo(bitmapFondo, altoPantalla);
        fondo[1] = new Fondo(bitmapFondo, 0, fondo[0].posicion.y - bitmapFondo.getHeight());


        vFondo=altoPantalla*0.001;
        vBala=altoPantalla*0.01;
        vBalaMarciano=vBala/3;
        //efectos sonoros
        maxSonidosSimultaneos = 10;
        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(maxSonidosSimultaneos);
            this.efectos = spb.build();
        } else {
            this.efectos = new SoundPool(maxSonidosSimultaneos, AudioManager.STREAM_MUSIC, 0);
        }

        sonidoMuereNave = efectos.load(contexto, R.raw.muerenave, 1);
        sonidoDisparoNave = efectos.load(contexto, R.raw.shot, 1);
        estoyJugando = false;
        miTimer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (estoyJugando) {
                    disparanMarcianos();
                }
            }
        };
        // Empezamos dentro de 10ms y luego lanzamos la tarea cada 1000ms
        miTimer.schedule(task, 2000, 2000);

        probabilidadDisparoMarcianos = 33;
        empece = false;
        pausa = false;
        perdi = false;
        pideSiglas = false;
        hiceInsert = false;
        //----------------STRINGS----------------
        txtContinuar = contexto.getString(R.string.continuar);
        txtSalir = contexto.getString(R.string.salir);
        txtAccion = contexto.getString(R.string.accion);
        txtEmpezar = contexto.getString(R.string.listo);
        txtSi = contexto.getString(R.string.si);
        txtRepetir = contexto.getString(R.string.repetir);
        txtNo = contexto.getString(R.string.no);
        txtSiglas = contexto.getString(R.string.siglas);
        txtEnviar = contexto.getString(R.string.enviar);

        //----------------ABECEDARIO----------------
        abecedario = new ArrayList<Character>();
        for (int i = 0; i < 26; i++) {
            abecedario.add((char) ('A' + i));
        }

        //----------------SIGLAS INICIALES----------------
        siglas = new char[3];
        siglas[0] = abecedario.get(0);
        siglas[1] = abecedario.get(0);
        siglas[2] = abecedario.get(0);

        tamañoSiglas = altoPantalla / 12;
        //----------------BOTONES SIGLAS----------------

        //----------------IMAGENES TRIANGULOS----------------
        trianguloAbajo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.triangulodown);
        trianguloAbajo = Bitmap.createScaledBitmap(trianguloAbajo, anchoPantalla / 10, anchoPantalla / 10, true);

        trianguloArriba = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.trianguloup);
        trianguloArriba = Bitmap.createScaledBitmap(trianguloArriba, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------BOTONES SIGLA 1----------------
        btnSiglaArriba = new Boton(anchoPantalla / 20 + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla / 20 + trianguloArriba.getWidth() + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSiglaArriba.setImg(trianguloArriba);

        btnSiglaAbajo = new Boton(anchoPantalla / 20 + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla / 20 + trianguloArriba.getWidth() + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSiglaAbajo.setImg(trianguloAbajo);

        //----------------BOTONES SIGLA 2----------------
        btnSigla2Abajo = new Boton(anchoPantalla / 2 - trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla / 2 + trianguloArriba.getWidth() / 2,
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSigla2Abajo.setImg(trianguloAbajo);

        btnSigla2Arriba = new Boton(anchoPantalla / 2 - trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla / 2 + trianguloArriba.getWidth() / 2,
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSigla2Arriba.setImg(trianguloArriba);

        //----------------BOTONES SIGLA 3----------------
        btnSigla3Arriba = new Boton(anchoPantalla - trianguloArriba.getWidth() * 2,
                altoPantalla / 2 - altoPantalla / 50 - trianguloArriba.getHeight() / 2,
                anchoPantalla - trianguloArriba.getWidth(),
                altoPantalla / 2 - altoPantalla / 50 + trianguloArriba.getHeight() / 2, Color.TRANSPARENT);
        btnSigla3Arriba.setImg(trianguloArriba);

        btnSigla3Abajo = new Boton(anchoPantalla - trianguloArriba.getWidth() * 2,
                altoPantalla / 3 * 2 - altoPantalla / 100 - trianguloArriba.getHeight(),
                anchoPantalla - trianguloArriba.getWidth(),
                altoPantalla / 3 * 2 - altoPantalla / 100, Color.TRANSPARENT);
        btnSigla3Abajo.setImg(trianguloAbajo);

        altoMenuIniciales = altoPantalla / 3;

        //----------------BTN ENVIAR----------------
        btnEnviar = new Boton(anchoPantalla / 2 - anchoPantalla / 10,
                altoPantalla / 3 * 2 + altoPantalla / 20,
                anchoPantalla / 2 + anchoPantalla / 10,
                altoPantalla / 3 * 2 + altoPantalla / 20 * 2, Color.GREEN);

        btnEnviar.setTexto(txtEnviar, altoPantalla / 40, Color.BLACK);
        //-----------------MENU PAUSA----------------
        margenLateralPausa = anchoPantalla / 20;
        altoMenuPausa = altoPantalla / 4;

        //----------------ARCHIVO CONFIGURACIÓN--------------
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editorPreferencias = preferencias.edit();
        //--------------BOOLEAN GIROSCOPIO--------------
        giroscopio = preferencias.getBoolean("giroscopio", false);
        //--------------BOOLEAN VIBRACION--------------
        vibracion = preferencias.getBoolean("vibracion", true);
        //----------------BOTON PAUSA----------------
        btnPausa = new Boton(anchoPantalla - anchoPantalla / 10, 0,
                anchoPantalla, anchoPantalla / 10, Color.TRANSPARENT);

        //----------------IMAGEN PAUSA----------------
        imgPausa = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.pause);
        imgPausa = Bitmap.createScaledBitmap(imgPausa, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------SET IMAGEN PAUSA----------------
        btnPausa.setImg(imgPausa);

        //----------------IMAGEN PLAY----------------
        imgPlay = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.play);
        imgPlay = Bitmap.createScaledBitmap(imgPlay, anchoPantalla / 10, anchoPantalla / 10, true);


        //----------------BOTON MÚSICA----------------
        btnMusica = new Boton(anchoPantalla - anchoPantalla / 10 * 2, 0,
                anchoPantalla - anchoPantalla / 10, anchoPantalla / 10, Color.TRANSPARENT);

        //----------------IMAGEN MUSICA ON----------------
        imgMusicaOn = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.musica);
        imgMusicaOn = Bitmap.createScaledBitmap(imgMusicaOn, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------IMAGEN MUSICA OFF----------------
        imgMusicaOff = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.musicano);
        imgMusicaOff = Bitmap.createScaledBitmap(imgMusicaOff, anchoPantalla / 10, anchoPantalla / 10, true);

        //----------------MUSICA----------------
        musica = preferencias.getBoolean("musica", true);
        configuraMusica(R.raw.spectre);
        if (musica) {
            btnMusica.setImg(imgMusicaOff);
            suenaMusica();
        } else {
            btnMusica.setImg(imgMusicaOn);
            paraMusica();
        }
        //----------------MILISEGUNDOS VIBRACIÓN----------------
        tiempoVibracion = 1000;

        //----------------ALTO DE LA PUNTUACIÓN----------------
        tamañoPuntuacion = altoPantalla / 15;
        //-----------------PAINT PUNTUACIÓN-----------------
        pPunutacion = new Paint();
        pPunutacion.setColor(Color.WHITE);
        pPunutacion.setTextAlign(Paint.Align.CENTER);
        pPunutacion.setTextSize(tamañoPuntuacion);

        //-----------------PUNTUACIÓN GLOBAL-----------------
        puntuacionGlobal = 0;

        //-----------------POSICIÓN PRIMER MARCIANO-----------------
        primeraX = 0;
        primeraY = 0;
        //al comienzo los marcianos se moverán hacia la derecha
        voyIzquierda = false;
        //al comienzo los marcianos no irán hacia abajo ya
        voyAbajo = false;
        //al comienzo la nave todavía no se mueve
        mueveNave = false;
        //-----------------NIVEL INICIAL-----------------
        nivel = 0;
        //-----------------FILAS Y COLUMNAS DE MARCIANOS-----------------
        filas = 5;
        columnas = 6;
        marcianos = new Marciano[filas][columnas];  //cinco filas seis columnas de marcianos
        //velocidad de movimiento lateral de los marcianos al comienzo
        vMarciano = anchoPantalla*0.001;
        //imagen marcianos impacto 1
//        imgMarciano1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.marciano1);
//        imgMarciano1 = Bitmap.createScaledBitmap(imgMarciano1, anchoPantalla / 20, altoPantalla / 30, true);

        imgMarciano1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio1);
        imgMarciano1 = Bitmap.createScaledBitmap(imgMarciano1, anchoPantalla / 20, altoPantalla / 30, true);


        //imagen marcianos impacto 2
//        imgMarciano2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.marciano2);
//        imgMarciano2 = Bitmap.createScaledBitmap(imgMarciano2, anchoPantalla / 20, altoPantalla / 30, true);

        imgMarciano2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio2);
        imgMarciano2 = Bitmap.createScaledBitmap(imgMarciano2, anchoPantalla / 20, altoPantalla / 30, true);


        proyectilMarciano = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.bombamarciano);
        proyectilMarciano = Bitmap.createScaledBitmap(proyectilMarciano, anchoPantalla / 20, altoPantalla / 30, true);


        balaNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.proyectilnave);
        balaNave = Bitmap.createScaledBitmap(balaNave, anchoPantalla / 30, altoPantalla / 20, true);
        //llenar de marcianos el array bidimensional
        rellenaMarcianos();

        //imagen de la nave
        codNave = preferencias.getInt("idNave", 1);
        switch (codNave) {
            case 0:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
                break;
            case 1:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
                break;
            case 2:
                imgNave = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);
                imgNave = Bitmap.createScaledBitmap(imgNave, anchoPantalla / 10, altoPantalla / 15, true);
        }

        explosion = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.explosion);
        explosion = Bitmap.createScaledBitmap(explosion, anchoPantalla / 10, altoPantalla / 15, true);
        //creo el objeto nave
        miNave = new Nave(imgNave, anchoPantalla / 2 - imgNave.getWidth() / 2, altoPantalla - imgNave.getHeight(),
                vBala, balaNave);

        //arraylist con las balas generadas por los marcianos
        balasMarcianos = new ArrayList<BalaMarciano>();

        //arraylist auxiliar para que disparen solamente los ultimos marcianos de cada columna
        misColumnas = new ArrayList();

        //----------------BTN REANUDAR----------------
        btnReanudar = new Boton(margenLateralPausa * 2, altoPantalla / 2,
                anchoPantalla / 2 - margenLateralPausa,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.GREEN);
        btnReanudar.setTexto(txtContinuar, altoPantalla / 30, Color.BLACK);

        //----------------BTN SALIR----------------
        btnSalir = new Boton(anchoPantalla / 2 + margenLateralPausa, altoPantalla / 2,
                anchoPantalla - margenLateralPausa * 2,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.RED);
        btnSalir.setTexto(txtSalir, altoPantalla / 30, Color.BLACK);

        //----------------BTN PARA EMPEZAR A JUGAR----------------
        btnJugar = new Boton(anchoPantalla / 2 - anchoPantalla / 4, altoPantalla / 2,
                anchoPantalla / 2 , altoPantalla / 2 + altoPantalla / 11, Color.GREEN);
        btnJugar.setTexto(txtSi, altoPantalla / 15, Color.BLACK);

        btnNoJugar= new Boton(anchoPantalla / 2 , altoPantalla / 2,
                anchoPantalla / 2 + anchoPantalla / 4, altoPantalla / 2 + altoPantalla / 11, Color.RED);
        btnNoJugar.setTexto(txtNo, altoPantalla / 15, Color.BLACK);

        //----------------BTN PARA REPETIR O NO----------------
        btnSi = new Boton(margenLateralPausa * 2, altoPantalla / 2,
                anchoPantalla / 2 - margenLateralPausa,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.GREEN);
        btnSi.setTexto(txtSi, altoPantalla / 30, Color.BLACK);


        btnNo = new Boton(anchoPantalla / 2 + margenLateralPausa, altoPantalla / 2,
                anchoPantalla - margenLateralPausa * 2,
                altoPantalla / 2 + altoMenuPausa / 2 - margenLateralPausa, Color.RED);
        btnNo.setTexto(txtNo, altoPantalla / 30, Color.BLACK);
    }

    // Actualizamos la física de los elementos en pantalla
    public void actualizarFisica() {
        mueveFondo();

        //si no he pausado, el gameplay continua
        if (estoyJugando) {
            //------------------------DISPARO DE LA NAVE------------------------
            disparaNave();

            //------------------------DISPARO DE LOS MARCIANOS------------------------
            //lo hago a traves del timer en el constructor


            //------------------------MOVER BALAS MARCIANOS (ARRAYLIST)------------------------
            actualizaBalasMarcianos();

            //------------------------MOVIMIENTO VERTICAL Y HORIZONTAL DE LOS MARCIANOS------------------------

            //VEO EN QUE DIRECCIÓN SE TIENEN QUE MOVER Y SI DESCIENDEN UN NIVEL O NO Y ACTUALIZO LAS BANDERAS
            actualizaBanderasMovimiento();

            //MUEVO LOS MARCIANOS SEGÚN LAS BANDERAS
            mueveMarcianos();
        }

        if (tengoSiglas && !hiceInsert) {
            insertPuntuacion();
            perdi = true;
        }
    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {
            dibujaFondo(c);
            //si he empezado a jugar
            if (estoyJugando) {
                dibujaJuego(c);
            } else {
                //si aun no he empezado a jugar
                dibujaInicio(c);
            }
            //si he pulsado el boton de pausa
            if (pausa) {
                dibujaJuego(c);
                dibujaPausa(c);
            }
            //si he perdido
            if (perdi) {
                dibujaJuego(c);
                dibujaPerdi(c);
            }
            if (pideSiglas && !tengoSiglas) {
                dibujaJuego(c);
                dibujaPideSiglas(c);
            }
        } catch (Exception e) {
            Log.i("Error al dibujar", e.getLocalizedMessage());
        }
    }


    public void rellenaMarcianos() {
        //incremento el nivel
        nivel++;
        //recorro las filas
        for (int i = 0; i < marcianos.length; i++) {
            //incremento la pos y
            primeraY += imgMarciano1.getHeight() + altoPantalla / 20;
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //dependiendo del nivel y de la fila en la que esté
                //pongo un marciano nivel 1 o marciano nivel 2
                //por ejemplo, si estoy en la ultima fila y en el nivel 2-1, sera de marcianos de dos impactos
                if (i >= marcianos.length - (nivel - 1)) {
                    marcianos[i][j] = new Marciano(imgMarciano2, primeraX, primeraY, 2, vMarciano, 25);
                } else {
                    marcianos[i][j] = new Marciano(imgMarciano1, primeraX, primeraY, 1, vMarciano, 10);
                }
                //aumento la posX
                primeraX += imgMarciano1.getWidth() + anchoPantalla / 10;
            }
            //una vez recorro todas las columnas de la fila actual
            primeraX = 0;
        }
        //una vez recorro todas las filas y columnas
        primeraY = 0;
        //si el nivel - 1 es igual al numero de filas, es decir, he llegado a cubrir la pantalla de marcianos de dos impactos
        if (nivel - 1 == filas) {
            nivel = 0;
            vMarciano = vMarciano * 2;
        }
    }

    //devuelvo true si encuentro algun marciano, devuelvo false si no hay 7ningun marciano
    public boolean hayMarcianos() {
        for (int i = 0; i < marcianos.length; i++) {
            for (int j = 0; j < marcianos[0].length; j++) {
                if (marcianos[i][j] != null) {
                    return true;
                }
            }
        }
        //si no he encontrado ningun null
        return false;
    }

    public void vaciaBalas(){
        balasMarcianos.clear();
    }

    public void mueveFondo(){
        if(estoyJugando){
            // Movemos
            fondo[0].mover(vFondo);
            fondo[1].mover(vFondo);
// Comprobamos que se sobrepase la pantalla y reiniciamos
            if (fondo[0].posicion.y > altoPantalla) {
                fondo[0].posicion.y = fondo[1].posicion.y - fondo[0].imagen.getHeight();
            }
            if (fondo[1].posicion.y > altoPantalla) {
                fondo[1].posicion.y = fondo[0].posicion.y - fondo[1].imagen.getHeight();
            }
        }
    }
    //------------------------DISPARO DE LA NAVE------------------------
    public void disparaNave() {
        //solo habrá una bala de la nave en la pantalla
        //si hay bala, la muevo
        if (miNave.getHayBala()) {
            //acutalizo las posiciones del proyectil
            miNave.actualizaProyectil();
            //recorro los marcianos, para ver si alguno choca, en el caso de ser asi miNave.hayBala = false
            for (int i = 0; i < marcianos.length; i++) {
                for (int j = 0; j < marcianos[0].length; j++) {
                    //si hay un marciano
                    if (marcianos[i][j] != null) {
                        // si la bala impacta con un marciano
                        if (marcianos[i][j].colisiona(miNave.getBala())) {
                            //le resto uno de salud al marciano
                            marcianos[i][j].setSalud(marcianos[i][j].getSalud() - 1);
                            //si salud es cero
                            if (marcianos[i][j].getSalud() == 0) {

                                //sumo a mi puntuacion global los puntos del marciano
                                puntuacionGlobal += marcianos[i][j].getPuntuacion();
                                //elimino el marciano
                                marcianos[i][j] = null;
                            } else {
                                //si continua con salud
                                //cambio la imagen del marciano por una de marciano nivel 1
                                marcianos[i][j].setImagen(imgMarciano1);
                            }
                            //quito la bala
                            miNave.setHayBala(false);

                            //si no hay marcianos
                            //relleno el array segun el nivel, de ello se encarga rellena marcianos
                            if (!hayMarcianos()) {
                                vaciaBalas();
                                rellenaMarcianos();
                            }
                            //salgo del bucle porque no hace falta seguir recorriendo todos los marcianos, ya que solo es posible que haya un impacto
                            break;
                        }
                    }
                }
            }
        } else {
            //si no hay bala, la genero
            miNave.disparar();

            //reproduzco el sonido del disparo nave
            if (musica) {
              suenaDisparoNave();
            }

        }
    }

    //------------------------SONIDOS DISPARO NAVE Y MUERE NAVE------------------------
    public void suenaMuereNave(){
        int v = getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
        efectos.play(sonidoMuereNave, v, v, 1, 0, 1);
    }
    public void suenaDisparoNave(){
        int v = getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
        efectos.play(sonidoDisparoNave, v, v, 2, 0, 1);
    }

    //------------------------DISPARO DE LOS MARCIANOS------------------------
    public void disparanMarcianos() {
        //solo podrán disparar los últimos marcianos de cada columna
        //recorro las filas de abajo a arriba
        for (int i = marcianos.length - 1; i >= 0; i--) {
            //recorro las columnas de izq a drch
            for (int j = 0; j < marcianos[0].length; j++) {
                //si esa columna no está en mi arraylist de columnas y en la posicion actual hay un marciano
                if (misColumnas.indexOf(j) == -1 && marcianos[i][j] != null) {
                    //añado la columna a mi arraylist
                    misColumnas.add(j);
                    //si se decide que el marciano dispare (porque sale x probabilidad)
                    if (marcianos[i][j].dispara(probabilidadDisparoMarcianos)) {
                        //genero una nueva bala marciano que añado a su array
                        balasMarcianos.add(new BalaMarciano((int) marcianos[i][j].getContenedor().centerX() -
                                proyectilMarciano.getWidth() / 2,
                                (int) marcianos[i][j].getPos().y + marcianos[i][j].getImagen().getHeight(),
                                proyectilMarciano.getWidth(),
                                proyectilMarciano.getHeight(), proyectilMarciano,vBalaMarciano));
                    }
                }
            }
        }
        //limpio el arraylist que uso de contenedor de las columnas
        misColumnas.clear();
    }

    //------------------------MOVER BALAS MARCIANOS (ARRAYLIST)------------------------
    public void actualizaBalasMarcianos() {
        //además de mover las balas, gestiono si chocan o no con la nave, y si desaparecen de la pantalla las elimino
        //de atrás alante para no tener problemas al eliminar
        for (int i = balasMarcianos.size() - 1; i >= 0; i--) {
            //muevo la bala actual hacia abajo
            balasMarcianos.get(i).bajar();
            //si choca con la nave
            if (balasMarcianos.get(i).getContenedor().intersect(miNave.getContenedor())) {

                miNave.setImagen(explosion);
                //vibra el dispositivo
                if (vibracion) {

                    vibrar();
                }
                //perdi
                if (musica) {

                    //sonido explosion nave
                    suenaMuereNave();
                }
                estoyJugando = false;
                mueveNave = false;
                acabaMusica();
                if (mejoraPuntuacion()) {
                    pideSiglas = true;
                } else {
                    perdi = true;
                }
            } else {
                //si no ha chocado con la nave
                //veo si ha chocado o no con la bala de la nave, si es asi, elimino ambas balas
                if (balasMarcianos.get(i).getContenedor().intersect(miNave.getBala())) {

                    //elimino ambas balas
                    //elimino la bala marciano
                    balasMarcianos.remove(i);

                    //elimino la bala de la nave
                    miNave.setHayBala(false);

                    //si no choca con la nave ni con la bala de la nave
                } else {
                    //veo si desaparece de la pantalla, si es así
                    if (balasMarcianos.get(i).getContenedor().top >= altoPantalla) {
                        //la elimino para no mover balas que no se ven
                        balasMarcianos.remove(i);
                    }
                }

            }
        }
    }

    //------------------------MOVIMIENTO VERTICAL Y HORIZONTAL DE LOS MARCIANOS------------------------
    public void actualizaBanderasMovimiento() {
        //aqui veré en que dirección tienen que ir los marcianos (izq o drch) -> bandera voyIzquierda
        //también veré si descienden un nivel o no -> bandera voy abajo
        //recorro las filas de marcianos
        for (int i = 0; i < marcianos.length; i++) {
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //si hay un marciano
                if (marcianos[i][j] != null) {
                    //si un marciano llega al limite de la derecha
                    if (marcianos[i][j].limiteDerecha(anchoPantalla)) {
                        //bandera voy abajo a true
                        voyAbajo = true;
                        //pongo la bandera voyIzquierda a true
                        voyIzquierda = true;
                        //salgo del for porque uno de ellos a llegado al limite
                        break;
                    } else {
                        //si no llegue al limite por la derecha, miro si llegue al limite por la izquierda
                        if (marcianos[i][j].limiteIzquierda()) {
                            //bandera voy abajo a true
                            voyAbajo = true;
                            //pongo la bandera voyIzquierda a false
                            voyIzquierda = false;
                            //salgo del for porque uno de ellos a llegado al limite
                            break;
                        }
                    }
                }
            }
        }
    }

    public void mueveMarcianos() {
        //recorro las filas
        for (int i = 0; i < marcianos.length; i++) {
            //recorro las columnas
            for (int j = 0; j < marcianos[0].length; j++) {
                //si hay un marciano
                if (marcianos[i][j] != null) {
                    //lo muevo de manera lateral segun en que dirección tengo que ir
                    marcianos[i][j].moverLateral(voyIzquierda);
                    //en caso de tener que descender un nivel, lo hace
                    marcianos[i][j].moverAbajo(voyAbajo);
                    if (marcianos[i][j].limiteAbajo(altoPantalla - miNave.getImagen().getHeight())) {
                        //hago que el dispositivo vibre
                        vibrar();
                        perdi = true;
                        estoyJugando = false;
                    }
                }
            }
        }
        //después de mover todos los marcianos
        //pongo la bandera voyAbajo a false
        voyAbajo = false;
    }

    //------------------------VIBRACIÓN DEL DISPOSITIVO------------------------
    public void vibrar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            miVibrador.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            miVibrador.vibrate(tiempoVibracion);
        }
    }

    //------------------------CUANDO PULSO LA PANTALLA (PUEDO PULSAR UN BOTON O MOVER LA NAVE)------------------------
    public int onTouchEvent(MotionEvent event) {
        //cuando el dedo esté en la pantalla, muevo la nave con respecto al eje x!!!!!!!!!
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación


        switch (accion) {
            case MotionEvent.ACTION_DOWN:// Primer dedo toca
                //si estoy jugando con la opcion del giroscopio desactivada, y pulso en la pos x donde está la nave, puedo mover la nave
                if (!giroscopio && pointerID == 0 && estoyJugando && (event.getX() >= miNave.getContenedor().left && event.getX() <= miNave.getContenedor().right) || mueveNave) {
                    mueveNave = true;
                }
                if (estoyJugando) {
                    //si pulso el btn musica
                    if (pulsa(btnMusica.getRectangulo(), event)) {
                        btnMusica.setBandera(true);
                        btnAux = btnMusica;
                    }
                    //si pulso el btn pausa
                    if (pulsa(btnPausa.getRectangulo(), event)) {
                        btnPausa.setBandera(true);
                        btnAux = btnPausa;
                    }
                }
                if (!empece) {
                    //si no empece y pulso el btn si a jugar
                    if (pulsa(btnJugar.getRectangulo(), event)) {
                        btnJugar.setBandera(true);
                        btnAux = btnJugar;
                    }
                    if (pulsa(btnNoJugar.getRectangulo(), event)) {
                        btnNoJugar.setBandera(true);
                        btnAux = btnNoJugar;
                    }

                }
                //si estoy en pausa
                if (pausa) {
                    //si pulso el btn salir
                    if (pulsa(btnSalir.getRectangulo(), event)) {
                        btnSalir.setBandera(true);
                        btnAux = btnSalir;
                    }
                    //si pulso el btn reanudar
                    if (pulsa(btnReanudar.getRectangulo(), event)) {
                        btnReanudar.setBandera(true);
                        btnAux = btnReanudar;
                    }

                    if (pulsa(btnPausa.getRectangulo(), event)) {
                        btnPausa.setBandera(true);
                        btnAux = btnPausa;
                    }
                }
                if (pideSiglas) {
                    if (pulsa(btnSiglaArriba.getRectangulo(), event)) {
                        btnSiglaArriba.setBandera(true);
                        btnAux = btnSiglaArriba;
                    }
                    if (pulsa(btnSigla2Arriba.getRectangulo(), event)) {
                        btnSigla2Arriba.setBandera(true);
                        btnAux = btnSigla2Arriba;
                    }
                    if (pulsa(btnSigla3Arriba.getRectangulo(), event)) {
                        btnSigla3Arriba.setBandera(true);
                        btnAux = btnSigla3Arriba;
                    }

                    if (pulsa(btnSiglaAbajo.getRectangulo(), event)) {
                        btnSiglaAbajo.setBandera(true);
                        btnAux = btnSiglaAbajo;
                    }
                    if (pulsa(btnSigla2Abajo.getRectangulo(), event)) {
                        btnSigla2Abajo.setBandera(true);
                        btnAux = btnSigla2Abajo;
                    }
                    if (pulsa(btnSigla3Abajo.getRectangulo(), event)) {
                        btnSigla3Abajo.setBandera(true);
                        btnAux = btnSigla3Abajo;
                    }
                    if (pulsa(btnEnviar.getRectangulo(), event)) {
                        btnEnviar.setBandera(true);
                        btnAux = btnEnviar;
                    }
                }
                if (perdi) {
                    if (pulsa(btnSi.getRectangulo(), event)) {
                        btnSi.setBandera(true);
                        btnAux = btnSi;
                    }
                    if (pulsa(btnNo.getRectangulo(), event)) {
                        btnNo.setBandera(true);
                        btnAux = btnNo;
                    }
                }


            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;
            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                if (mueveNave) {
                    mueveNave = false;
                }
                //mientras estoy jugando
                if (estoyJugando) {
                    //si levanto el dedo en el btn musica
                    if (pulsa(btnMusica.getRectangulo(), event) && btnMusica.getBandera()) {
                        cambiaBtnMusica();
                    }
                    //si pulso la opcion pausa
                    if (pulsa(btnPausa.getRectangulo(), event) && btnPausa.getBandera()) {

                        //muestro pantallaPausa, reanudar o salir
                        pausa = true;
                        estoyJugando = false;

                        paraMusica();
                        btnPausa.setImg(imgPlay);

                    }
                } else {
                    if (pulsa(btnJugar.getRectangulo(), event) && btnJugar.getBandera()) {
                        empece = true;
                        estoyJugando = true;
                    }
                    if (pulsa(btnNoJugar.getRectangulo(), event) && btnNoJugar.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }
                    if (pausa) {

                        //si he pulsado la opcion salir
                        if (pulsa(btnSalir.getRectangulo(), event) && btnSalir.getBandera()) {
                            //vuelvo al menu
                            acabaMusica();
                            return 0;
                        }
                        if (pulsa(btnReanudar.getRectangulo(), event) && btnReanudar.getBandera()) {
                            //reanudo el gameplay
                            reanudaGame();
                        }
                        if (pulsa(btnPausa.getRectangulo(), event) && btnPausa.getBandera()) {
                            //reanudo el gameplay
                            reanudaGame();

                        }
                    }

                }

                if (pideSiglas) {
                    if (pulsa(btnSiglaArriba.getRectangulo(), event) && btnSiglaArriba.getBandera()) {
                        siglas[0] = retrocede(siglas[0]);
                    }
                    if (pulsa(btnSigla2Arriba.getRectangulo(), event) && btnSigla2Arriba.getBandera()) {
                        siglas[1] = retrocede(siglas[1]);
                    }
                    if (pulsa(btnSigla3Arriba.getRectangulo(), event) && btnSigla3Arriba.getBandera()) {
                        siglas[2] = retrocede(siglas[2]);
                    }

                    if (pulsa(btnSiglaAbajo.getRectangulo(), event) && btnSiglaAbajo.getBandera()) {
                        siglas[0] = avanza(siglas[0]);
                    }
                    if (pulsa(btnSigla2Abajo.getRectangulo(), event) && btnSigla2Abajo.getBandera()) {
                        siglas[1] = avanza(siglas[1]);
                    }
                    if (pulsa(btnSigla3Abajo.getRectangulo(), event) && btnSigla3Abajo.getBandera()) {
                        siglas[2] = avanza(siglas[2]);
                    }
                    if (pulsa(btnEnviar.getRectangulo(), event) && btnEnviar.getBandera()) {
                        tengoSiglas = true;
                    }
                }
                if (perdi) {
                    if (pulsa(btnSi.getRectangulo(), event) && btnSi.getBandera()) {
                        return 6;
                    }
                    if (pulsa(btnNo.getRectangulo(), event) && btnNo.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }

                }
                //pongo la bandera del btn que anteriormente puse a true a false
                if (btnAux != null) {
                    btnAux.setBandera(false);
                }

            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último
                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                //solo puedo poner mueve nave a true, cuando giroscopio está a false entre una de las condiciones
                if (mueveNave && pointerID == 0) {
                    miNave.moverNave(event.getX());
                }
                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }


    //------------------------CUANDO PULSO EL BTN DE LA MÚSICA EN EL GAMEPLAY------------------------
    public void cambiaBtnMusica() {
        musica = !musica;
        if (musica) {
            suenaMusica();
            btnMusica.setImg(imgMusicaOff);
        } else {
            paraMusica();
            btnMusica.setImg(imgMusicaOn);
        }
        editorPreferencias.putBoolean("musica", musica);
        editorPreferencias.commit();
    }

    //------------------------DIBUJAR LA PANTALLA------------------------
    public void dibujaFondo(Canvas c){
        if (!estoyJugando) c.drawBitmap(bitmapFondo, 0, 0, null); // Dibujamos el fondo
        else {
            c.drawBitmap(fondo[0].imagen, fondo[0].posicion.x, fondo[0].posicion.y, null);
            c.drawBitmap(fondo[1].imagen, fondo[1].posicion.x, fondo[1].posicion.y, null);
        }
    }
    public void dibujaInicio(Canvas c) {
        c.drawText(txtEmpezar, anchoPantalla / 2, altoPantalla / 2 - tamañoPuntuacion / 2, pPunutacion);
        btnJugar.dibujar(c);
        btnNoJugar.dibujar(c);
    }

    public void dibujaJuego(Canvas c) {
        //dibujo el btnPausa
        btnPausa.dibujar(c);

        //dibujo el btn sonido
        btnMusica.dibujar(c);
        //dibujo los marcianos del array bidimensional (marcianos)
        for (int i = 0; i < marcianos.length; i++) {
            for (int j = 0; j < marcianos[0].length; j++) {
                if (marcianos[i][j] != null) {
                    //dibujo a los marcianos y su contenedor
                    marcianos[i][j].dibujar(c);
                }
            }
        }
        //dibujo todas las balas marcianos
        for (int i = 0; i < balasMarcianos.size(); i++) {
            balasMarcianos.get(i).dibujar(c);
        }

        //dibujo la nave y el proyectil que genera
        miNave.dibujar(c);

        //dibujo la puntuacion
        c.drawText(Integer.toString(puntuacionGlobal), anchoPantalla / 2, altoPantalla / 20, pPunutacion);
    }

    public void dibujaPausa(Canvas c) {

        //fondo
        Paint a = new Paint();
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuPausa / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuPausa / 2, a);

        //dibujo los botones reanudar y salir
        btnSalir.dibujar(c);
        btnReanudar.dibujar(c);

        //dibujo la pregunta
        a.setColor(Color.BLACK);
        a.setTextSize(altoPantalla / 20);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(txtAccion, anchoPantalla / 2, altoPantalla / 2 - altoMenuPausa / 2 + altoPantalla / 20 + margenLateralPausa, a);
    }

    public void dibujaPerdi(Canvas c) {
        //fondo
        Paint a = new Paint();
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuPausa / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuPausa / 2, a);

        //dibujo los botones si y no
        btnNo.dibujar(c);
        btnSi.dibujar(c);
        //dibujo la pregunta
        a.setColor(Color.BLACK);
        a.setTextSize(altoPantalla / 20);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(txtRepetir, anchoPantalla / 2, altoPantalla / 2 - altoMenuPausa / 2 + altoPantalla / 20 + margenLateralPausa, a);
    }

    public void dibujaPideSiglas(Canvas c) {
        //fondo
        Paint a = new Paint();
        a.setColor(Color.LTGRAY);
        a.setAlpha(125);
        c.drawRect(0, 0, anchoPantalla, altoPantalla, a);
        a.setColor(Color.WHITE);
        c.drawRect(margenLateralPausa, altoPantalla / 2 - altoMenuIniciales / 2,
                anchoPantalla - margenLateralPausa, altoPantalla / 2 + altoMenuIniciales / 2, a);

        //dibujo los botones
        btnSiglaArriba.dibujar(c);
        btnSiglaAbajo.dibujar(c);
        btnSigla2Arriba.dibujar(c);
        btnSigla2Abajo.dibujar(c);
        btnSigla3Arriba.dibujar(c);
        btnSigla3Abajo.dibujar(c);
        btnEnviar.dibujar(c);
        //dibujo las siglas
        a.setColor(Color.BLACK);
        a.setTextSize(tamañoSiglas);
        a.setTextAlign(Paint.Align.CENTER);
        c.drawText(siglas[0] + "", margenLateralPausa * 3, altoPantalla / 2 + tamañoSiglas, a);
        c.drawText(siglas[1] + "", anchoPantalla / 2, altoPantalla / 2 + altoPantalla / 12, a);
        c.drawText(siglas[2] + "", anchoPantalla - margenLateralPausa * 3, altoPantalla / 2 + tamañoSiglas, a);

        //dibujo la pregunta
        a.setTextSize(altoPantalla / 20);
        c.drawText(txtSiglas, anchoPantalla / 2,
                altoPantalla / 2 - altoMenuIniciales / 2 + altoPantalla / 20 + margenLateralPausa, a);

    }

    public boolean mejoraPuntuacion() {
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        consultaUltima = "SELECT min(puntuacion) FROM puntuaciones";
        //ejecuto la consultaUltima que me devuelve la ultima punutacion y la guardo en, ultimaPuntuacion
        c = db.rawQuery(consultaUltima, null);
        if (c.moveToFirst()) {
            do {
                ultimaPuntuacion = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        //si mi puntuacion es mayor que la ultima
        if (puntuacionGlobal > ultimaPuntuacion) {
            return true;
        }
        return false;
    }

    public void insertPuntuacion() {
        bd = new BaseDeDatos(contexto, "puntuacionesJuego", null, 1);
        db = bd.getWritableDatabase();
        //obtengo el ultimo id para el orden de antiguedad
        consultaId = "SELECT max(id )FROM puntuaciones";
        c = db.rawQuery(consultaId, null);
        if (c.moveToFirst()) {
            do {
                ultimoId = c.getInt(0);
            } while (c.moveToNext());
        }
        //ejecuto la consulta borrar
        //BORRO LA MENOR PUNTUACION MAS NUEVA, CON EL ID MAS ALTO
        db.delete("puntuaciones", "id=(SELECT id FROM puntuaciones WHERE puntuacion=" +
                "(SELECT min(puntuacion) FROM puntuaciones) ORDER BY id DESC LIMIT 1)", null);

        //ejecuto el insert
        ContentValues fila = new ContentValues();
        fila.put("siglas", Character.toString(siglas[0]) + Character.toString(siglas[1]) + Character.toString(siglas[2]));
        fila.put("id", ultimoId + 1);
        fila.put("puntuacion", puntuacionGlobal);
        db.insert("puntuaciones", null, fila);
        c.close();
        hiceInsert = true;
        perdi = true;
    }

    public char avanza(char letra) {
        pos = abecedario.indexOf(letra);
        if (pos == abecedario.size() - 1) {
            pos = 0;
        } else {
            pos++;
        }
        letra = abecedario.get(pos);
        return letra;
    }

    public char retrocede(char letra) {
        pos = abecedario.indexOf(letra);
        if (pos == 0) {
            pos = abecedario.size() - 1;
        } else {
            pos--;
        }
        letra = abecedario.get(pos);
        return letra;
    }

    public void reanudaGame() {
        //reanudo el gameplay
        estoyJugando = true;
        if (musica) {
            suenaMusica();
        }
        btnPausa.setImg(imgPausa);
        pausa = false;
    }
}