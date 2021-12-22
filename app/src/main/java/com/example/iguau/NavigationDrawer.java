package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.iguau.fragments.fragment_Coach;
import com.example.iguau.fragments.fragment_Community;
import com.example.iguau.fragments.fragment_MyProfile;
import com.example.iguau.fragments.fragment_Store;
import com.example.iguau.fragments.fragment_Veterinarian;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // NavigationDrawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    // Elementos UI DrawerHeader
    private TextView drawerHeader_TXTvName, drawerHeader_TXTvEmail;
    private View viewHeader;

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Variables para cargar el Fragment fragment_MyProfile
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        // Variables
        String tipoCuenta = getIntent().getStringExtra("TipoCuenta");

        toolbar = findViewById(R.id.drawer_Toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        // Instancias UI DrawerHeader
        viewHeader = navigationView.getHeaderView(0);
        drawerHeader_TXTvName = viewHeader.findViewById(R.id.drawerHeader_TXTvName);
        drawerHeader_TXTvEmail = viewHeader.findViewById(R.id.drawerHeader_TXTvEmail);

        // Establecer evento onclick al NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Cargar Fragment fragment_MyProfile por defecto
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new fragment_MyProfile());
        fragmentTransaction.commit();

        // Cargar foto de perfil, nombre y email al DrawerHeader
        CargarDatosDrawerHeader();
    }

    // MÉTODO para cambiar de fragments dependiento del item seleccionado
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        // item == Mi perfil
        if (item.getItemId() == R.id.menu_MyProfile) {
            // Cargar Fragment fragment_MyProfile
            toolbar.setTitle("Mi perfil");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_MyProfile());
            fragmentTransaction.commit();
        }

        // item == Entrenador
        if (item.getItemId() == R.id.menu_Coach) {
            // Cargar Fragment fragment_coach
            toolbar.setTitle("Entrenador");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Coach());
            fragmentTransaction.commit();
        }

        // item == Veterinario
        if (item.getItemId() == R.id.menu_Veterinarian) {
            // Cargar Fragment fragment_veterinarian
            toolbar.setTitle("Veterinario");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Veterinarian());
            fragmentTransaction.commit();
        }

        // item == Mercado
        if (item.getItemId() == R.id.menu_Store) {
            // Cargar Fragment fragment_store
            toolbar.setTitle("Mercado");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Store());
            fragmentTransaction.commit();
        }

        // item == Comunidad
        if (item.getItemId() == R.id.menu_Community) {
            // Cargar Fragment fragment_community
            toolbar.setTitle("Comunidad");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Community());
            fragmentTransaction.commit();
        }

        // item == Cerrar sesión
        if (item.getItemId() == R.id.menu_LogOut) {
            // Cerrar sesión
            mAuth.signOut();
            startActivity(new Intent(NavigationDrawer.this, SignUp.class));
            finish();
        }

        return false;
    }
    // FIN MÉTODO para cambiar de fragments dependiento del item seleccionado/

    // MÉTODO para cargar foto de perfil, nombre y email al DrawerHeader
    public void CargarDatosDrawerHeader(){

    }
    // FIN MÉTODO para cargar foto de perfil, nombre y email al DrawerHeader
}