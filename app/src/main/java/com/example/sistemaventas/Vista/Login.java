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

public class Login extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";
    private TextView registrarse;
    private EditText cedula;
    private EditText clave;
    private Button login;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cedula = findViewById(R.id.et_cedula_login);
        clave = findViewById(R.id.et_contraseña_login);
        login = findViewById(R.id.btn_login);
        registrarse = findViewById(R.id.tv_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvocarServicio();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar() {
        Intent intent = new Intent(this, AccionesCliente.class);
        startActivity(intent);
    }

    public void InvocarServicio(){

        String dni = cedula.getText().toString();
        String contra = clave.getText().toString();
        JSONObject obj = new JSONObject();
        Cliente cl;

        try {
            cl =  ApiHandler.LoginAsync(url + "Login/" + dni + "&" + contra, obj, new ApiHandler.OnPostDataListener() {
                @Override
                public void onPostDataSuccess(String response) {
                    //logica de login
                    try {
                        token = response;
                        JSONObject responseJson = new JSONObject(token);
                        boolean val = Boolean.parseBoolean(responseJson.getString("success"));

                        if (!val){
                            Toast.makeText(Login.this.getBaseContext(),"Login Incorrecto", Toast.LENGTH_SHORT).show();
                        } else {
                            token = responseJson.getString("message");
                            token = token.replace("token: ", "");
                        }

                    } catch (Exception e){
                        Toast.makeText(Login.this.getBaseContext(),"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPostDataError(IOException e) {
                    Toast.makeText(Login.this.getBaseContext(),"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (cl != null){
                Intent intent = new Intent(Login.this, Ventas.class);
                intent.putExtra("token", token);
                intent.putExtra("codCliente", cl.codigoCliente);
                intent.putExtra("nombre", cl.nombre + " " + cl.apellido);
                intent.putExtra("rol", cl.rol);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Login.this.getBaseContext(),"Error: Inicio de sesión no valido", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}