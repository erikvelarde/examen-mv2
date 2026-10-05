package com.erk.examen;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private int id;
    private String nombre;
    private String carrera;
    private List<Nota> notas = new ArrayList<>();

    public Alumno(int id, String nombre, String carrera) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public void agregarNota(Nota nota) {
        notas.add(nota);
    }

    public boolean tieneNotas() {
        return notas != null && !notas.isEmpty();
    }
}