package com.example.iguau.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iguau.R;
import com.example.iguau.adaptadores.AdapterEmpleado;
import com.example.iguau.model.User;

import java.util.ArrayList;

public class fragment_Coach extends Fragment {
    //
    AdapterEmpleado adapterEmpleado;
    RecyclerView recyclerViewEmpleado;
    ArrayList<User> userArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        recyclerViewEmpleado = view.findViewById(R.id.coach_RV);
        userArrayList = new ArrayList<>();

        // Cargar la lista
        CargarLista();
        // Mostrar datos
        MostrarLista();
        return view;
    }

    public void CargarLista(){
        // TODO: 1.2.- Traer datos de Firebase para agregarlos al userArrarList
        userArrayList.add(new User("Entrenador", "", "name_C1", "apellidoP_C1", "apellidoM_C1", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
        userArrayList.add(new User("Entrenador", "", "name_C2", "apellidoP_C2", "apellidoM_C2", "15", "masculino", "5532659865", "123,123,123", "carrera", "certificacion", "2", "coach@pruebas.com"));
    }

    public void MostrarLista(){
        // Como es fragment, se pone getContext(), si fuera Activity, se pone this
        recyclerViewEmpleado.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterEmpleado = new AdapterEmpleado(getContext(), userArrayList);
        recyclerViewEmpleado.setAdapter(adapterEmpleado);

        // Evento para cuando toca un empleado
        adapterEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = userArrayList.get(recyclerViewEmpleado.getChildAdapterPosition(v)).getNombre();
                System.out.println("NOMBRE: " + nombre);
            }
        });
    }
}
