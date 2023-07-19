package com.example.sistemaventas.Modelo.Entidades;

public class Factura {
    public int idVenta;
    public String cedula;
    public String direccion;
    public String telefono;
    public String cliente;
    public double total;
    public String fechaRegistro;

    public Factura(int idVenta, String cedula,
                   String direccion, String telefono,
                   String cliente, double total,
                   String fechaRegistro) {
        this.idVenta = idVenta;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cliente = cliente;
        this.total = total;
        this.fechaRegistro = fechaRegistro;
    }
}
