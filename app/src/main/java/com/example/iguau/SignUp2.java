package com.example.iguau;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.iguau.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp2 extends AppCompatActivity {

    private CircleImageView signUp2_IVphoto;
    private ImageView signUp2_IVaddPhoto;
    private TextInputLayout signUp2_TXTnombre, signUp2_TXTapellidoP, signUp2_TXTapellidoM, signUp2_TXTedad, signUp2_TXTphone, signUp2_TXTdomicilio, signUp2_TXTcarrera, signUp2_TXTcertificacion, signUp2_TXTexperiencia;
    private EditText signUp2_eTXTnombre, signUp2_eTXTapellidoP, signUp2_eTXTapellidoM, signUp2_eTXTedad, signUp2_eTXTphone, signUp2_eTXTdomicilio, signUp2_eTXTcarrera, signUp2_eTXTcertificacion, signUp2_eTXTexperiencia;
    private Spinner signUp2_SpinnerTipoCuenta, signUp2_SpinnerSexo;
    private LinearLayout signUp2_LLdatosProfesionales, signUp2_LLexperiencia;
    private Switch signUp2_SWcarrera, signUp2_SWcertificacion, signUp2_SWexperiencia;
    private Button signUp2_BTNsignUp;

    // Variables Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorage = storage.getReference();

    // Dialog
    private ProgressDialog cargandoDialog;

    // Variables
    private String tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email, password;
    private static final int PERMISSION_FILE = 23;
    private static final int ACCESS_FILE = 43;
    private Uri resultUri;

//****************************** ONCREATE ******************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        Init();

        Network();

        // Ocultar errores de eTXT cuando escriba algo el usuario
        OcultarErroreseTXT();

        // Llenar Spinner
        AdapterSpinner();
        // Mostrar datos profesionales cuando el SpinnerTipoCuenta sea "Entrenador" o "Veterinario"
        MostrarDatosProfesionales();

        // EVENTO (IVaddPhoto) para agregar una foto de perfil
        signUp2_IVaddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Solicitar permisos para acceder a la galer??a
                if (ContextCompat.checkSelfPermission(SignUp2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // Denegado
                    ActivityCompat.requestPermissions(SignUp2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
                } else { // Aceptado
                    // Abrir men?? para seleccionar imagen
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Selecciona una opci??n"), ACCESS_FILE);
                }
            }
        });

        // EVENTO (BTNsignUp) para registrar la cuenta
        signUp2_BTNsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar que tiene conexi??n a internet
                if (Network()) { // Si hay internet
                    // Recibir par??metros de SignUp
                    email = getIntent().getStringExtra("email");
                    password = getIntent().getStringExtra("password");

                    // Guardar eTXT en variables
                    if (signUp2_SpinnerTipoCuenta.getSelectedItem().toString().equals("Due??o de una mascota")) {
                        tipoCuenta = "Cliente";
                    } else {
                        tipoCuenta = signUp2_SpinnerTipoCuenta.getSelectedItem().toString();
                    }
                    nombre = signUp2_eTXTnombre.getText().toString().trim();
                    apellidoP = signUp2_eTXTapellidoP.getText().toString().trim();
                    apellidoM = signUp2_eTXTapellidoM.getText().toString().trim();
                    edad = signUp2_eTXTedad.getText().toString().trim();
                    genero = signUp2_SpinnerSexo.getSelectedItem().toString();
                    phone = signUp2_eTXTphone.getText().toString().trim();
                    domicilio = signUp2_eTXTdomicilio.getText().toString().trim();
                    carrera = signUp2_eTXTcarrera.getText().toString().trim();
                    certificacion = signUp2_eTXTcertificacion.getText().toString().trim();
                    experiencia = signUp2_eTXTexperiencia.getText().toString().trim();

                    // Verificar que eTXT est??n llenos
                    if (EditTextCompletos()) { // Est??n llenos
                        // Crear cuenta
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Validar si se pudo crear cuenta o no
                                if (task.isSuccessful()) { // SI se cre?? la cuenta
                                    // Crear Intent para enviar el tipo de cuenta a NavigationDrawer
                                    Intent tipoCuentaIntent = new Intent(SignUp2.this, NavigationDrawer.class);

                                    // Validar el tipo de cuenta
                                    if (tipoCuenta.equals("Cliente")) { // Cliente
                                        // Enviar TipoDecuenta a NavigationDrawer
                                        tipoCuentaIntent.putExtra("TipoCuenta", tipoCuenta);

                                        // Validar si el usuario subi?? una foto de perfil o no
                                        if (resultUri != null) { // Si subi?? una foto de perfil
                                            // Crear direcci??n en donde se va a guardar la foto
                                            StorageReference filePath = mStorage.child("photoProfile").child(mAuth.getUid()).child(resultUri.getLastPathSegment());

                                            // Subir foto a Firebase Storage
                                            filePath.putFile(resultUri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                                // Obtener link de descarga y guardarlo en la variable linkPhoto
                                                String linkPhoto = String.valueOf(uri);

                                                // Crear cuenta
                                                User myUser = new User(tipoCuenta, linkPhoto, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, email);
                                                mDatabase.child("Usuarios").child("Cliente").child(mAuth.getUid()).setValue(myUser);
                                            })).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Ocurri?? un error:" + e + " \n LA IMAGEN NO SE SUBI??");
                                                }
                                            });
                                        } else { // No subi?? foto de perfil
                                            // Crear cuenta
                                            User myUser = new User(tipoCuenta, "", nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, email);
                                            mDatabase.child("Usuarios").child("Cliente").child(mAuth.getUid()).setValue(myUser);
                                        }
                                        Toast.makeText(SignUp2.this, "Cuenta creada con ??xito", Toast.LENGTH_SHORT).show();
                                    } else if (tipoCuenta.equals("Entrenador")) { // Entrenador
                                        // Enviar TipoDecuenta a NavigationDrawer
                                        tipoCuentaIntent.putExtra("TipoCuenta", tipoCuenta);
                                        // Validar si el usuario subi?? una foto de perfil o no
                                        if (resultUri != null) { // Si subi?? una foto de perfil
                                            // Crear direcci??n en donde se va a guardar la foto
                                            StorageReference filePath = mStorage.child("photoProfile").child(mAuth.getUid()).child(resultUri.getLastPathSegment());

                                            // Subir foto a Firebase Storage
                                            filePath.putFile(resultUri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                                // Obtener link de descarga y guardarlo en la variable linkPhoto
                                                String linkPhoto = String.valueOf(uri);

                                                // Crear cuenta
                                                User myUser = new User(tipoCuenta, linkPhoto, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                                mDatabase.child("Usuarios").child("Entrenador").child(mAuth.getUid()).setValue(myUser);
                                            })).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Ocurri?? un error:" + e + " \n LA IMAGEN NO SE SUBI??");
                                                }
                                            });
                                        } else { // No subi?? foto de perfil
                                            // Crear cuenta
                                            User myUser = new User(tipoCuenta, "", nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                            mDatabase.child("Usuarios").child("Entrenador").child(mAuth.getUid()).setValue(myUser);
                                        }
                                        Toast.makeText(SignUp2.this, "Cuenta creada con ??xito", Toast.LENGTH_SHORT).show();
                                    } else if (tipoCuenta.equals("Veterinario")) { // Veterinario
                                        // Enviar TipoDecuenta a NavigationDrawer
                                        tipoCuentaIntent.putExtra("TipoCuenta", tipoCuenta);
                                        // Validar si el usuario subi?? una foto de perfil o no
                                        if (resultUri != null) { // Si subi?? una foto de perfil
                                            // Crear direcci??n en donde se va a guardar la foto
                                            StorageReference filePath = mStorage.child("photoProfile").child(mAuth.getUid()).child(resultUri.getLastPathSegment());

                                            // Subir foto a Firebase Storage
                                            filePath.putFile(resultUri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                                                // Obtener link de descarga y guardarlo en la variable linkPhoto
                                                String linkPhoto = String.valueOf(uri);

                                                // Crear cuenta
                                                User myUser = new User(tipoCuenta, linkPhoto, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                                mDatabase.child("Usuarios").child("Veterinario").child(mAuth.getUid()).setValue(myUser);
                                            })).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Ocurri?? un error:" + e + " \n LA IMAGEN NO SE SUBI??");
                                                }
                                            });
                                        } else {
                                            // Crear cuenta
                                            User myUser = new User(tipoCuenta, "", nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email);
                                            mDatabase.child("Usuarios").child("Veterinario").child(mAuth.getUid()).setValue(myUser);
                                        }
                                        Toast.makeText(SignUp2.this, "Cuenta creada con ??xito", Toast.LENGTH_SHORT).show();
                                    }
                                    // FIN Validar el tipo de cuenta
                                    startActivity(tipoCuentaIntent);
                                    finish();
                                } else { // NO se cre?? la cuenta
                                    System.out.println(task.getException());
                                    Toast.makeText(SignUp2.this, "Esa cuenta ya existe", Toast.LENGTH_SHORT).show();
                                }
                                // FIN Validar si se pudo crear cuenta o no
                            }
                        });
                        // FIN Crear cuenta
                    }
                    // FIN Verificar que eTXT est??n llenos
                } else { // No hay internet
                    Toast.makeText(SignUp2.this, "Por favor verifica tu conexi??n a internet", Toast.LENGTH_SHORT).show();
                }
                // FIN Verificar que tiene conexi??n a internet
            }
        });
        // FIN EVENTO BTNsignUp
    }
