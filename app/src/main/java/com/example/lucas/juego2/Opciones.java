package com.example.lucas.juego2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Esta clase se encarga de dibujar la pantalla de opciones y gestionar su funcionalidad
 * @author Lucas Alonso de San Segundo
 */
public class Opciones extends Pantalla {
    private String selNave,opciones,txtMusica,txtSi,txtNo,txtVibracion,txtGiroscopio,txtNoSensor;
    private Bitmap n,n1,n2,imgVolver;
    private int anchoSelectNave;
    private Rect selectNave;
    private int alturaBoton,espacioTextoBoton,altoTexto;
    private int naveSeleccionada;
    private SensorManager sensorManager;
    private Sensor sensorGiroscopio;
    private Boton nave,nave1,nave2,back,siMusica,noMusica,siVibracion,noVibracion,noGiroscopio,siGiroscopio,btnAux;
    private SharedPreferences.Editor editorPreferencias;
    private boolean vibracion,giroscopio;
    private Paint pTexto;
    /**
     * Constructor de la pantalla Opciones
     * @param contexto Objeto contexto
     * @param idPantalla Entero que representa el id de esta pantalla
     * @param anchoPantalla Entero que representa el ancho de la pantalla
     * @param altoPantalla Entero que representa el alto de la pantalla
     */
    public Opciones(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        alturaBoton=altoPantalla/11;
        altoTexto=altoPantalla/15;
        espacioTextoBoton=altoPantalla/75;
        //--------------FONDO--------------
        fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.fondo2);
        fondo = Bitmap.createScaledBitmap(fondo, anchoPantalla, altoPantalla, true);

        //----------------ARCHIVO DE CONFIGURACION--------------
        editorPreferencias=preferencias.edit();

