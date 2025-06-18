package com.example.napbabby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoricoSonoActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView recyclerViewHistorico;
    private HistoricoSonoAdapter adapter;
    private List<RegistroSono> registrosSono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_sono);

        initViews();
        setupRecyclerView();
        loadHistoricoData();
        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistorico);
    }

    private void setupRecyclerView() {
        registrosSono = new ArrayList<>();
        adapter = new HistoricoSonoAdapter(registrosSono);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistorico.setAdapter(adapter);
    }

    private void loadHistoricoData() {
        // Dados de exemplo - em um app real, estes viriam do Firebase ou banco local
        registrosSono.add(new RegistroSono("18/06/2025", "22:30", "06:30", "8h"));
        registrosSono.add(new RegistroSono("17/06/2025", "23:00", "07:00", "8h"));
        registrosSono.add(new RegistroSono("16/06/2025", "22:15", "06:45", "8h 30min"));
        registrosSono.add(new RegistroSono("15/06/2025", "22:45", "06:15", "7h 30min"));
        registrosSono.add(new RegistroSono("14/06/2025", "23:30", "07:30", "8h"));

        adapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
    }

    // Classe para representar um registro de sono
    public static class RegistroSono {
        private String data;
        private String horaInicio;
        private String horaFim;
        private String duracao;

        public RegistroSono(String data, String horaInicio, String horaFim, String duracao) {
            this.data = data;
            this.horaInicio = horaInicio;
            this.horaFim = horaFim;
            this.duracao = duracao;
        }

        // Getters
        public String getData() { return data; }
        public String getHoraInicio() { return horaInicio; }
        public String getHoraFim() { return horaFim; }
        public String getDuracao() { return duracao; }
    }
}

