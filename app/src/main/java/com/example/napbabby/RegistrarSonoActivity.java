package com.example.napbabby;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrarSonoActivity extends AppCompatActivity {

    private TextView tvSelectedStartTime, tvSelectedEndTime, tvCurrentDate;
    private Button btnSalvar;
    private ImageView ivBack;

    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_sono);

        initViews();
        setupCurrentDate();
        setupListeners();
    }

    private void initViews() {
        tvSelectedStartTime = findViewById(R.id.tvSelectedStartTime);
        tvSelectedEndTime = findViewById(R.id.tvSelectedEndTime);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
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

        // Selecionar horário de início do sono
        findViewById(R.id.layoutStartTime).setOnClickListener(v -> showTimePicker(true));

        // Selecionar horário de fim do sono
        findViewById(R.id.layoutEndTime).setOnClickListener(v -> showTimePicker(false));

        // Salvar registro de sono
        btnSalvar.setOnClickListener(v -> saveSleeRecord());
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

                    if (isStartTime) {
                        startTime = time;
                        tvSelectedStartTime.setText(time);
                        tvSelectedStartTime.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        endTime = time;
                        tvSelectedEndTime.setText(time);
                        tvSelectedEndTime.setTextColor(getResources().getColor(android.R.color.black));
                    }
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void saveSleeRecord() {
        if (startTime.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione o horário de início do sono", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTime.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione o horário de fim do sono", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aqui você salvaria os dados no Firebase ou banco local
        // Por enquanto, apenas mostrar uma mensagem de sucesso
        Toast.makeText(this, "Registro de sono salvo com sucesso!", Toast.LENGTH_SHORT).show();

        // Voltar para a tela Home
        finish();
    }
}

