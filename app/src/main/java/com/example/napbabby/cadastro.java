package com.example.napbabby;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.content.Intent;
import android.widget.Button;

public class cadastro extends AppCompatActivity {
    private Button btnCadastrar; // Declare a variável para o seu botão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro); // Seu arquivo de layout

        // 1. Encontre o botão pelo ID definido no XML
        // Certifique-se de que o ID no XML para o botão "Cadastrar" seja "btnCadastrar"
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // 2. Configure um "Ouvinte de Cliques" para o botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Crie um Intent para ir para a HomeActivity
                // O primeiro parâmetro é o contexto (esta Activity), o segundo é a Activity de destino
                Intent intent = new Intent(cadastro.this, LoginActivity.class); // Mude HomeActivity.class para o nome da sua nova Activity

                // 4. Inicie a nova Activity
                startActivity(intent);

                // Opcional: Se você não quiser que o usuário volte para a tela de login ao pressionar o botão "Voltar",
                // você pode finalizar a LoginActivity:
                // finish();
            }
        });
    }
}

