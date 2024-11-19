package com.example.tiendaonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListadoProductosActivity extends AppCompatActivity {

    private Button btnCrearProducto;
    private LinearLayout layoutProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        btnCrearProducto = findViewById(R.id.btnCrearProducto);
        layoutProductos = findViewById(R.id.layoutProductos);

        new ListarProductosTask().execute();

        btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListadoProductosActivity.this, CrearProductoActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ListarProductosTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = null;
            try {
                URL url = new URL("https://apitienda.techcloud.com.co/api_tienda/listar_productos.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();
                response = responseBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);

                    if ("success".equals(jsonResponse.getString("status"))) {
                        JSONArray productosArray = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < productosArray.length(); i++) {
                            JSONObject producto = productosArray.getJSONObject(i);

                            String codigoProducto = producto.getString("Codigo");
                            String nombreProducto = producto.getString("Nombre");
                            String precioProducto = producto.getString("Precio");

                            View productoView = getLayoutInflater().inflate(R.layout.item_producto, layoutProductos, false);

                            TextView txtCodigoProducto = productoView.findViewById(R.id.txtCodigoProducto);
                            TextView txtNombreProducto = productoView.findViewById(R.id.txtNombreProducto);
                            TextView txtPrecioProducto = productoView.findViewById(R.id.txtPrecioProducto);
                            Button btnComprar = productoView.findViewById(R.id.btnComprarProducto);

                            txtCodigoProducto.setText("Código: " + codigoProducto);
                            txtNombreProducto.setText("Nombre: " + nombreProducto);
                            txtPrecioProducto.setText("Precio: " + precioProducto);

                            btnComprar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ListadoProductosActivity.this, PerfilProductoActivity.class);

                                    intent.putExtra("codigo", codigoProducto);
                                    intent.putExtra("nombre", nombreProducto);
                                    intent.putExtra("precio", precioProducto);


                                    startActivity(intent);
                                }
                            });


                            layoutProductos.addView(productoView);
                        }
                    } else {
                        Toast.makeText(ListadoProductosActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ListadoProductosActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ListadoProductosActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
            Toast.makeText(ListadoProductosActivity.this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ListadoProductosActivity.this, "Error al añadir el producto al carrito", Toast.LENGTH_SHORT).show();
        }
    }}