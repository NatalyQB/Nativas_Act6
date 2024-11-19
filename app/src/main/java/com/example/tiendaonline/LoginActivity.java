package com.example.tiendaonline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btnIniciarSesion;
    private TextView tvCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvCrearCuenta = findViewById(R.id.tvCrearCuenta);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    new IniciarSesionTask().execute(usuario, contrasena);
                }
            }
        });



        tvCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    private class IniciarSesionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String contrasena = params[1];

            try {
                // URL de la API para el login
                URL url = new URL("https://apitienda.techcloud.com.co/api_tienda/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Datos a enviar en el POST
                String postData = "correo=" + usuario + "&clave=" + contrasena;

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                // Leer la respuesta del servidor
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
                // Muestra el mensaje recibido del servidor (puede ser un error o éxito)
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

                // Si el inicio de sesión fue exitoso
                if (result.equals("Inicio de sesion exitoso")) {
                    // Redirige a PrincipalActivity
                    Intent intent = new Intent(LoginActivity.this, PrincipalActiity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Si el inicio de sesión falló, muestra un mensaje apropiado
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            } else {
                // En caso de error en la conexión
                Toast.makeText(LoginActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Si el usuario presiona el botón de retroceso
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }
