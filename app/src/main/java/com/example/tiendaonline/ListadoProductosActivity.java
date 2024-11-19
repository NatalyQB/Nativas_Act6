package com.example.tiendaonline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

    private LinearLayout layoutProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        layoutProductos = findViewById(R.id.layoutProductos);


        new ListarProductosTask().execute();
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

                            txtCodigoProducto.setText("Código: " + codigoProducto);
                            txtNombreProducto.setText("Nombre: " + nombreProducto);
                            txtPrecioProducto.setText("Precio: " + precioProducto);

                            productoView.setOnClickListener(new View.OnClickListener() {
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
}
