package com.example.lucas.juego2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Juego  extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder surfaceHolder;      // Interfaz abstracta para manejar la superficie de dibujado
    private Context context;                  // Contexto de la aplicación
    Pantalla pantallaActual;
    private int anchoPantalla=1;              // Ancho de la pantalla, su valor se actualiza en el método surfaceChanged
    private int altoPantalla=1;               // Alto de la pantalla, su valor se actualiza en el método surfaceChanged
    private Hilo hilo;                        // Hilo encargado de dibujar y actualizar la física
    private boolean funcionando = false;      // Control del hilo

    public Juego(Context context) {
        super(context);
        this.surfaceHolder = getHolder();       // Se obtiene el holder
        this.surfaceHolder.addCallback(this);   // Se indica donde van las funciones callback
        this.context = context;                 // Obtenemos el contexto
        hilo = new Hilo();                      // Inicializamos el hilo
        setFocusable(true);                     // Aseguramos que reciba eventos de toque
    }

    //GESTIONO LA PULSACIOON DE LA PANTALLA
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (surfaceHolder) {
            int codigo=  pantallaActual.onTouchEvent(event);
            //si he cambiado de escena
            if(codigo!=pantallaActual.idPantalla){
                //veo a que pantalla voy
                switch (codigo){
                    case 0:
                        pantallaActual=new Menu(context,0,anchoPantalla,altoPantalla);
                        break;
                    case 1:
                        pantallaActual=new Gameplay(context,1,anchoPantalla,altoPantalla);
                        break;
                    case 2:
                        pantallaActual=new Opciones(context,2,anchoPantalla,altoPantalla);
                        break;
                    case 3:
                        pantallaActual=new Records(context,3,anchoPantalla,altoPantalla);
                        break;
                    case 4:
                        pantallaActual=new Ayuda(context,4,anchoPantalla,altoPantalla);
                }
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        hilo.setFuncionando(false);  // Se para el hilo
        try {
            hilo.join();   // Se espera a que finalize
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        anchoPantalla = width;               // se establece el nuevo ancho de pantalla
        altoPantalla = height;               // se establece el nuevo alto de pantalla
        //creo la primera pantalla!!!
        pantallaActual=new Menu(getContext(),0,anchoPantalla,altoPantalla);
        hilo.setSurfaceSize(width,height);   // se establece el nuevo ancho y alto de pantalla en el hilo
        hilo.setFuncionando(true); // Se le indica al hilo que puede arrancar
        if (hilo.getState() == Thread.State.NEW) hilo.start(); // si el hilo no ha sido creado se crea;
        if (hilo.getState() == Thread.State.TERMINATED) {      // si el hilo ha sido finalizado se crea de nuevo;
            hilo=new Hilo();
            hilo.start(); // se arranca el hilo
        }
    }


    // Clase Hilo en la cual implementamos el método de dibujo (y física) para que se haga en paralelo con la gestión de la interfaz de usuario
    class Hilo extends Thread {
        public Hilo(){

        }

        @Override
        public void run() {
            while (funcionando) {
                Canvas c = null; //Necesario repintar todo el lienzo
                try {
                    if (!surfaceHolder.getSurface().isValid()) continue; // si la superficie no está preparada repetimos
                    c = surfaceHolder.lockCanvas(); // Obtenemos el lienzo.  La sincronización es necesaria por ser recurso común
                    synchronized (surfaceHolder) {

                        pantallaActual.actualizarFisica();  // Movimiento de los elementos
                        pantallaActual.dibujar(c);              // Dibujamos los elementos
                        //si estoy en el gameplay y he perdido
                        if(pantallaActual.idPantalla==1){

                            //perdi la partida por el motivo que sea
                            if(pantallaActual.perdi){
                                //espero 3 segundos
                                hilo.sleep(1000);
                                //vuelvo al menu
                                pantallaActual=new Menu(context,0,anchoPantalla,altoPantalla);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {  // Haya o no excepción, hay que liberar el lienzo
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        // Activa o desactiva el funcionamiento del hilo
        void setFuncionando(boolean flag) {
            funcionando = flag;
        }

        // Función es llamada si cambia el tamaño de la pantall o la orientación
        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {  // Se recomienda realizarlo de forma atómica

            }
        }
    }
}

