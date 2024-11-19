package com.example.tiendaonline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CrearProductoActivity extends AppCompatActivity {

    private EditText etCodigo, etNombre, etValor;
    private Button btnGuardarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etValor = findViewById(R.id.etValor);
        btnGuardarProducto = findViewById(R.id.btnGuardarProducto);

        btnGuardarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etCodigo.getText().toString().trim();
                String nombre = etNombre.getText().toString().trim();
                String valor = etValor.getText().toString().trim();

                if (codigo.isEmpty() || nombre.isEmpty() || valor.isEmpty()) {
                    Toast.makeText(CrearProductoActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    new CrearProductoTask().execute(codigo, nombre, valor);
                }
            }
        });
    }

    private class CrearProductoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String codigo = params[0];
            String nombre = params[1];
            String valor = params[2];

            try {
                URL url = new URL("https://apitienda.techcloud.com.co/api_tienda/crear_productos.php"); // Cambia esto por tu URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


                String postData = "codigo=" + codigo + "&nombre=" + nombre + "&valor=" + valor;


                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();


                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JSONObject jsonResponse = new JSONObject(response.toString());


                if (jsonResponse.getString("status").equals("success")) {
                    return "Producto creado correctamente";
                } else {
                    return "Error al crear el producto";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Error en la conexión o en el servidor";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(CrearProductoActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result.equals("Producto guardado exitosamente")) {
                    Intent intent = new Intent(CrearProductoActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(CrearProductoActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
