package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.iguau.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SignUp extends AppCompatActivity {

    private EditText signUp_eTXTnombre, signUp_eTXTapellidoP, signUp_eTXTapellidoM, signUp_eTXTedad, signUp_eTXTphone, signUp_eTXTdomicilio, signUp_eTXTcarrera, signUp_eTXTcertificacion, signUp_eTXTexperiencia, signUp_eTXTemail, signUp_eTXTpassword;
    private Spinner signUp_SpinnerTipoCuenta, signUp_SpinnerSexo;
    private LinearLayout signUp_LLdatosProfesionales, signUp_LLexperiencia;
    private Switch signUp_SWcarrera, signUp_SWcertificacion, signUp_SWexperiencia;
    private Button signUp_BTNsignUp, signUp_BTNlogIn;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email, password;

//****************************** ONCREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp_SpinnerTipoCuenta = findViewById(R.id.signUp_SpinnerTipoCuenta);
        signUp_eTXTnombre = findViewById(R.id.signUp_eTXTnombre);
        signUp_eTXTapellidoP = findViewById(R.id.signUp_eTXTapellidoP);
        signUp_eTXTapellidoM = findViewById(R.id.signUp_eTXTapellidoM);
        signUp_eTXTedad = findViewById(R.id.signUp_eTXTedad);
        signUp_SpinnerSexo = findViewById(R.id.signUp_SpinnerSexo);
        signUp_eTXTphone = findViewById(R.id.signUp_eTXTphone);
        signUp_eTXTdomicilio = findViewById(R.id.signUp_eTXTdomicilio);
        signUp_LLdatosProfesionales = findViewById(R.id.signUp_LLdatosProfesionales);
        signUp_SWcarrera = findViewById(R.id.signUp_SWcarrera);
        signUp_eTXTcarrera = findViewById(R.id.signUp_eTXTcarrera);
        signUp_SWcertificacion = findViewById(R.id.signUp_SWcertificacion);
        signUp_eTXTcertificacion = findViewById(R.id.signUp_eTXTcertificacion);
        signUp_LLexperiencia = findViewById(R.id.signUp_LLexperiencia);
        signUp_SWexperiencia = findViewById(R.id.signUp_SWexperiencia);
        signUp_eTXTexperiencia = findViewById(R.id.signUp_eTXTexperiencia);
        signUp_eTXTemail = findViewById(R.id.signUp_eTXTemail);
        signUp_eTXTpassword = findViewById(R.id.signUp_eTXTpassword);
        signUp_BTNsignUp = findViewById(R.id.signUp_BTNsignUp);
        signUp_BTNlogIn = findViewById(R.id.signUp_BTNlogIn);

        // Llenar Spinner
        AdapterSpinner();
        // Mostrar datos profesionales cuando el SpinnerTipoCuenta sea "Entrenador" o "Veterinario"
        MostrarDatosProfesionales();

        // EVENTO BTNsignUp
        signUp_BTNsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoCuenta = signUp_SpinnerTipoCuenta.getSelectedItem().toString();
                nombre = signUp_eTXTnombre.getText().toString().trim();
                apellidoP = signUp_eTXTapellidoP.getText().toString().trim();
                apellidoM = signUp_eTXTapellidoM.getText().toString().trim();
                edad = signUp_eTXTedad.getText().toString().trim();
                genero = signUp_SpinnerSexo.getSelectedItem().toString();
                phone = signUp_eTXTphone.getText().toString().trim();
                domicilio = signUp_eTXTdomicilio.getText().toString().trim();
                carrera = signUp_eTXTcarrera.getText().toString().trim();
                certificacion = signUp_eTXTcertificacion.getText().toString().trim();
                experiencia = signUp_eTXTexperiencia.getText().toString().trim();
                email = signUp_eTXTemail.getText().toString().trim();
                password = signUp_eTXTpassword.getText().toString().trim();

                if (EditTextCompletos()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view =inflater.inflate(R.layout.dialog_password, null);
                    builder.setView(view);

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(false);

                    // Instanciar elementos de dialog_password.xml
                    EditText dialogPass_eTXTpassword = view.findViewById(R.id.dialogPass_eTXTpassword);
                    Button dialogPass_BTNverificar = view.findViewById(R.id.dialogPass_BTNverificar), dialogPass_BTNcancelar = view.findViewById(R.id.dialogPass_BTNcancelar);

                    // EVENTO del BTNverificar
                    dialogPass_BTNverificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Verificar que todos los eTXT estén completos
                            if (EditTextCompletos()) { // Todos completos
                                // Verificar que dialogPass_eTXTpassword esté lleno
                                if (!dialogPass_eTXTpassword.getText().toString().trim().isEmpty()) { // Está lleno
                                    // Verificar que ambas contraseñas coincidan
                                    if (password.equals(dialogPass_eTXTpassword.getText().toString().trim())) { // SI coinciden
                                        // Crear cuenta
                                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                // Validar si se pudo crear cuenta o no
                                                if (task.isSuccessful()) { // SI se creó la cuenta
                                                    // Validar el tipo de cuenta
                                                    if (tipoCuenta.equals("Dueño de una mascota")) { // Dueño de una mascota
                                                        User myUser = new User(tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, email);
                                                        mDatabase.child("Usuarios").child("DueñoDeUnaMascota").child(mAuth.getUid()).setValue(myUser);
                                                        dialog.dismiss();
                                                        Toast.makeText(SignUp.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                                                        // TODO: Enviar a activity principal y finalizar esta
                                                    } else if (tipoCuenta.equals("Entrenador")) { // Entrenador
                                                        User myUser = new User(tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                                        mDatabase.child("Usuarios").child("Entrenador").child(mAuth.getUid()).setValue(myUser);
                                                        dialog.dismiss();
                                                        Toast.makeText(SignUp.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                                                        // TODO: Enviar a activity principal y finalizar esta
                                                    } else if (tipoCuenta.equals("Veterinario")) { // Veterinario
                                                        User myUser = new User(tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                                        mDatabase.child("Usuarios").child("Veterinario").child(mAuth.getUid()).setValue(myUser);
                                                        dialog.dismiss();
                                                        Toast.makeText(SignUp.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                                                        // TODO: Enviar a activity principal y finalizar esta
                                                    }
                                                    // FIN Validar el tipo de cuenta
                                                } else { // NO se creó la cuenta
                                                    Toast.makeText(SignUp.this, "Ocurrió un error. No se pudo crear tu cuenta.", Toast.LENGTH_SHORT).show();
                                                }
                                                // FIN Validar si se pudo crear cuenta o no
                                            }
                                        });
                                        // FIN Crear cuenta
                                    } else { // NO coinciden
                                        Toast.makeText(SignUp.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                                        signUp_eTXTpassword.setFocusable(true);
                                        dialog.dismiss();
                                    }
                                    // FIN Verificar que ambas contraseñas coincidan
                                } else { // Está vacío
                                    dialogPass_eTXTpassword.setError("Completa este campo");
                                }
                                // FIN Verificar que dialogPass_eTXTpassword esté lleno
                            }
                            // FIN Verificar que todos los eTXT estén completos
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
                }
            }
        });
        // FIN EVENTO BTNsignUp

        // EVENTO BTNlogIn
        signUp_BTNlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: crear instancia para ir a Activity logIn
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
    // MÉTODO para llenar Spinner
    public void AdapterSpinner(){
        String[] spinner_TipoCuenta = {"Dueño de una mascota", "Entrenador", "Veterinario"};
        ArrayAdapter<String> spinnerAdapter_TipoCuenta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_TipoCuenta);
        signUp_SpinnerTipoCuenta.setAdapter(spinnerAdapter_TipoCuenta);

        String[] spinner_Sexo = {"Hombre", "Mujer"};
        ArrayAdapter<String> spinnerAdapter_Sexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_Sexo);
        signUp_SpinnerSexo.setAdapter(spinnerAdapter_Sexo);
    }
    // FIN MÉTODO para llenar Spinner

    // MÉTODO para mostrar u ocultar datos profesionales dependiento del SpinnerTipoCuenta
    public void MostrarDatosProfesionales(){
        signUp_SpinnerTipoCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Dueño de una mascota
                    signUp_LLdatosProfesionales.setVisibility(View.GONE);
                    signUp_SWcarrera.setChecked(false);
                    signUp_SWcertificacion.setChecked(false);
                    signUp_SWexperiencia.setChecked(false);
                    signUp_eTXTcarrera.setVisibility(View.GONE);
                    signUp_eTXTcertificacion.setVisibility(View.GONE);
                    signUp_eTXTexperiencia.setVisibility(View.GONE);
                } else if (position == 1 || position == 2) { // Entrenador o veterinario
                    signUp_LLdatosProfesionales.setVisibility(View.VISIBLE);

                    // EVENTO para mostrar eTXTcarrera
                    signUp_SWcarrera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (signUp_SWcarrera.isChecked() || signUp_SWcertificacion.isChecked()) {
                                signUp_LLexperiencia.setVisibility(View.VISIBLE);
                            } else {
                                signUp_LLexperiencia.setVisibility(View.GONE);
                                signUp_SWexperiencia.setChecked(false);
                                signUp_eTXTexperiencia.setVisibility(View.GONE);
                                signUp_eTXTexperiencia.setText("");
                            }

                            // Mostrar u ocultar eTXTcarrera
                            if (signUp_SWcarrera.isChecked()) { // Mostrar
                                signUp_eTXTcarrera.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp_eTXTcarrera.setVisibility(View.GONE);
                                signUp_eTXTcarrera.setText("");
                            }
                            // FIN Mostrar u ocultar eTXTcarrera
                        }
                    });
                    // FIN EVENTO para mostrar eTXTcarrera

                    // EVENTO para mostrar eTXTcertificacion
                    signUp_SWcertificacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (signUp_SWcarrera.isChecked() || signUp_SWcertificacion.isChecked()) {
                                signUp_LLexperiencia.setVisibility(View.VISIBLE);
                            } else {
                                signUp_LLexperiencia.setVisibility(View.GONE);
                                signUp_SWexperiencia.setChecked(false);
                                signUp_eTXTexperiencia.setVisibility(View.GONE);
                                signUp_eTXTexperiencia.setText("");
                            }

                            // Mostrar u ocultar eTXTcertificacion
                            if (signUp_SWcertificacion.isChecked()) { // Mostrar
                                signUp_eTXTcertificacion.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp_eTXTcertificacion.setVisibility(View.GONE);
                                signUp_eTXTcertificacion.setText("");
                            }
                            // FIN Mostrar u ocultar eTXTcertificacion
                        }
                    });
                    // FIN EVENTO para mostrar eTXTcertificacion

                    // EVENTO para mostrar eTXTexperiencia
                    signUp_SWexperiencia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Mostrar u ocultar eTXTexperiencia
                            if (signUp_SWexperiencia.isChecked()) { // Mostrar
                                signUp_eTXTexperiencia.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp_eTXTexperiencia.setVisibility(View.GONE);
                                signUp_eTXTexperiencia.setText("");
                            }
                            // FIN Mostrar u ocultar eTXTexperiencia
                        }
                    });
                    // FIN EVENTO para mostrar eTXTexperiencia
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // FIN MÉTODO para mostrar u ocultar datos profesionales dependiento del SpinnerTipoCuenta

    // MÉTODO para validar que los datos personales y datos de cuenta están llenos
    public Boolean EditTextCompletos(){
        // Validar si los eTXT están completos
        if (signUp_SpinnerTipoCuenta.getSelectedItem().toString().equals("Dueño de una mascota") && !nombre.isEmpty() && !apellidoP.isEmpty() && !apellidoM.isEmpty() && !edad.isEmpty() && !phone.isEmpty() && !domicilio.isEmpty() && !email.isEmpty() && !password.isEmpty()) { // Dueño de una mascota = Datos personales y de cuenta llenos
            return true;
        } else if ((signUp_SpinnerTipoCuenta.getSelectedItem().toString().equals("Entrenador") || signUp_SpinnerTipoCuenta.getSelectedItem().toString().equals("Veterinario")) && !nombre.isEmpty() && !apellidoP.isEmpty() && !apellidoM.isEmpty() && !edad.isEmpty() && !phone.isEmpty() && !domicilio.isEmpty() && !email.isEmpty() && !password.isEmpty()) { // Entrenador o veterinario = Datos personales y de cuenta llenos
            // Validar si los Switch están activos (Y están completos los campos) o están inactivos los SW
            if (((signUp_SWcarrera.isChecked() && !carrera.isEmpty()) || !signUp_SWcarrera.isChecked()) && ((signUp_SWcertificacion.isChecked() && !certificacion.isEmpty()) || !signUp_SWcertificacion.isChecked()) && ((signUp_SWexperiencia.isChecked() && !experiencia.isEmpty()) || !signUp_SWexperiencia.isChecked())) { // Todos los SW que están activos están completos o simplemente los SW están inactivos
                return true;
            } else { // Algún SW está activo y está algún campo vacío
                // Carrera
                if (signUp_SWcarrera.isChecked() && carrera.isEmpty()) {
                    signUp_eTXTcarrera.setError("Completa este campo");
                }
                // Certificacion
                if (signUp_SWcertificacion.isChecked() && certificacion.isEmpty()) {
                    signUp_eTXTcertificacion.setError("Completa este campo");
                }
                // Experiencia
                if (signUp_SWexperiencia.isChecked() && experiencia.isEmpty()) {
                    signUp_eTXTexperiencia.setError("Completa este campo");
                }
                return false;
            }
            // FIN Validar si los Switch están activos (Y están completos los campos) o están inactivos los SW
        } else { // Algún dato personal o de cuenta está vacío
            // Datos personales
            if (nombre.isEmpty()) { // Nombre
                signUp_eTXTnombre.setError("Completa este campo");
            }
            if (apellidoP.isEmpty()) { // ApellidoP
                signUp_eTXTapellidoP.setError("Completa este campo");
            }
            if (apellidoM.isEmpty()) { // ApellidoM
                signUp_eTXTapellidoM.setError("Completa este campo");
            }
            if (edad.isEmpty()) { // Edad
                signUp_eTXTedad.setError("Completa este campo");
            }
            if (phone.isEmpty()) { // Phone
                signUp_eTXTphone.setError("Completa este campo");
            }
            if (domicilio.isEmpty()) { // Domicilio
                signUp_eTXTdomicilio.setError("Completa este campo");
            }
            // FIN Datos personales

            // Datos profesionales
            if (signUp_SWcarrera.isChecked() && carrera.isEmpty()) { // Carrera
                signUp_eTXTcarrera.setError("Completa este campo");
            }
            if (signUp_SWcertificacion.isChecked() && certificacion.isEmpty()) { // Certificación
                signUp_eTXTcertificacion.setError("Completa este campo");
            }
            if (signUp_SWexperiencia.isChecked() && experiencia.isEmpty()) { // Experiencia
                signUp_eTXTexperiencia.setError("Completa este campo");
            }
            // FIN Datos profesionales

            // Datos de cuenta
            if (email.isEmpty()) { // Email
                signUp_eTXTemail.setError("Completa este campo");
            }
            if (password.isEmpty()) { // Password
                signUp_eTXTpassword.setError("Completa este campo");
            }
            // FIN Datos de cuenta
            return false;
        }
        // FIN Validar si los eTXT están completos
    }
    // FIN MÉTODO para validar que los datos personales y datos de cuenta están llenos
//****************************** FIN MÉTODOS ******************************
}