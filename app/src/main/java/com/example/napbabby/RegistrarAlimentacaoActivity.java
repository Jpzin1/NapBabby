package com.example.napbabby;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrarAlimentacaoActivity extends AppCompatActivity {

    private TextView tvSelectedTime, tvCurrentDate;
    private EditText etQuantidade, etObservacoes;
    private Button btnSalvar;
    private ImageView ivBack;

    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_alimentacao);

        initViews();
        setupCurrentDate();
        setupListeners();
    }

    private void initViews() {
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        etQuantidade = findViewById(R.id.etQuantidade);
        etObservacoes = findViewById(R.id.etObservacoes);
        btnSalvar = findViewById(R.id.btnSalvar);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setupCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        tvCurrentDate.setText(currentDate);
    }

    private void setupListeners() {
        // Voltar para a tela anterior
        ivBack.setOnClickListener(v -> finish());

        // Selecionar horário da alimentação
        findViewById(R.id.layoutTime).setOnClickListener(v -> showTimePicker());

        // Salvar registro de alimentação
        btnSalvar.setOnClickListener(v -> saveFeedingRecord());
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    tvSelectedTime.setText(selectedTime);
                    tvSelectedTime.setTextColor(getResources().getColor(android.R.color.black));
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void saveFeedingRecord() {
        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione o horário da alimentação", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantidade = etQuantidade.getText().toString().trim();
        if (quantidade.isEmpty()) {
            Toast.makeText(this, "Por favor, informe a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        String observacoes = etObservacoes.getText().toString().trim();

        // Aqui você salvaria os dados no Firebase ou banco local
        // Por enquanto, apenas mostrar uma mensagem de sucesso
        Toast.makeText(this, "Registro de alimentação salvo com sucesso!", Toast.LENGTH_SHORT).show();

        // Voltar para a tela Home
        finish();
    }
}

