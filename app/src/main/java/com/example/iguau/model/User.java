package com.example.iguau.model;

public class User {
    String tipoCuenta, nombre, apellidoP, apellidoM, edad, genero, phone, domicilio, carrera, certificacion, experiencia, email;

    public User() {
    }

    public User(String tipoCuenta, String nombre, String apellidoP, String apellidoM, String edad, String genero, String phone, String domicilio, String email) {
        this.tipoCuenta = tipoCuenta;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.genero = genero;
        this.phone = phone;
        this.domicilio = domicilio;
        this.email = email;
    }

    public User(String tipoCuenta, String nombre, String apellidoP, String apellidoM, String edad, String genero, String phone, String domicilio, String carrera, String certificacion, String experiencia, String email) {
        this.tipoCuenta = tipoCuenta;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.genero = genero;
        this.phone = phone;
        this.domicilio = domicilio;
        this.carrera = carrera;
        this.certificacion = certificacion;
        this.experiencia = experiencia;
        this.email = email;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
