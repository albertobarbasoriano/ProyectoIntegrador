package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Participante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class RegistroActivity extends AppCompatActivity {

    TextInputEditText etNombre, etUsuario, etEmail, etPassword, etConfPassword;
    Button btnRegistro;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView tvLogin;
    Participante participante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        etNombre = findViewById(R.id.nombre);
        etUsuario = findViewById(R.id.usuario);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etConfPassword = findViewById(R.id.confPassword);

        btnRegistro = findViewById(R.id.btn_registro);
        progressBar = findViewById(R.id.progressBar);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = String.valueOf(etNombre.getText());
                String username = String.valueOf(etUsuario.getText());
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPassword.getText());
                String confPassword = String.valueOf(etConfPassword.getText());

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://aequalis-db-default-rtdb.firebaseio.com/");
                DatabaseReference refUsuario = database.getReference("Usuarios");

                if (TextUtils.isEmpty(nombre)) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistroActivity.this, R.string.introduce_nombre, Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(username)) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistroActivity.this, R.string.introduce_username, Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(email)) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistroActivity.this, R.string.introduce_email, Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(password)) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistroActivity.this, R.string.introduce_password, Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(confPassword)) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistroActivity.this, R.string.introduce_confPassword, Toast.LENGTH_LONG).show();

                } else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confPassword) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(nombre)) {

                    if (password.equals(confPassword)){
                        refUsuario.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(RegistroActivity.this, R.string.usuario_existente, Toast.LENGTH_SHORT).show();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {

                                                participante = new Participante(username, nombre, email);

                                                refUsuario.child(participante.getUsername()).setValue(participante)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(RegistroActivity.this, R.string.usuario_creado, Toast.LENGTH_SHORT);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegistroActivity.this, R.string.error_usuario_creado, Toast.LENGTH_SHORT);
                                                            }
                                                        });
                                                Toast.makeText(RegistroActivity.this, R.string.cuenta_creada, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegistroActivity.this, R.string.error_cuenta_creada, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(RegistroActivity.this, "Error en la consulta a la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegistroActivity.this, R.string.password_coincidir, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}