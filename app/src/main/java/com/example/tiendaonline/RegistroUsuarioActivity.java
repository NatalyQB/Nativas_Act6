package com.example.tiendaonline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etClave, etIdentificacion;
    private Button btnRegistrar, buttonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario1);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etClave = findViewById(R.id.etClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        etIdentificacion = findViewById(R.id.etIdentificacion);
        buttonBuscar = findViewById(R.id.button);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identificacion = etIdentificacion.getText().toString().trim();

                if (identificacion.isEmpty()) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Por favor ingrese una identificación", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lógica de búsqueda (simulación)
                boolean usuarioExiste = verificarUsuario(identificacion);
                if (usuarioExiste) {
                    // Suponiendo que se encontraron estos datos
                    String nombre = "Juan";
                    String apellido = "Pérez";
                    String correo = "juan.perez@example.com";


                    // Iniciar la nueva actividad y pasar los datos del usuario
                    Intent intent = new Intent(RegistroUsuarioActivity.this, PerfilUsuarioActivity.class);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("apellido", apellido);
                    intent.putExtra("correo", correo);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistroUsuarioActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String correo = etCorreo.getText().toString().trim();
                String clave = etClave.getText().toString().trim();
                String identificacion = etIdentificacion.getText().toString().trim();

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || clave.isEmpty() || identificacion.isEmpty()) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    new RegistrarUsuarioTask().execute(nombre, apellido, correo, clave, identificacion);
                }
            }
        });
    }

    private boolean verificarUsuario(String identificacion) {
        // Lógica de verificación simulada: el usuario existe si el ID es "12345"
        return "12345".equals(identificacion);
    }

    private class RegistrarUsuarioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String nombre = params[0];
            String apellido = params[1];
            String correo = params[2];
            String clave = params[3];
            String identificacion = params[4];  // Recibir el nuevo campo

            try {
                URL url = new URL("https://apitienda.techcloud.com.co/api_tienda/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Incluir el campo 'identificacion' en los datos enviados
                String postData = "nombre=" + nombre + "&apellido=" + apellido + "&correo=" + correo + "&clave=" + clave + "&identificacion=" + identificacion;

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
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(RegistroUsuarioActivity.this, result, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegistroUsuarioActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
