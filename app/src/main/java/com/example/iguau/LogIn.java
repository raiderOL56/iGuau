package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

    private TextInputLayout logIn_TXTemail, logIn_TXTpassword;
    private EditText logIn_eTXTemail, logIn_eTXTpassword;
    private Button logIn_BTNlogIn, logIn_BTNsignUp;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String email, password;

//****************************** ONCREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logIn_TXTemail = findViewById(R.id.logIn_TXTemail);
        logIn_eTXTemail = findViewById(R.id.logIn_eTXTemail);
        logIn_TXTpassword = findViewById(R.id.logIn_TXTpassword);
        logIn_eTXTpassword = findViewById(R.id.logIn_eTXTpassword);
        logIn_BTNlogIn = findViewById(R.id.logIn_BTNlogIn);
        logIn_BTNsignUp = findViewById(R.id.logIn_BTNsignUp);

        // Ocultar errores de eTXT cuando escriba algo el usuario
        OcultarErroreseTXT();

        // EVENTO para iniciar sesi??n
        logIn_BTNlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar que tenga conexi??n a internet
                if (Network()) { // Si hay internet
                    email = logIn_eTXTemail.getText().toString().trim();
                    password = logIn_eTXTpassword.getText().toString().trim();

                    // Validar que los campos est??n llenos
                    if (!email.isEmpty() && !password.isEmpty()) { // Ambos campos llenos
                        // Iniciar sesi??n
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Validar si se pudo iniciar sesi??n o no
                                if (task.isSuccessful()) { // Si se pudo iniciar sesi??n
                                    // TODO: Validar qu?? tipo de cuenta es usando orderby("email).equalsTo(mAuth.getCurrentUser.getEmail)
                                    // Validar si tipoCuenta es Due??oDeUnaMascota
                                    mDatabase.child("Usuarios").child("Cliente").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) { // tipoCuenta == Cliente
//                                                tipoCuentaIntent.putExtra("TipoCuenta", "Cliente");
//                                                startActivity(tipoCuentaIntent);
                                                finish();
                                            } else { // tipoCuenta != Cliente
                                                // Validar si tipoCuenta es Entrenador
                                                mDatabase.child("Usuarios").child("Entrenador").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) { // tipoCuenta == Entrenador
                                                        if (snapshot.exists()) {
//                                                            tipoCuentaIntent.putExtra("TipoCuenta", "Entrenador");
//                                                            startActivity(tipoCuentaIntent);
                                                            finish();
                                                        } else { // tipoCuenta != Cliente &&  tipoCuenta != Entrenador
                                                            // Validar si tipoCuenta es Veterinario
                                                            mDatabase.child("Usuarios").child("Veterinario").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) { // tipoCuenta == Veterinario
                                                                    if (snapshot.exists()) {
//                                                                        tipoCuentaIntent.putExtra("TipoCuenta", "Veterinario");
//                                                                        startActivity(tipoCuentaIntent);
                                                                        finish();
                                                                    } else { // tipoCuenta != Cliente &&  tipoCuenta != Entrenador && tipoCuenta != Veterinario
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
                                } else { // No se pudo iniciar sesi??n.
                                    Toast.makeText(LogIn.this, "Correo electr??nico o contrase??a incorrectos.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        // FIN Iniciar sesi??n
                    } else { // Alg??n campo vac??o
                        if (email.isEmpty()) { // Email vac??o
                            logIn_TXTemail.setError("Escribe tu correo electr??nico");
                        }
                        if (password.isEmpty()) { // Password vac??o
                            logIn_TXTpassword.setError("Ingresa tu contrase??a");
                        }
                    }
                    // FIN Validar que los campos est??n llenos
                } else { // No hay internet
                    Toast.makeText(LogIn.this, "Por favor verifica tu conexi??n a internet", Toast.LENGTH_SHORT).show();
                }
                // FIN Validar que tenga conexi??n a internet
            }
        });
        // FIN EVENTO para iniciar sesi??n

        // EVENTO para ir a Activity SignUp
        logIn_BTNsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
        // FIN EVENTO para ir a Activity SignUp
    }
//****************************** FIN ONCREATE ******************************

//****************************** M??TODOS ******************************
    // M??TODO para verificar conexi??n a internet
    public boolean Network(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexi??n a Internet en este momento
            return true;
        } else {
            // No hay conexi??n a Internet en este momento
            return false;
        }
    }
    // FIN M??TODO para verificar conexi??n a internet

    // M??TODO para ocultar los errores de los eTXT
    public void OcultarErroreseTXT(){
        logIn_eTXTemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                logIn_TXTemail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        logIn_eTXTpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                logIn_TXTpassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    // FIN M??TODO para ocultar los errores de los eTXT
//****************************** FIN M??TODOS ******************************
}