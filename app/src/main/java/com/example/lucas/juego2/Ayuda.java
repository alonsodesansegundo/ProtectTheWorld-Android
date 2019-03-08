package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Esta clase se encarga de dibujar la pantalla de ayuda y gestionar su funcionalidad
 *
 * @author Lucas Alonso de San Segundo
 */
public class Ayuda extends Pantalla {
    /**
     * Bitmap (imagen) para el boton de volver al menu principal
     */
    private Bitmap imgVolver;

    /**
     * Objeto Boton para volver al menu principal
     */
    private Boton back;

    /**
     * Bitmap (imagen) del marciano que muere tras recibir un impacto
     */
    private Bitmap m1;

    /**
     * Bitmap (imagen) del marciano que muere tras recibir dos impactos
     */
    private Bitmap m2;

    /**
     * Float que nos servirá para situar los botones respecto al eje Y
     */
    private float primeraY;

    /**
     * Float que nos servirá para situar los botones respecto al eje X
     */
    private float posX;

    /**
     * Array de palabras que aparecerán en el caso de pulsar el boton Finalidad
     */
    private String[] finalidad;

    /**
     * Array de palabras que aparecerán en el caso de pulsar el boton Nave
     */
    private String[] infoNave;

    /**
     * Array de palabras que aparecerán en el caso de pulsar el boton Niveles
     */
    private String[] infoNiveles;

    /**
     * Array de palabras que aparecerán en el caso de pulsar el boton Marcianos
     */
    private String[] infoMarcianos;

    /**
     * Cadena que representará el texto del menu  Ayuda
     */
    private String txtAyuda;

    /**
     * Cadena que representará el texto del menu Finalidad
     */
    private String txtFinalidad;

    /**
     * Cadena que representará el texto puntuacion de los marcianos de un impacto
     */
    private String txtP;

    /**
     * Cadena que representará el texto puntuacion de los marcianos de dos impactos
     */
    private String txtP2;

    /**
     * Cadena que representará la palabra Nave
     */
    private String txtNave;

    /**
     * Cadena que representará el texto del menu  Niveles
     */
    private String txtNiveles;

    /**
     * Cadena que representará el texto del menu Marcianos
     */
    private String txtMarcianos;

    /**
     * Cadena que representará la palabra Finalidad
     */
    private String tFin;

    /**
     * Cadena que representará la palabra Nave
     */
    private String tNave;

    /**
     * Cadena que representará la palabra Niveles
     */
    private String tNiveles;

    /**
     * Cadena que representará la palabra Marcianos
     */
    private String tMarcianos;

    /**
     * Cadena que representará el texto 1 impacto
     */
    private String impacto1;

    /**
     * Cadena que representará el texto dos impactos
     */
    private String impacto2;

    /**
     * Objeto Paint para dibujar el texto
     */
    private Paint pTexto;

    /**
     * Objeto Paint para dibujar las cadenas impactos de los marcianos
     */
    private Paint pImpactos;

    /**
     * Objeto boton para acceder a la información sobre la finalidad del juego
     */
    private Boton btnFinalidad;

    /**
     * Objeto boton para acceder a la información sobre los niveles del juego
     */
    private Boton btnNiveles;

    /**
     * Objeto boton para acceder a la información sobre la nave del juego
     */
    private Boton btnNave;

    /**
     * Objeto boton para acceder a la información sobre los marcianos del juego
     */
    private Boton btnMarcianos;

    /**
     * Cadena auxiliar que nos servirá para conocer que tenemos que dibujar
     */
    private String modo;

    /**
     * Entero que nos indica la posicion en el eje x que empieza el boton
     */
    private int izqBoton;

    /**
     * Entero que nos indica la posicion en el eje x que acaba el boton
     */
    private int drchBoton;

    /**
     * Entero que nos indica la altura que tendrán los botones (eje Y)
     */
    private int altoBoton;

    /**
     * Constructor de la clase Ayuda
     *
     * @param contexto      Objeto contexto
     * @param idPantalla    Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla  Entero que representa el alto de la pantalla
     */
    public Ayuda(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        pImpactos = new Paint();
        pImpactos.setColor(Color.WHITE);
        pImpactos.setTypeface(getTypeFace());
        pImpactos.setTextSize(altoPantalla / 30);
        pImpactos.setTextAlign(Paint.Align.CENTER);
        m1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio1);
        m1 = Bitmap.createScaledBitmap(m1, anchoPantalla / 10, altoPantalla / 15, true);


