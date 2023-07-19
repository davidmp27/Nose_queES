package com.example.sistemaventas.Modelo.Entidades;

public class ListaVenta {
    public int idVenta;
    public String numeroDocumento;
    public String tipoPago;
    public String cliente;
    public double total;
    public String fechaRegistro;

    public ListaVenta(int idVenta, String numeroDocumento,
                      String tipoPago, String cliente,
                      double total, String fechaRegistro) {
        this.idVenta = idVenta;
        this.numeroDocumento = numeroDocumento;
        this.tipoPago = tipoPago;
        this.cliente = cliente;
        this.total = total;
        this.fechaRegistro = fechaRegistro;
    }
}
