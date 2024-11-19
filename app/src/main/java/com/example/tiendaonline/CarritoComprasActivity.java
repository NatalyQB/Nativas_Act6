package com.example.tiendaonline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CarritoComprasActivity extends AppCompatActivity {

    private TextView txtTotalCarrito;
    private Button btnComprar;
    private List<Producto> carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);


        TextView txtTotalCarrito = findViewById(R.id.txtTotalCarrito);
        btnComprar = findViewById(R.id.btnComprar);
        LinearLayout layoutCarrito = findViewById(R.id.layoutCarrito);

        carrito = new ArrayList<>();


        SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
        String productos = prefs.getString("productos", "");

        if (!productos.isEmpty()) {
            String[] productosArray = productos.split(";");
            for (String producto : productosArray) {
                String[] datosProducto = producto.split(",");
                String codigo = datosProducto[0];
                String nombre = datosProducto[1];
                String precio = datosProducto[2];


                View productoView = getLayoutInflater().inflate(R.layout.item_producto, layoutCarrito, false);
                TextView txtNombreCarrito = productoView.findViewById(R.id.txtNombreCarrito);
                TextView txtPrecioCarrito = productoView.findViewById(R.id.txtPrecioCarrito);

                txtNombreCarrito.setText("Nombre: " + nombre);
                txtPrecioCarrito.setText("Precio: " + precio);


                layoutCarrito.addView(productoView);


                carrito.add(new Producto(codigo, nombre, precio));
            }
        } else {
            Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
        }


        mostrarProductos();


        btnComprar.setOnClickListener(v -> realizarCompra());
    }

    private void mostrarProductos() {
        if (carrito.isEmpty()) {
            txtTotalCarrito.setText("El carrito está vacío.");
            btnComprar.setEnabled(false);
            btnComprar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGris)); // Usar ContextCompat
            return;
        }

        StringBuilder mensaje = new StringBuilder("Productos en el carrito:\n");
        for (Producto producto : carrito) {
            mensaje.append(producto.getNombre())
                    .append(" - $")
                    .append(producto.getPrecio())
                    .append("\n");
        }

        txtTotalCarrito.setText(mensaje.toString());

        btnComprar.setEnabled(true);
        btnComprar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVerde)); // Usar ContextCompat
    }

    private void realizarCompra() {
        Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
        carrito.clear();
        finish();
    }
}
