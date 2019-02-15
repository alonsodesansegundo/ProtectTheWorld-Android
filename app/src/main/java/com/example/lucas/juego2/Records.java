package com.example.lucas.juego2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Records extends Pantalla {
    Bitmap imgVolver;
    String consulta;
    Boton back;
    BaseDeDatos bd;
    SQLiteDatabase db ;
    Cursor c;
    ArrayList<String>texto;
    String aux;
    int contador=0;
    int posY;
    Paint pPuntuaciones;
    String txtRecords;
    public Records(Context contexto, int idPantalla, int anchoPantalla, int altoPantalla) {
        super(contexto, idPantalla, anchoPantalla, altoPantalla);
        pPuntuaciones=new Paint();
        pPuntuaciones.setTextSize(altoPantalla/20);
        pPuntuaciones.setColor(Color.WHITE);
        pPuntuaciones.setTextAlign(Paint.Align.CENTER);
        txtRecords=contexto.getString(R.string.records);
        posY=altoPantalla/5;
        texto=new ArrayList<String>();
        back=new Boton(anchoPantalla-anchoPantalla/10,0,anchoPantalla,anchoPantalla/10, Color.TRANSPARENT);
        imgVolver= BitmapFactory.decodeResource(contexto.getResources(), R.drawable.back);
        imgVolver = Bitmap.createScaledBitmap(imgVolver, anchoPantalla/10, anchoPantalla/10, true);
        back.setImg(imgVolver);
        bd= new BaseDeDatos(contexto,"puntuacionesJuego",null,1);
    db= bd.getWritableDatabase();
    //ORDENO LAS PUNTUACIONES DE MAS A MENOS PUNTOS, Y DE MENOS ANTIGUO A MAS EN EL CASO DE COINCIDIR LA PUNTUACION
    consulta="SELECT * FROM puntuaciones ORDER BY 3 desc, 1 ";
    //ejecuto la consulta
        c = db.rawQuery(consulta, null);
        contador=0;
        if (c.moveToFirst()) {
            do {
                contador++;
                aux=contador+".- "+c.getString(1)+" "+c.getInt(2);
                texto.add(aux);
            } while(c.moveToNext());
        }
        c.close();
    }

    @Override
    public void dibujar(Canvas c) {
        try{

            c.drawColor(Color.BLACK);
            pTexto.setTextSize(altoPantalla/10);
            c.drawText(txtRecords,anchoPantalla/2,altoPantalla/8,pTexto);
            back.dibujar(c);
            for(int i=0;i<texto.size();i++){
                c.drawText(texto.get(i),anchoPantalla/2,posY,pPuntuaciones);
                //espacio entre records
                posY+=altoPantalla/15;
            }
            //vuelvo a la posY inicial
            posY=altoPantalla/5;
        }catch (Exception e){

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
                //si pulso la opcion jugar
                if (pulsa(back.getRectangulo(), event)) {
                    //vuelvo al menu
                    return 0;
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
}

