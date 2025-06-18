package com.example.napbabby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvUserName, tvUserEmail, tvLogout;
    private Switch switchModoNoturno;

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);

        initViews();
        setupUserInfo();
        setupNightMode();
        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvLogout = findViewById(R.id.tvLogout);
        switchModoNoturno = findViewById(R.id.switchModoNoturno);
    }

    private void setupUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            tvUserName.setText(displayName != null ? displayName : "Usuário");
            tvUserEmail.setText(email != null ? email : "email@exemplo.com");
        }
    }

    private void setupNightMode() {
        // Verificar se o modo noturno está ativado
        boolean isNightModeEnabled = sharedPreferences.getBoolean("night_mode", false);
        switchModoNoturno.setChecked(isNightModeEnabled);
    }

    private void setupListeners() {
        // Voltar para a tela anterior
        ivBack.setOnClickListener(v -> finish());

        // Toggle do modo noturno
        switchModoNoturno.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Salvar preferência
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night_mode", isChecked);
            editor.apply();

            // Aplicar o tema
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            Toast.makeText(this, "Modo noturno " + (isChecked ? "ativado" : "desativado"),
                    Toast.LENGTH_SHORT).show();
        });

        // Logout
        tvLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Fazer logout do Firebase
        mAuth.signOut();

        // Limpar preferências se necessário
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();

        // Navegar para a tela de login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

