package com.erk.examen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notas_alumnos.db";
    private static final int DB_VERSION = 1;

    private static final String T_ALUMNOS = "alumnos";
    private static final String T_NOTAS = "notas";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_ALUMNOS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "carrera TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + T_NOTAS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "alumno_id INTEGER NOT NULL, "
                + "materia TEXT NOT NULL, "
                + "nota REAL NOT NULL, "
                + "FOREIGN KEY(alumno_id) REFERENCES " + T_ALUMNOS + "(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_NOTAS);
        db.execSQL("DROP TABLE IF EXISTS " + T_ALUMNOS);
        onCreate(db);
    }

    public long insertarAlumno(String nombre, String carrera) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("carrera", carrera);
        return getWritableDatabase().insert(T_ALUMNOS, null, valores);
    }

    public long insertarNota(int alumnoId, String materia, double nota) {
        ContentValues valores = new ContentValues();
        valores.put("alumno_id", alumnoId);
        valores.put("materia", materia);
        valores.put("nota", nota);
        return getWritableDatabase().insert(T_NOTAS, null, valores);
    }

    public List<Alumno> obtenerAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(T_ALUMNOS, new String[]{"id", "nombre", "carrera"},
                null, null, null, null, "id ASC");
        try {
            while (c.moveToNext()) {
                Alumno alumno = new Alumno(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("nombre")),
                        c.getString(c.getColumnIndexOrThrow("carrera")));
                alumno.setNotas(obtenerNotasPorAlumno(alumno.getId()));
                lista.add(alumno);
            }
        } finally {
            c.close();
        }
        return lista;
    }

    public List<Nota> obtenerNotasPorAlumno(int alumnoId) {
        List<Nota> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(T_NOTAS, new String[]{"id", "alumno_id", "materia", "nota"},
                "alumno_id = ?", new String[]{String.valueOf(alumnoId)}, null, null, "id ASC");
        try {
            while (c.moveToNext()) {
                lista.add(new Nota(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("alumno_id")),
                        c.getString(c.getColumnIndexOrThrow("materia")),
                        c.getDouble(c.getColumnIndexOrThrow("nota"))));
            }
        } finally {
            c.close();
        }
        return lista;
    }

    public boolean eliminarAlumno(int alumnoId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(T_NOTAS, "alumno_id = ?", new String[]{String.valueOf(alumnoId)});
        return db.delete(T_ALUMNOS, "id = ?", new String[]{String.valueOf(alumnoId)}) > 0;
    }

    public boolean eliminarNota(int notaId) {
        return getWritableDatabase().delete(T_NOTAS, "id = ?", new String[]{String.valueOf(notaId)}) > 0;
    }

    public boolean existeAlumno(int alumnoId) {
        Cursor c = getReadableDatabase().query(T_ALUMNOS, new String[]{"id"},
                "id = ?", new String[]{String.valueOf(alumnoId)}, null, null, null);
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }
}