package com.example.sistemaventas.Modelo.Entidades;

public class DetalleVenta {
    public int idDetalleVenta;
    public int idVenta;
    public String nombre;
    public int cantidad;
    public double precio;
    public double total;

    public DetalleVenta(int idDetalleVenta, int idVenta,
                        String nombre, int cantidad,
                        double precio, double total) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
    }
}
