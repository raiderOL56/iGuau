package com.example.iguau.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iguau.R;
import com.example.iguau.model.User;

import java.util.ArrayList;

public class AdapterEmpleado extends RecyclerView.Adapter<AdapterEmpleado.ViewHolder> implements View.OnClickListener {
    LayoutInflater inflater;
    ArrayList<User> model;

    // Listener
    private View.OnClickListener listener;

    public AdapterEmpleado(Context context, ArrayList<User> model){
        this.inflater = LayoutInflater.from(context);
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.empleado_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener los datos y guardarlos en una variable
        String nombre = model.get(position).getNombre();

        holder.empleadoList_name.setText(nombre);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    // MÉTODO OnClick
    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    // Clase ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Intancia UI
        TextView empleadoList_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empleadoList_name = itemView.findViewById(R.id.empleadoList_name);
        }
    }
}
