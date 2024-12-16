package com.example.simplecalculatorapp_areeba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewFullHistory extends AppCompatActivity {

    ImageButton back;
    TextView calculator_History;

    private Calculator calculator; // Instance of Calculator


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_full_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Retrieve the history string from the Intent
        Intent intent = getIntent();
        String history = intent.getStringExtra("calculation_history");

        // Set the history string in the TextView or a default message if null
        calculator_History = findViewById(R.id.calculatorHistory);

        if (history != null && !history.isEmpty()) {
            calculator_History.setText(history);
        } else {
            calculator_History.setText("No History Available");
            Log.d("Cal History", calculator_History.getText().toString());
        }

        //Back Button
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}