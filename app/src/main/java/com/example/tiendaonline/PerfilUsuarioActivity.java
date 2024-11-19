package com.example.tiendaonline;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private TextView tvPerfilNombre, tvPerfilApellido, tvPerfilCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvPerfilNombre = findViewById(R.id.tvPerfilNombre);
        tvPerfilApellido = findViewById(R.id.tvPerfilApellido);
        tvPerfilCorreo = findViewById(R.id.tvPerfilCorreo);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombre = extras.getString("nombre");
            String apellido = extras.getString("apellido");
            String correo = extras.getString("correo");


            tvPerfilNombre.setText("Nombre: " + nombre);
            tvPerfilApellido.setText("Apellido: " + apellido);
            tvPerfilCorreo.setText("Correo: " + correo);
        }
    }
}
