package com.example.sistemaventas.Modelo.Entidades;

public class Root {
    public Factura factura;
    public DetalleVenta detalleVenta;
    public double iva;
    public double subtotal;

    public Root(Factura factura, DetalleVenta detalleVenta,
                double iva, double subtotal) {
        this.factura = factura;
        this.detalleVenta = detalleVenta;
        this.iva = iva;
        this.subtotal = subtotal;
    }
}
