package com.example.iguau;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    private TextInputLayout signUp_TXTemail, signUp_TXTpassword;
    private EditText signUp_eTXTemail, signUp_eTXTpassword;
    private Button signUp_BTNcontinuar, signUp_BTNlogIn;

//****************************** ONCREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp_TXTemail = findViewById(R.id.signUp_TXTemail);
        signUp_eTXTemail = findViewById(R.id.signUp_eTXTemail);
        signUp_TXTpassword = findViewById(R.id.signUp_TXTpassword);
        signUp_eTXTpassword = findViewById(R.id.signUp_eTXTpassword);
        signUp_BTNcontinuar = findViewById(R.id.signUp_BTNcontinuar);
        signUp_BTNlogIn = findViewById(R.id.signUp_BTNlogIn);

        // Ocultar errores de eTXT cuando escriba algo el usuario
        OcultarErroreseTXT();

        // EVENTO BTNcontinuar
        signUp_BTNcontinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar que los eTXT estén llenos
                if (!signUp_eTXTemail.getText().toString().trim().isEmpty() && !signUp_BTNcontinuar.getText().toString().trim().isEmpty()) { // Ningún eTXT está vacío
                    String email = signUp_eTXTemail.getText().toString().trim(), password = signUp_eTXTpassword.getText().toString().trim();
                    // Validar que la contraseña tenga más de 6 caracteres
                    if (password.length() >= 6) { // Tiene más de 6 caracteres
                        // Dialog para verificar contraseña
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_password, null);
                        builder.setView(view);

                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCancelable(false);

                        // Instanciar elementos de dialog_password.xml
                        TextInputLayout dialogPass_TXTpassword = view.findViewById(R.id.dialogPass_TXTpassword);
                        EditText dialogPass_eTXTpassword = view.findViewById(R.id.dialogPass_eTXTpassword);
                        Button dialogPass_BTNverificar = view.findViewById(R.id.dialogPass_BTNverificar), dialogPass_BTNcancelar = view.findViewById(R.id.dialogPass_BTNcancelar);

                        // EVENTO del BTNverificar
                        dialogPass_BTNverificar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Verificar que dialosPass_eTXTPassword esté lleno
                                if (!dialogPass_eTXTpassword.getText().toString().trim().isEmpty()) { // Está lleno
                                    // Verificar que ambas contraseñas coincidan
                                    if (password.equals(dialogPass_eTXTpassword.getText().toString().trim())) { // SI coinciden
                                        Intent enviarDatos = new Intent(SignUp.this, SignUp2.class);
                                        enviarDatos.putExtra("email", email);
                                        enviarDatos.putExtra("password", password);
                                        startActivity(enviarDatos);
                                        finish();
                                    } else { // NO coinciden
                                        Toast.makeText(SignUp.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                                        signUp_eTXTpassword.setFocusable(true);
                                        dialog.dismiss();
                                    }
                                } else {
                                    dialogPass_TXTpassword.setError("Vuelve a escribir tu contraseña");
                                }
                                // FIN Verificar que dialosPass_eTXTPassword esté lleno
                            }
                        });
                        // FIN EVENTO del BTNverificar

                        // EVENTO del BTNcancelar
                        dialogPass_BTNcancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        // FIN EVENTO del BTNcancelar
                    } else { // Tiene menos de 6 caracteres
                        signUp_TXTpassword.setError("La contraseña debe de contener al menos 6 caracteres");
                    }
                } else { // Algún eTXT está vacío
                    if (signUp_eTXTemail.getText().toString().trim().isEmpty()) { // eTXTemail vacío
                        signUp_TXTemail.setError("Escribe tu correo electrónico");
                    }
                    if (signUp_eTXTpassword.getText().toString().trim().isEmpty()) { // eTXTpassword vacío
                        signUp_TXTpassword.setError("Ingresa tu contraseña");
                    }
                }
                // FIN Validar que los eTXT estén llenos
            }
        });
        // FIN EVENTO BTNcontinuar

        // EVENTO BTNlogIn
        signUp_BTNlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });
        // FIN EVENTO BTNlogIn
    }
//****************************** FIN ONCREATE ******************************

//****************************** ONSTART ******************************
    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Validar si ya se inició sesión o no
    }
//****************************** FIN ONSTART ******************************

//****************************** MÉTODOS ******************************
    // MÉTODO para ocultar los errores de los eTXT
    public void OcultarErroreseTXT(){
        signUp_eTXTemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp_TXTemail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp_eTXTpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp_TXTpassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    // FIN MÉTODO para ocultar los errores de los eTXT
//****************************** FIN MÉTODOS ******************************
}