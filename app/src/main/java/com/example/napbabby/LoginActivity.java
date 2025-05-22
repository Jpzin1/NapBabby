package com.example.napbabby;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private ImageButton btnGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        initViews();

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar navegação para tela de recuperação de senha
                Toast.makeText(LoginActivity.this, "Recuperação de senha", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar navegação para tela de cadastro
                Toast.makeText(LoginActivity.this, "Cadastro de novo usuário", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar login com Google
                Toast.makeText(LoginActivity.this, "Login com Google", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean rememberMe = cbRememberMe.isChecked();

        // Validação simples
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

        // Aqui você implementaria a lógica de autenticação real
        // Por enquanto, apenas exibimos uma mensagem de sucesso
        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
