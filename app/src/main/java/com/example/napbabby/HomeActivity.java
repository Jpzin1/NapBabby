package com.example.napbabby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome, tvBabyName;
    private ImageView ivProfile;
    private CardView cardRegistrarSono, cardRegistrarAlimentacao, cardHistoricoSono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupData();
        setupListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBabyName = findViewById(R.id.tvBabyName);
        ivProfile = findViewById(R.id.ivProfile);
        cardRegistrarSono = findViewById(R.id.cardRegistrarSono);
        cardRegistrarAlimentacao = findViewById(R.id.cardRegistrarAlimentacao);
        cardHistoricoSono = findViewById(R.id.cardHistoricoSono);
    }

    private void setupData() {
        // Recuperar dados do bebê passados da tela anterior
        Intent intent = getIntent();
        String babyName = intent.getStringExtra("baby_name");

        if (babyName != null && !babyName.isEmpty()) {
            tvBabyName.setText(babyName);
            tvWelcome.setText("Olá, " + babyName + "!");
        } else {
            tvWelcome.setText("Olá!");
            tvBabyName.setText("Bebê");
        }
    }

    private void setupListeners() {
        // Navegação para Registrar Sono
        cardRegistrarSono.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RegistrarSonoActivity.class);
            startActivity(intent);
        });

        // Navegação para Registrar Alimentação
        cardRegistrarAlimentacao.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RegistrarAlimentacaoActivity.class);
            startActivity(intent);
        });

        // Navegação para Histórico de Sono
        cardHistoricoSono.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HistoricoSonoActivity.class);
            startActivity(intent);
        });

        // Navegação para Configurações (perfil do usuário)
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ConfiguracoesActivity.class);
            startActivity(intent);
        });
    }
}

