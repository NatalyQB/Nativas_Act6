package com.example.tiendaonline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerfilProductoActivity extends AppCompatActivity {

    private TextView txtCodigo, txtNombre, txtPrecio;
    private Button btnBorrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_producto);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        btnBorrar = findViewById(R.id.btnBorrar);

        String codigoProducto = getIntent().getStringExtra("codigo");
        String nombreProducto = getIntent().getStringExtra("nombre");
        String precioProducto = getIntent().getStringExtra("valor");

        // Mostrar los datos en los TextViews
        txtCodigo.setText("Código: " + codigoProducto);
        txtNombre.setText("Nombre: " + nombreProducto);
        txtPrecio.setText("Precio: " + precioProducto);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EliminarProductoTask(codigoProducto).execute();
            }
        });
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
