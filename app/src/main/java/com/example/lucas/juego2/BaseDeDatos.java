package com.example.lucas.juego2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {
    String sqlCreateTable=" CREATE TABLE puntuaciones (siglas TEXT, puntuacion INTEGER)";
    public BaseDeDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //cuando instalo la app en el movil
        db.execSQL(sqlCreateTable);
        db.execSQL(
                "INSERT INTO puntuaciones (siglas, puntuacion) VALUES"+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0), "+
                        "('???',0) ");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