        m2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.mio2);
        m2 = Bitmap.createScaledBitmap(m2, anchoPantalla / 10, altoPantalla / 15, true);

        modo = "principal";
        izqBoton = anchoPantalla / 2 - anchoPantalla / 4;
        drchBoton = anchoPantalla / 2 + anchoPantalla / 4;
        primeraY = altoPantalla / 7;
        altoBoton = altoPantalla / 10;
        //--------------STRINGS--------------
        txtAyuda = contexto.getString(R.string.ayuda);
        txtFinalidad = contexto.getString(R.string.finalidad);
        txtNave = contexto.getString(R.string.infoNave);
        txtNiveles = contexto.getString(R.string.infoNiveles);
        txtMarcianos = contexto.getString(R.string.infoMarcianos);
        tFin = contexto.getString(R.string.btnFinalidad);
        tNave = contexto.getString(R.string.btnNave);
        tNiveles = contexto.getString(R.string.btnNiveles);
        tMarcianos = contexto.getString(R.string.btnMarcianos);
        impacto1 = contexto.getString(R.string.oneD);
        impacto2 = contexto.getString(R.string.twoD);
        txtP = contexto.getString(R.string.points);
        txtP2 = contexto.getString(R.string.points2);
        finalidad = txtFinalidad.split(" ");
        infoNave = txtNave.split(" ");
        infoNiveles = txtNiveles.split(" ");
        infoMarcianos = txtMarcianos.split(" ");
        primeraY = altoPantalla / 5;
        posX = 0;
        //-----------FONDO----------
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


        //BOTONES PARA LA INFO
        btnFinalidad = new Boton(izqBoton, altoPantalla / 4, drchBoton, altoPantalla / 4 + altoBoton, Color.LTGRAY);
        btnFinalidad.setTexto(tFin, altoBoton / 2, Color.BLACK, getTypeFace());

        btnNiveles = new Boton(izqBoton,
                altoPantalla / 4 + altoBoton + altoPantalla / 100,
                drchBoton,
                altoPantalla / 4 + altoBoton * 2 + altoPantalla / 100,
                Color.LTGRAY);
        btnNiveles.setTexto(tNiveles, altoBoton / 2, Color.BLACK, getTypeFace());

        btnNave = new Boton(izqBoton,
                altoPantalla / 4 + altoBoton * 2 + altoPantalla / 100 * 2,
                drchBoton,
                altoPantalla / 4 + altoBoton * 3 + altoPantalla / 100,
                Color.LTGRAY);
        btnNave.setTexto(tNave, altoBoton / 2, Color.BLACK, getTypeFace());

        btnMarcianos = new Boton(izqBoton,
                altoPantalla / 4 + altoBoton * 3 + altoPantalla / 100 * 2,
                drchBoton,
                altoPantalla / 4 + altoBoton * 4 + altoPantalla / 100,
                Color.LTGRAY);
        btnMarcianos.setTexto(tMarcianos, altoBoton / 2, Color.BLACK, getTypeFace());
        pTexto = new Paint();
        pTexto.setTypeface(getTypeFace());
        pTexto.setColor(Color.WHITE);
        pTexto.setTextSize(altoPantalla / 30);

    }

    /**
     * Este metodo se encarga de dibujar la información perteneciente a la nave
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujarInformacionNave(Canvas c) {
        dibujaTexto(c, infoNave);

    }

    /**
     * Este metodo se encarga de dibujar una lista de palabras de manera que entren en la pantalla
     * @param c     Objeto canvas para poder dibujar
     * @param texto Array de palabras que tendrá que dibujar de manera que todas entren en la pantalla
     */
    public void dibujaTexto(Canvas c, String[] texto) {
        Rect aux = new Rect();
        posX = anchoPantalla / 20;
        for (String s :
                texto) {

            //VAMOS A ESCRIBIR BIEN
            pTexto.getTextBounds(s, 0, s.length(), aux);
            posX += aux.width() + anchoPantalla / 20;
            //SI AQUI NO ENTRA LA PALABRA, PASO A LA SIGUEINTE LINEA
            if (posX >= anchoPantalla) {
                posX = anchoPantalla / 20;
                primeraY += altoPantalla / 20;
                c.drawText(s, posX, primeraY, pTexto);

            } else {
                posX = posX - aux.width() - anchoPantalla / 20;
                c.drawText(s, posX, primeraY, pTexto);
            }

            posX += aux.width() + anchoPantalla / 20;
        }

        primeraY = altoPantalla / 5;
    }

    /**
     * Este método se encarga de dibujar la información perteneciente a la finalidad del juego
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujarFinalidad(Canvas c) {
        dibujaTexto(c, finalidad);

    }

    /**
     * Este método se encarga de dibujar la información perteneciente a los niveles del juego
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaNiveles(Canvas c) {
        dibujaTexto(c, infoNiveles);
    }

    /**
     * Este método se encarga de dibujar la información perteneciente a los marcianos, junto a las imagenes de los dos tipos de marcianos
     * @param c Objeto canvas para poder dibujar
     */
    public void dibujaMarcianos(Canvas c) {
        dibujaTexto(c, infoMarcianos);

        c.drawBitmap(m1, anchoPantalla / 20 + m1.getWidth(), altoPantalla - altoPantalla / 4, null);
        c.drawBitmap(m2, anchoPantalla - m2.getWidth() * 2 - anchoPantalla / 20, altoPantalla - altoPantalla / 4, null);
        c.drawText(impacto1, anchoPantalla / 20 + m1.getWidth() + m1.getWidth() / 2, altoPantalla - altoPantalla / 4 + m1.getHeight() * 2, pImpactos);
        c.drawText(txtP, anchoPantalla / 20 + m1.getWidth() + m1.getWidth() / 2, altoPantalla - altoPantalla / 4 + m1.getHeight() * 3, pImpactos);

        c.drawText(impacto2, anchoPantalla - anchoPantalla / 20 - m2.getWidth() * 2 + m2.getWidth() / 2, altoPantalla - altoPantalla / 4 + m1.getHeight() * 2, pImpactos);
        c.drawText(txtP2, anchoPantalla - anchoPantalla / 20 - m2.getWidth() * 2 + m2.getWidth() / 2, altoPantalla - altoPantalla / 4 + m1.getHeight() * 3, pImpactos);

    }

    /**
     * Esté método se encarga de dibujar el fondo, el titulo. Según el modo en el que esté dibujo unas cosas u otras. En el caso de estar en modo "principal" dibujo los 4 botones, a partir de ahí, en el caso de haber pulsado alguno de estos cuatro botones, dibujo la información perteneciente a dicho boton
     * @param c Objeto canvas para poder dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        try {
            //fondo
            c.drawBitmap(fondo, 0, 0, null);

            //titulo
            c.drawText(txtAyuda, anchoPantalla / 2, altoPantalla / 8, pTitulo);
            switch (modo) {
                case "principal":
                    btnFinalidad.dibujar(c);
                    btnNiveles.dibujar(c);
                    btnNave.dibujar(c);
                    btnMarcianos.dibujar(c);
                    break;
                case "finalidad":
                    dibujarFinalidad(c);
                    break;
                case "nave":
                    dibujarInformacionNave(c);
                    break;
                case "niveles":
                    dibujaNiveles(c);
                    break;
                case "marcianos":
                    dibujaMarcianos(c);
                    break;
            }
            //boton volver
            back.dibujar(c);
        } catch (Exception e) {

        }
    }

    /**
     * Este método se encarga de gestionar los movimientos que se producen en dicha pantalla
     *
     * @param event Evento según el tipo de pulsación o movimiento en la pantalla
     * @return Devuelve un entero. En el caso de pulsar el boton de volver, devuelve el entero que representa la pantalla de inicio, es decir, devuelve 0. De haber pulsado cualquier otra cosa que no fuera el boton de volver, devuelve el entero de la pantalla actual.
     */
    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                if (pulsa(back.getRectangulo(), event)) {
                    back.setBandera(true);
                }
                if (modo.equals("principal")) {
                    if (pulsa(btnFinalidad.getRectangulo(), event)) {
                        btnFinalidad.setBandera(true);
                    }

                    if (pulsa(btnNave.getRectangulo(), event)) {
                        btnNave.setBandera(true);
                    }
                    if (pulsa(btnMarcianos.getRectangulo(), event)) {
                        btnMarcianos.setBandera(true);
                    }
                    if (pulsa(btnNiveles.getRectangulo(), event)) {
                        btnNiveles.setBandera(true);
                    }
                }

            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si pulso la opcion jugar
                if (pulsa(back.getRectangulo(), event) && back.getBandera() && modo.equals("principal")) {
                    acabaMusica();
                    return 0;
                }
                if (pulsa(back.getRectangulo(), event) && back.getBandera()) {
                    modo = "principal";
                }
                if (pulsa(btnFinalidad.getRectangulo(), event) && btnFinalidad.getBandera()) {
                    modo = "finalidad";
                }

                if (pulsa(btnNiveles.getRectangulo(), event) && btnNiveles.getBandera()) {
                    modo = "niveles";
                }
                if (pulsa(btnMarcianos.getRectangulo(), event) && btnMarcianos.getBandera()) {
                    modo = "marcianos";
                }
                if (pulsa(btnNave.getRectangulo(), event) && btnNave.getBandera()) {
                    modo = "nave";
                }
                btnNave.setBandera(false);
                btnMarcianos.setBandera(false);
                btnNiveles.setBandera(false);
                btnFinalidad.setBandera(false);
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

