package com.example.proyectointegrador.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.utils.MyApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilActivity extends AppCompatActivity {

    TextView tvNombreUsuario, tvNombre, tvEmail, tvCerrarSesion;
    DatabaseReference reference;
    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvNombre = findViewById(R.id.tvNombre);
        tvEmail = findViewById(R.id.tvEmail);
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion);
        app = (MyApp) getApplicationContext();
        reference = FirebaseDatabase.getInstance().getReference();

        tvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("logout", true);

                setResult(PerfilActivity.RESULT_OK, resultIntent);
                finish();
            }
        });

        cargarDatos();

    }

    private void cargarDatos(){
        tvNombreUsuario.setText(app.getLoggedParticipante().getUsername());
        tvNombre.setText(app.getLoggedParticipante().getNombre());
        tvEmail.setText(app.getLoggedParticipante().getEmail());

    }


}