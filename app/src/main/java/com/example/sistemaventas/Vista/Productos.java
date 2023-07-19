package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Productos extends AppCompatActivity {

    String url = "http://www.sistemaventasepe.somee.com/api/";

    private TableLayout tablaProductos;
    private List<Producto> listaProductos;
    private Intent intentProductos;
    private Button buttonCliente, crear,anterior, siguiente;
    private int currentPage = 1, pageSize = 5, totalPages;

    //menu
    private TextView logout, clientes, productos, ventas, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        intentProductos = this.getIntent();
        listaProductos = llenarDatos();
        tablaProductos = findViewById(R.id.tableLayoutProductos);
        crear = findViewById(R.id.buttonCrearProducto);
        anterior = findViewById(R.id.buttonAnterior);
        siguiente = findViewById(R.id.buttonSiguiente);
        habilitarMenu();

        totalPages = (int) Math.ceil((double) listaProductos.size() / pageSize);
        updateTable();

        buttonCliente = findViewById(R.id.buttonClientes);
        buttonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verClientes();
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarProducto(0);
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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, Ventas.class);
                intent.putExtra("codCliente", Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
                startActivity(intent);
                Productos.this.finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, Login.class);
                startActivity(intent);
                Productos.this.finish();
            }
        });
        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, Clientes.class);
                intent.putExtra("codCliente",
                        Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
                startActivity(intent);
                Productos.this.finish();
            }
        });
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, Productos.class);
                intent.putExtra("codCliente", Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
                startActivity(intent);
                Productos.this.finish();
            }
        });
        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, CarritoCompras.class);
                intent.putExtra("codCliente", Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
                startActivity(intent);
                Productos.this.finish();
            }
        });
    }

    public List<Producto> llenarDatos(){
        List<Producto> resp;
        try {
            if (intentProductos.getExtras().get("rol").toString().equals("admin")) {
                resp = new ApiHandler.GetProductosTask().execute(url + "Productos").get();
                return resp;
            } else {
                resp = new ApiHandler.GetProductosActivosTask().execute(url + "Productos/ACTIVOS").get();
                return resp;
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarProducto(int id){
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd
                .setMessage("¿Está seguro que desea eliminar el producto?")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiHandler.deleteAsync(url + "Productos/" + id, new ApiHandler.OnDeleteDataListener() {
                            @Override
                            public void onDeleteDataSuccess(String response) {
                                Toast.makeText(Productos.this,"Dato eliminado exitosamente.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Productos.this, Productos.class);
                                intent.putExtra("codCliente",
                                        Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
                                intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
                                intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
                                startActivity(intent);
                            }

                            @Override
                            public void onDeleteDataError(IOException e) {
                                Toast.makeText(Productos.this,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        bd.create();
        bd.show();
    }

    public void editarProducto(int id){
        Intent intent = new Intent(this, AccionesProductos.class);
        intent.putExtra("codCliente",
                Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
        intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
        intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
        intent.putExtra("codigo", id);
        startActivity(intent);
    }

    public void verClientes(){
        Intent intent = new Intent(this, Clientes.class);
        intent.putExtra("codCliente",
                Integer.parseInt(intentProductos.getExtras().get("codCliente").toString()));
        intent.putExtra("nombre", intentProductos.getExtras().get("nombre").toString());
        intent.putExtra("rol", intentProductos.getExtras().get("rol").toString());
        startActivity(intent);
    }

    private void updateTable() {
        tablaProductos.removeAllViews();

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, listaProductos.size());

        for(int i = start; i < end; i++){
            Producto fac = listaProductos.get(i);
            TableRow tableRow = new TableRow(this);

            TextView campo1 = new TextView(this);
            campo1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo1.setText(String.valueOf(fac.idProducto));
            tableRow.addView(campo1);

            TextView campo2 = new TextView(this);
            campo2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo2.setText(fac.nombre);
            tableRow.addView(campo2);

            TextView campo3 = new TextView(this);
            campo3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo3.setText(String.valueOf(fac.precio));
            tableRow.addView(campo3);

            TextView campo4 = new TextView(this);
            campo4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            campo4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            campo4.setText(String.valueOf(fac.stock));
            tableRow.addView(campo4);

            Button editarProducto = new Button(this);
            editarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            editarProducto.setText("EDITAR");
            editarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarProducto(fac.idProducto);
                }
            });
            tableRow.addView(editarProducto);

            Button eliminarProducto = new Button(this);
            eliminarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            eliminarProducto.setText("ELIMINAR");
            eliminarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarProducto(fac.idProducto);
                }
            });
            tableRow.addView(eliminarProducto);

            tablaProductos.addView(tableRow);
        }
    }
}