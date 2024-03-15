package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Participante;
import com.example.proyectointegrador.utils.MyApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView tvRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        tvRegistro = findViewById(R.id.tvRegistro);
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(etEmail.getText());
                password = String.valueOf(etPassword.getText());

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(LoginActivity.this, R.string.introduce_email_contraseña, Toast.LENGTH_SHORT).show();

                    return;

                } else if (TextUtils.isEmpty(email)) {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(LoginActivity.this, R.string.introduce_email, Toast.LENGTH_SHORT).show();

                    return;

                } else if (TextUtils.isEmpty(password)) {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(LoginActivity.this, R.string.introduce_password, Toast.LENGTH_SHORT).show();

                    return;

                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    lookForUser(email);
                                    limpiarCampos();
                                } else {

                                    Toast.makeText(LoginActivity.this, R.string.autenticacion_fallida,
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            }
                        });
            }
        });
    }

    private void limpiarCampos() {
        etEmail.setText("");
        etPassword.setText("");
    }

    private void lookForUser(String email) {
        MyApp app = (MyApp) getApplicationContext();
        FirebaseDatabase.getInstance()
                .getReference("Usuarios")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.i("lookForUser().onDataChange:", "Estamos en el onDataChange");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Participante participante = dataSnapshot.getValue(Participante.class);
                            app.setLoggedParticipante(participante);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.login_successful, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, GruposActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}