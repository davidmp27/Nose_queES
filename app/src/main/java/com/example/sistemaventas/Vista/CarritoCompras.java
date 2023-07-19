package com.example.sistemaventas.Vista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemaventas.Controllers.GridAdapter;
import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.Modelo.Responses.ApiHandler;
import com.example.sistemaventas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CarritoCompras extends AppCompatActivity {

    private static final String URL = "http://www.sistemaventasepe.somee.com/api/";
    private List<String[]> carrito;
    private float totalApagar;


    private GridView gridViewProductos;
    private Button cancelar, continuar, mostrarCarrito;
    private Intent intentComprar;
    private TextView total;

    //menu
    private TextView logout, clientes, productos, ventas, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);
        intentComprar = this.getIntent();
        gridViewProductos = findViewById(R.id.gridViewProductos);
        cancelar = findViewById(R.id.buttonCancelar);
        continuar = findViewById(R.id.buttonContinuar);
        mostrarCarrito = findViewById(R.id.buttonMostrarCarrito);
        total = findViewById(R.id.textViewTotal);
        totalApagar = 0;
        total.setText("Total: " + totalApagar);

        habilitarMenu();

        //llenar datos
        List<Producto> lista = llenarDatos();
        GridAdapter gridAdapter = new GridAdapter(this, lista);
        gridViewProductos.setAdapter(gridAdapter);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cancelar();
            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _continuar();
            }
        });

        gridViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                agregarProducto(
                        lista.get(position).idProducto,
                        lista.get(position).nombre,
                        lista.get(position).stock,
                        lista.get(position).precio,
                        view);
            }
        });

        mostrarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCarrito();
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
                Intent intent = new Intent(CarritoCompras.this, Login.class);
                startActivity(intent);
                CarritoCompras.this.finish();
            }
        });
        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoCompras.this, Clientes.class);
                intent.putExtra("codCliente",
                        Integer.parseInt(intentComprar.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
                startActivity(intent);
                CarritoCompras.this.finish();
            }
        });
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoCompras.this, Productos.class);
                intent.putExtra("codCliente", Integer.parseInt(intentComprar.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
                startActivity(intent);
                CarritoCompras.this.finish();
            }
        });
        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoCompras.this, CarritoCompras.class);
                intent.putExtra("codCliente", Integer.parseInt(intentComprar.getExtras().get("codCliente").toString()));
                intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
                startActivity(intent);
                CarritoCompras.this.finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoCompras.this, Ventas.class);
                intent.putExtra("codCliente", Integer.parseInt(intentComprar.getExtras().get("codCliente").toString()));
                intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
                intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                startActivity(intent);
                CarritoCompras.this.finish();
            }
        });
    }
    public void _continuar(){
        if (carrito == null){
            Toast.makeText(CarritoCompras.this, "No ha seleccionado ningun producto.", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<ArrayList<String>> listaExtra = new ArrayList<>();
            for (String[] array : carrito) {
                listaExtra.add(new ArrayList<>(Arrays.asList(array)));
            }

            Intent intent = new Intent(this, RealizarVenta.class);
            intent.putExtra("codCliente", intentComprar.getExtras().get("codCliente").toString());
            intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
            intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
            intent.putExtra("carrito", listaExtra);
            intent.putExtra("subtotal", totalApagar);
            intent.putExtra("verificador", -1);
            startActivity(intent);
        }
    }

    public void mostrarCarrito(){

        if (carrito == null){
            Toast.makeText(CarritoCompras.this,"Tu carrito está vacio.",Toast.LENGTH_SHORT).show();
        } else {
            View rl = tablaMostrarCarrito();
            AlertDialog.Builder bd = new AlertDialog.Builder(this);
            bd
                    .setView(rl)
                    .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            bd.create();
            bd.show();
        }
    }

    public void _cancelar(){
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd
                .setMessage("¿Está seguro que desea cancelar el pedido?")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CarritoCompras.this, Ventas.class);
                        intent.putExtra("codCliente", intentComprar.getExtras().get("codCliente").toString());
                        intent.putExtra("nombre", intentComprar.getExtras().get("nombre").toString());
                        intent.putExtra("rol", intentComprar.getExtras().get("rol").toString());
                        startActivity(intent);
                    }
                });
        bd.create();
        bd.show();
    }

    private List<Producto> llenarDatos() {
        List<Producto> lista = new ArrayList<>();

        try {
            lista = new ApiHandler.GetProductosActivosTask().execute(URL + "Productos/ACTIVOS").get();
            return lista;
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(CarritoCompras.this,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return lista;
    }

    private void agregarProducto(int idProducto, String nombre, int max, double precio, View v){
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        EditText cantidad = new EditText(this);
        cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (!input.isEmpty()) {
                    int value = Integer.parseInt(input);

                    if (value > max) {
                        value = max;
                        cantidad.setText(String.valueOf(value));
                    } else if (value <= 0) {
                        value = 1;
                        cantidad.setText(String.valueOf(value));
                    }
                }
            }
        });

        AlertDialog alert;
        bd.setMessage("¿Cuanto(s) " + nombre +"(s) desea comprar?")
                .setView(cantidad)
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accionAgregarProducto(
                                idProducto,
                                nombre,
                                precio,
                                Integer.parseInt(cantidad.getText().toString()),
                                max);
                    }
                });
        alert = bd.create();
        alert.show();
    }

    public View tablaMostrarCarrito(){
        LinearLayout rl = new LinearLayout(this);
        rl.setPadding(5,5,5,5);
        TableLayout tl = new TableLayout(this);
        tl.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        tl.setGravity(Gravity.CENTER);

        TableRow tr = new TableRow(this);

        TextView h0 = new TextView(this);
        h0.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h0.setText("Codigo");
        h0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h0.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        tr.addView(h0);

        TextView h1 = new TextView(this);
        h1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h1.setText("Producto");
        h1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h1.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        tr.addView(h1);

        TextView h3 = new TextView(this);
        h3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h3.setText("Cantidad");
        h3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h3.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        tr.addView(h3);

        TextView h2 = new TextView(this);
        h2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h2.setText("Precio");
        h2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h2.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        tr.addView(h2);

        TextView h4 = new TextView(this);
        h4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h4.setText("Total");
        h4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h4.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        tr.addView(h4);

        TextView h5 = new TextView(this);
        h5.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h5.setText("");
        tr.addView(h5);

        TextView h6 = new TextView(this);
        h6.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        h6.setText("");
        tr.addView(h6);

        tl.addView(tr);

        for (int i = 0; i < carrito.size(); i++) {
            tr = new TableRow(this);

            TextView t0 = new TextView(this);
            t0.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t0.setText(carrito.get(i)[0]);
            t0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t0);

            TextView t1 = new TextView(this);
            t1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t1.setText(carrito.get(i)[1]);
            t1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t1);

            TextView t3 = new TextView(this);
            t3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t3.setText(carrito.get(i)[2]);
            t3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t3);

            TextView t2 = new TextView(this);
            t2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t2.setText(carrito.get(i)[3]);
            t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t2);

            TextView t4 = new TextView(this);
            t4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            t4.setText(carrito.get(i)[4]);
            t4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(t4);

            TextView t5 = new TextView(this);
            t5.setText(carrito.get(i)[5]);
            t5.setVisibility(View.GONE);
            tr.addView(t5);

            Button editarProducto = new Button(this);
            editarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            editarProducto.setText("EDITAR");
            int finalI = i;
            editarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarProducto(
                            Integer.parseInt(carrito.get(finalI)[0]),
                            carrito.get(finalI)[1],
                            Integer.parseInt(carrito.get(finalI)[5]),
                            Double.parseDouble(carrito.get(finalI)[3]),
                            v);
                }
            });
            tr.addView(editarProducto);

            Button quitarProducto = new Button(this);
            quitarProducto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            quitarProducto.setText("QUITAR");
            quitarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accionQuitarProductos(carrito.get(finalI)[1]);
                }
            });
            tr.addView(quitarProducto);

            tl.addView(tr);
        }

        rl.addView(tl);

        return rl;
    }

    private void accionAgregarProducto(int idProducto, String nombre, double precio, int cantidad, int max){
        if (carrito != null && carrito.size() != 0){
            totalApagar += precio * cantidad;
            String[] valor = new String[] {
                    String.valueOf(idProducto),
                    nombre,
                    String.valueOf(cantidad),
                    String.valueOf(precio),
                    String.valueOf(precio * cantidad),
                    String.valueOf(max)
            };

            boolean encontrado = false;
            for (int i = 0; i < carrito.size(); i++) {
                String[] elemento = carrito.get(i);
                if (elemento[0].equals(valor[0])) {
                    totalApagar = totalApagar - Float.parseFloat(carrito.get(i)[4]);
                    carrito.set(i, valor);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                carrito.add(valor);
            }
        } else {
            carrito = new ArrayList<>();
            totalApagar += precio * cantidad;
            String[] valor = new String[] {
                    String.valueOf(idProducto),
                    nombre,
                    String.valueOf(cantidad),
                    String.valueOf(precio),
                    String.valueOf(precio * cantidad),
                    String.valueOf(max)
            };
            carrito.add(valor);
        }

        Toast.makeText(CarritoCompras.this, "Agregado al carrito.", Toast.LENGTH_SHORT).show();
        total.setText("Total: " + totalApagar);
    }

    private void accionQuitarProductos(String nombre){
        if (carrito != null) {
            for (int i = 0; i < carrito.size(); i++) {
                String[] elemento = carrito.get(i);
                if (elemento[1].equals(nombre)) {
                    totalApagar = totalApagar - Float.parseFloat(carrito.get(i)[4]);
                    carrito.remove(elemento);
                    break;
                }
            }
            total.setText("Total: " + totalApagar);
            Toast.makeText(CarritoCompras.this, "Producto quitado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CarritoCompras.this, "El carrito se encuentra vacio.", Toast.LENGTH_SHORT).show();
        }
    }
}