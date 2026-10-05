package com.erk.examen;

public class Nota {
    private int id;
    private int alumnoId;
    private String materia;
    private double nota;

    public Nota(int id, int alumnoId, String materia, double nota) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.materia = materia;
        this.nota = nota;
    }

    public boolean esAprobada() {
        return nota >= 51;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}