//****************************** FIN ONCREATE ******************************


//****************************** ON ACTIVITY RESULT ******************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri FILE_URI = data.getData();
            CropImage.activity(FILE_URI)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityTitle("Recorta tu foto")
                    .setFixAspectRatio(true)
                    .setCropMenuCropButtonTitle("LISTO").start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // TODO: Librer??a GLIDE -> https://youtu.be/QgYpdjvfz_w?t=288
                // Guardar la imagen seleccionada en la variable resultUri
                resultUri = result.getUri();
                signUp2_IVphoto.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                System.out.println(error.getMessage());
            }
        }
    }
//****************************** FIN ON ACTIVITY RESULT ******************************

    //****************************** M??TODOS ******************************
    // M??TODO para elementos UI
    private void Init() {
        this.signUp2_IVphoto = findViewById(R.id.signUp2_IVphoto);
        this.signUp2_IVaddPhoto = findViewById(R.id.signUp2_IVaddPhoto);
        this.signUp2_SpinnerTipoCuenta = findViewById(R.id.signUp2_SpinnerTipoCuenta);
        this.signUp2_TXTnombre = findViewById(R.id.signUp2_TXTnombre);
        this.signUp2_eTXTnombre = findViewById(R.id.signUp2_eTXTnombre);
        this.signUp2_TXTapellidoP = findViewById(R.id.signUp2_TXTapellidoP);
        this.signUp2_eTXTapellidoP = findViewById(R.id.signUp2_eTXTapellidoP);
        this.signUp2_TXTapellidoM = findViewById(R.id.signUp2_TXTapellidoM);
        this.signUp2_eTXTapellidoM = findViewById(R.id.signUp2_eTXTapellidoM);
        this.signUp2_TXTedad = findViewById(R.id.signUp2_TXTedad);
        this.signUp2_eTXTedad = findViewById(R.id.signUp2_eTXTedad);
        this.signUp2_SpinnerSexo = findViewById(R.id.signUp2_SpinnerSexo);
        this.signUp2_TXTphone = findViewById(R.id.signUp2_TXTphone);
        this.signUp2_eTXTphone = findViewById(R.id.signUp2_eTXTphone);
        this.signUp2_TXTdomicilio = findViewById(R.id.signUp2_TXTdomicilio);
        this.signUp2_eTXTdomicilio = findViewById(R.id.signUp2_eTXTdomicilio);
        this.signUp2_LLdatosProfesionales = findViewById(R.id.signUp2_LLdatosProfesionales);
        this.signUp2_SWcarrera = findViewById(R.id.signUp2_SWcarrera);
        this.signUp2_TXTcarrera = findViewById(R.id.signUp2_TXTcarrera);
        this.signUp2_eTXTcarrera = findViewById(R.id.signUp2_eTXTcarrera);
        this.signUp2_SWcertificacion = findViewById(R.id.signUp2_SWcertificacion);
        this.signUp2_TXTcertificacion = findViewById(R.id.signUp2_TXTcertificacion);
        this.signUp2_eTXTcertificacion = findViewById(R.id.signUp2_eTXTcertificacion);
        this.signUp2_LLexperiencia = findViewById(R.id.signUp2_LLexperiencia);
        this.signUp2_SWexperiencia = findViewById(R.id.signUp2_SWexperiencia);
        this.signUp2_TXTexperiencia = findViewById(R.id.signUp2_TXTexperiencia);
        this.signUp2_eTXTexperiencia = findViewById(R.id.signUp2_eTXTexperiencia);
        this.signUp2_BTNsignUp = findViewById(R.id.signUp2_BTNsignUp);
    }
    // M??TODO para elementos UI
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


    // M??TODO para llenar Spinner
    public void AdapterSpinner(){
        String[] spinner_TipoCuenta = {"Due??o de una mascota", "Entrenador", "Veterinario"};
        ArrayAdapter<String> spinnerAdapter_TipoCuenta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_TipoCuenta);
        signUp2_SpinnerTipoCuenta.setAdapter(spinnerAdapter_TipoCuenta);

        String[] spinner_Sexo = {"Hombre", "Mujer"};
        ArrayAdapter<String> spinnerAdapter_Sexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_Sexo);
        signUp2_SpinnerSexo.setAdapter(spinnerAdapter_Sexo);
    }
    // FIN M??TODO para llenar Spinner

    // M??TODO para mostrar u ocultar datos profesionales dependiento del SpinnerTipoCuenta
    public void MostrarDatosProfesionales(){
        signUp2_SpinnerTipoCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Due??o de una mascota
                    signUp2_LLdatosProfesionales.setVisibility(View.GONE);
                    signUp2_SWcarrera.setChecked(false);
                    signUp2_SWcertificacion.setChecked(false);
                    signUp2_SWexperiencia.setChecked(false);
                    signUp2_TXTcarrera.setVisibility(View.GONE);
                    signUp2_TXTcertificacion.setVisibility(View.GONE);
                    signUp2_TXTexperiencia.setVisibility(View.GONE);
                } else if (position == 1 || position == 2) { // Entrenador o veterinario
                    signUp2_LLdatosProfesionales.setVisibility(View.VISIBLE);

                    // EVENTO para mostrar eTXTcarrera
                    signUp2_SWcarrera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (signUp2_SWcarrera.isChecked() || signUp2_SWcertificacion.isChecked()) {
                                signUp2_LLexperiencia.setVisibility(View.VISIBLE);
                            } else {
                                signUp2_LLexperiencia.setVisibility(View.GONE);
                                signUp2_SWexperiencia.setChecked(false);
                                signUp2_TXTexperiencia.setVisibility(View.GONE);
                                signUp2_eTXTexperiencia.setText("");
                            }

                            // Mostrar u ocultar eTXTcarrera
                            if (signUp2_SWcarrera.isChecked()) { // Mostrar
                                signUp2_TXTcarrera.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp2_TXTcarrera.setVisibility(View.GONE);
                                signUp2_eTXTcarrera.setText("");
                            }
                            // FIN Mostrar u ocultar eTXTcarrera
                        }
                    });
                    // FIN EVENTO para mostrar eTXTcarrera

                    // EVENTO para mostrar eTXTcertificacion
                    signUp2_SWcertificacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (signUp2_SWcarrera.isChecked() || signUp2_SWcertificacion.isChecked()) {
                                signUp2_LLexperiencia.setVisibility(View.VISIBLE);
                            } else {
                                signUp2_LLexperiencia.setVisibility(View.GONE);
                                signUp2_SWexperiencia.setChecked(false);
                                signUp2_TXTexperiencia.setVisibility(View.GONE);
                                signUp2_eTXTexperiencia.setText("");
                            }

                            // Mostrar u ocultar eTXTcertificacion
                            if (signUp2_SWcertificacion.isChecked()) { // Mostrar
                                signUp2_TXTcertificacion.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp2_TXTcertificacion.setVisibility(View.GONE);
                                signUp2_eTXTcertificacion.setText("");
                            }
                            // FIN Mostrar u ocultar eTXTcertificacion
                        }
                    });
                    // FIN EVENTO para mostrar eTXTcertificacion

                    // EVENTO para mostrar eTXTexperiencia
                    signUp2_SWexperiencia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Mostrar u ocultar eTXTexperiencia
                            if (signUp2_SWexperiencia.isChecked()) { // Mostrar
                                signUp2_TXTexperiencia.setVisibility(View.VISIBLE);
                            } else { // Ocultar
                                signUp2_TXTexperiencia.setVisibility(View.GONE);
                                signUp2_eTXTexperiencia.setText("");
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
    // FIN M??TODO para mostrar u ocultar datos profesionales dependiento del SpinnerTipoCuenta

    // M??TODO para validar que los datos personales llenos
    public Boolean EditTextCompletos(){
        // Validar si los eTXT est??n completos
        if (signUp2_SpinnerTipoCuenta.getSelectedItem().toString().equals("Due??o de una mascota") && !nombre.isEmpty() && !apellidoP.isEmpty() && !apellidoM.isEmpty() && !edad.isEmpty() && !phone.isEmpty() && !domicilio.isEmpty()) { // Due??o de una mascota = Datos personales llenos
            return true;
        } else if ((signUp2_SpinnerTipoCuenta.getSelectedItem().toString().equals("Entrenador") || signUp2_SpinnerTipoCuenta.getSelectedItem().toString().equals("Veterinario")) && !nombre.isEmpty() && !apellidoP.isEmpty() && !apellidoM.isEmpty() && !edad.isEmpty() && !phone.isEmpty() && !domicilio.isEmpty()) { // Entrenador o veterinario = Datos personales llenos
            // Validar si los Switch est??n activos (Y est??n completos los campos) o est??n inactivos los SW
            if (((signUp2_SWcarrera.isChecked() && !carrera.isEmpty()) || !signUp2_SWcarrera.isChecked()) && ((signUp2_SWcertificacion.isChecked() && !certificacion.isEmpty()) || !signUp2_SWcertificacion.isChecked()) && ((signUp2_SWexperiencia.isChecked() && !experiencia.isEmpty()) || !signUp2_SWexperiencia.isChecked())) { // Todos los SW que est??n activos est??n completos o simplemente los SW est??n inactivos
                return true;
            } else { // Alg??n SW est?? activo y est?? alg??n campo vac??o
                // Carrera
                if (signUp2_SWcarrera.isChecked() && carrera.isEmpty()) {
                    signUp2_TXTcarrera.setError("Escribe tu carrera o desactiva la opci??n");
                }
                // Certificacion
                if (signUp2_SWcertificacion.isChecked() && certificacion.isEmpty()) {
                    signUp2_TXTcertificacion.setError("Escribe tus certificaciones o desactiva la opci??n.");
                }
                // Experiencia
                if (signUp2_SWexperiencia.isChecked() && experiencia.isEmpty()) {
                    signUp2_TXTexperiencia.setError("Escribe tu experiencia profesional o desactiva la opci??n.");
                }
                return false;
            }
            // FIN Validar si los Switch est??n activos (Y est??n completos los campos) o est??n inactivos los SW
        } else { // Alg??n dato personal est?? vac??o
            // Datos personales
            if (nombre.isEmpty()) { // Nombre
                signUp2_TXTnombre.setError("Campo obligatorio");
            }
            if (apellidoP.isEmpty()) { // ApellidoP
                signUp2_TXTapellidoP.setError("Campo obligatorio");
            }
            if (apellidoM.isEmpty()) { // ApellidoM
                signUp2_TXTapellidoM.setError("Campo obligatorio");
            }
            if (edad.isEmpty()) { // Edad
                signUp2_TXTedad.setError("Campo obligatorio");
            }
            if (phone.isEmpty()) { // Phone
                signUp2_TXTphone.setError("Campo obligatorio");
            }
            if (domicilio.isEmpty()) { // Domicilio
                signUp2_TXTdomicilio.setError("Campo obligatorio");
            }
            // FIN Datos personales

            // Datos profesionales
            if (signUp2_SWcarrera.isChecked() && carrera.isEmpty()) { // Carrera
                signUp2_TXTcarrera.setError("Escribe tu carrera o desactiva la opci??n");
            }

            if (signUp2_SWcertificacion.isChecked() && certificacion.isEmpty()) { // Certificacion
                signUp2_TXTcertificacion.setError("Escribe tus certificaciones o desactiva la opci??n.");
            }

            if (signUp2_SWexperiencia.isChecked() && experiencia.isEmpty()) { // Experiencia
                signUp2_TXTexperiencia.setError("Escribe tu experiencia profesional o desactiva la opci??n.");
            }
            // FIN Datos profesionales

            return false;
        }
        // FIN Validar si los eTXT est??n completos
    }
    // FIN M??TODO para validar que los datos personales llenos

    // M??TODO para ocultar los errores de los eTXT
    private void OcultarErroreseTXT() {
        signUp2_eTXTnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTnombre.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTapellidoP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTapellidoP.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTapellidoM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTapellidoM.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTedad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTedad.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTphone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTdomicilio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTdomicilio.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTcarrera.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTcarrera.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTcertificacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTcertificacion.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUp2_eTXTexperiencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signUp2_TXTexperiencia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    // FIN M??TODO para ocultar los errores de los eTXT
//****************************** FIN M??TODOS ******************************
}