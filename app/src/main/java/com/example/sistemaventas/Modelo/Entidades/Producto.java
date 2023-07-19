package com.example.sistemaventas.Modelo.Entidades;

public class Producto {
    public int stock;
    public int idProducto;
    public String nombre;
    public double precio;
    public boolean esActivo;
    public String fechaActivo;

    public Producto(){}
    public Producto(int stock, int idProducto,
                    String nombre, double precio,
                    boolean esActivo, String fechaActivo) {
        this.stock = stock;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.esActivo = esActivo;
        this.fechaActivo = fechaActivo;
    }
}
