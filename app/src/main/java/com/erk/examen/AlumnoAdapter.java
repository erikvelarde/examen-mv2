package com.erk.examen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {

    public interface Listener {
        void onAlumnoEliminado();

        void onNotaEliminada();
    }

    private final Context context;
    private final DatabaseHelper db;
    private final Listener listener;
    private final List<Alumno> alumnos = new ArrayList<>();

    public AlumnoAdapter(Context context, DatabaseHelper db, Listener listener) {
        this.context = context;
        this.db = db;
        this.listener = listener;
    }

    public void setAlumnos(List<Alumno> nuevos) {
        alumnos.clear();
        alumnos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        holder.bind(alumnos.get(position));
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    class AlumnoViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombre;
        private final TextView tvCarrera;
        private final TextView tvSinNotas;
        private final LinearLayout containerNotas;

        AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreAlumno);
            tvCarrera = itemView.findViewById(R.id.tvCarreraAlumno);
            tvSinNotas = itemView.findViewById(R.id.tvSinNotas);
            containerNotas = itemView.findViewById(R.id.containerNotas);
        }

        void bind(final Alumno alumno) {
            tvNombre.setText(context.getString(R.string.prefijo_alumno, alumno.getNombre()));
            tvCarrera.setText(context.getString(R.string.prefijo_carrera, alumno.getCarrera()));

            containerNotas.removeAllViews();
            tvSinNotas.setVisibility(alumno.tieneNotas() ? View.GONE : View.VISIBLE);

            for (Nota nota : alumno.getNotas()) {
                containerNotas.addView(crearFilaNota(nota));
            }

            itemView.findViewById(R.id.btnEliminarAlumno).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.eliminarAlumno(alumno.getId());
                    Toast.makeText(context, R.string.toast_alumno_eliminado, Toast.LENGTH_SHORT).show();
                    listener.onAlumnoEliminado();
                }
            });
        }

        private View crearFilaNota(final Nota nota) {
            View fila = LayoutInflater.from(context).inflate(R.layout.item_nota, containerNotas, false);

            TextView tvMateria = fila.findViewById(R.id.tvMateria);
            TextView tvNota = fila.findViewById(R.id.tvNota);
            TextView tvEstado = fila.findViewById(R.id.tvEstado);
            ImageButton btnEliminarNota = fila.findViewById(R.id.btnEliminarNota);

            tvMateria.setText(nota.getMateria());
            tvNota.setText(String.format(Locale.US, "%.1f", nota.getNota()));

            if (nota.esAprobada()) {
                tvEstado.setText(R.string.aprobado);
                tvEstado.setTextColor(ContextCompat.getColor(context, R.color.verde));
            } else {
                tvEstado.setText(R.string.reprobado);
                tvEstado.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            }

            btnEliminarNota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.eliminarNota(nota.getId());
                    Toast.makeText(context, R.string.toast_nota_eliminada, Toast.LENGTH_SHORT).show();
                    listener.onNotaEliminada();
                }
            });

            return fila;
        }
    }
}