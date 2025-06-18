package com.example.napbabby;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";

    private EditText etNome, etEmail, etPassword;
    private Button btnCadastrar;
    private TextView tvLogin;

    // Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar componentes
        initViews();

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        etNome = findViewById(R.id.nome);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        tvLogin = findViewById(R.id.tvRegister);
    }

    private void setupListeners() {
        btnCadastrar.setOnClickListener(v -> validateAndRegister());

        tvLogin.setOnClickListener(v -> {
            // Voltar para tela de login
            Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateAndRegister() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validação simples
        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Digite seu nome");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Digite seu e-mail");
            return;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("E-mail inválido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Digite sua senha");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("A senha deve ter pelo menos 6 caracteres");
            return;
        }

        // Criar conta no Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Cadastro bem-sucedido
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Atualizar o nome do usuário
                        if (user != null) {
                            com.google.firebase.auth.UserProfileChangeRequest profileUpdates =
                                    new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                            .setDisplayName(nome)
                                            .build();

                            user.updateProfile(profileUpdates);
                        }

                        updateUI(user);
                    } else {
                        // Cadastro falhou
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Usuário cadastrado com sucesso, navegar para a tela Primeiros Passos
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PrimeirosPassosActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

