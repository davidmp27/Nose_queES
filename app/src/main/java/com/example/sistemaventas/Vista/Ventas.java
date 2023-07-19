package com.example.sistemaventas.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Factura;
import com.example.sistemaventas.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Ventas extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";

    private TableLayout tablaVentas;
    private List<Factura> listaVentas;
    private Intent intentVentas;
    private Button buttonProductos, buttonComprar, anterior, siguiente;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private int currentPage = 1, pageSize = 5, totalPages;


    //menu
    private TextView logout, clientes, productos, ventas, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        buttonProductos = findViewById(R.id.buttonProductos);
        buttonComprar = findViewById(R.id.buttonRealizarVenta);
        intentVentas = this.getIntent();
        drawerLayout = findViewById(R.id.drawerMenuFlotante);
        navigationView = findViewById(R.id.nav_view);
        anterior = findViewById(R.id.buttonAnterior);
        siguiente = findViewById(R.id.buttonSiguiente);
        tablaVentas = findViewById(R.id.tableLayoutVentas);

        habilitarMenu();

        listaVentas = filtrarPorUsuario(llenarDatos());
        totalPages = (int) Math.ceil((double) listaVentas.size() / pageSize);
        updateTable();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                android.R.string.ok,
                android.R.string.cancel
        );

        buttonProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mirarProductos();
            }
        });

        buttonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarVenta();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    updateTable();
                }
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < totalPages) {
                    currentPage++;
                    updateTable();
                }
            }
        });

    }

    private void habilitarMenu() {
        //Habilitar el menu
        logout = findViewById(R.id.btMenuCerrarSesion);
        clientes = findViewById(R.id.btMenuClientes);
        productos = findViewById(R.id.btMenuProductos);
        ventas = findViewById(R.id.btMenuVentas);
        home = findViewById(R.id.btHome);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, Login.class);
                startActivity(intent);
                Ventas.this.finish();
            }
        });
        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, Clientes.class);
                intent.putExtra("codCliente",
                        Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
                startActivity(intent);
                Ventas.this.finish();
            }
        });
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, Productos.class);
                intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
                startActivity(intent);
                Ventas.this.finish();
            }
        });
        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, CarritoCompras.class);
                intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
                startActivity(intent);
                Ventas.this.finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, Ventas.class);
                intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
                startActivity(intent);
                Ventas.this.finish();
            }
        });
    }

    public void mirarFactura(int verificar) {
        Intent intent = new Intent(this, RealizarVenta.class);
        intent.putExtra("verificador", verificar);
        intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
        intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
        intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
        startActivity(intent);
        finish();
    }

    public void mirarProductos() {
        Intent intent = new Intent(this, Productos.class);
        intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
        intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
        intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
        startActivity(intent);
        finish();
    }

    public void realizarVenta() {
        Intent intent = new Intent(this, CarritoCompras.class);
        intent.putExtra("codCliente", Integer.parseInt(intentVentas.getExtras().get("codCliente").toString()));
        intent.putExtra("nombre", intentVentas.getExtras().get("nombre").toString());
        intent.putExtra("rol", intentVentas.getExtras().get("rol").toString());
        startActivity(intent);
        finish();
    }

    public List<Factura> llenarDatos() {
        List<Factura> resp;
        try {
            resp = new ApiHandler.GetFacturasTask().execute(url + "Ventas").get();
            return resp;
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private void updateTable() {
        tablaVentas.removeAllViews();

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, listaVentas.size());

        TableRow tableRow = new TableRow(this);

        TextView campo1 = new TextView(this);
        campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
        campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        campo1.setText(String.valueOf(fac.idVenta));
        tableRow.addView(campo1);

        TextView campo2 = new TextView(this);
        campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
        campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        campo2.setText(fac.fechaRegistro);
        tableRow.addView(campo2);

        TextView campo3 = new TextView(this);
        campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        campo3.setText(String.valueOf(fac.total));
        tableRow.addView(campo3);

        Button verFactura = new Button(this);
        verFactura.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
        verFactura.setText("VER");
        verFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mirarFactura(fac.idVenta);
            }
        });
        tableRow.addView(verFactura);

        tablaVentas.addView(tableRow);

        for (int i = start; i < end; i++) {
            Factura fac = listaVentas.get(i);

            TableRow tableRow = new TableRow(this);

            TextView campo1 = new TextView(this);
            campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo1.setText(String.valueOf(fac.idVenta));
            tableRow.addView(campo1);

            TextView campo2 = new TextView(this);
            campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
            campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo2.setText(fac.fechaRegistro);
            tableRow.addView(campo2);

            TextView campo3 = new TextView(this);
            campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo3.setText(String.valueOf(fac.total));
            tableRow.addView(campo3);

            Button verFactura = new Button(this);
            verFactura.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            verFactura.setText("VER");
            verFactura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mirarFactura(fac.idVenta);
                }
            });
            tableRow.addView(verFactura);

            tablaVentas.addView(tableRow);
        }
    }

    public List<Factura> filtrarPorUsuario(List<Factura> data){
        List<Factura> lista = new ArrayList<>();
        if (intentVentas.getExtras().get("rol").toString().equals("usuario")){
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).cliente.equals(intentVentas.getExtras().get("nombre").toString())){
                    lista.add(data.get(i));
                }

            }

            return lista;
        } else {
            return data;
        }
    }

}