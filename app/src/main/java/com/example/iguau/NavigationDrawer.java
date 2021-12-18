package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.iguau.fragments.fragment_Coach;
import com.example.iguau.fragments.fragment_Community;
import com.example.iguau.fragments.fragment_MyProfile;
import com.example.iguau.fragments.fragment_Store;
import com.example.iguau.fragments.fragment_Veterinarian;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    // Variables para cargar el Fragment fragment_MyProfile
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        toolbar = findViewById(R.id.drawer_Toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        // Establecer evento onclick al NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Cargar Fragment fragment_MyProfile
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new fragment_MyProfile());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        // Programar evento onclick
        if (item.getItemId() == R.id.menu_MyProfile) {
            // Cargar Fragment fragment_MyProfile
            toolbar.setTitle("Mi perfil");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_MyProfile());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.menu_Coach) {
            // Cargar Fragment fragment_coach
            toolbar.setTitle("Entrenador");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Coach());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.menu_Veterinarian) {
            // Cargar Fragment fragment_veterinarian
            toolbar.setTitle("Veterinario");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Veterinarian());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.menu_Store) {
            // Cargar Fragment fragment_store
            toolbar.setTitle("Mercado");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Store());
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.menu_Community) {
            // Cargar Fragment fragment_community
            toolbar.setTitle("Comunidad");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new fragment_Community());
            fragmentTransaction.commit();
        }

        return false;
    }
}