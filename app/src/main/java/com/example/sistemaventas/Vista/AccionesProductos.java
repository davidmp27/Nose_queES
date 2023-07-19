package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AccionesProductos extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";
    private Intent intentAcciones;
    private Producto productoSel;
    private TextView fecha, nombre;
    private EditText precio, cantidad, codigo;
    private Switch activo;
    private Button volver, guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones_productos);
        intentAcciones = this.getIntent();

        codigo = findViewById(R.id.editTextCodigo);
        fecha = findViewById(R.id.textViewFecha);
        nombre = findViewById(R.id.editTextNombreProducto);
        precio = findViewById(R.id.editTextPrecio);
        cantidad = findViewById(R.id.editTextStock);
        activo = findViewById(R.id.switchActivo);
        volver = findViewById(R.id.buttonVolver);
        guardar = findViewById(R.id.buttonGuardarProducto);

        if (Integer.parseInt(intentAcciones.getExtras().get("codigo").toString()) != 0) {

            productoSel = traerProducto(Integer.parseInt(intentAcciones.getExtras().get("codigo").toString()));
            Producto p = productoSel;

            codigo.setText(String.valueOf(p.idProducto));
            fecha.setText(p.fechaActivo);
            nombre.setText(p.nombre);
            precio.setText(String.valueOf(p.precio));
            cantidad.setText(String.valueOf(p.stock));
            activo.setChecked(p.esActivo);

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarProducto();
                }
            });

        } else {

            codigo.setText("0");
            fecha.setText("");
            nombre.setText("");
            precio.setText("");
            cantidad.setText("");
            activo.setChecked(false);

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarProducto();
                }
            });
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });
    }

    private void guardarProducto() {
        if (validar()){
            Toast.makeText(this,"Ingrese los campos correctamente",Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(this.cantidad.getText().toString());
        int idProducto = 0;
        String nombre = this.nombre.getText().toString();
        double precio = Double.parseDouble(this.precio.getText().toString());
        boolean esActivo = this.activo.isChecked();
        String fechaActivo = "";

        Producto cl = new Producto(
                stock, idProducto, nombre, precio, esActivo, fechaActivo);

        try {
            JSONObject json = new JSONObject();
            json.put("idProducto", cl.idProducto);
            json.put("nombre", cl.nombre);
            json.put("stock", cl.stock);
            json.put("precio", cl.precio);
            json.put("esActivo", cl.esActivo);

            ApiHandler.CrearDataAsync(URL + "Productos", json, new ApiHandler.OnPostDataListener() {
                @Override
                public void onPostDataSuccess(String response) {
                    Toast.makeText(AccionesProductos.this, "Guardado exitoso!", Toast.LENGTH_SHORT).show();
                    volverAtras();
                }

                @Override
                public void onPostDataError(IOException e) {
                    Toast.makeText(AccionesProductos.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(AccionesProductos.this, "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarProducto() {
        if (validar()){
            Toast.makeText(this,"Ingrese los campos correctamente",Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(this.cantidad.getText().toString());
        int idProducto = Integer.parseInt(this.codigo.getText().toString());
        String nombre = this.nombre.getText().toString();
        double precio = Double.parseDouble(this.precio.getText().toString());
        boolean esActivo = this.activo.isChecked();
        String fechaActivo = this.fecha.getText().toString();

        Producto cl = new Producto(
                stock, idProducto, nombre, precio, esActivo, fechaActivo);

        try {
            JSONObject json = new JSONObject();
            json.put("idProducto", cl.idProducto);
            json.put("nombre", cl.nombre);
            json.put("stock", cl.stock);
            json.put("precio", cl.precio);
            json.put("esActivo", cl.esActivo);

            ApiHandler.ActualizarAsync(URL + "Productos", json, new ApiHandler.OnUpdateDataListener() {
                @Override
                public void onUpdateDataSuccess(String response) {
                    Toast.makeText(AccionesProductos.this, "Guardado exitoso!", Toast.LENGTH_SHORT).show();
                    volverAtras();
                }

                @Override
                public void onUpdateDataError(IOException e) {
                    Toast.makeText(AccionesProductos.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(AccionesProductos.this, "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Producto traerProducto(int codigo) {
        Producto p = new Producto();
        try {
            p = new ApiHandler.getProductoPorID().execute(URL + "Productos/" + codigo).get();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return p;
    }

    private void volverAtras() {
            Intent intent = new Intent(this, Productos.class);
            intent.putExtra("codCliente",
                    Integer.parseInt(intentAcciones.getExtras().get("codCliente").toString()));
            intent.putExtra("rol", intentAcciones.getExtras().get("rol").toString());
            startActivity(intent);
    }

    public boolean validar(){
        if(nombre.getText().toString().isEmpty())
            return true;
        else if(codigo.getText().toString().isEmpty())
            return true;
        else if(nombre.getText().toString().isEmpty())
            return true;
        else if(precio.getText().toString().isEmpty())
            return true;
        else if(cantidad.getText().toString().isEmpty())
            return true;
        else if(!activo.isChecked())
            return true;
        else
            return false;
    }
}