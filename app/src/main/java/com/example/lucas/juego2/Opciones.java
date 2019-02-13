package com.example.lucas.juego2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Opciones extends Pantalla {
    private String selNave,opciones,txtMusica,txtSi,txtNo;
    private Bitmap n,n1,n2,imgVolver;
    private int anchoSelectNave;
    private Rect selectNave;
    private int naveSeleccionada;
    private Boton nave,nave1,nave2,back,siMusica,noMusica;
    private SharedPreferences.Editor editorPreferencias;
    public Opciones(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        //----------------ARCHIVO DE CONFIGURACION--------------
        editorPreferencias=preferencias.edit();

        //---------------------BOOLEAN MUSICA---------------------
        musica=preferencias.getBoolean("musica",true);
        //----------------NAVE SELECCIONADA---------------------
        naveSeleccionada=preferencias.getInt("idNave",0);
        //----------------BTN VOLVER--------------
        selectNave=new Rect();
        back=new Boton(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10, Color.TRANSPARENT);
        imgVolver=BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla/10, anchoPantalla/10, true);
        back.setImg(imgVolver);

        //--------------IMAGENES NAVE--------------
        n=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave);
        n1=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave1);
        n2=fondo = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nave2);

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
        //para obtener el ancho de la cadena seleccionar nave
        pTexto.getTextBounds(selNave, 0, selNave.length(), selectNave);
        //ancho seleccionar nave
        anchoSelectNave=selectNave.width();

        //----------------BOTONES SELECCIONAR NAVE---------------
        nave=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/3+altoPantalla/20,(anchoPantalla-anchoSelectNave)/2+n.getWidth(),
                altoPantalla/3+altoPantalla/20+n.getHeight(), Color.TRANSPARENT);
        nave.setImg(n);

        nave1=new Boton(anchoPantalla/2-n1.getWidth()/2,altoPantalla/3+altoPantalla/20,anchoPantalla/2+n1.getWidth()/2,
                altoPantalla/3+altoPantalla/20+n1.getHeight(), Color.TRANSPARENT);
        nave1.setImg(n1);

        nave2=new Boton(anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2,altoPantalla/3+altoPantalla/20,
                anchoPantalla-n2.getWidth()-(anchoPantalla-anchoSelectNave)/2+n2.getWidth(),altoPantalla/3+altoPantalla/20+n2.getHeight(), Color.TRANSPARENT);
        nave2.setImg(n2);

        //--------------MARCO LA NAVE SELECCIONADA--------------
        actualizaNaveSeleccionada();

        //--------------BOTONES SELECCIONAR MUSICA--------------
        //--------------MUSICA--------------
        musica=preferencias.getBoolean("musica",true);
        configuraMusica(R.raw.musica);

        siMusica=new Boton((anchoPantalla-anchoSelectNave)/2,altoPantalla/2+altoPantalla/10,
                anchoPantalla/2,altoPantalla/2+altoPantalla/10*2, Color.TRANSPARENT);
        siMusica.setTexto(txtSi,altoPantalla/15, Color.BLACK);
        noMusica=new Boton(anchoPantalla/2,altoPantalla/2+altoPantalla/10,
                anchoPantalla-(anchoPantalla-anchoSelectNave)/2,
                altoPantalla/2+altoPantalla/10*2, Color.TRANSPARENT);
        noMusica.setTexto(txtNo,altoPantalla/15, Color.BLACK);
        actualizaMusica();


    }

    @Override
    public void dibujar(Canvas c) {
        try{
            //dibujo el fondo
            c.drawColor(Color.BLACK);

            //tamaño texto opciones
            pTexto.setTextSize(altoPantalla/10);

            //dibujo el texto opciones
            c.drawText(opciones,anchoPantalla/2,altoPantalla/5,pTexto);

            //dibujo el boton para volver hacia atras
            back.dibujar(c);

            //tamaño texto seleccionar nave
            pTexto.setTextSize(altoPantalla/20);

            //dibujo el texto seleccionar nave
            c.drawText(selNave,anchoPantalla/2,altoPantalla/3,pTexto);

            //dibujo los botones de las diferentes naves
            nave.dibujar(c);
            nave1.dibujar(c);
            nave2.dibujar(c);

            //dibujo el texto musica
            c.drawText(txtMusica,anchoPantalla/2,altoPantalla/2+altoPantalla/20,pTexto);
            siMusica.dibujar(c);
            noMusica.dibujar(c);
        }catch (Exception e){

        }
    }

    @Override
    public void actualizarFisica() {
        super.actualizarFisica();
    }
    public int onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();        //Obtenemos el índice de la acción
        int pointerID = event.getPointerId(pointerIndex); //Obtenemos el Id del pointer asociado a la acción
        int accion = event.getActionMasked();             //Obtenemos el tipo de pulsación
        switch (accion) {
            case MotionEvent.ACTION_DOWN:           // Primer dedo toca
                //si pulso el si musica
                if(pulsa(siMusica.getRectangulo(),event)){
                    siMusica.setBandera(true);
                }

                //si pulso el no musica
                if(pulsa(noMusica.getRectangulo(),event)){
                    noMusica.setBandera(true);
                }
                //si pulso el btn volver
                if (pulsa(back.getRectangulo(), event)) {
                    //pongo su bandera a true
                    back.setBandera(true);
                }
                //he pulsado la primera nave
                if(pulsa(nave.getRectangulo(),event)){
                    nave.setBandera(true);
                }
                //he pulsado la segunda nave
                if(pulsa(nave1.getRectangulo(),event)){
                    nave1.setBandera(true);
                }

                //he pulsado la tercera nave
                if(pulsa(nave2.getRectangulo(),event)){
                    nave2.setBandera(true);
                }
            case MotionEvent.ACTION_POINTER_DOWN:  // Segundo y siguientes tocan
                break;

            case MotionEvent.ACTION_UP:                     // Al levantar el último dedo
                //si he pulsado si musica
                if(pulsa(siMusica.getRectangulo(),event)&&siMusica.getBandera()){
                    musica=true;
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
                //pongo las banderas de todos los botones a false
                back.setBandera(false);
                nave.setBandera(false);
                nave1.setBandera(false);
                nave2.setBandera(false);
                siMusica.setBandera(false);
                noMusica.setBandera(false);
            case MotionEvent.ACTION_POINTER_UP:  // Al levantar un dedo que no es el último

                break;

            case MotionEvent.ACTION_MOVE: // Se mueve alguno de los dedos

                break;
            default:
                Log.i("Otra acción", "Acción no definida: " + accion);
        }
        return idPantalla;
    }
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
    public void actualizaMusica(){
        if(musica){
            editorPreferencias.putBoolean("musica",true);
            editorPreferencias.commit();
            siMusica.setColor(Color.LTGRAY);
            noMusica.setColor(Color.DKGRAY);
            suenaMusica();
        }else{
            editorPreferencias.putBoolean("musica",false);
            editorPreferencias.commit();
            paraMusica();
            siMusica.setColor(Color.DKGRAY);
            noMusica.setColor(Color.LTGRAY);
        }
    }
}
