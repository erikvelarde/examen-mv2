package com.erk.examen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etNombre, etCarrera, etAlumnoId, etMateria, etNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = new DatabaseHelper(this);

        etNombre = findViewById(R.id.etNombre);
        etCarrera = findViewById(R.id.etCarrera);
        etAlumnoId = findViewById(R.id.etAlumnoId);
        etMateria = findViewById(R.id.etMateria);
        etNota = findViewById(R.id.etNota);

        findViewById(R.id.btnGuardarAlumno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAlumno();
            }
        });

        findViewById(R.id.btnGuardarNota).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNota();
            }
        });

        findViewById(R.id.btnVolver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void guardarAlumno() {
        String nombre = etNombre.getText().toString().trim();
        String carrera = etCarrera.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError(getString(R.string.err_nombre));
            etNombre.requestFocus();
            return;
        }
        if (carrera.isEmpty()) {
            etCarrera.setError(getString(R.string.err_carrera));
            etCarrera.requestFocus();
            return;
        }

        long id = db.insertarAlumno(nombre, carrera);
        if (id == -1) {
            Toast.makeText(this, R.string.err_no_existe, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, R.string.toast_alumno_guardado, Toast.LENGTH_SHORT).show();

        etAlumnoId.setText(String.valueOf(id));
        etNombre.setText("");
        etCarrera.setText("");
        etAlumnoId.requestFocus();
    }

    private void guardarNota() {
        String idTexto = etAlumnoId.getText().toString().trim();
        String materia = etMateria.getText().toString().trim();
        String notaTexto = etNota.getText().toString().trim();

        if (idTexto.isEmpty()) {
            etAlumnoId.setError(getString(R.string.err_id));
            etAlumnoId.requestFocus();
            return;
        }
        if (materia.isEmpty()) {
            etMateria.setError(getString(R.string.err_materia));
            etMateria.requestFocus();
            return;
        }
        if (notaTexto.isEmpty()) {
            etNota.setError(getString(R.string.err_nota_vacia));
            etNota.requestFocus();
            return;
        }

        double nota;
        int alumnoId;
        try {
            alumnoId = Integer.parseInt(idTexto);
            nota = Double.parseDouble(notaTexto.replace(',', '.'));
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_id, Toast.LENGTH_SHORT).show();
            return;
        }

        if (nota < 0 || nota > 100) {
            etNota.setError(getString(R.string.err_nota_rango));
            etNota.requestFocus();
            return;
        }
        if (!db.existeAlumno(alumnoId)) {
            Toast.makeText(this, R.string.err_no_existe, Toast.LENGTH_SHORT).show();
            return;
        }

        db.insertarNota(alumnoId, materia, nota);
        Toast.makeText(this, R.string.toast_nota_guardada, Toast.LENGTH_SHORT).show();

        etMateria.setText("");
        etNota.setText("");
        etMateria.requestFocus();
    }
}