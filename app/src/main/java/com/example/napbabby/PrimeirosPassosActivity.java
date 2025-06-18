package com.example.napbabby;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PrimeirosPassosActivity extends AppCompatActivity {

    private EditText editTextBabyName;
    private TextView textViewSelectedDate;
    private LinearLayout layoutDatePicker, layoutBoy, layoutGirl;
    private ImageView btnContinue;

    private String selectedDate = "";
    private String selectedGender = "";
    private boolean isBoySelected = false;
    private boolean isGirlSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiros_passos);

        initViews();
        setupListeners();
    }

    private void initViews() {
        editTextBabyName = findViewById(R.id.editTextBabyName);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        layoutDatePicker = findViewById(R.id.layoutDatePicker);
        layoutBoy = findViewById(R.id.layoutBoy);
        layoutGirl = findViewById(R.id.layoutGirl);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void setupListeners() {
        layoutDatePicker.setOnClickListener(v -> showDatePicker());

        layoutBoy.setOnClickListener(v -> selectGender("Menino", true));

        layoutGirl.setOnClickListener(v -> selectGender("Menina", false));

        btnContinue.setOnClickListener(v -> validateAndContinue());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    textViewSelectedDate.setText(selectedDate);
                    textViewSelectedDate.setTextColor(getResources().getColor(android.R.color.black));
                },
                year, month, day
        );

        // Definir data máxima como hoje
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void selectGender(String gender, boolean isBoy) {
        selectedGender = gender;

        if (isBoy) {
            isBoySelected = true;
            isGirlSelected = false;
            layoutBoy.setBackgroundResource(R.drawable.gender_selector_selected);
            layoutGirl.setBackgroundResource(R.drawable.gender_selector);
        } else {
            isBoySelected = false;
            isGirlSelected = true;
            layoutGirl.setBackgroundResource(R.drawable.gender_selector_selected);
            layoutBoy.setBackgroundResource(R.drawable.gender_selector);
        }
    }

    private void validateAndContinue() {
        String babyName = editTextBabyName.getText().toString().trim();

        if (babyName.isEmpty()) {
            Toast.makeText(this, "Por favor, digite o nome do bebê", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione a data de nascimento", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedGender.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione o gênero do bebê", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar dados do bebê (pode ser em SharedPreferences ou Firebase)
        // Por enquanto, apenas navegar para a próxima tela
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("baby_name", babyName);
        intent.putExtra("baby_birth_date", selectedDate);
        intent.putExtra("baby_gender", selectedGender);
        startActivity(intent);
        finish();
    }
}

