package com.example.sistemaventas.Modelo.Entidades;

public class Cliente {
    public int codigoCliente;
    public String nombre;
    public String apellido;
    public boolean activo;
    public String direccion;
    public String telefono;
    public String cedula;
    public String rol;
    public String correo;
    public String contrasenia;

    public Cliente(){

    }
    public Cliente(int codigoCliente, String nombre, String apellido, boolean activo, String direccion, String telefono, String cedula, String rol, String correo, String contrasenia) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cedula = cedula;
        this.rol = rol;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }
}
