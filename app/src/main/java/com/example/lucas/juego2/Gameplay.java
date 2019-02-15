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
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class Gameplay extends Pantalla {
    //------------------------PROPIEDADES GAMEPLAY------------------------
    private boolean empece;
    private int nivel, filas, columnas, puntuacionGlobal;
    private Bitmap imgMarciano1, imgMarciano2, imgNave, proyectilMarciano, balaNave, explosion;
    private float primeraX, primeraY, tamañoPuntuacion;
    private double vMarciano;
    private Marciano marcianos[][];
    private Nave miNave;
    private boolean voyIzquierda, voyAbajo, mueveNave;
    private ArrayList misColumnas;
    private ArrayList<BalaMarciano> balasMarcianos;
    private Paint pPunutacion;
    private int tiempoVibracion;
    private Boton btnPausa, btnReanudar, btnSalir, btnMusica, btnJugar, btnSi, btnNo, btnAux;
    private Bitmap imgPausa, imgPlay, imgMusicaOn, imgMusicaOff;
    private int codNave;
    private int margenLateralPausa;
    private boolean musica;
    private int altoMenuPausa;
    private int ultimaPuntuacion;
    private SharedPreferences.Editor editorPreferencias;
    private SharedPreferences preferencias;
    private boolean vibracion;

    //para la bd
    private int ultimoId;
    private String consultaUltima, consultaId;
    private BaseDeDatos bd;
    private SQLiteDatabase db;
    private Cursor c;
    private String txtContinuar, txtSalir, txtAccion, txtEmpezar, txtSi, txtRepetir, txtNo, txtSiglas;

    //------------------------CONSTRUCTOR------------------------
    public Gameplay(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        empece = false;

        //----------------STRINGS----------------
        txtContinuar = contexto.getString(R.string.continuar);
        txtSalir = contexto.getString(R.string.salir);
        txtAccion = contexto.getString(R.string.accion);
        txtEmpezar = contexto.getString(R.string.listo);
        txtSi = contexto.getString(R.string.si);
        txtRepetir = contexto.getString(R.string.repetir);
        txtNo = contexto.getString(R.string.no);
        txtSiglas = contexto.getString(R.string.siglas);

        //-----------------MENU PAUSA----------------
        margenLateralPausa = anchoPantalla / 20;
        altoMenuPausa = altoPantalla / 4;

        //----------------ARCHIVO CONFIGURACIÓN--------------
        preferencias = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editorPreferencias = preferencias.edit();

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
        primeraY = altoPantalla / 10;
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
        vMarciano = 0.5;

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
                10, balaNave);

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
        btnJugar = new Boton(anchoPantalla / 2 - anchoPantalla / 8, altoPantalla / 2,
                anchoPantalla / 2 + anchoPantalla / 8, altoPantalla / 2 + altoPantalla / 11, Color.GREEN);
        btnJugar.setTexto(txtSi, altoPantalla / 15, Color.BLACK);

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
        //si no he pausado, el gameplay continua
        if (!pausa && empece && !perdi) {
            //------------------------DISPARO DE LA NAVE------------------------
            disparaNave();

            //------------------------DISPARO DE LOS MARCIANOS------------------------
            disparanMarcianos();


            //------------------------MOVER BALAS MARCIANOS (ARRAYLIST)------------------------
            actualizaBalasMarcianos();

            //------------------------MOVIMIENTO VERTICAL Y HORIZONTAL DE LOS MARCIANOS------------------------
            //VEO EN QUE DIRECCIÓN SE TIENEN QUE MOVER Y SI DESCIENDEN UN NIVEL O NO Y ACTUALIZO LAS BANDERAS
            actualizaBanderasMovimiento();

            //MUEVO LOS MARCIANOS SEGÚN LAS BANDERAS
            mueveMarcianos();
        }
    }

    // Rutina de dibujo en el lienzo. Se le llamará desde el hilo
    public void dibujar(Canvas c) {
        try {
            c.drawColor(Color.BLACK);
            if (empece) {
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
            } else {
                c.drawText(txtEmpezar, anchoPantalla / 2, altoPantalla / 2 - tamañoPuntuacion / 2, pPunutacion);
                btnJugar.dibujar(c);
            }
            //si he pulsado el boton de pausa
            if (pausa) {
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
            if (perdi) {
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
        }
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
                    if (marcianos[i][j].dispara()) {
                        //genero una nueva bala marciano que añado a su array
                        balasMarcianos.add(new BalaMarciano((int) marcianos[i][j].getPos().x,
                                (int) marcianos[i][j].getPos().y + marcianos[i][j].getImagen().getHeight(), marcianos[i][j].getImagen().getWidth(),
                                marcianos[i][j].getImagen().getHeight(), proyectilMarciano));
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
                //veo que la puntuación sea mayor que la ultima!!!!!!!!!!!!!!!!!!!!!!!!!!!

                acabaMusica();
                //----------------BASE DE DATOS----------------
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
                    fila.put("siglas", "BBB");
                    fila.put("id", ultimoId + 1);
                    fila.put("puntuacion", puntuacionGlobal);
                    db.insert("puntuaciones", null, fila);
                }
                c.close();
                perdi = true;
            } else {
                //si no ha chocado con la nave
                //veo si desaparece de la pantalla, si es así
                if (balasMarcianos.get(i).getContenedor().top >= altoPantalla) {
                    //la elimino para no mover balas que no se ven
                    balasMarcianos.remove(i);
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

                if (empece && !perdi) {
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
                } else {
                    //si no empece y pulso el btn si a jugar
                    if (pulsa(btnJugar.getRectangulo(), event)) {
                        btnJugar.setBandera(true);
                        btnAux = btnJugar;
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
                mueveNave = false;
                if (empece && !perdi) {
                    //si levanto el dedo en el btn musica
                    if (pulsa(btnMusica.getRectangulo(), event) && btnMusica.getBandera() &&!pausa) {
                        cambiaBtnMusica();
                    }
                    //si pulso la opcion pausa
                    if (pulsa(btnPausa.getRectangulo(), event) && btnPausa.getBandera()) {
                        //pongo la bandera del propio boton a false
                        btnPausa.setBandera(false);
                        //muestro pantallaPausa, reanudar o salir
                        pausa = !pausa;
                        if(!pausa && musica){
                            suenaMusica();
                        }

                    }
                } else {
                    if (pulsa(btnJugar.getRectangulo(), event) && btnJugar.getBandera()) {
                        empece = true;
                    }
                }
                if (pausa) {
                    paraMusica();
                    btnPausa.setImg(imgPlay);

                    //si he pulsado la opcion salir
                    if (pulsa(btnSalir.getRectangulo(), event) && btnSalir.getBandera()) {
                        //vuelvo al menu
                        acabaMusica();
                        return 0;
                    }
                    if (pulsa(btnReanudar.getRectangulo(), event) && btnReanudar.getBandera()) {
                        //reanudo el gameplay

                        if(musica){
                            suenaMusica();
                        }
                        btnPausa.setImg(imgPausa);
                        pausa = false;
                    }
                }else{
                    btnPausa.setImg(imgPausa);
                }

                if (perdi) {
                    if (pulsa(btnSi.getRectangulo(), event) && btnSi.getBandera()) {
                        return 5;
                    }
                    if (pulsa(btnNo.getRectangulo(), event) && btnNo.getBandera()) {
                        return 0;
                    }

                }
                //pongo las banderas de todos lso botones a false
/*                    btnMusica.setBandera(false);
                    btnSalir.setBandera(false);
                    btnPausa.setBandera(false);
                    btnReanudar.setBandera(false);
                btnSi.setBandera(false);
                btnNo.setBandera(false);
                btnJugar.setBandera(false);*/

//pongo la bandera del btn que anteriormente puse a true a false
                if (btnAux != null) {
                    btnAux.setBandera(false);
                }
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos
                if (!perdi && empece && !pausa && (event.getX() > miNave.getContenedor().left && event.getX() < miNave.getContenedor().right) || mueveNave) {
                    mueveNave = true;
                    miNave.moverNave(event.getX());
                }
                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }

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
}