package com.erk.examen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AlumnoAdapter.Listener {

    private DatabaseHelper db;
    private AlumnoAdapter adapter;
    private TextView tvVacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        tvVacio = findViewById(R.id.tvVacio);

        RecyclerView rvAlumnos = findViewById(R.id.rvAlumnos);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlumnoAdapter(this, db, this);
        rvAlumnos.setAdapter(adapter);

        findViewById(R.id.btnRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAlumnos();
    }

    private void cargarAlumnos() {
        List<Alumno> alumnos = db.obtenerAlumnos();
        adapter.setAlumnos(alumnos);
        tvVacio.setVisibility(alumnos.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAlumnoEliminado() {
        cargarAlumnos();
    }

    @Override
    public void onNotaEliminada() {
        cargarAlumnos();
    }
}