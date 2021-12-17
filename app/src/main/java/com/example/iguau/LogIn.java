package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private EditText logIn_eTXTemail, logIn_eTXTpassword;
    private Button lognIn_BTNlognIn, lognIn_BTNsignUp;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String email, password;

//****************************** ONCREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logIn_eTXTemail = findViewById(R.id.logIn_eTXTemail);
        logIn_eTXTpassword = findViewById(R.id.logIn_eTXTpassword);
        lognIn_BTNlognIn = findViewById(R.id.lognIn_BTNlognIn);
        lognIn_BTNsignUp = findViewById(R.id.lognIn_BTNsignUp);

        mAuth.signOut();

        lognIn_BTNlognIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar que los campos estén llenos
                if (!email.isEmpty() && !password.isEmpty()) { // Ambos campos llenos
                    email = logIn_eTXTemail.getText().toString().trim();
                    password = logIn_eTXTpassword.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Validar si se pudo iniciar sesión o no
                            if (task.isSuccessful()) { // Si se pudo iniciar sesión
                                // Validar si tipoCuenta es DueñoDeUnaMascota
                                mDatabase.child("Usuarios").child("DueñoDeUnaMascota").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) { // tipoCuenta = DueñoDeUnaMascota
                                            // TODO: Enviar a HomeDueno.class
                                        } else { // tipoCuenta != DueñoDeUnaMascota
                                            // Validar si tipoCuenta es Entrenador
                                            mDatabase.child("Usuarios").child("Entrenador").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) { // tipoCuenta = Entrenador
                                                    if (snapshot.exists()) {
                                                        // TODO: Enviar a HomeEntrenador.class
                                                    } else { // tipoCuenta != Entrenador
                                                        // Validar si tipoCuenta es Veterinario
                                                        mDatabase.child("Usuarios").child("Veterinario").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) { // tipoCuenta = Veterinario
                                                                if (snapshot.exists()) {
                                                                    // TODO: Enviar a HomeVeterinario.class

                                                                    // TODO: 2.- Crear Activity HomeDueno. Esta Activity ya está finalizada.
                                                                } else { // tipoCuenta != Veterinario
                                                                    System.out.println("Ese tipo de cuenta no existe.");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else { // No se pudo iniciar sesión.
                                Toast.makeText(LogIn.this, "Correo electrónico o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { // Algún campo vacío
                    if (email.isEmpty()) { // Email vacío
                        logIn_eTXTemail.setError("Completa este campo");
                    }
                    if (password.isEmpty()) { // Password vacío
                        logIn_eTXTpassword.setError("Completa este campo");
                    }
                }
                // FIN Validar que los campos estén llenos
            }
        });

        lognIn_BTNsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
    }
//****************************** FIN ONCREATE ******************************

//****************************** MÉTODOS ******************************

//****************************** FIN MÉTODOS ******************************
}