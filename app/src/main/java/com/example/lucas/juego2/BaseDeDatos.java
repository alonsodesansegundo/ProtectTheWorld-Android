package com.example.lucas.juego2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase para poder realizar la conexión a la base de datos
 */
public class BaseDeDatos extends SQLiteOpenHelper {
    String sqlCreateTable=" CREATE TABLE puntuaciones ( id INTEGER,siglas TEXT, puntuacion INTEGER)";

    /**
     *
     * @param context Objeto contexto
     * @param name Nombre que tendrá la base de datos
     * @param factory Objeto SQLiteDatabase.CursorFactory
     * @param version Entero que representa la version
     */
    public BaseDeDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Este método se encarga de crear la tabla puntuaciones
     * @param db Objeto SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //cuando instalo la app en el movil
        db.execSQL(sqlCreateTable);
        db.execSQL(
                "INSERT INTO puntuaciones VALUES"+
                        "(0,'???',0), "+
                        "(1,'???',0), "+
                        "(2,'???',0), "+
                        "(3,'???',0), "+
                        "(4,'???',0), "+
                        "(5,'???',0), "+
                        "(6,'???',0), "+
                        "(7,'???',0), "+
                        "(8,'???',0), "+
                        "(9,'???',0) ");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}