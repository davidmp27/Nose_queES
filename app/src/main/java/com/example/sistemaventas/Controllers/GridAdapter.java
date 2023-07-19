package com.example.sistemaventas.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sistemaventas.Modelo.Entidades.Producto;
import com.example.sistemaventas.R;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    Context context;
    List<Producto> productos;
    LayoutInflater inflater;

    public GridAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_producto, null);

        ImageView imageView = convertView.findViewById(R.id.producto_imagen);
        TextView textView1 = convertView.findViewById(R.id.productoNombre);
        TextView textView2 = convertView.findViewById(R.id.productoCantidad);
        TextView textView3 = convertView.findViewById(R.id.productoPrecio);
        TextView textView4 = convertView.findViewById(R.id.productoCodigo);

        imageView.setImageResource(R.drawable.ic_launcher_background);
        textView1.setText(productos.get(position).nombre);
        textView2.setText(String.valueOf(productos.get(position).stock));
        textView3.setText(String.valueOf(productos.get(position).precio));
        textView4.setText(String.valueOf(productos.get(position).idProducto));

        return convertView;
    }
}
