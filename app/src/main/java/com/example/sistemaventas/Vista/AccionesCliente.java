package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AccionesCliente extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";
    private Cliente usuario;
    private Intent intentAcciones;
    private TextView cedula,textVCedula;
    private EditText editCedula,
            correo,
            direccion,
            telefono,
            contrasenia,
            nombre;
    private Button volver,guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones_cliente);
        intentAcciones = this.getIntent();

        nombre = findViewById(R.id.editTextNombre);
        cedula = findViewById(R.id.textViewCedula);
        correo = findViewById(R.id.editTextCorreo);
        direccion = findViewById(R.id.editTextDireccion);
        telefono = findViewById(R.id.editTextTelefono);
        contrasenia = findViewById(R.id.editTextContraseña);
        volver = findViewById(R.id.buttonVolver);
        guardar = findViewById(R.id.buttonGuardarCliente);
        editCedula = findViewById(R.id.editTextCedula);
        textVCedula = findViewById(R.id.textView1);

        if (intentAcciones.getExtras() != null) {

            editCedula.setVisibility(View.GONE);
            textVCedula.setVisibility(View.GONE);

            usuario = traerCliente(
                    Integer.parseInt(intentAcciones.getExtras().get("codigo").toString())
            );

            nombre.setText(usuario.nombre +" " +  usuario.apellido);
            cedula.setText(usuario.cedula);
            editCedula.setText(usuario.cedula);
            correo.setText(usuario.correo);
            direccion.setText(usuario.direccion);
            telefono.setText(usuario.telefono);
            contrasenia.setText(usuario.contrasenia);

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarCliente();
                }
            });

        } else {
            editCedula.setVisibility(View.VISIBLE);
            textVCedula.setVisibility(View.VISIBLE);

            editCedula.setText("");
            nombre.setText("");
            cedula.setText("");
            correo.setText("");
            direccion.setText("");
            telefono.setText("");
            contrasenia.setText("");

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarCliente();
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

    private void guardarCliente() {
        if (validar()){
            Toast.makeText(this,"Ingrese los campos correctamente",Toast.LENGTH_SHORT).show();
            return;
        }

        String contrasenia = this.contrasenia.getText().toString();
        String telefono = this.telefono.getText().toString();
        String apellido = this.nombre.getText().toString()
                .substring(this.nombre.getText().toString().indexOf(' ') + 1);;

        String nombre = this.nombre.getText().toString()
                .substring(0,this.nombre.getText().toString().indexOf(' '));

        String direccion = this.direccion.getText().toString();
        boolean activo = true;
        String cedula = this.editCedula.getText().toString();
        String correo = this.correo.getText().toString();
        String rol = "usuario";

        Cliente cl = new Cliente(
                0, nombre, apellido, activo,
                direccion, telefono, cedula, rol,
                correo, contrasenia);
        try {
            JSONObject json = new JSONObject();
            json.put("codigoCliente", cl.codigoCliente);
            json.put("nombre", cl.nombre);
            json.put("apellido", cl.apellido);
            json.put("activo", cl.activo);
            json.put("direccion", cl.direccion);
            json.put("telefono", cl.telefono);
            json.put("cedula", cl.cedula);
            json.put("rol", cl.rol);
            json.put("correo", cl.correo);
            json.put("contraseña", cl.contrasenia);

            ApiHandler.CrearDataAsync(URL + "Clientes", json, new ApiHandler.OnPostDataListener() {
                @Override
                public void onPostDataSuccess(String response) {
                    Toast.makeText(AccionesCliente.this, "Guardado exitoso!", Toast.LENGTH_SHORT).show();
                    volverAtras();
                }

                @Override
                public void onPostDataError(IOException e) {
                    Toast.makeText(AccionesCliente.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(
                    AccionesCliente.this,
                    "Error desconocido: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

    }
    private void actualizarCliente() {
        if (validar()){
            Toast.makeText(this,"Ingrese los campos correctamente",Toast.LENGTH_SHORT).show();
            return;
        }

        String contrasenia = this.contrasenia.getText().toString();
        String telefono = this.telefono.getText().toString();

        String apellido = this.nombre.getText()
                .toString()
                .substring(this.nombre.getText().toString().indexOf(' ') + 1);

        String nombre = this.nombre.getText()
                .toString()
                .substring(0,this.nombre.getText().toString().indexOf(' '));

        String direccion = this.direccion.getText().toString();
        boolean activo = usuario.activo;
        editCedula.setText(usuario.cedula);
        String cedula = usuario.cedula;
        String correo = this.correo.getText().toString();
        String rol = usuario.rol;

        try {
            Cliente cl = new Cliente(
                    usuario.codigoCliente, nombre, apellido, activo,
                    direccion, telefono, cedula, rol,
                    correo, contrasenia);

                JSONObject json = new JSONObject();
                json.put("codigoCliente", cl.codigoCliente);
                json.put("nombre", cl.nombre);
                json.put("apellido", cl.apellido);
                json.put("activo", true);
                json.put("direccion", cl.direccion);
                json.put("telefono", cl.telefono);
                json.put("cedula", cl.cedula);
                json.put("rol", "usuario");
                json.put("correo", cl.correo);
                json.put("contraseña", cl.contrasenia);
                ApiHandler.ActualizarAsync(URL + "Clientes", json, new ApiHandler.OnUpdateDataListener() {
                    @Override
                    public void onUpdateDataSuccess(String response) {
                        Toast.makeText(AccionesCliente.this, "Guardado exitoso!", Toast.LENGTH_SHORT).show();
                        volverAtras();
                    }

                    @Override
                    public void onUpdateDataError(IOException e) {
                        Toast.makeText(AccionesCliente.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        } catch (Exception e){
            Toast.makeText(AccionesCliente.this, "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void volverAtras() {
        if (intentAcciones.getExtras() != null){
            Intent intent = new Intent(this, Clientes.class);
            intent.putExtra("codCliente",
                    Integer.parseInt(intentAcciones.getExtras().get("codCliente").toString()));
            intent.putExtra("rol", intentAcciones.getExtras().get("rol").toString());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

    }
    public Cliente traerCliente(int codigo){
        Cliente cl = new Cliente();

        try {
            cl = new ApiHandler.getClientesPorID().execute(URL + "Clientes/" + codigo).get();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cl;
    }
    public boolean validar(){
        if(nombre.getText().toString().isEmpty())
            return true;
        else if(editCedula.getText().toString().isEmpty() || editCedula.getText().toString().length() != 10)
            return true;
        else if(correo.getText().toString().isEmpty())
            return true;
        else if(direccion.getText().toString().isEmpty())
            return true;
        else if(telefono.getText().toString().isEmpty())
            return true;
        else if(contrasenia.getText().toString().isEmpty() || contrasenia.getText().toString().length() < 4)
            return true;
        else
            return false;
    }
}