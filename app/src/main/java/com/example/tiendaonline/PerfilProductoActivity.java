package com.example.tiendaonline;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerfilProductoActivity extends AppCompatActivity {

    private TextView txtCodigo, txtNombre, txtPrecio;
    private Button btnBorrar, btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_producto);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnComprar = findViewById(R.id.btnComprar);

        String codigoProducto = getIntent().getStringExtra("codigo");
        String nombreProducto = getIntent().getStringExtra("nombre");
        String precioProducto = getIntent().getStringExtra("valor");

        // Mostrar los datos en los TextViews
        txtCodigo.setText("Código: " + codigoProducto);
        txtNombre.setText("Nombre: " + nombreProducto);
        txtPrecio.setText("Precio: " + precioProducto);

        // Configurar el botón "Comprar"
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarrito(codigoProducto, nombreProducto, precioProducto);
            }
        });

        // Configurar el botón "Borrar"
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EliminarProductoTask(codigoProducto).execute();
            }
        });
    }

    // Método para agregar el producto al carrito
    private void agregarAlCarrito(String codigo, String nombre, String precio) {
        SharedPreferences prefs = getSharedPreferences("Carrito", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String carrito = prefs.getString("productos", "");

        String nuevoProducto = codigo + "," + nombre + "," + precio;

        if (carrito.isEmpty()) {
            carrito = nuevoProducto;
        } else {
            carrito += ";" + nuevoProducto;
        }

        editor.putString("productos", carrito);
        boolean success = editor.commit();

        if (success) {
            Toast.makeText(PerfilProductoActivity.this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PerfilProductoActivity.this, "Error al añadir el producto al carrito", Toast.LENGTH_SHORT).show();
        }
    }

    private class EliminarProductoTask extends AsyncTask<Void, Void, Boolean> {
        private String codigo;

        public EliminarProductoTask(String codigo) {
            this.codigo = codigo;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://apitienda.techcloud.com.co/api_tienda/borrar_producto.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{\"codigo\":\"" + codigo + "\"}";

                OutputStream os = conn.getOutputStream();
                os.write(jsonInputString.getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(PerfilProductoActivity.this, "Producto eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(PerfilProductoActivity.this, "Error al eliminar el producto.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