        //---------------------BOOLEAN MUSICA---------------------
        musica=preferencias.getBoolean("musica",true);
        //----------------NAVE SELECCIONADA---------------------
        naveSeleccionada=preferencias.getInt("idNave",1);
        //----------------BTN VOLVER--------------
        selectNave=new Rect();
        back=new Boton(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10, Color.TRANSPARENT);
        imgVolver=BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla/10, anchoPantalla/10, true);
        back.setImg(imgVolver);

        //--------------IMAGENES NAVE--------------
        n = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
        n1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
        n2 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);

        //--------------ESCALADO IMAGENES NAVE--------------
        n = Bitmap.createScaledBitmap(n, anchoPantalla / 8, altoPantalla / 15, true);
        n1 = Bitmap.createScaledBitmap(n1, anchoPantalla / 8, altoPantalla / 15, true);
        n2 = Bitmap.createScaledBitmap(n2, anchoPantalla / 8, altoPantalla / 15, true);

        //----------------STRINGS---------------
        opciones=contexto.getString(R.string.opciones);
        selNave=contexto.getString(R.string.escogerNave);
        txtMusica=contexto.getString(R.string.musica);
        txtSi=contexto.getString(R.string.si);
        txtNo=contexto.getString(R.string.no);
        txtVibracion=contexto.getString(R.string.vibracion);
        txtGiroscopio=contexto.getString(R.string.giroscopio);
        txtNoSensor=contexto.getString(R.string.noSensor);


        pTexto = new Paint();
        pTexto.setTypeface(getTypeFace());
        pTexto.setColor(Color.LTGRAY);
        pTexto.setTextAlign(Paint.Align.CENTER);
        pTexto.setTextSize(altoPantalla / 20);

        //para obtener el ancho de la cadena seleccionar nave
        pTexto.getTextBounds(selNave, 0, selNave.length(), selectNave);
        //ancho seleccionar nave
        anchoSelectNave=selectNave.width();

        //----------------BOTONES SELECCIONAR NAVE---------------
        nave=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/4+altoPantalla/20,(anchoPantalla-anchoSelectNave)/2+n.getWidth(),
                altoPantalla/4+altoPantalla/20+n.getHeight(), Color.TRANSPARENT);
        nave.setImg(n);

        nave1=new Boton(anchoPantalla/2-n1.getWidth()/2,altoPantalla/4+altoPantalla/20,anchoPantalla/2+n1.getWidth()/2,
                altoPantalla/4+altoPantalla/20+n1.getHeight(), Color.TRANSPARENT);
        nave1.setImg(n1);

        nave2=new Boton(anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2,altoPantalla/4+altoPantalla/20,
                anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2+n2.getWidth(),altoPantalla/4+altoPantalla/20+n2.getHeight(), Color.TRANSPARENT);
        nave2.setImg(n2);

        //--------------MARCO LA NAVE SELECCIONADA--------------
        actualizaNaveSeleccionada();

        //--------------MUSICA--------------
        musica=preferencias.getBoolean("musica",true);
        configuraMusica(R.raw.submenus);
        siMusica=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/2-altoPantalla/20+espacioTextoBoton,
                anchoPantalla/2,altoPantalla/2-altoPantalla/20+espacioTextoBoton+alturaBoton, Color.TRANSPARENT);
        siMusica.setTexto(txtSi,altoTexto, Color.BLACK,getTypeFace());
        noMusica=new Boton(anchoPantalla/2,altoPantalla/2-altoPantalla/20+espacioTextoBoton,
                anchoPantalla-(anchoPantalla-anchoSelectNave)/2,
                altoPantalla/2-altoPantalla/20+espacioTextoBoton+alturaBoton, Color.TRANSPARENT);
        noMusica.setTexto(txtNo,altoTexto, Color.BLACK,getTypeFace());
        actualizaMusica();

        //--------------BOTONES SI O NO VIBRACION--------------
        siVibracion=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/2-altoPantalla/25+altoPantalla/10+altoPantalla/15+espacioTextoBoton,
                anchoPantalla/2,altoPantalla/2-altoPantalla/25+altoPantalla/10+altoPantalla/15+espacioTextoBoton+alturaBoton,Color.RED);
        siVibracion.setTexto(txtSi,altoTexto,Color.BLACK,getTypeFace());

        noVibracion=new Boton(anchoPantalla/2,altoPantalla/2-altoPantalla/25+altoPantalla/10+altoPantalla/15+espacioTextoBoton,
                anchoPantalla-(anchoPantalla-anchoSelectNave)/2,altoPantalla/2-altoPantalla/25+altoPantalla/10+altoPantalla/15+espacioTextoBoton+alturaBoton, Color.RED);
        noVibracion.setTexto(txtNo,altoTexto,Color.BLACK,getTypeFace());

        //--------------BOOLEAN VIBRACION--------------
        vibracion=preferencias.getBoolean("vibracion",true);
        actualizaVibracion();

        //--------------BOTONES SI O NO GIROSCOPIO--------------

        siGiroscopio=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla-altoPantalla/4+espacioTextoBoton*5,
                anchoPantalla/2,altoPantalla-altoPantalla/4+espacioTextoBoton*5+alturaBoton,Color.RED);
        siGiroscopio.setTexto(txtSi,altoTexto,Color.BLACK,getTypeFace());

        noGiroscopio=new Boton(anchoPantalla/2,altoPantalla-altoPantalla/4+espacioTextoBoton*5,
                anchoPantalla-(anchoPantalla-anchoSelectNave)/2,altoPantalla-altoPantalla/4+espacioTextoBoton*5+alturaBoton, Color.RED);
        noGiroscopio.setTexto(txtNo,altoTexto,Color.BLACK,getTypeFace());


        //---------------------BOOLEAN GIROSCOPIO---------------------
        giroscopio=preferencias.getBoolean("giroscopio",false);
        actualizaGiroscopio();
    }

    /**
     * Método que se encarga de dibujar la pantalla de opciones
     * @param c Objeto Canvas para poder dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        try{
            //dibujo el fondo
            c.drawColor(Color.BLACK);
            c.drawBitmap(fondo, 0, 0, null);

            //dibujo el texto opciones
            c.drawText(opciones,anchoPantalla/2,altoPantalla/8,pTitulo);

            //dibujo el boton para volver hacia atras
            back.dibujar(c);

            //tamaño texto seleccionar nave
            pTexto.setTextSize(altoPantalla/20);

            //dibujo el texto seleccionar nave
            c.drawText(selNave,anchoPantalla/2,altoPantalla/4,pTexto);

            //dibujo los botones de las diferentes naves
            nave.dibujar(c);
            nave1.dibujar(c);
            nave2.dibujar(c);

            //dibujo el texto musica
            c.drawText(txtMusica,anchoPantalla/2,altoPantalla/2-altoPantalla/20,pTexto);
            //dibujo los botones de la musica
            siMusica.dibujar(c);
            noMusica.dibujar(c);

            //dibujo el texto vibracion
            c.drawText(txtVibracion,anchoPantalla/2,
                    altoPantalla/2-altoPantalla/25+altoPantalla/10+altoPantalla/15,pTexto);

            //dibujo los botones vibracion
            siVibracion.dibujar(c);
            noVibracion.dibujar(c);

            //dibujo el texto giroscopio
            c.drawText(txtGiroscopio,anchoPantalla/2,altoPantalla-altoPantalla/4+espacioTextoBoton*4,pTexto);
            //dibujo los botones giroscopio
            siGiroscopio.dibujar(c);
            noGiroscopio.dibujar(c);
        }catch (Exception e){

        }
    }

    /**
     * Este método se encarga de gestionar los movimientos que se producen en dicha pantalla
     * @param event Evento según el tipo de pulsación o movimiento en la pantalla
     * @return Devuelve un entero. En el caso de pulsar el boton de volver, devuelve el entero que representa la pantalla de inicio, es decir, devuelve 0. De haber pulsado cualquier otra cosa que no fuera el boton de volver, devuelve el entero de la pantalla actual.
     */
    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                //si pulso en si vibracion
                if(pulsa(siVibracion.getRectangulo(),event)){
                    siVibracion.setBandera(true);
                    btnAux = siVibracion;
                }
                //si pulso en no vibracion
                if(pulsa(noVibracion.getRectangulo(),event)){
                    noVibracion.setBandera(true);
                    btnAux = noVibracion;
                }
                //si pulso el si musica
                if(pulsa(siMusica.getRectangulo(),event)){
                    siMusica.setBandera(true);
                    btnAux=siMusica;
                }

                //si pulso el no musica
                if(pulsa(noMusica.getRectangulo(),event)){
                    noMusica.setBandera(true);
                    btnAux=noMusica;
                }
                //si pulso el btn volver
                if (pulsa(back.getRectangulo(), event)) {
                    //pongo su bandera a true
                    back.setBandera(true);
                    btnAux=back;
                }
                //he pulsado la primera nave
                if(pulsa(nave.getRectangulo(),event)){
                    nave.setBandera(true);
                    btnAux=nave;
                }
                //he pulsado la segunda nave
                if(pulsa(nave1.getRectangulo(),event)){
                    nave1.setBandera(true);
                    btnAux=nave1;
                }

                //he pulsado la tercera nave
                if(pulsa(nave2.getRectangulo(),event)){
                    nave2.setBandera(true);
                    btnAux=nave2;
                }

                //pulsacion giroscopio
                if(pulsa(siGiroscopio.getRectangulo(),event)){
                    siGiroscopio.setBandera(true);
                    btnAux=siGiroscopio;
                }
                if(pulsa(noGiroscopio.getRectangulo(),event)){
                    noGiroscopio.setBandera(true);
                    btnAux=noGiroscopio;
                }

            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si he pulsado si vibracion

                if(pulsa(siVibracion.getRectangulo(),event)&&siVibracion.getBandera()){
                    //el dispositivo vibrará cuando pulse si y previamente esté false
                    if(!vibracion){
                        vibrar();
                    }
                    vibracion=true;
                    actualizaVibracion();
                }
                //si he pulsado no vibracion
                if(pulsa(noVibracion.getRectangulo(),event)&&noVibracion.getBandera()){
                    vibracion=false;
                    actualizaVibracion();
                }
                //si he pulsado si musica
                if(pulsa(siMusica.getRectangulo(),event)&&siMusica.getBandera()){
                    musica=true;

                    Log.i("HOLA",musica+"eeeeeeeeeeeeeeeee");
                    actualizaMusica();
                }
                if(pulsa(noMusica.getRectangulo(),event)&&noMusica.getBandera()){
                    musica=false;
                    actualizaMusica();
                }
                //si pulso la opcion jugar
                if (pulsa(back.getRectangulo(), event)&&back.getBandera()) {
                    //vuelvo al menu
                    acabaMusica();
                    return 0;
                }
                //si he pulsado la primera nave
                if(pulsa(nave.getRectangulo(),event)&&nave.getBandera()){
                    editorPreferencias.putInt("idNave",0);
                    editorPreferencias.commit();
                    naveSeleccionada=0;
                }
                //he pulsado la segunda nave
                if(pulsa(nave1.getRectangulo(),event)&&nave1.getBandera()){
                    editorPreferencias.putInt("idNave",1);
                    editorPreferencias.commit();
                    naveSeleccionada=1;
                }
                //si he pulsado la tercera nave
                if(pulsa(nave2.getRectangulo(),event)&&nave2.getBandera()){
                    editorPreferencias.putInt("idNave",2);
                    editorPreferencias.commit();
                    naveSeleccionada=2;
                }
                actualizaNaveSeleccionada();

                //pulsacion botones giroscopio
                if(pulsa(siGiroscopio.getRectangulo(),event)&&siGiroscopio.getBandera()){
                    giroscopio=true;
                    actualizaGiroscopio();
                }
                if(pulsa(noGiroscopio.getRectangulo(),event)&&noGiroscopio.getBandera()){
                    giroscopio=false;
                    actualizaGiroscopio();
                }
                //pongo las bandera del btn aux a false
                if(btnAux!=null){
                    btnAux.setBandera(false);
                }
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
     * Método que se encarga de cambiar la nave seleccionada en el fichero Shared Preferences, y de indicar mediante un color gris oscuro la nave seleccionada
     */
    public void actualizaNaveSeleccionada(){
        switch (naveSeleccionada){
            case 0:
                nave.setColor(Color.LTGRAY);
                nave1.setColor(Color.TRANSPARENT);
                nave2.setColor(Color.TRANSPARENT);
                break;
            case 1:
                nave.setColor(Color.TRANSPARENT);
                nave1.setColor(Color.LTGRAY);
                nave2.setColor(Color.TRANSPARENT);
                break;
            case 2:
                nave.setColor(Color.TRANSPARENT);
                nave1.setColor(Color.TRANSPARENT);
                nave2.setColor(Color.LTGRAY);
                break;
        }
    }

    /**
     * Método que se encarga de actualizar los botones musica según en cual hayamos pulsado, de activar o parar la musica cuando hay que hacerlo, y guardar dicha seleccion el el fichero Shared Preferences
     */
    public void actualizaMusica(){
        if(musica){
            siMusica.setColor(Color.LTGRAY);
            noMusica.setColor(Color.DKGRAY);
            suenaMusica();
        }else{
            paraMusica();
            siMusica.setColor(Color.DKGRAY);
            noMusica.setColor(Color.LTGRAY);
        }
        editorPreferencias.putBoolean("musica",musica);
        editorPreferencias.commit();
    }

    /**
     * Método que se encarga de actualizar los botones vibrar según en cual hayamos pulsado y guardar dicha configuracón en el fichero Shared Preferences.
     */
    public void actualizaVibracion(){
        if(vibracion){
            siVibracion.setColor(Color.LTGRAY);
            noVibracion.setColor(Color.DKGRAY);
        }else{
            siVibracion.setColor(Color.DKGRAY);
            noVibracion.setColor(Color.LTGRAY);
        }
        editorPreferencias.putBoolean("vibracion",vibracion);
        editorPreferencias.commit();

    }

    /**
     * Método que se encarga de actualizar los botones de como jugar segun cual hayamos pulsado y guardar dicha configuración en el fichero Shared Preferences.
     */
    public void actualizaGiroscopio(){
        if(giroscopio){
            sensorManager = (SensorManager) contexto.getSystemService(Context.SENSOR_SERVICE);
            sensorGiroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            if(sensorGiroscopio==null){
                giroscopio=!giroscopio;
                Toast.makeText(contexto,txtNoSensor,Toast.LENGTH_SHORT).show();
            }
        }
        if(giroscopio){
            siGiroscopio.setColor(Color.LTGRAY);
            noGiroscopio.setColor(Color.DKGRAY);
        }else{
            siGiroscopio.setColor(Color.DKGRAY);
            noGiroscopio.setColor(Color.LTGRAY);
        }
        editorPreferencias.putBoolean("giroscopio",giroscopio);
        editorPreferencias.commit();
    }
    //------------------------VIBRACIÓN DEL DISPOSITIVO------------------------

    /**
     * Método que hace vibrar nuestro dispositivo durante 1000 milisegundos
     */
    public void vibrar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            miVibrador.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            miVibrador.vibrate(1000);
        }
    }
}