package com.example.mislibrosprestados.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class SqlIO extends SQLiteOpenHelper {
    private static final String DB_NOMBRE = "libros_prestados";
    private static final int DB_VERSION = 1;

    public static final String TABLA_LIBROS = "libros";
    public static final String CAMPO_LIBROS_ID = "_id";
    public static final String CAMPO_LIBROS_TITULO = "titulo";
    public static final String CAMPO_LIBROS_AUTOR = "autor";
    public static final String CAMPO_LIBROS_FECHA_PRESTAMO = "fecha_prestamo";
    public static final String CAMPO_LIBROS_DIAS_PRESTAMO = "dias_prestamo";

    public SqlIO(Context cntxt)
    {
        super( cntxt, DB_NOMBRE, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            Log.i( DB_NOMBRE, "Creando tablas" );
            db.beginTransaction();

            db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLA_LIBROS
                    + "("
                        + CAMPO_LIBROS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CAMPO_LIBROS_TITULO + " TEXT NOT NULL,"
                        + CAMPO_LIBROS_AUTOR + " TEXT,"
                        + CAMPO_LIBROS_FECHA_PRESTAMO + " INTEGER NOT NULL,"
                        + CAMPO_LIBROS_DIAS_PRESTAMO + " INTEGER NOT NULL"
                    + ")"
            );

            db.setTransactionSuccessful();
        } catch(SQLException error)
        {
            Log.e( DB_NOMBRE, error.getMessage() );
        } finally {
            db.endTransaction();
        }

        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try {
            Log.i( DB_NOMBRE, "Actualizando base de datos" );
            db.beginTransaction();

            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_LIBROS );

            db.setTransactionSuccessful();
        } catch(SQLException error) {
            Log.e( DB_NOMBRE, error.getMessage() );
        } finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }

    public Cursor getCursorTodosLibros()
    {
        final SQLiteDatabase DB = this.getReadableDatabase();

        return DB.query(
                TABLA_LIBROS,
                null,
                null,
                null,
                null,
                null,
                CAMPO_LIBROS_FECHA_PRESTAMO
        );
    }

    public void inserta(String titulo)
    {
        final SQLiteDatabase DB = this.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put( CAMPO_LIBROS_TITULO, titulo );
        VALORES.putNull( CAMPO_LIBROS_AUTOR );
        VALORES.put( CAMPO_LIBROS_DIAS_PRESTAMO, 15 );
        VALORES.put( CAMPO_LIBROS_FECHA_PRESTAMO, Calendar.getInstance().getTimeInMillis() );

        try {
            DB.beginTransaction();

            DB.insert(
                    TABLA_LIBROS,
                    null,
                    VALORES
            );

            DB.setTransactionSuccessful();
        } catch(SQLException error)
        {
            Log.e( DB_NOMBRE, error.getMessage() );
        } finally {
            DB.endTransaction();
        }
    }
}
